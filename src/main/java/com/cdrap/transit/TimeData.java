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
public class TimeData {
	private String[] elements;
	private String room;
	/**
	 * 
	 * @param room of this element
	 * @param elements timeframe and data of these entries
	 */
	public TimeData(String room, String... elements){
		this.room = room;
		this.elements = elements;
	}
	/**
	 * 
	 * @return elements as an array
	 */
	public String[] getElements() {
		return elements;
	}
	/**
	 * 
	 * @param elements array to set
	 */
	public void setElements(String[] elements) {
		this.elements = elements;
	}
	/**
	 * 
	 * @return room of this TimeData
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * 
	 * @param room value of this entry to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	
}
