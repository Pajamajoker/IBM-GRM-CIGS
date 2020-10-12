package com.ibm.cigs.service;

import com.ibm.cigs.interfaces.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.Lists;

@Service
public class TranslationGoogleService implements TranslationInterface{
	
	private Translate translate;
	private String projectId;
	private File credentialsPath;
	private String identifyLanguage;
	private String apiKey;

	public TranslationGoogleService() throws FileNotFoundException, IOException {
		
		//credentialsPath = new File("/home/lenovo/MyProject.json");
		/*projectId = "the-field-256313";
		GoogleCredentials credentials;
		  try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
		    credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		  }
		  
		  translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
		  	
		//GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/home/lenovo/MyProject.json"))
		//        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));	    
		//System.setProperty("GOOGLE_API_KEY", "/home/lenovo/MyProject.json");
		//translate = TranslateOptions.getDefaultInstance().getService();
		//translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();	*/	// TODO Auto-generated constructor stub
		
		Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();

	  
	}
	@Override
	public String identifyLanguages(String ip) {
		Detection detection = translate.detect(ip);
	    String detectedLanguage = detection.getLanguage();
		return detectedLanguage;
	}

	@Override
	public String translateToEnglish(String ip) {
		// TODO Auto-generated method stub
		identifyLanguage = identifyLanguages(ip);
		Translation translation =
		        translate.translate(
		            ip,
		            TranslateOption.sourceLanguage(identifyLanguage),
		            TranslateOption.targetLanguage("en"));
		
		return translation.getTranslatedText();

	}

	@Override
	public String translateToOriginal(String ip) {
		Translation translation =
		        translate.translate(
		            ip,
		            TranslateOption.sourceLanguage("en"),
		            TranslateOption.targetLanguage(identifyLanguage));
		
		return translation.getTranslatedText();
	}
}
