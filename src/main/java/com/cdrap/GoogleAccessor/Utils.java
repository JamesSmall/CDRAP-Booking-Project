package com.cdrap.GoogleAccessor;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cdrap.ErrorManager.InvalidInputError;
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
public class Utils {
	//character to do math on.
	public static final char CHARACTER_POS = 'A';
	public static final int MAX_CHARACTER = ('Z'-'A')+1;
	public static final String SHEET_SEPERATOR_TO_POSITION = "!";
	public static final String SHEET_SEPERATOR_POSITIONS = ":";
	private Utils(){
		
	}
	public static final String convertXToLetter(int n){
		//0 based calculation, converts to google based api positioning
		n++;
		//formula to get percise char array size needed
		char[] buf = new char[(int) Math.floor(Math.log(25*(n+1))/Math.log(26))];
		for(int i = buf.length-1;i >=0;i--){
			//reduce by 1 to remove off by one error
			n--;
			//get asssossiated character with slot
			buf[i] = (char) (CHARACTER_POS + n%MAX_CHARACTER);
			//divide down.
			n /= MAX_CHARACTER;
		}
		//return as string
		return new String(buf);
	}
	public static String toStringY(int y){
		//0 based calculation, converts to google sheets api positioning
		return ""+(y+1);
	}
	public static String convertToPosition(int x, int y){
		//super basic math
		return ""+convertXToLetter(x)+toStringY(y);
	}
	public static String convertToPosition(String sheet,int x, int y){
		return sheet+SHEET_SEPERATOR_TO_POSITION+convertToPosition(x,y);
	}
	public static String convertToPosition(String sheet,int x1, int y1, int x2, int y2){
		return convertToPosition(sheet,x1,y1)+SHEET_SEPERATOR_POSITIONS+convertToPosition(x2,y2);
	}
	public static String convertToPosition(LocationRequest request){
		System.out.println(convertToPosition(request.getSheet(),request.getX1(),request.getY1(),request.getX2(),request.getY2()));
		return convertToPosition(request.getSheet(),request.getX1(),request.getY1(),request.getX2(),request.getY2());
	}
	//*** switch String Day to Integer" ****
	public static int getDaySelect(String day){
		switch(day.toLowerCase()){
			case "monday":
				return 0;
			case "tuesday":
				return 1;
			case "wednesday":
				return 2;
			case "thursday":
				return 3;
			case "friday":
				return  4;
			case "saturday":
				return 5;
			case "sunday":
			default:
				return 6;
		}
	}
	public static String toDay(int day){
		switch(day%7){
			case 0:
				return "Monday";
			case 1:
				return "Tuesday";
			case 2:
				return "Wednsday";
			case 3:
				return "Thursday";
			case 4:
				return "Friday";
			case 5:
				return "Saturday";
			case 6:
				return "Sunday";
		}
		//an error has occured.
		throw new InvalidInputError("Invalid input, day to string mishap");
	}
	/**
	 * used in overlap detection, it returns the distance between two points on a circufrance calculator.
	 * @param x datapoint 1
	 * @param y datapoint 2
	 * @param max rolloverpoint
	 * @return distance between x and y
	 */
	public static int getCircularDistance(int x, int y, int max){
		return Math.min(Math.abs(x-y), max-Math.abs(x-y));
	}
	/**
	 * supports direct minutes or hours speperated by : from minutes
	 * @param fragment string to be converted to int time
	 * @return int of converted string
	 */
	public static int convertToTime(String fragment){
		if(fragment.split(":").length==1){
			int frag = Integer.parseInt(fragment);
			int hour = frag/60;
			int fragminunte = frag%60;
			int ret = (hour*4)+(fragminunte/15);
			switch(fragminunte){
				case 15:
				case 30:
				case 45:
				case 0:
					break;
				default:
					ret++;	
			}
			return ret;
		}
		else{
			String[] frags = fragment.split(":");
			int hour = Integer.parseInt(frags[0]);
			int minutes = Integer.parseInt(frags[1]);
			int fragminunte = minutes;
			int ret = (hour*4)+(fragminunte/15);
			switch(fragminunte){
				case 15:
				case 30:
				case 45:
				case 0:
					break;
				default:
					ret++;	
			}
			return ret;
		}
	}
	public static List<List<Object>> generateEmptyFakeDataList(int width, int height){
		return new AbstractList<List<Object>>(){

			@Override
			public List<Object> get(int arg0) {
				return new AbstractList<Object>(){
					public Object get(int arg0){
						return "";
					}
					
					@Override
					public int size() {
						return width;
					}
				};
			}

			@Override
			public int size() {
				return height;
			}
		};
	}
}
