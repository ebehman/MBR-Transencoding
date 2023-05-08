package com.MBR.transcoder.MBR.impl;

/**
 * @author MANASRANJAN BEHERA
 *
 */
public class ProcessKiller extends Thread
{
/* The process to kill */
private Process process;
public ProcessKiller(Process process)
{
	this.process = process;
}
public void run()
{
	process.destroy();
}
}
