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
public class RequestSession {
	private String name, email,courseNum,CRN,semester,sessions,day,room,notes,request,status;
	private int startTime,timeFrame;
	private int down = 0;
	/**
	 * 
	 * @param name entry of this request session
	 * @param email entry of this request session
	 * @param courseNum entry of this request session
	 * @param cRN entry of this request session
	 * @param semester entry of this request session
	 * @param sessions entry of this request session
	 * @param day entry of this request session
	 * @param room entry of this request session
	 * @param startTime entry of this request session
	 * @param timeFrame entry of this request session
	 * @param notes entry of this request session
	 * @param request entry of this request session
	 * @param status entry of this request session
	 * @param loc entry of this request session
	 */
	public RequestSession(String name, String email, String courseNum, String cRN, String semester, String sessions,
			String day, String room, int startTime, int timeFrame, String notes, String request, String status, int loc) {
		super();
		this.name = name;
		this.email = email;
		this.courseNum = courseNum;
		CRN = cRN;
		this.semester = semester;
		this.sessions = sessions;
		this.day = day;
		this.room = room;
		this.notes = notes;
		this.request = request;
		this.status = status;
		this.startTime = startTime;
		this.timeFrame = timeFrame;
		down = loc;
	}
	/**
	 * 
	 * @return YLocation entry of this request session
	 */
	public int getRequestY(){
		return down;
	}
	/**
	 * 
	 * @return name entry of this request session
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name value to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return email entry of this request session
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 
	 * @param email value to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 
	 * @return CoursesNum of this entry of this request session
	 */
	public String getCourseNum() {
		return courseNum;
	}
	/**
	 * 
	 * @param courseNum to set of this requested entry
	 */
	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}
	/**
	 * 
	 * @return course CRN to display
	 */
	public String getCRN() {
		return CRN;
	}
	/**
	 * 
	 * @param CRN to set
	 */
	public void setCRN(String CRN) {
		this.CRN = CRN;
	}
	/**
	 * 
	 * @return semester of requested session
	 */
	public String getSemester() {
		return semester;
	}
	/**
	 * 
	 * @param semester value to set of this requested session
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}
	/**
	 * returns a string seperated by ',' of this session
	 * @return get sessions requested by this 
	 */
	public String getSessions() {
		return sessions;
	}
	/**
	 * use ',' to allow multiple session in this request
	 * @param sessions as a string
	 */
	public void setSessions(String sessions) {
		this.sessions = sessions;
	}
	/**
	 * @return day of this entry
	 */
	public String getDay() {
		return day;
	}
	/**
	 * 
	 * @param day value to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * 
	 * @return room number
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * 
	 * @param room value to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	/**
	 * 
	 * @return notes about this request
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * 
	 * @param notes about this request to be set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * 
	 * @return request acceptace by user
	 */
	public String getRequest() {
		return request;
	}
	/**
	 * 
	 * @param request value to be set
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	/**
	 * 
	 * @return status of this request
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 
	 * @param status of this value to be set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 
	 * @return startTime of this data entry
	 */
	public int getStartTime() {
		return startTime;
	}
	/**
	 * 
	 * @param startTime of this dataEntry
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	/**
	 * 
	 * @return the amountOfTime Slots this takes
	 */
	public int getTimeFrame() {
		return timeFrame;
	}
	/**
	 * 
	 * @param timeFrame of this dataEntry
	 */
	public void setTimeFrame(int timeFrame) {
		this.timeFrame = timeFrame;
	}
	
}
