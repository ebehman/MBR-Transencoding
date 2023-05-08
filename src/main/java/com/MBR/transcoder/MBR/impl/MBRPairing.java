package com.MBR.transcoder.MBR.impl;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table
public class MBRPairing {

    @PrimaryKey
    private int RequestId;
    private String CustomerId;
    private String file;
    private String protocol;
    private String bitrate;
    private String response_code;
    private String responseinfo;
    private long PID;
    private MediaInfo fileinfo;
    private String startTime;
    private float duration;
    private float size;
    private String mbrinstance;

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponseinfo() {
        return responseinfo;
    }

    public void setResponseinfo(String responseinfo) {
        this.responseinfo = responseinfo;
    }

    public long getPID() {
        return PID;
    }

    public void setPID(long PID) {
        this.PID = PID;
    }

    public MediaInfo getFileinfo() {
        return fileinfo;
    }

    public void setFileinfo(MediaInfo fileinfo) {
        this.fileinfo = fileinfo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getMbrinstance() {
        return mbrinstance;
    }

    public void setMbrinstance(String mbrinstance) {
        this.mbrinstance = mbrinstance;
    }

    @Override
    public String toString() {
        return "MBRPairing{" +
                "RequestId=" + RequestId +
                ", CustomerId='" + CustomerId + '\'' +
                ", file='" + file + '\'' +
                ", protocol='" + protocol + '\'' +
                ", bitrate='" + bitrate + '\'' +
                ", response_code='" + response_code + '\'' +
                ", responseinfo='" + responseinfo + '\'' +
                ", PID=" + PID +
                ", fileinfo=" + fileinfo +
                ", duration=" + duration +
                ", size=" + size +
                ", mbrinstance='" + mbrinstance + '\'' +
                '}';
    }
}
