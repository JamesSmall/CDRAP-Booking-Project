/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class ClassSession {
	private Session parent;
    private String room;
    private int YPointer = -1;
    /**
     * 
     * @param parent of this class Session
     * @param room number of this class session
     * @param yPointer location on the massivelist
     */
    public ClassSession(Session parent,String room,int yPointer) {
        this.room = room;
        this.parent = parent;
        this.YPointer = yPointer;
    }
    /**
     * returns this rooms name
     * @return rooms name
     */
    public String getRoom() {
        return room;
    }
    /**
     * returns the y pointer of this room which locates the rooms location on the spreadsheet
     * @return distancedown on the page
     */
    public int getYPointer(){
    	return YPointer;
    }
    /**
     * the name of the room to be set
     * @param room value to set
     */
    public void setRoom(String room) {
        this.room = room;
    }
    /**
     * gets the parent of this Room
     * @return parent of this entry
     */
    public Session getParent(){
    	return parent;
    }
    
}
