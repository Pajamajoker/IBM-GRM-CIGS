package com.ibm.cigs.service;

import java.util.List;
import com.ibm.cigs.interfaces.TranslationInterface;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiedLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiedLanguages;
import com.ibm.watson.language_translator.v3.model.IdentifyOptions;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslationIBMService implements TranslationInterface {
	
    @Value("${translator.authentication}")
	private String authentication = "h9gm1czwJqLuwFJLSnYDpgfEFLrX5xXfjKMriWbNY5tp";
    
    @Value("${translator.url}")
    private String url = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/838b1eb9-f576-4adb-b6d4-40d9f9329830";
   
    @Value("${translator.version}")
    private String version;
    
    private String identifyLanguage;
    private Authenticator authenticator;
    private LanguageTranslator languageTranslator;
    
    public TranslationIBMService()
    {
    	authenticator = new IamAuthenticator(authentication);
        languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl(url);
    }
    
    private boolean isNumber(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (Character.isDigit(s.charAt(i)) == false)
                return false;
 
        return true;
    }

    @Override
	public String identifyLanguages(String ip)
	{	
		IdentifyOptions identifyOptions = new IdentifyOptions.Builder()
				  .text(ip)
				  .build();
		
		IdentifiedLanguages languages = languageTranslator.identify(identifyOptions)
				  .execute().getResult();
		List<IdentifiedLanguage>ls = languages.getLanguages();
		
		return ls.get(0).getLanguage();
	}
	
    @Override
	public String translateToEnglish(String ip)
	{
    	try
    	{
			this.identifyLanguage = identifyLanguages(ip);
			if(isNumber(ip)) {
				this.identifyLanguage="en";
	    		return ip;
	    		
			}
			if(identifyLanguage.equals("en"))
			{
				return ip;
			}
			
			TranslateOptions translateOptions = new TranslateOptions.Builder()
					  .addText(ip)
					  .source(identifyLanguage)
					  .target("en")
					  .build();
	
			TranslationResult result = languageTranslator.translate(translateOptions)
			  .execute().getResult();
	
			List<com.ibm.watson.language_translator.v3.model.Translation>ls = result.getTranslations();
			return  ls.get(0).getTranslation();
			
    	}
    	catch(Exception ob)
    	{
    		this.identifyLanguage="en";
    		return ip;
    	}
	}
	
    @Override
	public String translateToOriginal(String ip)
	{
		if(identifyLanguage.equals("en"))
			return ip;
		
		TranslateOptions translateOptions = new TranslateOptions.Builder()
				  .addText(ip)
				  .source("en")
				  .target(identifyLanguage)
				  .build();

		TranslationResult result = languageTranslator.translate(translateOptions)
		  .execute().getResult();

		List<com.ibm.watson.language_translator.v3.model.Translation>ls = result.getTranslations();
		return  ls.get(0).getTranslation();
		
	}
}
