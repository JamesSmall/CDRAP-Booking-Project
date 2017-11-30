
package com.cdrap.GoogleAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.sheets.v4.Sheets;

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
public class DriveEssentials {
	private static final String APPLICATION_NAME = "BookingApp";
	//stored location of authorization file
	private static final java.io.File AUTHOERIZE_FILE = new java.io.File("spreadsheetData");
	
    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    
    //JSON factory from google
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	//http transporter
	private static HttpTransport HTTP_TRANSPORT;
	
	//scopes required by this project, basically all of google drive (because copy files, add/remove lots of data from sheets, and write emails)
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_COMPOSE,DriveScopes.DRIVE);
    //basic setup
	private static void setup() throws IOException, GeneralSecurityException{
		if(HTTP_TRANSPORT == null){//wont setup twice
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(AUTHOERIZE_FILE);
		}
    }
	public synchronized static Credential authorize() throws IOException, GeneralSecurityException{
		setup();
		//get authorization file, do Internet stuff if nessassary
		InputStream in =
	           DriveEssentials.class.getResourceAsStream("/client_id.json");
	        GoogleClientSecrets clientSecrets =
	            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	   //check scopes
	        // Build flow and trigger user authorization request.
	        GoogleAuthorizationCodeFlow flow =
	                new GoogleAuthorizationCodeFlow.Builder(
	                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setDataStoreFactory(DATA_STORE_FACTORY)
	                .setAccessType("offline")
	                .build();
        //generate credentials
	        Credential credential = new AuthorizationCodeInstalledApp(
	            flow, new LocalServerReceiver()).authorize("user");
	       
	        return credential;
	}
	/**
	 * Gets Drive file Service
	 * @return Drive Service
	 * @throws IOException is something goes wrong
	 * @throws GeneralSecurityException if user declines login
	 */
	public synchronized static Drive getDriveService() throws IOException, GeneralSecurityException {
		//get credentials
        Credential credential = authorize();
        //build Drive
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

	/**
	 * Gets Sheets service
	 * @return Sheets service
	 * @throws IOException is something goes wrong
	 * @throws GeneralSecurityException if user declines login
	 */
	public synchronized static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		//get Credentials
        Credential credential = authorize();
        //build Sheets handler
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        
    }
	/**
	 * Gets gmail service
	 * @return Gmail Service
	 * @throws IOException is something goes wrong
	 * @throws GeneralSecurityException if user declines login
	 */
	public static Gmail getGmailService() throws IOException, GeneralSecurityException {
			//get Credentials
	        Credential credential = authorize();
	        //build Gmail Sender
	        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
	                .setApplicationName(APPLICATION_NAME)
	                .build();
    }
}
