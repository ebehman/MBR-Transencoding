/**
 * 
 */
package com.MBR.transcoder.MBR.impl;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

/**
 * @author MANASRANJAN BEHERA
 * Instances of this class will report the video file size information.
 *
 */
@UserDefinedType
public class VideoSize implements Serializable
{
private static final long serialVersionUID =1L;
private int width ;
private int height;


public VideoSize(int width, int height) {
	super();
	this.width = width;
	this.height = height;
}

/**
 * @return the width
 */
public int getWidth() {
	return width;
}

/**
 * @return the height
 */
public int getHieght() {
	return height;
}

public String toString() {
	return getClass().getName() + " (width=" + width + ", height=" + height
			+ ")";
}

 
}
