package com.cdrap.transit;
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
public class ClassFragment {
	private int startTime,endTime;
	//private int requestY;
	private int session; //duplicated for each instance
	private int day;
	private String season;
	private String inputText;
	
	private String name,course,room;
	//notes if updated or not
	private boolean accepted;
	
	
	private String email; //NOT A SHEET INPUT
	
	/**
	 * 
	 * @param fileid fileid to handle
	 * @param startTime to start at
	 * @param endTime time fragment to end at
	 * @param season season to select to
	 * @param session session num to select to
	 * @param day to select to
	 * @param name of the user to input
	 * @param course to be inserted
	 * @param room number to insert into
	 * @param accepted value allowed
	 * @param email to insert into
	 * @param requesty yPos of the accepter
	 */
	public ClassFragment(String fileid,int startTime, int endTime, String season, int session,int day, String name, String course,String room, boolean accepted,
			String email) {
		super();
		this.startTime = startTime;
		this.endTime = endTime-1;//this fixes a techical off by one error.
		this.session = session;
		this.season = season;
		this.inputText = name+"\n"+course;
		this.name = name;
		this.course = course;
		this.accepted = accepted;
		this.email = email;
		this.day = day;
		this.room = room;
		//this.requestY = requesty;
	}
	/**
	 * 
	 * @return room contained in this data entry
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * room to set
	 * @param room value to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	/**
	 * name of the data entry
	 * @return name value
	 */
	public String getName() {
		return name;
	}
	/**
	 * sets name value of this entry
	 * @param name value to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * course number to return
	 * @return course num  value to return
	 */
	public String getCourse() {
		return course;
	}
	/**
	 * sets the course number to set in this dataset
	 * @param course value to set
	 */
	public void setCourse(String course) {
		this.course = course;
	}
	/**
	 * starting day value to return
	 * @return day value
	 */
	public int getDay() {
		return day;
	}
	/**
	 * sets the starting day value
	 * @param day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	/**
	 * startTime within the dataset
	 * @return string values
	 */
	public int getStartTime() {
		return startTime;
	}
	/**
	 * starting time frame
	 * @param startTime value to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	/**
	 * lasting time for this dataset
	 * @return lastingtime value
	 */
	public int getEndTime() {
		return endTime;
	}
	/**
	 * sets the lasting time for this dataset
	 * @param endTime value to set
	 */
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	/**
	 * returns session value (1-5)
	 * @return int of session value
	 */
	public int getSession() {
		return session;
	}
	/**
	 * value of session to set
	 * @param session to set
	 */
	public void setSession(int session) {
		this.session = session;
	}
	/**
	 * getSeason string
	 * @return season
	 */
	public String getSeason() {
		return season;
	}
	/**
	 * season of the string
	 * @param season of the string
	 */
	public void setSeason(String season) {
		this.season = season;
	}
	/**
	 * returns input text for each cell
	 * @return text to insert 
	 */
	public String getInputText() {
		return inputText;
	}
	/**
	 * sets the input text of all cell entries
	 * @param inputText of cells
	 */
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	/**
	 * has been accepted
	 * @return isAccepted
	 */
	public boolean isAccepted() {
		return accepted;
	}
	/**
	 * sets if it is accepted or not
	 * @param accepted value to set
	 */
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	/**
	 * gets the current email
	 * @return email value returned
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * email setter
	 * @param email value to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
