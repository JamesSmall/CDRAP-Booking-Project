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
public class LocationRequest {
	private int x1,x2,y1,y2;
	private String sheet;
	public LocationRequest(String sheet, int x1,int y1, int x2, int y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.sheet = sheet;
	}
	/**
	 * 
	 * @param x1 and x2 this direction
	 */
	public void shiftX(int x){
		x1+=x;
		x2+=x;
	}
	/**
	 * 
	 * @param y shift y1 and y2 this direction
	 */
	public void shiftY(int y){
		y1+=y;
		y2+=y;
	}
	/**
	 * 
	 * @return lesserX
	 */
	public int getX1() {
		return x1;
	}
	/**
	 * 
	 * @param x1 value to be set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}
	/**
	 * 
	 * @return greaterX
	 */
	public int getX2() {
		return x2;
	}
	/**
	 * 
	 * @param x2 value to be set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}
	/**
	 * 
	 * @return getLesserY
	 */
	public int getY1() {
		return y1;
	}
	/**
	 * 
	 * @param y1 value to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}
	/**
	 * 
	 * @return getGreaterY
	 */
	public int getY2() {
		return y2;
	}
	/**
	 * 
	 * @param y2 value to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}
	/**
	 * 
	 * @return Sheet To send To
	 */
	public String getSheet() {
		return sheet;
	}
	/**
	 * 
	 * @param sheet value to be set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	/**
	 * 
	 * @return a copy of this value
	 */
	public LocationRequest createCopy(){
		return new LocationRequest(sheet,x1,y1,x2,y2);
	}
	
}
