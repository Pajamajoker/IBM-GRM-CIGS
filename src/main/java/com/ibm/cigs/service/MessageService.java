package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.watson.assistant.v2.model.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import java.util.HashMap;
import java.util.List;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);
    private HashMap<String,SessionResponse> sessionMap=new HashMap<>();

    @Value("${watson.apikey}")
    private String apikey;
    @Value("${watson.serviceUrl}")
    private String serviceUrl;
    @Value("${assistant.assistantId}")
    private String assistantId;
    @Value("${assistant.versionDate}")
    private String versionDate;
    @Value("${twilio.accountId}")
    private String twilioAccountId;
    @Value("${twilio.authToken}")
    private String twilioAuthToken;
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public String sendMessageToWatson(MessageEntity messageEntity) {
        String fromPhoneNumber=messageEntity.getFrom();

        //authenticate into Watson Assistant
        Authenticator authenticator = new IamAuthenticator(apikey); // replace with API key

        Assistant service = new Assistant(versionDate, authenticator);
        service.setServiceUrl(serviceUrl);

        String sessionId=null;
        String retry="Please restart the conversation"; // Default reply incase of any error
        // Create session if it doesn't exist.
        if(sessionMap.get(fromPhoneNumber)==null)
        {
            try {
                CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistantId).build();
                SessionResponse session = service.createSession(createSessionOptions).execute().getResult();
                sessionId = session.getSessionId();
                System.out.println(fromPhoneNumber);
                sessionMap.put(fromPhoneNumber, session);
            }
            catch (Exception e)
            {
                System.out.println("Could not create a new session!");
                System.out.println(e.getMessage());
            }
        }
        else // get the session if it exists
        {
            SessionResponse session=sessionMap.get(fromPhoneNumber);
            sessionId=session.getSessionId();
        }

        //delete session after conversation is over: A conversation is over when the message says: "done"
        System.out.println(messageEntity.getBody());
        if(messageEntity.getBody().equals("done") || messageEntity.getBody().equals("Done")) {
            try {
                DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
                service.deleteSession(deleteSessionOptions).execute();
                sessionMap.remove(fromPhoneNumber);
                return "Thank you for your time!";
            }
            catch(Exception e)
            {
                System.out.println("The session you are trying to end has already expired!");
                System.out.println(e.getMessage());
                sessionMap.remove(fromPhoneNumber);
                return "Thank you for your time!";
            }
        }

        //create text
        try {
            MessageInputOptions inputOptions = new MessageInputOptions.Builder()
                    .returnContext(true)
                    .build();

            MessageInput input = new MessageInput.Builder()
                .text(messageEntity.getBody())
                .options(inputOptions)
                .build();
            // Start conversation with message from messageEntity.
            MessageOptions messageOptions = new MessageOptions.Builder(assistantId,
                sessionId).input(input).build();

            //Get response from Watson Assistant

            MessageResponse response = service.message(messageOptions).execute().getResult();

            //print response from Watson to console, assumes single text response
            List<RuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
            if (responseGeneric.size() > 0) {
                if (responseGeneric.get(0).responseType().equals("text")) {
                    System.out.println(response);
                }
            }

            System.out.println(sessionMap);
            return responseGeneric.get(0).text();
        }
        catch(Exception e)
        {
            System.out.println("Could not get a valid response from Watson Assistant, probably the session expired.");
            System.out.println(e.getMessage());
        }

        return retry;
    }

    public Message sendMessageToUser(String body, String number) {
        String toPhoneNumber="whatsapp:" + number;
        String fromPhoneNumber="whatsapp:"+twilioPhoneNumber;

        Twilio.init(twilioAccountId,twilioAuthToken); // replace with Twilio credentials
        Message message = Message.creator( new com.twilio.type.PhoneNumber(toPhoneNumber),   // replace with "To" whatsapp phone number
                new com.twilio.type.PhoneNumber(fromPhoneNumber),body)                       //replace with "From" whatsapp phone number
                .create();
        //logger.info("Sent message on: " + message.getMessagingServiceSid() + "\nmessage SID:" + message.getSid());
        return message;
    }
}