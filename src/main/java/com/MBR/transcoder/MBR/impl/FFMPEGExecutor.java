package com.MBR.transcoder.MBR.impl;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MANASRANJAN BEHERA
 *
 */
public class FFMPEGExecutor 
{
	
private static final Logger logger = LoggerFactory.getLogger(FFMPEGExecutor.class);
	
private String 	ffmpExecutablePath;
/**
 * Arguments for the execution.
 */
private List<String> cmdlist = new ArrayList<String>();
private Process ffmpegP;
private ProcessKiller ffmpegkill ;
private int exitValue;
private long PID;
/**
 * @return the pID
 */
public long getPID() {
	return PID;
}
/**
 * 
 */
private InputStream inputStream;
/**
 * 
 */
private OutputStream outputStream;

/**
 * A stream reading from the ffmpeg process standard error channel.
 */
private InputStream errorStream = null;



/**
 * It build the executor.
 * 
 * @param ffmpegExecutablePath
 *            The path of the ffmpeg executable.
 */
public FFMPEGExecutor(String ffmpExecutablePath) {
	this.ffmpExecutablePath = ffmpExecutablePath;
	cmdlist.add(0,ffmpExecutablePath);
	cmdlist.add(1,"-i");
}
/**
* Addition of arguments for ffmpeg command.
*/
public void addArgument(String arg) {
	cmdlist.add(arg);
}

public Process execute() throws IOException
{
	Runtime runtime = Runtime.getRuntime();
	ProcessBuilder p= new ProcessBuilder(cmdlist);
	String cmd = cmdlist.stream().collect(Collectors.joining(" "));
	logger.info(cmd);
		
	p.redirectErrorStream(true);
	ffmpegP = p.start();
	PID = getFFMPEGpid(ffmpegP);
	
	ffmpegkill =new ProcessKiller(ffmpegP);
	runtime.addShutdownHook(ffmpegkill);
	inputStream = ffmpegP.getInputStream();
	outputStream = ffmpegP.getOutputStream();
	errorStream = ffmpegP.getErrorStream();
	
	return ffmpegP;
	}
/**
 * @return the exitValue
 */
public int getExitValue() {
	return exitValue;
}
/**
 * @return the inputStream
 */
public InputStream getInputStream() {
	return inputStream;
}
/**
 * @return the outputStream
 */
public OutputStream getOutputStream() {
	return outputStream;
}
/**
 * @return the errorStream
 */
public InputStream getErrorStream() {
	return errorStream;
}
/**
 * Killing ffmpeg command excecution 
 */
public void destroy()
{
	if (inputStream!=null)

{
		try
		{
			inputStream.close();
		}
		catch (Throwable t)
		{
			;
		}
		inputStream = null;
}
	if (outputStream != null) {
		try {
			outputStream.close();
		} catch (Throwable t) {
			;
		}
		outputStream = null;
	}
	if (errorStream != null) {
		try {
			errorStream.close();
		} catch (Throwable t) {
			;
		}
		errorStream = null;
	}
	if (ffmpegP != null) {
		ffmpegP.destroy();
		ffmpegP = null;
	}
	if (ffmpegkill != null) {
		Runtime runtime = Runtime.getRuntime();
		runtime.removeShutdownHook(ffmpegkill);
		ffmpegkill = null;
	}
	
}

private long getFFMPEGpid(Process p)
{
	long pid = -1;

    try {
      if (p.getClass().getName().equals("java.lang.UNIXProcess")) 
      {
        Field f = p.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getLong(p);
        f.setAccessible(false);
      }
    } catch (Exception e) {
      pid = -1;
    }
    return pid;
	
}
public int getExitStatus(Process p)
{
	int status = 1;
	try {
		p.waitFor();
		status = p.exitValue();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return status;
}

}