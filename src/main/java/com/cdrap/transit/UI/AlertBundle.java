package com.cdrap.transit.UI;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
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
public class AlertBundle extends ResourceBundle{
	private String displayText;
	private LinkedHashMap<String,Runnable> onclick = new LinkedHashMap<>();
	
	/**
	 * 
	 * @param text warning to display
	 */
	public AlertBundle(String text) {
		// TODO Auto-generated constructor stub
		this.displayText = text;
	}
	/**
	 * 
	 * @return display text that is displayed
	 */
	public String getDisplayText() {
		return displayText;
	}
	/**
	 * 
	 * @param displayText to set
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	/**
	 * 
	 * @return buttons entries to display
	 */
	public LinkedHashMap<String, Runnable> getButtons() {
		return onclick;
	}
	/**
	 * 
	 * @param onclick directly set LinkedHashMap inside
	 */
	public void setButtons(LinkedHashMap<String, Runnable> onclick) {
		this.onclick = onclick;
	}
	/**
	 * !Ordered Operation
	 * @param str text of the button
	 * @param run method to run when the button is inserted
	 */
	public void addButton(String str, Runnable run){
		this.onclick.put(str, run);
	}
	/**
	 *unused method
	 */
	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * unused method
	 */
	@Override
	protected Object handleGetObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
