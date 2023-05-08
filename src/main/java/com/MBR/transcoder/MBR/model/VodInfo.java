package com.MBR.transcoder.MBR.model;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel(value = "VodInfo")
public class VodInfo {

	   	@ApiModelProperty(dataType="String",example="Ifolder",notes = "location of input files")
		private String Ifolder; 
		@ApiModelProperty(dataType="String",example="Dfolder",notes = "location of converted files in case of DCO")
		private String Dfolder;
		@ApiModelProperty(dataType="List",example="Vbitrate",notes = "Video bitrate")
		private List<String> Vbitrate;
		@ApiModelProperty(dataType="List",example="resoultion",notes = "resoultion")
		private List<String> resoultion;
		@ApiModelProperty(dataType="String",example="Abitrate",notes = "Audio bit rate")
		private List<String> Abitrate;
		@ApiModelProperty(dataType="String",example="vcodec",notes = "video codec")
		private String vcodec;
		@ApiModelProperty(dataType="String",example="acodec",notes = "audieo codec")
		private String acodec;
		@ApiModelProperty(dataType="boolean",example="Mp4Conversion",notes = "enable Mp4 Conversion")
		private boolean Mp4Conversion;
		@ApiModelProperty(dataType="boolean",example="HLSchunkConversion",notes = "enable HLS")
		private boolean HLSchunkConversion;
		@ApiModelProperty(dataType="String",example="level",notes = "level")
		private List<String> level;
		@ApiModelProperty(dataType="String",example="fps",notes = "fps")
		private List<String> fps;
		@ApiModelProperty(dataType="String",example="profile",notes = "profile")
		private List<String> profile;
		@ApiModelProperty(dataType="String",example="[aspectratio1,aspectratio2]",notes = "aspectratio")
		private List<String> aspectratio;
	    @ApiModelProperty(dataType="String",example="http://localhost/hls/enckey.txt",notes = "key file to encrypt HLS chunks")
		private String enckey;

	public String getIfolder() {
		return Ifolder;
	}

	public void setIfolder(String ifolder) {
		Ifolder = ifolder;
	}

	public String getDfolder() {
		return Dfolder;
	}

	public void setDfolder(String dfolder) {
		Dfolder = dfolder;
	}

	public List<String> getVbitrate() {
		return Vbitrate;
	}

	public void setVbitrate(List<String> vbitrate) {
		Vbitrate = vbitrate;
	}

	public List<String> getResoultion() {
		return resoultion;
	}

	public void setResoultion(List<String> resoultion) {
		this.resoultion = resoultion;
	}

	public List<String> getAbitrate() {
		return Abitrate;
	}

	public void setAbitrate(List<String> abitrate) {
		Abitrate = abitrate;
	}

	public String getVcodec() {
		return vcodec;
	}

	public void setVcodec(String vcodec) {
		this.vcodec = vcodec;
	}

	public String getAcodec() {
		return acodec;
	}

	public void setAcodec(String acodec) {
		this.acodec = acodec;
	}

	public boolean isMp4Conversion() {
		return Mp4Conversion;
	}

	public void setMp4Conversion(boolean mp4Conversion) {
		Mp4Conversion = mp4Conversion;
	}

	public boolean isHLSchunkConversion() {
		return HLSchunkConversion;
	}

	public void setHLSchunkConversion(boolean HLSchunkConversion) {
		this.HLSchunkConversion = HLSchunkConversion;
	}

	public List<String> getLevel() {
		return level;
	}

	public void setLevel(List<String> level) {
		this.level = level;
	}

	public List<String> getFps() {
		return fps;
	}

	public void setFps(List<String> fps) {
		this.fps = fps;
	}

	public List<String> getProfile() {
		return profile;
	}

	public void setProfile(List<String> profile) {
		this.profile = profile;
	}

	public List<String> getAspectratio() {
		return aspectratio;
	}

	public void setAspectratio(List<String> aspectratio) {
		this.aspectratio = aspectratio;
	}

	public String getEnckey() {
		return enckey;
	}

	public void setEnckey(String enckey) {
		this.enckey = enckey;
	}

	@Override
	public String toString() {
		return "VodInfo{" +
				"Ifolder='" + Ifolder + '\'' +
				", Dfolder='" + Dfolder + '\'' +
				", Vbitrate=" + Vbitrate +
				", resoultion=" + resoultion +
				", Abitrate=" + Abitrate +
				", vcodec='" + vcodec + '\'' +
				", acodec='" + acodec + '\'' +
				", Mp4Conversion=" + Mp4Conversion +
				", HLSchunkConversion=" + HLSchunkConversion +
				", level=" + level +
				", fps=" + fps +
				", profile=" + profile +
				", aspectratio=" + aspectratio +
				", enckey='" + enckey + '\'' +
				'}';
	}
}

