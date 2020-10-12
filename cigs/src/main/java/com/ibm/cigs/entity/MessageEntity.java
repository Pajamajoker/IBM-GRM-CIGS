package com.ibm.cigs.entity;

public class MessageEntity {
    String SmsMessageSid, SmsSid, SmsStatus, Body, From, To, ApiVersion;
    Integer NumSegments;

    public String getSmsMessageSid() {
        return SmsMessageSid;
    }

    public void setSmsMessageSid(String smsMessageSid) {
        SmsMessageSid = smsMessageSid;
    }

    public String getSmsSid() {
        return SmsSid;
    }

    public void setSmsSid(String smsSid) {
        SmsSid = smsSid;
    }

    public String getSmsStatus() {
        return SmsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        SmsStatus = smsStatus;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getApiVersion() {
        return ApiVersion;
    }

    public void setApiVersion(String apiVersion) {
        ApiVersion = apiVersion;
    }

    public Integer getNumSegments() {
        return NumSegments;
    }

    public void setNumSegments(Integer numSegments) {
        NumSegments = numSegments;
    }

    @Override
    public String toString() {
        return "MessageEntity [ApiVersion=" + ApiVersion + ", Body=" + Body + ", From=" + From + ", NumSegments="
                + NumSegments + ", SmsMessageSid=" + SmsMessageSid + ", SmsSid=" + SmsSid + ", SmsStatus=" + SmsStatus
                + ", To=" + To + "]";
    }

}