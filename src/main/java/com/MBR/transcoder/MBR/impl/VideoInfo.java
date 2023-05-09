/**
 * 
 */
package com.MBR.transcoder.MBR.impl;


/**
 * @author MANASRANJAN BEHERA
 * Instances of the class will report aboout the information the V codecs of the media file.
 */
//@UserDefinedType
public class VideoInfo 
{
private String decoder ;
private int bitRate =-1;;
private float frameRate =-1;
private VideoSize vsize =null;
/**
 * @return the decoder
 */
public String getDecoder() {
	return decoder;
}
/**
 * @return the bitRate
 */
public int getBitRate() {
	return bitRate;
}
/**
 * @return the frameRate
 */
public float getFrameRate() {
	return frameRate;
}
/**
 * @return the vsize
 */
public VideoSize getVsize() {
	return vsize;
}
/**
 * @param decoder the decoder to set
 */
void setDecoder(String decoder) {
	this.decoder = decoder;
}
/**
 * @param bitRate the bitRate to set
 */
void setBitRate(int bitRate) {
	this.bitRate = bitRate;
}
/**
 * @param frameRate the frameRate to set
 */
void setFrameRate(float frameRate) {
	this.frameRate = frameRate;
}
/**
 * @param vsize the vsize to set
 */
void setVsize(VideoSize vsize) {
	this.vsize = vsize;
}

public String toString() {
	return getClass().getName() + " (decoder=" + decoder + ", size=" + vsize
			+ ", bitRate=" + bitRate + ", frameRate=" + frameRate + ")";
}


}

