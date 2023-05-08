package com.MBR.transcoder.MBR.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "SetTranscodingInput")
public class SetTranscodingInput {
	
	@ApiModelProperty(dataType="String",example="CustomerId",notes = "Customer unique ID")
	private String CustomerId;
	@ApiModelProperty(dataType="List",example="[File1,File2]", notes = "file name" )
	private List<String> files;
	private VodInfo preset;
	
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	
	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}
	public VodInfo getPreset() {
		return preset;
	}
	public void setPreset(VodInfo preset) {
		this.preset = preset;
	}

	@Override
	public String toString() {
		return "SetTranscodingInput [CustomerId=" + CustomerId + 
				", files=" + files + 
				",preset=" +preset.toString()+"]";
	}
	
	
}
