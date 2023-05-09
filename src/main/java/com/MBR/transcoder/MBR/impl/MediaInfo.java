package com.MBR.transcoder.MBR.impl;

/*
 * Instances of this class will report the information about the a Media File 
 */
//@UserDefinedType
public class MediaInfo 
{
	
/**
 * 
 */
private String format ;
private long duration;
private AudioInfo audioInfo;
private VideoInfo videoInfo;
/**
 * @return the format
 */
public String getFormat() {
	return format;
}
/**
 * @return the duration
 */
public long getDuration() {
	return duration;
}
/**
 * @return the audioInfo
 */
public AudioInfo getAudioInfo() {
	return audioInfo;
}
/**
 * @return the videoInfo
 */
public VideoInfo getVideoInfo() {
	return videoInfo;
}
/**
 * @param format the format to set
 */
void setFormat(String format) {
	this.format = format;
}
/**
 * @param duration the duration to set
 */
void setDuration(long duration) {
	this.duration = duration;
}
/**
 * @param audioInfo the audioInfo to set
 */
void setAudioInfo(AudioInfo audioInfo) {
	this.audioInfo = audioInfo;
}
/**
 * @param videoInfo the videoInfo to set
 */
void setVideoInfo(VideoInfo videoInfo) {
	this.videoInfo = videoInfo;
}

public String toString() {
	return getClass().getName() + " (format=" + format + ", duration="
			+ duration + ", video=" + videoInfo + ", audio=" + audioInfo + ")";
}


}
