package com.cdrap.GoogleAccessor;
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
public class SheetFileContainer {
	private String fileid;
	private String fileName;
	private int year;
	private int SummerSessions;
	private int FallSession;
	private int SpringSessions;
	/**
	 * 
	 * @param fileid of this file
	 * @param fileName of this file
	 * @param year of this selected file
	 * @param summerSessions of this selected file
	 * @param fallSession of this selected file
	 * @param springSessions of this selected file
	 */
	public SheetFileContainer(String fileid,String fileName,int year, int summerSessions, int fallSession, int springSessions) {
		this.fileid = fileid;
		this.fileName = fileName;
		this.year = year;
		SummerSessions = summerSessions;
		FallSession = fallSession;
		SpringSessions = springSessions;
	}
	/**
	 * 
	 * @return File Name
	 */
	public String getFileName(){
		return fileName;
	}
	/**
	 * 
	 * @param name of this file
	 */
	public void setFileName(String name){
		this.fileName = name;
	}
	/**
	 * 
	 * @return id of this file
	 */
	public String getFileid() {
		return fileid;
	}
	/**
	 * 
	 * @param fileid of this file
	 */
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	/**
	 * 
	 * @return number of Sessions for summer
	 */
	public int getSummerSessions() {
		return SummerSessions;
	}
	/**
	 * 
	 * @param summerSessions value set
	 */
	public void setSummerSessions(int summerSessions) {
		SummerSessions = summerSessions;
	}
	/**
	 * 
	 * @return number of Sessions for Fall
	 */
	public int getFallSessions() {
		return FallSession;
	}
	/**
	 * 
	 * @param fallSession value to set
	 */
	public void setFallSession(int fallSession) {
		FallSession = fallSession;
	}
	/**
	 * 
	 * @return number of Sessions for Spring
	 */
	public int getSpringSessions() {
		return SpringSessions;
	}
	/**
	 * 
	 * @param springSessions value to set
	 */
	public void setSpringSessions(int springSessions) {
		SpringSessions = springSessions;
	}
	/**
	 * 
	 * @return year
	 */
	public int getYear(){
		return year;
	}
	/**
	 * 
	 * @param year to be set
	 */
	public void setYear(int year){
		this.year = year;
	}
	
}
