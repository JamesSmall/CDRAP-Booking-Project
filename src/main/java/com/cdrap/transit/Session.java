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
public class Session {
    
	private String fileID;
    private int year;
    private String season;
    private int session;
    private String fileName;
    /**
     * 
     * @param fileID of this session
     * @param fileName of this session
     * @param year of this session
     * @param season of this session
     * @param session number of this session
     */
    public Session(String fileID,String fileName,int year, String season, int session) {
        this.year = year;
        this.season = season;
        this.session = session;
        this.fileID = fileID;
        this.fileName = fileName;
    }
    /**
     * 
     * @return year of this session
     */
    public int getYear() {
        return year;
    }
    /**
     * 
     * @param year of this session
     */
    public void setYear(int year) {
        this.year = year;
    }
    /**
     * 
     * @return the name of this file
     */
    public String getFileName(){
    	return this.fileName;
    }
    /**
     * 
     * @return season of this entry
     */
    public String getSeason() {
        return season;
    }
	/**
	 * 
	 * @param season season of this entry
	 */
    public void setSeason(String season) {
        this.season = season;
    }
    /**
     * 
     * @return session of this entry
     */
    public int getSession() {
        return session;
    }
    /**
     * 
     * @param session of this entry
     */
    public void setSession(int session) {
        this.session = session;
    }
    /**
     * 
     * @return fileId of htis sesson
     */
    public String getFileID(){
    	return fileID;
    }
    
}
