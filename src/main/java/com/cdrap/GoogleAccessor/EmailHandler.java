package com.cdrap.GoogleAccessor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import com.cdrap.transit.ClassFragment;
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
public class EmailHandler {
	//email controller
	private EmailAccessor access;
	public EmailHandler() throws IOException, GeneralSecurityException{
		access = new EmailAccessor();
	}
	/**
	 * transfers class fragments into emails to be sent
	 * @param frags collection of data to be sent
	 * @throws IOException if something happens in transit
	 * @throws MessagingException if something happens in java mail
	 */
	public void prepareEmails(Collection<ClassFragment> frags) throws IOException, MessagingException{
		HashMap<String,EmailContainer> c = new HashMap<>();
		for(ClassFragment f:frags){
			//iterate through the emails, get each address
			EmailContainer ec = c.get(f.getEmail());
			//if email not assigned, create one
			if(ec == null){
				ec = new EmailContainer();
				c.put(f.getEmail(), ec);
			}
			//if entry is accepted, add to accepted list, otherwise decline
			if(f.isAccepted()){
				ec.acceptedClasses.add("\nCourse: "+ f.getCourse()+" On Day:"+f.getDay());
			}
			//if entry is accepted, add to accepted list, otherwise decline
			else{
				ec.declinedClasses.add("\nCourse: "+ f.getCourse()+" On Day:"+f.getDay());
			}
		}
		
		//sorted items to be sent
		String[] elements = new String[c.size()];
		String[] header = new String[c.size()];
		String[] to = new String[c.size()];
		int i = 0;
		for(Entry<String,EmailContainer> cc:c.entrySet()){
			//sorts items to be sent to the strings
			to[i] = cc.getKey();
			header[i] = "Class Schedueling";
			elements[i] = "The following requested classes were approved:";
			for(String s:cc.getValue().acceptedClasses){
				elements[i] += s;
			}
			elements[i] += "The following requested classes could not be accepted";
			for(String s:cc.getValue().declinedClasses){
				elements[i] += s;
			}
			i++;
		}
		//now send the message
		access.sendMessages(to, header, elements);
	}
	//messages entries to be sent
	private class EmailContainer{
		ArrayList<String> acceptedClasses = new ArrayList<>();
		ArrayList<String> declinedClasses = new ArrayList<>();
	}
}
