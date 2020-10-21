# IVR

This folder contains all recordings of conversations between the User and the Assistant via the IBM Voice Agent and Twilio(SIP Trunking). 
The conversations are recorded by & downloaded from Twilio. Transcription is generated using IBM Watson Text to Speech service.  

# Code snippet for Transcription
Use the following code to use the instance of Watson Text to Speech used in our project for Transcription:
```
curl -X POST -u "apikey:{apikey}" \
--header "Content-Type: audio/mp3" \
--data-binary @{path}/{filename} \
"{Service URL}"
```
Replace {path} & {filename} with appropriate details. {apikey} and {Service URL} can be found in the service crendentials section for 
the corresponding service instance page on IBM cloud.
