package com.MBR.transcoder.MBR.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.MBR.transcoder.MBR.model.VodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author MANASRANJAN BEHERA
 *
 *Factory pattern to choose the objects on the basis of input of the transcoder.
 */
public class Mp4Transcoder implements IConverter
{
	private static final Logger logger = LoggerFactory.getLogger(Mp4Transcoder.class);
	private static Integer numofth = 4;

	public void convert() {
		// TODO Auto-generated method stub
		
	}

	public void convert(int requestid, String customer, VodInfo vod, String filename, MediaInfo mf) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(numofth);
		String file = IConverter.getworkingDirectoy(customer,filename.replace(".mp4", ""));
		String source = "/usr/vol2/"+customer+"/"+filename;
		try {
			List<String> Vbitrate = vod.getVbitrate();
			List<String> Profile = vod.getProfile();
			List<String> level = vod.getLevel();
			List<String> fps = vod.getFps();
			List<String> Abitrate = vod.getAbitrate();
			List<String> Resoultion = vod.getResoultion();
			List<String> Aspect = vod.getAspectratio();
		logger.info("VideoBitRate list size :"+Vbitrate.size());
		
		for(int j=0;j<=Vbitrate.size();j++)
		{
			final int i = new Integer(j);
			String Tfilename = filename.replace(".mp4", "")+"_"+Vbitrate.get(i)+"k"+".mp4";
			
			Runnable ffmpegworker = new Runnable()
			{
				long PID;
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					
					
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
					ffmpegexecutor.addArgument(file+"/"+Tfilename);
					ffmpegexecutor.addArgument("-y");
										
					

					float duration =0;
					float fileSize =0;
					try {
						
						LocalDateTime startime = LocalDateTime.now();  
						logger.info("Start time :"+Tfilename+" "+startime);
										
						Process p = ffmpegexecutor.execute();
						PID = ffmpegexecutor.getPID();
						String hostname = InetAddress.getLocalHost().getHostName();
			 //Insert 2 DB to table mbr_response_status with Status = inprogress , duration =0 min;,filesize=0MB
						MBRPairing mbrPairing = new MBRPairing();
						mbrPairing.setRequestId(requestid);
						mbrPairing.setCustomerId(customer);
						mbrPairing.setFile(filename);
						mbrPairing.setProtocol("MP4");
						mbrPairing.setBitrate(Vbitrate.get(i));
						mbrPairing.setResponse_code("1");
						mbrPairing.setResponse_code("InProgress");
						mbrPairing.setPID(PID);
						mbrPairing.setFileinfo(mf);
						mbrPairing.setStartTime(startime.toString());
						mbrPairing.setDuration(duration);
						mbrPairing.setSize(fileSize);
						mbrPairing.setMbrinstance(InetAddress.getLocalHost().getHostName());

						logger.info(" Request and file detail "+requestid+" "+mf.toString()+"FFMPEGPID :"+PID+" Started & Inprogress");
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
						logger.info("Stop time :"+Tfilename+" "+stoptime);
						duration = Duration.between(startime, stoptime).toMinutes();
						logger.info( "Transcoding duration for file :"+Tfilename+" "+duration);
						File tfile = new File(file+"/"+Tfilename);
						
						LocalDateTime final_status_updated = LocalDateTime.now();
						
						if (status == 0)
						{
							logger.info("command is sucessfully executed.");
							
							// Creation of SMIL file for the converted MP4 files
							
							if (!tfile.exists() || !tfile.isFile())
							{
								logger.error("File size of  Tfilename is "+fileSize+", Please check the transcoded command");
							}
				
							else 
							{
								long TfileSize = tfile.length();
								fileSize = TfileSize / (1024 * 1024);
								logger.info("Size of transcoded file "+Tfilename+" "+fileSize+"mb");
							}
									
							
							//boolean Ispushed = MBRUtils.pushmbrfiles2DCO(vod.getDfolder(), file,"MP4",Tfilename,filename.replace(".mp4", ""));
								mbrPairing.setResponse_code("1");
								mbrPairing.setResponse_code("Completed");
								mbrPairing.setDuration(duration);
								mbrPairing.setSize(fileSize);
								logger.info(" Request and file detail "+requestid+" "+mf.toString()+"FFMPEGPID :"+PID);
							//Update the DB for failed status,duration,size & statuschange timestamp

						}
						else 
						{
							logger.info("ffmpeg command exceution failed");
							mbrPairing.setResponse_code("2");
							mbrPairing.setResponse_code("Failed");
							//Update the DB for failed status,duration,size & statuschange timestamp
							Thread.currentThread().interrupt();
											
						}
						
					    }
					catch (Exception e )
					{
						ffmpegexecutor.destroy();
						Thread.currentThread().interrupt();
						logger.error(e.getMessage());
						logger.debug(e.getMessage(), e.fillInStackTrace());
					}
					
				finally
				{
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
	logger.info("Finished all ffmpeg threads for MP4 transcoding "+filename);
			
	
	}
	catch (Exception e)
	{
		logger.error("Invalid preset Data "+e.getMessage());
		logger.debug(e.getMessage(), e.fillInStackTrace());
	}
	}
	
	

}

