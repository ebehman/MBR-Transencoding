package com.MBR.transcoder.MBR.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.MBR.transcoder.MBR.model.SetTranscodingInput;
import com.MBR.transcoder.MBR.model.VodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main class of this package.Instances of it can encode and decode video file as per ffmpeg specification.
 * @author MANASRANJAN BEHERA
 *
 */

public class Transencoder 
{
	private static final Logger logger = LoggerFactory.getLogger(Transencoder.class);
	private static Integer numofth = 4;

	/**
	 * This regexp is used to parse the ffmpeg output about the size of a video
	 * stream.
	 */
	private static final Pattern SIZE_PATTERN = Pattern.compile(
			"(\\d+)x(\\d+)", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the frame rate value
	 * of a video stream.
	 */
	private static final Pattern FRAME_RATE_PATTERN = Pattern.compile(
			"([\\d.]+)\\s+(?:fps|tb\\(r\\))", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the bit rate value
	 * of a stream.
	 */
	private static final Pattern BIT_RATE_PATTERN = Pattern.compile(
			"(\\d+)\\s+kb/s", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the sampling rate of
	 * an audio stream.
	 */
	private static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile(
			"(\\d+)\\s+Hz", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the channels number
	 * of an audio stream.
	 */
	private static final Pattern CHANNELS_PATTERN = Pattern.compile(
			"(mono|stereo)", Pattern.CASE_INSENSITIVE);

	
	
	/**
	 * The locator of the ffmpeg executable used by this encoder.
	 */
	private FFMPEG ffmpeg;
	
	public Transencoder() {

		this.ffmpeg = new DefaultFFMPEGLocator();
	}
	/*
	 * Building an encoder with custom ffmpeg runtime path.
	 */
	public Transencoder(FFMPEG ffmpeg)
	{
		this.ffmpeg=ffmpeg;
		
	}
	
	/**
	 * Returns a set informations about a multimedia file, if its format is
	 * supported for decoding.
	 * 
	 * @param source: The source multimedia file.
	 * @throws IOException 
	 * @throws EncoderException 
	 * @throws InputFormatException 
	 *   
	 *            
	 */
	
		// vodinfo  needs to removed.
	
MediaInfo getInfo(String source) throws IOException, InputFormatException, EncoderException
{
	FFMPEGExecutor ffmpegexecutor = new FFMPEGExecutor("ffprobe");//ffmpeg.createExecutor() //ffprobe.createExecutor;
	
	logger.info("Source location  :"+source);
	
	ffmpegexecutor.addArgument(source);
	try{
		ffmpegexecutor.execute();
		
		
	}
	catch(Exception e)
	{
		logger.error(e.getMessage());
		logger.debug(e.getMessage(), e.fillInStackTrace());
	}
	
	try {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(ffmpegexecutor.getInputStream()));
		if (ffmpegexecutor.getExitValue()==0)
		{
			logger.info("Exiting FFMPEGExecutor "+ffmpegexecutor.getExitValue());
		return parseMultimediaInfo(source, reader);
		}
		else 
		{
			return null;
		}
	}
	catch (Exception e )
	{
		logger.error(e.getMessage());
		logger.debug(e.getMessage(), e.fillInStackTrace());
		return null;
	}
		
	 finally {
		ffmpegexecutor.destroy();
	}
	
}
	private MediaInfo parseMultimediaInfo(String source, BufferedReader reader) throws IOException, InputFormatException, EncoderException {
		// TODO Auto-generated method stub
		
		//Pattern p1 = Pattern.compile("^\\s*Input #0, (\\w+).+$\\s*",
		//		Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile(
				"^\\s*Duration: (\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d).*$",
				Pattern.CASE_INSENSITIVE);
		Pattern p3 = Pattern.compile(
				"^\\s*Stream #\\S+: ((?:Audio)|(?:Video)|(?:Data)): (.*)\\s*$",
				Pattern.CASE_INSENSITIVE);
		
		
		MediaInfo mediainfo = new MediaInfo();
		try 
		{
			while (true) {
				String line = reader.readLine();
				
				if (line == null) {
					break;
				}
							
					if (line.startsWith(source)) {
						
						throw new InputFormatException(line);
					}
					if (line.contains("Input"))
					{
						Matcher m = p2.matcher(line); // To check p1
						if(m.matches())
						{
							String format = m.group(1);
							mediainfo.setFormat(format);
					}
					}
					
					if (line.contains("Duration"))
					{
						Matcher m = p2.matcher(line);
						if (m.matches()) {
							
							long hours = Integer.parseInt(m.group(1));
							long minutes = Integer.parseInt(m.group(2));
							long seconds = Integer.parseInt(m.group(3));
							long dec = Integer.parseInt(m.group(4));
							long duration = (dec * 100L) + (seconds * 1000L)
									+ (minutes * 60L * 1000L)
									+ (hours * 60L * 60L * 1000L);
							logger.info("Duration "+duration);
							mediainfo.setDuration(duration);
							logger.info("Mediainfo "+mediainfo.getDuration());
					}
								
					} 
					 if (line.contains("Stream #0:"))
					 {						
						 Matcher m = p3.matcher(line);
							if (m.matches())
							{
								String type = m.group(1);
								String specs = m.group(2);
								if ("Video".equalsIgnoreCase(type)) {
									VideoInfo video = new VideoInfo();
									StringTokenizer st = new StringTokenizer(specs, ",");
									for (int i = 0; st.hasMoreTokens(); i++) {
										String token = st.nextToken().trim();
										if (i == 0) {
											video.setDecoder(token);
										} else {
											boolean parsed = false;
											// Video size.
											Matcher m2 = SIZE_PATTERN.matcher(token);
											if (!parsed && m2.find()) {
												int width = Integer.parseInt(m2
														.group(1));
												int height = Integer.parseInt(m2
														.group(2));
												video.setVsize(new VideoSize(width,
														height));
												parsed = true;
											}
											// Frame rate.
											m2 = FRAME_RATE_PATTERN.matcher(token);
											if (!parsed && m2.find()) {
												try {
													float frameRate = Float
															.parseFloat(m2.group(1));
													video.setFrameRate(frameRate);
												} catch (NumberFormatException e) {
													;
												}
												parsed = true;
											}
											// Bit rate.
											m2 = BIT_RATE_PATTERN.matcher(token);
											if (!parsed && m2.find()) {
												int bitRate = Integer.parseInt(m2
														.group(1));
												video.setBitRate(bitRate);
												parsed = true;
											}
										}
									}
									mediainfo.setVideoInfo(video);
								} else if ("Audio".equalsIgnoreCase(type)) {
									AudioInfo audio = new AudioInfo();
									StringTokenizer st = new StringTokenizer(specs, ",");
									for (int i = 0; st.hasMoreTokens(); i++) {
										String token = st.nextToken().trim();
										if (i == 0) {
											audio.setDecoder(token);
										} else {
											boolean parsed = false;
											// Sampling rate.
											Matcher m2 = SAMPLING_RATE_PATTERN
													.matcher(token);
											if (!parsed && m2.find()) {
												int samplingRate = Integer.parseInt(m2
														.group(1));
												audio.setSamplingRate(samplingRate);
												parsed = true;
											}
											// Channels.
											m2 = CHANNELS_PATTERN.matcher(token);
											if (!parsed && m2.find()) {
												String ms = m2.group(1);
												if ("mono".equalsIgnoreCase(ms)) {
													audio.setChannels(1);
												} else if ("stereo"
														.equalsIgnoreCase(ms)) {
													audio.setChannels(2);
												}
												parsed = true;
											}
											// Bit rate.
											m2 = BIT_RATE_PATTERN.matcher(token);
											if (!parsed && m2.find()) {
												int bitRate = Integer.parseInt(m2
														.group(1));
												audio.setBitRate(bitRate);
												parsed = true;
											}
										}
									}
									mediainfo.setAudioInfo(audio);
								}
								
							}
					 }
			
				
		}
		}
		catch (IOException e) {
			throw new EncoderException(e);
		}
		
		 return mediainfo;
	}
	

	  public void transcoder(int requestid,String customer, VodInfo vod,final String filename,MediaInfo mf) throws InterruptedException 
	{
		
		  Thread mp4t = null;	 
		  Thread HLSth = null;
		  String ifile = "/usr/vol2/"+customer+"/"+filename;
		if(vod.isMp4Conversion())
		{
			
					
						IConverter converter = IConverterFactory.getConverter("MP4");
						mp4t = new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
									converter.convert(requestid,customer, vod, filename,mf);
								
								}
							});
						mp4t.start();
						
						
					
					
					
		}
		if(vod.isHLSchunkConversion())
		{
			
					IConverter converter = IConverterFactory.getConverter("HLS");
					HLSth = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							converter.convert(requestid,customer, vod, filename,mf);
							
						}
					});
					HLSth.start();					
					
				
				
		}
		
		
		/**
		 * Future Use cases : 
		 * 
		if(vod.isDASHchunkConversion())
			{
			Runnable task = new Runnable(){ 
				public void run(){
			DASHchunkConverter(customer,vod,filename);}};
			executor.execute(task);
		}
			
		if(vod.isOnlyViedo())
		{
			Runnable task = new Runnable(){ 
				public void run(){ OnlyViedo(vod);}};
				executor.execute(task);	
		}
		if (vod.isOnlyAudio())
		{
			Runnable task = new Runnable(){ 
				public void run(){ OnlyAudio(vod);}};
				executor.execute(task);	
		}
		**/
	if (mp4t!=null)
		{
			mp4t.join();
		}
	if(HLSth!=null)
	{
		HLSth.join();
	}
	deleteIfile(ifile);
	logger.info("Source file removed from Input directory");
	
	}

	  public void multiConverter(SetTranscodingInput input)
	  {
		  	VodInfo vodinfo = input.getPreset();
			List<String> files = input.getFiles();
			ExecutorService executor = Executors.newFixedThreadPool(numofth);
			
			for( String file :files)
			{
						Runnable mcworker = new Runnable()
						{  
							@Override
							public void run() {
								// TODO Auto-generated method stub
						try {
							// Getting the file into local directory for ffmpeg computation 
							String customer = input.getCustomerId();
							int requestid = 4094;
						//String iwf = getMedia(customer,vodinfo.getIfolder(),file);
							// if Ifloder has the file or not
							MediaInfo mf = getInfo(vodinfo.getIfolder());
						if (mf!= null) {
							logger.info(mf.toString());
							transcoder(requestid,customer, vodinfo, file,mf);
						} 
						else 
						{
							logger.error("Error in Source file, aborting the transcoding operation for "+file+"::"+requestid);
							
							List<String> Vbitrate = vodinfo.getVbitrate();
							String hostname = InetAddress.getLocalHost().getHostName();
							LocalDateTime startime = LocalDateTime.now(); 
							try {
							if (vodinfo.isHLSchunkConversion())
							{
								//Insert the filenotfound in DB
								for(String v :Vbitrate)
								{
									MBRPairing mbrPairing = new MBRPairing();
									mbrPairing.setRequestId(requestid);
									mbrPairing.setCustomerId(customer);
									mbrPairing.setFile(file);
									mbrPairing.setProtocol("HLS");
									mbrPairing.setBitrate(v);
									mbrPairing.setResponse_code("2");
									mbrPairing.setResponse_code("Failed:FileNotFound");
									mbrPairing.setPID(0);
									mbrPairing.setFileinfo(null);
									mbrPairing.setStartTime(startime.toString());
									mbrPairing.setDuration(0);
									mbrPairing.setSize(0);
									mbrPairing.setMbrinstance(InetAddress.getLocalHost().getHostName());
									//insert to DB;

								}
								
							}
							if (vodinfo.isMp4Conversion())
							{
								//Insert the filenotfound in DB
								for(String v :Vbitrate)
								{
									MBRPairing mbrPairing = new MBRPairing();
									mbrPairing.setRequestId(requestid);
									mbrPairing.setCustomerId(customer);
									mbrPairing.setFile(file);
									mbrPairing.setProtocol("HLS");
									mbrPairing.setBitrate(v);
									mbrPairing.setResponse_code("2");
									mbrPairing.setResponse_code("Failed:FileNotFound");
									mbrPairing.setPID(0);
									mbrPairing.setFileinfo(null);
									mbrPairing.setStartTime(startime.toString());
									mbrPairing.setDuration(0);
									mbrPairing.setSize(0);
									mbrPairing.setMbrinstance(InetAddress.getLocalHost().getHostName());
									//insert to DB;
								}
									
							}
							
						}
							catch(Exception e)
							{
								logger.error(e.getMessage());
								logger.debug(e.getMessage(), e.fillInStackTrace());
							}
							finally
							{

							}
						}
						}
						catch (IOException | InputFormatException | EncoderException | InterruptedException e) {
							// TODO Auto-generated catch block
							logger.error(e.getMessage());
							logger.debug(e.getMessage(), e.fillInStackTrace());
						}
						
					
							}	
						
						
			};
			executor.execute(mcworker);	
			
						}
			
			executor.shutdown();
			while (!executor.isTerminated()) 
			{
			}
			logger.info("Finished all Multiconversion threads");
			
				
			
			/* Update the status of the RequestID after all threads are completed/Failed/PartialCompleted
			 * to the MBR_RequestRecord Table.
			In case of PartialCompleted check the MBR_Records table for the failed status of individual file.
			Failed file can be requested to MBR server as a new Request.
			*/ 
	  }
	  /* To be used in case of DCO.

	private String getMedia(String customer, String ifolder,String file) 
	{
		String dcourl = getProps(customer);
		String source = "http://"+dcourl+"/"+customer+"/"+ifolder+file;
		logger.info("DCO_source "+source);
		
		  URL u;
	      InputStream is = null;
	      
	      
	      try
	      {
	    	  u = new URL(source);
	    	  HttpURLConnection con = (HttpURLConnection) u.openConnection();
	    	  if (con.getResponseCode()==200)
	    	  {
	    		  String cdir = "/usr/vol2/"+customer;
	    		  File c_dir = new File(cdir);
	    		  if(!c_dir.exists())
	    			{
	    				if (c_dir.mkdir()) 
	    				{
	    		            logger.info("Customer input Directory is created!");
	    		        } 
	    				else 
	    				{
	    					logger.info("Customer input Directory creation failed");
	    				}
	    				
	    				
	    			}
	    		  else 
	    		  {
	    			  logger.info("Customer input Directory exist");
	    		  }
	    		  is = u.openConnection().getInputStream();
		    	
		    	  String MediaF = "/usr/vol2/"+customer+"/"+file;
		    	 FileOutputStream stream = new FileOutputStream(MediaF);
		    	 int read = -1;
		         byte[] buffer = new byte[2048];
		         while((read = is.read(buffer)) != -1)
		         {
		        	 stream.write(buffer, 0, read);
		             
		         }
		         logger.info("[SYSTEM/INFO]: Downloading file... is completed");
		         is.close();
		         stream.close();
		         return MediaF;
	    	  }
	    	  
	    	  else {
	    		  //file download is incomplete abort the process.
	    		  logger.error("File not Found :"+con.getResponseCode()+" "+source);
	    		  
	    	      return null;
	    	       }
	    	  
	      }
	      catch (Exception e )
	      {
	    	  logger.error("File downloading Error "+e.getMessage());
	    	  logger.debug(e.getMessage(), e.fillInStackTrace());
	    	  return null;
	      }
		// TODO Auto-generated method stub
		
	}
	   */

	private void deleteIfile(String file)
	{
		Path path = Paths.get(file);
		try 
		{
			Files.delete(path);
		}
		catch(IOException e)
		{
			logger.error(e.getMessage());
			logger.debug(e.getMessage(), e.fillInStackTrace());
		}
		
	}
	
	
}

