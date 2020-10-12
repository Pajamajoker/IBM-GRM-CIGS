package com.ibm.cigs.interfaces;


public interface TranslationInterface {
	
	/**
    *
    * @param String ip represents the string whose language type is to be found
    * @return string representing the language type of @param
    * 
    **/
	String identifyLanguages(String ip);
	
	/**
    *
    * @param String ip represents the string to be translated to english
    * @return string translated to English from the original language
    * 
    **/
	String translateToEnglish(String ip);
	
	/**
    *
    * @param String ip represents the String which is to be translated to original language
    * @return string translated to English from the original language
    * 
    **/
	String translateToOriginal(String ip);

}

