package com.MBR.transcoder.MBR.impl;

import com.MBR.transcoder.MBR.model.VodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HLSChunkConverter implements IConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(HLSChunkConverter.class);
    private static Integer numofth = 4;
    //@Autowired
	//private MBRRepositry mbrRepositry;

	public void convert() {
		// TODO Auto-generated method stub
		
	}

	public void convert(String requestid, String customer, VodInfo vod, String filename, MediaInfo mf) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				ExecutorService executor = Executors.newFixedThreadPool(numofth);
				String wd = IConverter.getworkingDirectoy(customer,filename.replace(".mp4", ""));
				String source = "/var/volume/"+customer+"/"+filename;
				ConcurrentHashMap<String, String> mchm = new ConcurrentHashMap<String, String>();
				
				try {
				List<String> Vbitrate = vod.getVbitrate();
				List<String> Profile = vod.getProfile();
				List<String> level = vod.getLevel();
				List<String> fps = vod.getFps();
				List<String> Abitrate = vod.getAbitrate();
				List<String> Resoultion = vod.getResoultion();
				List<String> Aspect = vod.getAspectratio();
				boolean isEnc = vod.getEnckey()!= null && !vod.getEnckey().isEmpty();
				logger.info("VideoBitRate List size "+Vbitrate.size());
				for(int j=0;j<Vbitrate.size();j++)
				{
					final int i = new Integer(j);
					String Tfilename = filename.replace(".mp4", "")+"_"+Vbitrate.get(i)+"k";
					
					Runnable ffmpegworker = new Runnable(){
						long PID;
						@Override
						public void run() 
						{
							FFMPEGExecutor ffmpegexecutor = new FFMPEGExecutor("ffmpeg");
							ffmpegexecutor.addArgument(source);
							ffmpegexecutor.addArgument("-profile:v");
							ffmpegexecutor.addArgument(Profile.get(i)); 
							ffmpegexecutor.addArgument("-level");
							ffmpegexecutor.addArgument(level.get(i));
							ffmpegexecutor.addArgument("-r");
							ffmpegexecutor.addArgument(fps.get(i));
							ffmpegexecutor.addArgument("-g");
							ffmpegexecutor.addArgument(String.valueOf(Double.valueOf(fps.get(i))*2));
							ffmpegexecutor.addArgument("-keyint_min");
							ffmpegexecutor.addArgument(String.valueOf(Double.valueOf(fps.get(i))*2));
							ffmpegexecutor.addArgument("-b:v");
							ffmpegexecutor.addArgument(Vbitrate.get(i)+"k");
							ffmpegexecutor.addArgument("-b:a");
							ffmpegexecutor.addArgument(Abitrate.get(i)+"k");
							ffmpegexecutor.addArgument("-vcodec");
							ffmpegexecutor.addArgument(vod.getVcodec());
							ffmpegexecutor.addArgument("-acodec");
							ffmpegexecutor.addArgument(vod.getAcodec());
							ffmpegexecutor.addArgument("-s");
							ffmpegexecutor.addArgument(Resoultion.get(i));
							ffmpegexecutor.addArgument("-aspect");
							ffmpegexecutor.addArgument(Aspect.get(i));
							ffmpegexecutor.addArgument("-start_number");
							ffmpegexecutor.addArgument("0");
							ffmpegexecutor.addArgument("-hls_time");
							ffmpegexecutor.addArgument("10");
							ffmpegexecutor.addArgument("-hls_list_size");
							ffmpegexecutor.addArgument("0");
							ffmpegexecutor.addArgument("-f");
							ffmpegexecutor.addArgument("hls");
							if(isEnc) {
								ffmpegexecutor.addArgument("-hls_key_info_file");
								ffmpegexecutor.addArgument(vod.getEnckey());
							}
							ffmpegexecutor.addArgument("-hls_segment_filename");
							ffmpegexecutor.addArgument(wd+"/"+Tfilename+"_%01d.ts");
							ffmpegexecutor.addArgument(wd+"/"+Tfilename+".m3u8");
							if(isEnc) {
								ffmpegexecutor.addArgument("-hls_key_info_file");
								ffmpegexecutor.addArgument(vod.getEnckey());
							}
							ffmpegexecutor.addArgument("-y");
							
							
							float duration =0;
							float fileSize =0;
							
							try {
								LocalDateTime startime = LocalDateTime.now();  
								logger.info("Start time for "+Tfilename+" :"+startime);
								Process p = ffmpegexecutor.execute();
								PID = ffmpegexecutor.getPID();
								String hostname = InetAddress.getLocalHost().getHostName();
								logger.info(" Request and file detail "+requestid+" "+mf.toString()+"FFMPEGPID :"+PID+" Started & Inprogress");

								//Insert 2 DB to table mbr_response_status with Status = inprogress , duration =0 min;,filesize=0MB
								MBRPairing mbrPairing = new MBRPairing();
								mbrPairing.setRequestId(requestid);
								mbrPairing.setCustomerId(customer);
								mbrPairing.setFile(filename);
								mbrPairing.setProtocol("HLS");
								mbrPairing.setBitrate(Vbitrate.get(i));
								mbrPairing.setResponse_code("1");
								mbrPairing.setResponse_code("InProgress");
								mbrPairing.setPID(PID);
								mbrPairing.setFileinfo(mf);
								mbrPairing.setStartTime(startime.toString());
								mbrPairing.setDuration(duration);
								mbrPairing.setSize(fileSize);
								mbrPairing.setMbrinstance(InetAddress.getLocalHost().getHostName());
                                //mbrRepositry.save(mbrPairing);

								Reader reader = new BufferedReader(new InputStreamReader(ffmpegexecutor.getInputStream()));
								try(BufferedReader bufferedReader =
									    new BufferedReader(reader))
								  {
									    String line = bufferedReader.readLine();
									    while(line != null) {
									        //do something with line

									        line = bufferedReader.readLine();
									        logger.debug(line);
									        
									    }

									}
							int status = ffmpegexecutor.getExitStatus(p);
												
							LocalDateTime stoptime = LocalDateTime.now();
							logger.info("Stop time for "+Tfilename+":"+stoptime);
							duration = Duration.between(startime, stoptime).toMinutes();
							logger.info( "Transcoding duration for"+Tfilename+":"+duration);
							LocalDateTime final_status_updated = LocalDateTime.now();
							if (status==0)
							{
								logger.info("Command executed sucessfully");
								
								// Creation of Master playlist for all the conversion for HLS Protocol
								String  bitrate = String.valueOf((Integer.valueOf(Vbitrate.get(i))+Integer.valueOf(Abitrate.get(i)))* 1000)+",RESOLUTION="+Resoultion.get(i);
								String  playlistloc = Tfilename+".m3u8";
								mchm.put("#EXT-X-STREAM-INF:BANDWIDTH="+bitrate, playlistloc);
								
								fileSize = MBRUtils.gettransFilesSize(wd, Tfilename);
								logger.info("transcoded size in MB  "+Tfilename+" "+fileSize);

								//boolean Ispushed = MBRUtils.pushmbrfiles2DCO(vod.getDfolder(), wd,"HLS",Tfilename,filename.replace(".mp4", ""));
								//DBconnection.UpdateTranscoder(conn, 0, "Completed", final_status_updated, fileSize, duration, requestid,filename,"HLS",Vbitrate.get(i),customer);
									// update the DB
									mbrPairing.setResponse_code("1");
									mbrPairing.setResponse_code("Completed");
									mbrPairing.setDuration(duration);
									mbrPairing.setSize(fileSize);
									logger.info(" Request and file detail "+requestid+" "+mf.toString()+"FFMPEGPID :"+PID);
                                     //mbrRepositry.save(mbrPairing);
								//Update the MBR_Record table for the particular file its status,duration,size,&statuschange timestamp regarding the Conversion.
													
							}
							
							else 
							{
								logger.info("Command execution failed");
								mbrPairing.setResponse_code("2");
								mbrPairing.setResponse_code("Failed");
								//Update the DB for failed status,duration,size & statuschange timestamp
								//mbrRepositry.save(mbrPairing);
								//DBconnection.UpdateTranscoder(conn, 2, "Failed", final_status_updated, fileSize, duration, requestid,filename,"HLS",Vbitrate[i],customer);
								Thread.currentThread().interrupt();
								
							}
						}	
							catch (Exception  e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							//	ffmpegexecutor.destroy();
								logger.error(e.getMessage(),e.fillInStackTrace());
								Thread.currentThread().interrupt();
						}
							finally {
								ffmpegexecutor.destroy();
								logger.info("Removing the ffmpeg command");
							}
						}
					};

					executor.execute(ffmpegworker);	
				
				}
				    executor.shutdown();

					while (!executor.isTerminated()) 
					{
					}
					logger.info("Finished all ffmpeg threads for HLS chunk conversion "+filename);

					new ManifestCreator().HLSManifestCreator(wd, mchm, filename);

				/*boolean ispushMaster = MBRUtils.pushMasterplaylist(vod.getDfolder(), wd, filename.replace(".mp4", ""));
				if (ispushMaster)
				{
				logger.info("Master playlist pushed :"+ispushMaster);
				}
				else {logger.error("Master playlist sftppush Failed :"+ispushMaster);}

				 */
					logger.info("Master playlist pushed created in :"+wd);
					
			}

				catch(Exception e )
				{
					logger.info("Shuting down the executor framework started");
					executor.shutdown();
					logger.info("Shuting down the executor framework end");
					while (!executor.isTerminated())
					{
					}
					logger.info("Finished all ffmpeg threads for HLS chunk conversion "+filename);
					logger.error("Invalid preset Data "+e.getMessage());
					logger.debug(e.getMessage(), e.fillInStackTrace());
				}

				
			}
			
	}
	
	
