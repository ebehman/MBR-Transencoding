package com.MBR.transcoder.MBR.impl;


public abstract class FFMPEG 
{
/*
 * This method will return the runtime path of ffmpeg server.
 */
	protected abstract String getFFMPEGExecutablePath();
	/*
	 * Returns a FFMPEGExecutor object for to call the ffmpeg executable
	 */
	FFMPEGExecutor createExecutor()
	{
		return new FFMPEGExecutor(getFFMPEGExecutablePath());
		
	}

}

