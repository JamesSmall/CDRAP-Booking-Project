package com.cdrap.GoogleAccessor;

import com.google.api.services.drive.Drive;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class DriveAccessor {
	private Drive driver;
	private SheetHandler sheets;
	public DriveAccessor(SheetHandler handler) throws IOException, GeneralSecurityException{
		sheets = handler;
		driver = DriveEssentials.getDriveService();
	}
	/**
	 * 
	 * @param srcId of the file to copy from
	 * @param newName the name of the new file
	 * @throws IOException
	 */
	public String copyFile(String srcId, String newName) throws IOException{
		return driver.files().copy(srcId, new File()
										.setName(newName)).execute().getId();
	}
	/**
	 * https://drive.google.com/open?id=1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM
	 * 
	 * https://drive.google.com/open?id=1riTVmx0s2vEguD00-MHxOeXYvHwJcQV0v7AaEsy7sbk
	 * @throws GeneralSecurityException 
	 * @throws IOException 
	 */
	public static void main(String...strings) throws IOException, GeneralSecurityException{
		System.out.println(new DriveAccessor(new SheetHandler()).copyFile("1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM", "Copy test"));
	}
	/**
	 * 
	 * @return an ArrayList of SheetFileContainers
	 * @throws IOException when not connected to the internet, hit the daily limmits allowed by google docs, or other Unknown IO errors
	 */
	public synchronized ArrayList<SheetFileContainer> getFileReferences() throws IOException{
		//gets blocked files
		HashSet<String> blocked = BlockListHandler.getBlockedFileIds();
		//to return
		ArrayList<SheetFileContainer> ret = new ArrayList<>();
		//File is using google api, not java file
		List<File> files  = getAllFiles("mimeType='application/vnd.google-apps.spreadsheet'","nextPageToken, files(id, name,trashed)"); //get these files
		if(files == null || files.isEmpty()){
			//not even a template is useable, the app can't work without a sheet already inside the repository (google drive)
			throw new IOException("No Valid Files Found");
		}
		else{
			//file is google file, not java file
			for(File f:files){
				//if the blocklist does not contain the file id, check the file
				if(!blocked.contains(f.getId())){
					//if files not trash, look at it
					if(!f.getTrashed()){
						//create a container
						SheetFileContainer container = this.sheets.checkFile(f.getId(),f.getName());
						if(container!=null){
							ret.add(container);
						}
						else{//container was not made, likely cause was metadata not found
							BlockListHandler.blockFile(f.getId(), f.getName(),BlockListHandler.BLOCK_REASON_INVALID,BlockListHandler.BLOCK_REASON_INVALID_MISSING_METADATA);
						}
					}
					else{//the file was trash, mark it as ignore
						BlockListHandler.blockFile(f.getId(),f.getName(), BlockListHandler.BLOCK_REASON_TRASH,BlockListHandler.BLOCK_REASON_TRASH_MARKED_AS_TRASH);
					}
				}
			}
		}
		//final check to make sure it all didnt go up in smoke
		if(ret.isEmpty()){
			throw new IOException("Not drive files found");
		}
		//returned wanted items
		return ret;
	}
	/**
	 * 
	 * @param filterType collects file with this specific filter
	 * @param fields feilds wanted
	 * @return ArrayList of Google files
	 * @throws IOException if something goes wrong in transit
	 */
	public synchronized ArrayList<File> getAllFiles(String filterType, String fields) throws IOException{
		ArrayList<File> result = new ArrayList<>();
		com.google.api.services.drive.Drive.Files.List request = driver.files().list();
		///set required fields
		request.setQ(filterType);
		request.setFields(fields);
		//set page size(at max), will requst more if need be in do-while loop
		request.setPageSize(1000);
		do {
			//get files
	        FileList files = request.execute();
	        //add current found files
	        result.addAll(files.getFiles());
	        //set next page token, then go!!!
	        request.setPageToken(files.getNextPageToken());
	    } while (request.getPageToken() != null && //while more tokens exist, get more files
		             request.getPageToken().length() > 0);
		//return found files
		return result;
	}
}
