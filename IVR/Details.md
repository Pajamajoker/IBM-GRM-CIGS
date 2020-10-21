# IVR

This folder contains all recordings of conversations between the User and the Assistant via the IBM Voice Agent and Twilio(SIP Trunking). 
The conversations are recorded by & downloaded from Twilio. Transcription is generated using IBM Watson Text to Speech service.  

# Code snippet for Transcription
Use the following code to use the instance of Watson Text to Speech used in our project for Transcription:
```
curl -X POST -u "apikey:Wtzn1VDAE4BqqyO4n-jGNrginvkL_nQFBQcYsMLyYgRS" \
--header "Content-Type: audio/mp3" \
--data-binary @{path}/{filename} \
"https://api.us-east.speech-to-text.watson.cloud.ibm.com/instances/3d89819d-2435-4e57-a470-fd5774f134c9/v1/recognize"
```
Replace {path} & {filename} with appropriate details.

