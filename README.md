# Citizen Information Gather System using Context Sensitive Assistant- An IBM-GRM Project

### Description:
Gathering citizen's information easily is a very tough job for interested authorities. But it is one of the key aspects of providing necessary services to citizens. As a part of this project, we have built an easy and intuitive Multi-Lingual Context Sensitive Citizen Assistant to gather citizens information through multiple channels using AI.
The Assistant understands the context of what the citizen wants to do and performs the relevant task. It extracts relevant information from the conversation and stores the citizen's information for further processing. This system is scalable, modular and can be easily integrated with existing information retrieval/collection systems using REST-APIs

### Functionalities:
1. Information gathering through multiple channels.
2. Multilingual chat facility.
3. IVR functionality for information gathering via phone call.
4. Context Sensitive Assistant that can understand and remember conversation context and respond accordingly.
5. Easy integration with other services using Rest APIs.

### How does it work? (Architecture):
The arhictecture of the entire system can be split into 5 modules namely : 
1) **The User Interface end**
This is the user side of the system, all the information is retrived from this end. To ease the information gathering process, multiple & intuitive channels like Phone call ,SMS, Whatsapp, Facebook Messenger.

2) **Translator**
Since the system has miltilingual support, a translator service is used. Once a message is received from the user interface end, the message is processed by the translator service which auto-detects the language from the incoming message, translates it to english and sends the message to be processed by the Assistant, it then also translates the outgoing message (reply from assistant) from english to the original language in which the message was received.

3) **Natural Language Processor/Assistant**
The Natural Language Processor is the core module responsible for the conversation, it is also called the Assistant. The assistant is context sensitive, meaning it can understand the context of the ongoing conversation and act & reply accordingly.

4) **Middleware (Controller)**
The middleware module is essentially a controller. The controller is responsible for maintaining the flow of the system's execution. All incoming requests & outgoing responses are directed through the controller.

5) **Database/Storage**
The database module is where the gathered information is stored. This can be a NoSQL or an SQL database.

![alt text](architecture.jpeg)

### Prerequisities:

### Installation:

### API reference:

### Function Descriptions:


