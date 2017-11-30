package com.cdrap.GoogleAccessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
/**
 * 
 * Authors CDRAP Team, James Small, Seth Saunders,Nalin Joshi <br>
 * 
 * School: Johnson County Community College <br>
 * Built for Class: Application Development & Programming - CIS - 264-001 <br>
 * Date: Fall Semester of 2017<br>
 * 
 ********************************************************************************<br>
 * Copyright 2017 James Small, Seth Saunders,Nalin Joshi<br>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at<br>

     http://www.apache.org/licenses/LICENSE-2.0<br>

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ***************************************************************************************<br>
 */
public class EmailAccessor {
	private Gmail service;
	public EmailAccessor() throws IOException, GeneralSecurityException{
		service = DriveEssentials.getGmailService();
	}
	/**
	 * 
	 * @param emailConten creates a google message from a java mail message
	 * @return gmail message ail
	 * @throws MessagingException if something goes wrong with java mail
	 * @throws IOException if something goes wrong in general.
	 */
	public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
	/**
	 * creates a javamail message to be sent (or transfered to gmail message to be sent)
	 * @param to which person
	 * @param from whom
	 * @param subject of the email
	 * @param bodyText the user will see
	 * @return a java mail message
	 * @throws MessagingException if something goes wrong with javamail
	 */
	public static MimeMessage createEmail(String to,String from,String subject,String bodyText)throws MessagingException {
		Properties props = new Properties();
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
		
		MimeMessage email = new MimeMessage(session);
		
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO,
		new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
    }
	/**
	 * creates a gmail message using javamail as a base
	 * @param to to who it goes to
	 * @param subject the subject the user will see
	 * @param bodyText what they see
	 * @return a gmail message
	 * @throws MessagingException if something occurs with javamail
	 * @throws IOException if something occurs in gmail
	 */
	public static Message createMessageSend(String to,String subject,String bodyText) throws MessagingException, IOException {
		return createMessageWithEmail(createEmail(to,"me",subject,bodyText));
	}
	/**
	 * sends multiple string message to these addresses
	 * @param sendto whom to send to
	 * @param subject each individual will see
	 * @param bodytext for each individual
	 * @throws IOException if something occurs in gmail or transit
	 * @throws MessagingException if something occurs with javamail
	 */
	public void sendMessages(String[] sendto, String[]  subject, String[] bodytext) throws IOException, MessagingException{
		if(sendto.length!= subject.length && sendto.length != bodytext.length){
			throw new RuntimeException("Invalid amount of data");
		}
		for(int i = 0; i < sendto.length;i++){
			service.users().messages().send("me", createMessageSend(sendto[i],subject[i],bodytext[i])).execute();
		}
	}
}
