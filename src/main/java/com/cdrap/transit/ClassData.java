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
public class ClassData {
	private String monday, tuesday,wednesday,thursday,friday,saturday,sunday;
	private ClassSession parent;
	private int time;
	private boolean hasUpdated;
	
	/**
	 * this constructor builds class data.
	 * @param parent the class session that this originate from
	 * @param time slot for this row of data
	 * @param monday data
	 * @param tuesday data
	 * @param wendsday data
	 * @param thursday data
	 * @param friday data
	 * @param saturday data
	 * @param sunday data
	 */
	public ClassData(ClassSession parent,int time,String monday, String tuesday, String wendsday, String thursday, String friday, String saturday,
			String sunday) {
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wendsday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.time = time;
		this.parent = parent;
	}
	/**
	 * returns the parent of this classdata
	 * @return ClassSession
	 */
	public ClassSession getParent(){
		return parent;
	}
	/**
	 * returns true if it has any setValue() has been called
	 * @return true if setValue was called
	 */
	public boolean hasUpdated(){
		return hasUpdated;
	}
	/**
	 * returns true if any was called, than it resets the hasUpdated watcher
	 * @return true if set value was called
	 */
	public boolean refreshHasUpdated(){
		boolean hu = hasUpdated();
		this.hasUpdated = false;
		return hu;
	}
	/**
	 * returns data related to monday
	 * @return mondays data
	 */
	public String getMonday() {
		return monday;
	}
	/**
	 * sets mondays data, forces hasUpdated to return true
	 * @param monday value to be set
	 */
	public void setMonday(String monday) {
		this.monday = monday;
		hasUpdated = true;
	}
	/**
	 * returns data related to tuesday
	 * @return tuesday value
	 */
	public String getTuesday() {
		return tuesday;
	}
	/**
	 * sets tuesday value, forces hasUpdated to return true
	 * @param tuesday value to set
	 */
	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
		hasUpdated = true;
	}
	/**
	 * return data related to wednesday
	 * @return wednesday value
	 */
	public String getWednesday() {
		return wednesday;
	}
	/**
	 * sets wednesday value, forces hasUpdated to return true
	 * @param wednesday value to set
	 */
	public void setWednsday(String wednesday) {
		this.wednesday = wednesday;
		hasUpdated = true;
	}
	/**
	 * returns data related to thursday
	 * @return thursday value
	 */
	public String getThursday() {
		return thursday;
	}
	/**
	 * sets thursday value, forces hasUpdated to return true
	 * @param thursday value to set
	 */
	public void setThursday(String thursday) {
		this.thursday = thursday;
		hasUpdated = true;
	}
	/**
	 * returns data related to friday
	 * @return friday value
	 */
	public String getFriday() {
		return friday;
	}
	/**
	 * sets friday value, forces hasUpdated to return true
	 * @param friday  value to set
	 */
	public void setFriday(String friday) {
		this.friday = friday;
		hasUpdated = true;
	}
	/**
	 * returns data related to Saturday
	 * @return saturday value
	 */
	public String getSaturday() {
		return saturday;
	}
	/**
	 * sets saturday value, forces hasUpdated to return true
	 * @param saturday value to set
	 */
	public void setSaturday(String saturday) {
		this.saturday = saturday;
		hasUpdated = true;
	}
	/**
	 * returns data related to sunday
	 * @return sunday value
	 */
	public String getSunday() {
		return sunday;
	}
	/**
	 * sets sunday value, forces hasUpdated to return true
	 * @param sunday to set
	 */
	public void setSunday(String sunday) {
		this.sunday = sunday;
		hasUpdated = true;
	}
	/**
	 * returns time of these data entries.
	 * @return int of Time
	 */
	public int getTime() {
		return time;
	}
	/**
	 * sets a value in Integer form
	 * @param time value to set
	 */
	public void setTime(int time) {
		this.time = time;
		hasUpdated = true;
	}
	/**
	 * return time as String
	 * @return string of time
	 */
	public String getMilitaryTime(){
        return convertIntoMilitaryTime(this.time);
    }
	/**
	 * 
	 * @param time in int to convert to time
	 * @return string of time
	 */
	public static String convertIntoMilitaryTime(int time){
        int hour = time/4;
        return ""+hour+":"+(time%4==1?"15":time%4==2?"30":time%4==3?"45":"00");
	}
	/**
	 * 
	 * @param time converts militaryTime to int
	 * @return int version of time
	 */
	public static int convertMilitaryToInt(String time){
		String[] frags = time.split(":");
		return (Integer.parseInt(frags[0])*4)+(Integer.parseInt(frags[1])/15);
	}
}
