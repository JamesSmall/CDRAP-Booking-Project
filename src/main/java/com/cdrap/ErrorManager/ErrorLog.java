package com.cdrap.ErrorManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class ErrorLog {
	
	public static final String FILE_LOCATION_BASE = "spreadsheetData";
	public static final String FILE = "Log";
	public static final String EXTENSION = ".txt";
	private static File currentErrorLog;
	static{
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
		Date d = new Date();
		currentErrorLog = new File(FILE_LOCATION_BASE+File.separator+FILE+(dateFormat.format(d))+EXTENSION);
		checkDir();
	}
	public static void checkDir(){
		File f = new File(FILE_LOCATION_BASE);
		if(f.exists()){
			File[] files = f.listFiles();
			while(files.length > 5){
				int toRemoveLoc = -1;
				long lastUpdated = Long.MAX_VALUE;
				for(int i = 0; i < files.length;i++){
					if(lastUpdated > files[i].lastModified()){
						lastUpdated = files[i].lastModified();
						toRemoveLoc = i;
					}
				}
				files[toRemoveLoc].delete();
				files = f.listFiles();
			}
		}
		else{
			f.mkdirs();
		}
		
	}
	public static void writeError(Throwable thrower, ErrorLevel level){
		//claim basic file
		try (FileOutputStream fos = new FileOutputStream(currentErrorLog,true)){ 
			switch(level){
				case EXPECTED:
					fos.write(("this error is expected to happen, you can ignore this error"+System.lineSeparator()).getBytes());
					break;
				case INTERNET_ERROR:
					fos.write(("this error was caused due to some internet issues"+System.lineSeparator()).getBytes());
					break;
				case UNKNOWN_ERROR:
					fos.write(("this is an unknown error"+System.lineSeparator()).getBytes());
					break;
				case FILE_IO:
					fos.write(("this error is caused by file reading or writing problems"+System.lineSeparator()).getBytes());
					break;
				default:
						//user error is ignored
			}
			//automatic stream management, will autoclose when wrapped around like this when it leaves the brackets
			thrower.printStackTrace(new PrintStream(fos));
			thrower.printStackTrace(System.out);
			fos.write(System.lineSeparator().getBytes());
		} catch (IOException e) {
			//welp that really, really bad news, this probably means the harddrive is disconnected or is in restricted space. or is locked
			e.printStackTrace();
		}
	}
	public static void main(String...strings){
		writeError(new Throwable(),ErrorLevel.INTERNET_ERROR);
	}
	
}
