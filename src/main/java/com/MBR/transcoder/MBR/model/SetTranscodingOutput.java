package com.MBR.transcoder.MBR.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel(value = "SetTranscodingOutput")
public class SetTranscodingOutput extends Status {
	
	@ApiModelProperty(dataType="String",example="CustomerId",notes = "CustomerId")
     private String CustomerId;
	@ApiModelProperty(dataType="List",example="files",notes = "input files")
	 private List<String> files;
}
