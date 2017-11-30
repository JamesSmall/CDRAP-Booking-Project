package com.cdrap.transit.UI;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
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
public class ResourceList<T> extends ResourceBundle{
	private LinkedList<T> collections = new LinkedList<T>();
	/**
	 * 
	 * @param objs collection of objects to display
	 */
	public ResourceList(Collection<T> objs){
		this.collections.addAll(objs);
	}
	/**
	 * 
	 * @return linkedlist of dataobjects to display
	 */
	public LinkedList<T> getList(){
		return collections;
	}
	
	//Unused by code.
	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	//unused in code
	@Override
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
