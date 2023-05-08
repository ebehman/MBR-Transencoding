package com.MBR.transcoder.MBR.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestCreator 
{
private static final Logger logger = LoggerFactory.getLogger(ManifestCreator.class);
final String data1 = "#EXTM3U";
final String data2 = "#EXT-X-VERSION:3";
List<String> ll = new ArrayList<String>();

{
	ll.add(data1);
	ll.add(data2);
}

public void HLSManifestCreator(String wd,ConcurrentHashMap<String, String> chm,String f) throws IOException
{
if(chm.size()>0)
	{
for (String key : chm.keySet()) 
	  {
	      ll.add(key);
	      ll.add(chm.get(key));
	  }

Files.write(Paths.get(wd+"/master.m3u8"),ll,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
logger.info(f+" master.m3u8 created.");
	}
}

/* To be Implemented in Future.
public void MPDManifestCreator()
{
	
}
public void SMILCreator()
{
	
}
**/

}
