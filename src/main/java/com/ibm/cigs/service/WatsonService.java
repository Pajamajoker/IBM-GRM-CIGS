package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.cigs.interfaces.AssistantInterface;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatsonService implements AssistantInterface {
    private Logger logger = LoggerFactory.getLogger(WatsonService.class);
    private HashMap<String, SessionResponse> sessionMap = new HashMap<>();


    @Value("${watson.apikey}")
    private String apikey;
    @Value("${watson.serviceUrl}")
    private String serviceUrl;
    @Value("${assistant.assistantId}")
    private String assistantId;
    @Value("${assistant.versionDate}")
    private String versionDate;

    private String sessionId = null;
    private SessionResponse session=null;
    Authenticator authenticator;
    Assistant assistant;

    @PostConstruct
    public void init() {
        this.authenticator = new IamAuthenticator(apikey);
        this.assistant = new Assistant(versionDate, authenticator);
        this.assistant.setServiceUrl(serviceUrl);
    }

    @Override
    public String getSession(MessageEntity messageEntity) throws Exception {
        String phoneNumber = messageEntity.getFrom();

        if (sessionMap.containsKey(phoneNumber)) {
            SessionResponse session = sessionMap.get(phoneNumber);
            System.out.println(session.getSessionId());
            return session.getSessionId();
        } else {
            CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistantId).build();
            SessionResponse session = assistant.createSession(createSessionOptions).execute().getResult();
            sessionId = session.getSessionId();
            System.out.println(phoneNumber + " "+sessionId);
            sessionMap.put(phoneNumber, session);
            return session.getSessionId();
        }
    }


    @Override
    public String[] processMessage(MessageEntity messageEntity) throws Exception {
        String sessionId = getSession(messageEntity);
        Boolean isOver = isConversationOver(messageEntity);

        String defaultResponse = "We're sorry, we do not know how to response to: \"" + messageEntity.getBody() + "\"\nWhat else can we help you with?";
        if (!isOver) {
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
            MessageResponse response = assistant.message(messageOptions).execute().getResult();
            //print response from Watson to console, assumes single text response
            List<RuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
            
            //Map for storing all the context variables from Watson
            Map<String, Object> contextVariablesMap=new HashMap<String,Object>();
            
            //if userDefined context variables exist in Watson
            if(response.getContext().skills().get("main skill").userDefined()!=null)
            {
            	//Get all context variables from Watson in a Map
            	contextVariablesMap=response.getContext().skills().get("main skill").userDefined();
            }
            
            if(responseGeneric.size() > 0) {
            	//if information collection is complete, context variable "submitted" gets set
            	//return this information set in all context variables
            	if(!contextVariablesMap.isEmpty() && contextVariablesMap.containsKey("submitted")) {
            		// collected info from context variables returned in string format
            		contextVariablesMap.remove("submitted");
            		String contextVariables=contextVariablesMap.toString();
            
            /*		
             * 
             * TODO : handle context variables to avoid duplicates in cloudant
             * 
             * 
             * //Set the context variables in watson to null ; this will delete the context variables
               //Deletion of context variables is done to avoid duplication of same information in database for next information collection activity
             * for (Map.Entry<String,Object> me : contextVariablesMap.entrySet()) {
            			if(!(me.getKey()).equals("city"))
            			me.setValue(null);
            		}
            		System.out.println(contextVariablesMap);
            		
            		
            		
            		MessageContextSkill mainSkillContext = new MessageContextSkill.Builder()
            				  .userDefined(contextVariablesMap)
            				  .build();
            		

            				
            				MessageContextSkills skillsContext = new MessageContextSkills();
            				skillsContext.put("main skill", mainSkillContext);

            				MessageContext context = new MessageContext.Builder()
            				  .skills(skillsContext)
            				  .build();
            		
            		MessageOptions options = new MessageOptions.Builder(assistantId,
                                    sessionId).context(context).input(input).build();
            		
            		
            		
            		MessageResponse Contextresponse = assistant.message(options).execute().getResult();
            		
            		System.out.println(Contextresponse);
            		
            		*/
            		
            		return new String[] {sessionId,responseGeneric.get(0).text(),contextVariables.toString()};
            		
            	}
            	else
            	return new String[]{sessionId, responseGeneric.get(0).text(),""};
            }else{
            	
                return new String[]{sessionId, defaultResponse,""};
            }
        } else {
        	
            return new String[]{"", "Thank you for your time!",""};
        }
    }

    @Override
    public Boolean isConversationOver(MessageEntity messageEntity) throws Exception {
        String phoneNumber = messageEntity.getFrom();
        String messageBody = messageEntity.getBody();
        if (messageBody.equals("done") || messageBody.equals("Done")) {
            DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
            assistant.deleteSession(deleteSessionOptions).execute();
            sessionMap.remove(phoneNumber);
            return true;
        }
        return false;
    }
}
