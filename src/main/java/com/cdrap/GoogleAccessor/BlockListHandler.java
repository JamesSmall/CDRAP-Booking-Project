package com.cdrap.GoogleAccessor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
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
public class BlockListHandler {
	public static final String BLOCKLIST_DIR = "spreadsheetData";
	public static final String BLOCKLIST_FILE = "BlockList.csv";
	public static final String BLOCK_REASON_INVALID = "invalid_file";
	public static final String BLOCK_REASON_INVALID_MISSING_METADATA = "Metadata page was not found or valid";
	public static final String BLOCK_REASON_TRASH = "trashed_file";
	public static final String BLOCK_REASON_TRASH_MARKED_AS_TRASH = "File was marked as trash by google drive API.";
	private static final File blocklistFile;
	
	static{
		//puts file refernce directory in memory
		File f = new File(BLOCKLIST_DIR);
		if(!f.exists()){
			f.mkdirs();
		}
		blocklistFile = new File(BLOCKLIST_DIR+File.separator+BLOCKLIST_FILE);
	}
	/**
	 * a method that reads the correct file and returns the ids within the css file.
	 * @return map of blockedFileIds()
	 */
	public static HashSet<String> getBlockedFileIds(){
		HashSet<String> ids = new HashSet<>();
		if(blocklistFile.exists()){
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(blocklistFile))))){
				String current;
				while((current = reader.readLine()) !=null){
					ids.add(current.split(",")[0]);
				}
			}
			catch(IOException ex){
				ErrorLog.writeError(ex, ErrorLevel.FILE_IO);
			}
		}
		return ids;
	}
	/**
	 * This is used to make future looks ups of spreadsheets both cheaper(IO COST) and easier on the engine
	 * @param id the file to be blocked
	 * @param fileName name of the file
	 * @param reasonType type of blocking
	 * @param reason why the file was blocked
	 */
	public static void blockFile(String id,String fileName, String reasonType,String reason){
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(blocklistFile,true))))){
			//write csv lines
			writer.write(id+","+fileName+","+reasonType+","+reason);
			//next line
			writer.newLine();
		}
		catch(IOException ex){
			ErrorLog.writeError(ex, ErrorLevel.FILE_IO);
		}
	}
}
