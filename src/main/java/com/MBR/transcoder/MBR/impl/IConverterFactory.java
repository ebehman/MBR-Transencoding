package com.MBR.transcoder.MBR.impl;

public class IConverterFactory 
{
public static IConverter getConverter(String ConverterType)
{
	
	switch(ConverterType)
	{
		case  "MP4":
		{
			
			return new Mp4Transcoder();
			
		}
		
		case "HLS":
		{
			
			return new HLSChunkConverter();
		}
		
		case "DASH" :
		{
			return new DASHChunkConverter();
		}
		
		default:
		{
			throw new IllegalArgumentException
            ("Invalid Protocol type: " + ConverterType);
		}
	}
	
}
}
