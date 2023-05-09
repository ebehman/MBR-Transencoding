package com.MBR.transcoder.MBR.impl;

import com.MBR.transcoder.MBR.model.SetTranscodingInput;
import com.MBR.transcoder.MBR.model.SetTranscodingOutput;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.net.HttpURLConnection;
import java.util.UUID;

@Produces({"application/json","application/xml"})
@RestController

public class MbrTranscodingRestServiceV1 {

	
	private static final Logger logger = LoggerFactory.getLogger(MbrTranscodingRestServiceV1.class);
	@RequestMapping(value = "/set-Mbr-transcoding", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces= MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API for video transcoding", notes = "video transcoding")
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Check Status",
					response = SetTranscodingOutput.class)
	})
	@ApiParam(value = "MbrTranscoding information", required = true)
	public ResponseEntity<SetTranscodingOutput> setTranscoding(@RequestBody SetTranscodingInput input ) {
		
		SetTranscodingOutput output = new SetTranscodingOutput();

		long startTime = System.currentTimeMillis();
		logger.info(input.toString());
		Transencoder transencoder = new Transencoder();
		String UUID = java.util.UUID.randomUUID().toString();
		input.setRequestId(UUID);
		new Thread(new Runnable() 
		{
			
			@Override
			public void run() {
		transencoder.multiConverter(input);
			}
		}).start();
		output.setCustomerId(input.getCustomerId());
		output.setFiles(input.getFiles());
		output.setRequestId(input.getRequestId());
		return new ResponseEntity(output, HttpStatus.OK);
		
//				Response.status(202)
//				.entity("Multibit Rate VOD conversion Accepted "+reqinfo.getReqID()+" "+reqinfo.getCustomer()+" "+reqinfo.getFiles())
//				.build();

		
	}
	@RequestMapping(value="/hello")
public String Hello(){
		return "I am manas";
	}
	
}
