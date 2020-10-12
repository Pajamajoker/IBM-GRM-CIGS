package com.ibm.cigs.entity;

import java.sql.Timestamp;

import com.twilio.rest.api.v2010.account.Message;

public class Document {
    String From, To, Body, SmsMessageSid;
    Timestamp timestamp;

    public Document(MessageEntity messageEntity) {
        this.From = messageEntity.From;
        this.To = messageEntity.To;
        this.Body = messageEntity.Body;
        this.SmsMessageSid = messageEntity.SmsMessageSid;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Document(Message message) {
        this.From = message.getFrom().toString();
        this.To = message.getTo().toString();
        this.Body = message.getBody().toString();
        this.SmsMessageSid = message.getSid();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Document [Body=" + Body + ", From=" + From + ", SmsMessageSid=" + SmsMessageSid + ", To=" + To
                + ", timestamp=" + timestamp + "]";
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

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getSmsMessageSid() {
        return SmsMessageSid;
    }

    public void setSmsMessageSid(String smsMessageSid) {
        SmsMessageSid = smsMessageSid;
    }

}