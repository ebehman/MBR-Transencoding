package com.MBR.transcoder.MBR.impl;

import java.io.File;
import com.MBR.transcoder.MBR.model.VodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface IConverter
{
static final Logger logger = LoggerFactory.getLogger(HLSChunkConverter.class);
void convert();
void convert(String requestid, String customer,VodInfo vod,String filename,MediaInfo mf);

static String getworkingDirectoy(String customer,String filename)
{
	String cdir = "/var/volume/mbr/"+customer;
	File c_dir = new File(cdir);
	if(!c_dir.exists())
	{
		if (c_dir.mkdir()) 
		{
            logger.info("Customer Directory is created!");
        } 
		else {
            logger.info("Customer directory already exists");
        }
	}

    String wdf = cdir+"/"+filename;
	
	File file = new File(wdf);
			if (!file.exists())
			{				
				if (file.mkdir()) {
	                logger.info("Directory is created!");
	            } else {
	                logger.info("Failed to create directory!");
	            }
			}
			
	return wdf;
	
}

}
