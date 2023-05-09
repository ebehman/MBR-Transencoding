package com.MBR.transcoder.MBR.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel(value = "SetTranscodingOutput")
public class SetTranscodingOutput extends Status {

	@ApiModelProperty(dataType="String",example="njku4098",notes = "unique Id for a request.")
	private String RequestId;
	@ApiModelProperty(dataType="String",example="CustomerId",notes = "CustomerId")
     private String CustomerId;
	@ApiModelProperty(dataType="List",example="files",notes = "input files")
	 private List<String> files;

	public String getRequestId() {
		return RequestId;
	}

	public void setRequestId(String requestId) {
		RequestId = requestId;
	}

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

	@Override
	public String toString() {
		return "SetTranscodingOutput{" +
				"CustomerId='" + CustomerId + '\'' +
				", files=" + files +
				'}';
	}
}
