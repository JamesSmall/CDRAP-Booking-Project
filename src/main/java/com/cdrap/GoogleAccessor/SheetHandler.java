package com.cdrap.GoogleAccessor;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.ErrorManager.InvalidInputError;
import com.cdrap.GoogleAccessor.SheetsAccessor.RequestCompilePackage;
import com.cdrap.transit.ClassData;
import com.cdrap.transit.ClassFragment;
import com.cdrap.transit.ClassSession;
import com.cdrap.transit.RequestSession;
import com.cdrap.transit.Session;
import com.cdrap.transit.TimeData;
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
public class SheetHandler {
	public static final String SEASON_FALL_TAB = "Fall";
	public static final String SEASON_SPRING_TAB = "Spring";
	public static final String SEASON_SUMMER_TAB = "Summer";
	

	public static final String SEASON_FALL_TAB_LOWER_CASE = "fall"; // I would like to use SEASON_FALL_TAB.toLowerCase() but switch statements require it to be 'constant'
	public static final String SEASON_SPRING_TAB_LOWER_CASE = "spring";
	public static final String SEASON_SUMMER_TAB_LOWER_CASE = "summer";
	
	public static final String METADATA_PAGE = "Metadata";
	public static final int METADATA_MIN_X = 1;
	public static final int METADATA_MIN_Y = 1;
	public static final int METADATA_MAX_X = 5;
	public static final int METADATA_MAX_Y = 1;
	public static final int METADATA_SIZE_X = METADATA_MAX_X-METADATA_MIN_X+1;
	
	public static final int METADATA_LOCATION_YEAR = 0;
	public static final int METADATA_ROW = 0;
	public static final int METADATA_LOCATION_TEMPLATE_CHECK = 1;
	public static final int METADATA_LOCATION_SUMMER = 2;
	public static final int METADATA_LOCATION_FALL = 3;
	public static final int METADATA_LOCATION_SPRING = 4;
	
	public static final int CLASS_DATA_OFFSET_X = 1;//toget to first cell
	public static final int SPACE_BETWEEN_SETS_X = 2; //includes times on left
	public static final int DAYS_IN_WEEK = 7;
	public static final int TOTAL_SIZE_X = DAYS_IN_WEEK+SPACE_BETWEEN_SETS_X+CLASS_DATA_OFFSET_X;
	
	public static final int CLASS_DATA_OFFSET_Y = 2;
	public static final int EMPTY_SPACE_Y = 4;
	public static final int DATA_SIZE_Y = 95;
	public static final int TOTAL_SIZE_Y = DATA_SIZE_Y+EMPTY_SPACE_Y+CLASS_DATA_OFFSET_Y;
	
	public static final int CLASS_START_TIME = 28;
	public static final int DATA_ROLL_OVER_POINT = CLASS_START_TIME;
	public static final int NEXT_DAY_SLOT = 96;
	public static final String ROOM_LOCATION = "Room Numbers";
	
	
	public static final String REQUEST_PAGE = "Requests";
	public static final String REQUEST_PAGE_DIR = REQUEST_PAGE+"!A12:O";
	public static final String REQUEST_PAGE_DIR_SPLIT1 = REQUEST_PAGE+"!A12:I";
	public static final String REQUEST_PAGE_DIR_SPLIT2 = REQUEST_PAGE+"!K12:O";
	//public static final int REQUEST_PAGE_MIN_Y = 11; //off by 1, 11 is starting cell
	public static final int REQUEST_PAGE_NAME = 0;
	public static final int REQUEST_PAGE_EMAIL = 1;
	public static final int REQUEST_PAGE_COURSE = 2;
	public static final int REQUEST_PAGE_COURSE_CRN = 3;
	public static final int REQUEST_PAGE_SEMESTER = 4;
	public static final int REQUEST_PAGE_SESSIONS = 5;
	public static final int REQUEST_PAGE_DAY = 6;
	public static final int REQUEST_PAGE_ROOM_NUMBER = 7;
	public static final int REQUEST_PAGE_TIME = 8;
	public static final int REQUEST_PAGE_TIME_STANDARD = 9;
	public static final int REQUEST_PAGE_DURATION = 10;
	public static final int REQUEST_PAGE_NOTES = 11;
	public static final int REQUEST_PAGE_REQUEST = 12;
	public static final int REQUEST_PAGE_ADMIN_NOTES = 13;
	public static final int REQUEST_PAGE_STATUS = 14;
	public static final int REQUEST_PAGE_WIDTH = 14;
	
	public static final int TIME_DATA_LOC_X = 26*2+1;
	public static final int TIME_DATA_LOC_X_MAX = TIME_DATA_LOC_X+3;
	public static final int TIME_DATA_LOC_Y = 1;
	public static final int TIME_DATA_LOC_Y_MAX = 98+TIME_DATA_LOC_Y;
	
	public static final int DATA_HEADERS_Y = 0;
	public static final int DATA_HEADER_Y_END = DATA_HEADERS_Y;
	public static final int DATA_HEADER_ROOMNUMBER_X = 2;
	public static final int DATA_HEADER_SESSION = 4;
	public static final int DATA_HEADER_YEAR = 7;
	
	public static final int MAX_SESSIONS = 5;
	
	public static final String TEMPLATE_PAGE = "Template";
	
	private SheetsAccessor accessor;
	public SheetHandler() throws IOException, GeneralSecurityException{
		accessor = new SheetsAccessor();
	}
	
	public SheetFileContainer checkFile(String id,String name) throws IOException{
		try{
			LocationRequest request = new LocationRequest(METADATA_PAGE,METADATA_MIN_X,METADATA_MIN_Y,METADATA_MAX_X,METADATA_MAX_Y);
			List<List<String>> metadata = accessor.compileQueryCells(id,Arrays.asList(request)).get(0);
			if(metadata.size()== 1 && metadata.get(METADATA_ROW).size() == METADATA_SIZE_X){
				return new SheetFileContainer(id,name,
					Integer.parseInt(metadata.get(METADATA_ROW).get(METADATA_LOCATION_YEAR)),
					Integer.parseInt(metadata.get(METADATA_ROW).get(METADATA_LOCATION_SUMMER)),
					Integer.parseInt(metadata.get(METADATA_ROW).get(METADATA_LOCATION_FALL)),
					Integer.parseInt(metadata.get(METADATA_ROW).get(METADATA_LOCATION_SPRING)));
			}
		}
		catch(com.google.api.client.googleapis.json.GoogleJsonResponseException ex){
			//something went wrong on reading metadata, this is an expected error
			if(ex.getStatusCode()==400){//bad request, expected of bad sheets, other errors might have other causes
				ErrorLog.writeError(ex, ErrorLevel.EXPECTED);
			}
			else{
				//another type of message
				throw ex;
			}
		}
		return null;
	}
	public static <T> int seperateTimeData(List<LocationRequest> headers,List<Integer> ints,List<List<List<T>>> returnSplit,List<List<T>> strings,String season, int session,int startTime,int timeFrame,int day){
		int splits = 0;
		int currentTime = startTime;
		int addationalFrames = timeFrame;
		int currentDay = day;
		int index = 0;
		while(addationalFrames > 0){
			splits++;
			if(currentTime < DATA_ROLL_OVER_POINT && DATA_ROLL_OVER_POINT < currentTime+addationalFrames){
				LocationRequest request = new LocationRequest(season,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT+(DATA_ROLL_OVER_POINT-currentTime-1));
				headers.add(request);
				//float tempCurrentTime = DATA_ROLL_OVER_POINT;
				if(strings != null){
					returnSplit.add(strings.subList(index, DATA_ROLL_OVER_POINT-currentTime));
				}
				addationalFrames -= DATA_ROLL_OVER_POINT-currentTime;
				ints.add(DATA_ROLL_OVER_POINT-currentTime);
				currentTime = DATA_ROLL_OVER_POINT;
			}
			else if(NEXT_DAY_SLOT < currentTime+addationalFrames){
				//ask question about time fragment
				LocationRequest request = new LocationRequest(season,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT+(NEXT_DAY_SLOT-currentTime-1));
				headers.add(request);
				if(strings != null){
					index += NEXT_DAY_SLOT-currentTime;
					returnSplit.add(strings.subList(index, NEXT_DAY_SLOT-currentTime));
				}
				addationalFrames -= NEXT_DAY_SLOT-currentTime;
				ints.add(NEXT_DAY_SLOT-currentTime);
				currentTime = 0;
				currentDay++;
				currentDay %= DAYS_IN_WEEK;
			}
			else{
				LocationRequest request = new LocationRequest(season,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT,
						(CLASS_DATA_OFFSET_X+currentDay)+((session-1)*TOTAL_SIZE_X),
						CLASS_DATA_OFFSET_Y+(currentTime-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT+(addationalFrames));
				//headers.add(request);
				headers.add(request);
				if(strings != null){
					returnSplit.add(strings.subList(index, addationalFrames));
				}
				ints.add(addationalFrames);
				addationalFrames = 0;
			}
		}
		return splits;
	}
	private TimeData getSingleTimeData(String fileid,String season,int session, int day,int startTime,int timeFrame,String roomStr) throws IOException{
		ArrayList<TimeData> ret = new ArrayList<>();
		ArrayList<ClassSession> tempRoom = getClassFragments(fileid);
		LinkedList<LocationRequest> toRequest = new LinkedList<>();
		LinkedList<Integer> ints = new LinkedList<>();
		
		
		ArrayList<LocationRequest> headers = new ArrayList<>();
		int splits = seperateTimeData(headers,ints,null,null,season,session,startTime,timeFrame,day);
		
		int roomShift = 0;
		for(ClassSession room:tempRoom){
			if(roomStr.equals(room.getRoom())){
				for(int i = 0; i < splits;i++){
					LocationRequest copy = headers.get(i).createCopy();
					copy.shiftY(TOTAL_SIZE_Y*room.getYPointer());
					toRequest.add(copy);
				}
				
			}
			roomShift++;
		}
		if(toRequest.isEmpty()){
			throw new InvalidInputError("Room not found");
		}
		roomShift = 0;
		int count = 0;
		ArrayList<List<List<String>>> items = accessor.compileQueryCells(fileid, toRequest);
		ArrayList<String> fragment = new ArrayList<>();
		//Major edit may be needed her for split
		for(List<List<String>> list:items){
			
			int i = 0;
			if(list !=null){
				for(;i < list.size();i++){
					if(list.get(i).size()!=0){
						fragment.add(list.get(i).get(0));
					}
					else{
						fragment.add("");
					}
				}
			}
			for(;i<ints.get(count%splits);i++){
				fragment.add("");
			}
			if(count%splits == splits-1){
				ret.add(new TimeData(tempRoom.get(roomShift).getRoom(),fragment.toArray(new String[fragment.size()])));
				roomShift++;
				fragment.clear();
			}
			count++;
		}
		return ret.get(0);
	}

	public ArrayList<ClassSession> getClassFragments(String fileid) throws IOException{
		return getSessions(fileid,null);
	}
	public ArrayList<ClassSession> getSessions(String fileid,Session parent) throws IOException{
		//LocationRequest request = new LocationRequest(ROOMS_NUMBER_PAGE,0,1,0,100);
		List<List<String>> ret = accessor.queryCells(fileid, Arrays.asList((SheetHandler.ROOM_LOCATION+"!A2:A"))).get(0);
		ArrayList<ClassSession> sessions = new ArrayList<>();
		int i = 0;
		for(List<String> r: ret){
			sessions.add(new ClassSession(parent,r.get(0),i));
			i++;
		}
		return sessions;
	}
	public void insertNewData(String file,Collection<ClassFragment> cf, Collection<RequestSession> noteworthy) throws IOException{
		ArrayList<ClassSession> tempRoom = getClassFragments(file);
		for(ClassFragment frag:cf){
			
			for(String z:getSingleTimeData(file,frag.getSeason(),frag.getSession(),frag.getDay(),frag.getStartTime(),frag.getEndTime(),frag.getRoom()).getElements()){
				if(!z.trim().isEmpty()){
					throw new InvalidInputError("data already exists, cannot insert data");
				}
			}
		}
		ArrayList<LocationRequest> headers = new ArrayList<>();
		List<List<List<Object>>> retSplit = new ArrayList<>();
		for(ClassFragment frag:cf){
			ArrayList<LocationRequest> temp = new ArrayList<>();
			List<List<Object>> outer = new ArrayList<>();
			for(int i = 0; i < frag.getEndTime();i++){
				outer.add(Arrays.asList(frag.getInputText()));
			}
			//a(List<LocationRequest> headers,List<Integer> ints,List<List<List<Object>>> returnSplit,List<List<Object>> strings,String season, int session,int startTime,int timeFrame,int day){
			seperateTimeData(temp,new ArrayList<>(),retSplit,outer,frag.getSeason(),frag.getSession(),frag.getStartTime(),frag.getEndTime(),frag.getDay());
			int y = -1;
			breakable:{
				for(ClassSession tr:tempRoom){
					if(tr.getRoom().equals(frag.getRoom())){
						y = tr.getYPointer();
						break breakable;
					}
				}
				throw new InvalidInputError("Class" + frag.getRoom()+" was not found");
			}
			for(LocationRequest lre: temp){
				lre.shiftY(y);
			}
			headers.addAll(temp);
		}

		insertApproveals(file,noteworthy,headers,retSplit);
	}
	private void insertApproveals(String id,Collection<RequestSession> sessions,ArrayList<LocationRequest> lr,List<List<List<Object>>> strings) throws IOException{
		for(RequestSession rs:sessions){
			strings.add(Arrays.asList(Arrays.asList(rs.getStatus())));
			lr.add(new LocationRequest(SheetHandler.REQUEST_PAGE
					,SheetHandler.REQUEST_PAGE_STATUS
					,0+rs.getRequestY()-1
					,SheetHandler.REQUEST_PAGE_STATUS
					,0+rs.getRequestY()-1));
		}
		accessor.compileInsertIntoTable(id, lr, strings);
	}
	public ArrayList<TimeData> getTimeData(String fileid,String season,int session, int day,int startTime,int timeFrame) throws IOException{
		ArrayList<TimeData> ret = new ArrayList<>();
		ArrayList<ClassSession> tempRoom = getClassFragments(fileid);
		LinkedList<LocationRequest> toRequest = new LinkedList<>();
		LinkedList<Integer> ints = new LinkedList<>();
		
		
		ArrayList<LocationRequest> headers = new ArrayList<>();
		int splits = seperateTimeData(headers,ints,null,null,season,session,startTime,timeFrame,day);
		
		int roomShift;
		for(roomShift = 0; roomShift < tempRoom.size();roomShift++){
			for(int i = 0; i < splits;i++){
				LocationRequest copy = headers.get(i).createCopy();
				copy.shiftY(TOTAL_SIZE_Y*roomShift);
				toRequest.add(copy);
			}
		}
		roomShift = 0;
		int count = 0;
		ArrayList<List<List<String>>> items = accessor.compileQueryCells(fileid, toRequest);
		ArrayList<String> fragment = new ArrayList<>();
		//Major edit may be needed her for split
		for(List<List<String>> list:items){
			
			int i = 0;
			if(list !=null){
				for(;i < list.size();i++){
					if(list.get(i).size()!=0){
						fragment.add(list.get(i).get(0));
					}
					else{
						fragment.add("");
					}
				}
			}
			for(;i<ints.get(count%splits);i++){
				fragment.add("");
			}
			if(count%splits == splits-1){
				ret.add(new TimeData(tempRoom.get(roomShift).getRoom(),fragment.toArray(new String[fragment.size()])));
				roomShift++;
				fragment.clear();
			}
			count++;
		}
		return ret;
	}
	public ArrayList<ClassData> getClassData(String id,ClassSession parent) throws IOException{
		ArrayList<ClassData> ret = new ArrayList<>();
		LocationRequest lr = new LocationRequest(parent.getParent().getSeason(),CLASS_DATA_OFFSET_X,CLASS_DATA_OFFSET_Y,CLASS_DATA_OFFSET_X+DAYS_IN_WEEK-1,CLASS_DATA_OFFSET_Y+DATA_SIZE_Y);
		lr.shiftY((parent.getYPointer())*TOTAL_SIZE_Y);
		lr.shiftX((parent.getParent().getSession()-1)*TOTAL_SIZE_X);//offby one error, session 1 is in pointer location of the array @ 0.
		List<List<String>> data = accessor.queryCells(id, Arrays.asList(Utils.convertToPosition(lr))).get(0);
		int i = CLASS_START_TIME;
		int j = 0;
		//if no data exists, skip this step.
		if(data != null && !data.isEmpty()){
			for(List<String> strings:data){
				String monday = "", tuesday = "", wednsday = "", thursday = "", friday = "", saturday = "", sunday = "";
				switch(strings.size()){
					case 7:
						sunday = strings.get(6);
					case 6:
						saturday = strings.get(5);
					case 5:
						friday = strings.get(4);
					case 4:
						thursday = strings.get(3);
					case 3:
						wednsday = strings.get(2);
					case 2:
						tuesday = strings.get(1);
					case 1:
						monday = strings.get(0);
						break;
				}
				ret.add(new ClassData(parent,i,monday,tuesday,wednsday,thursday,friday,saturday,sunday));
				i++;
				j++;
				i%=NEXT_DAY_SLOT;
			}
		}
		for(;j<=DATA_SIZE_Y;j++,i++){
			i%=NEXT_DAY_SLOT;
			ret.add(new ClassData(parent,i,"","","","","","",""));
		}
		return ret;
	}
	public List<RequestSession> getRequestData(String sheet) throws IOException{
		ArrayList<RequestSession> data = new ArrayList<>();
		String lr = SheetHandler.REQUEST_PAGE_DIR;
		List<List<String>> strings = accessor.queryCells(sheet,Arrays.asList(lr)).get(0);
		int i = 11;
		for(List<String> string: strings){
			try{
				i++;
				String status = string.get(REQUEST_PAGE_STATUS);
				String request = string.get(REQUEST_PAGE_REQUEST);
				if(!status.equals("Pending") || !request.equals("Active")){
					System.out.println(status+"_"+request);
					continue; //not completed entry
				}
				else{
				}
				String name = string.get(REQUEST_PAGE_NAME);
				String email = string.get(REQUEST_PAGE_EMAIL);
				String courseNum = string.get(REQUEST_PAGE_COURSE);
				String CRN = string.get(REQUEST_PAGE_COURSE_CRN);
				String semester = string.get(REQUEST_PAGE_SEMESTER);
				String session = string.get(REQUEST_PAGE_SESSIONS);
				String day = string.get(REQUEST_PAGE_DAY);
				String room = string.get(REQUEST_PAGE_ROOM_NUMBER);
				int time = 0;
				int frame = 0;
				try{
					time = ClassData.convertMilitaryToInt(string.get(REQUEST_PAGE_TIME));
					frame = Utils.convertToTime(string.get(REQUEST_PAGE_DURATION));
				}
				catch(Exception ex){
					ErrorLog.writeError(new RuntimeException("malformed data entries, must be in military or integer time, see "+name+" entry to see error in detail"), ErrorLevel.USER_ERROR);
					continue; //skip this entry, since it wont work.
				}
				String note = string.get(REQUEST_PAGE_NOTES);
				
				data.add(new RequestSession(name,email,courseNum,CRN,semester,session,day,room,time,frame,note,request,status,i));
				//}
			}
			catch(Exception ex){
				ErrorLog.writeError(new RuntimeException("Extensive user error in parsing data"), ErrorLevel.USER_ERROR);
			}
		}
		return data;
	}

	public void insertTimeData(Collection<ClassData> data) throws IOException{
		//LocationRequest lr = new LocationRequest(parent.getParent().getSeason(),CLASS_DATA_OFFSET_X,CLASS_DATA_OFFSET_Y,CLASS_DATA_OFFSET_X+DAYS_IN_WEEK-1,CLASS_DATA_OFFSET_Y+DATA_SIZE_Y);
		List<List<List<Object>>> toInsert = new ArrayList<>();
		List<LocationRequest> requests = new ArrayList<>();
		if(data.stream().filter(new Predicate<ClassData>(){
			HashSet<String> contained = new HashSet<>();
			@Override
			public boolean test(ClassData arg0) {
				String str = arg0.getParent().getParent().getFileID();
				if(!contained.contains(str)){
					contained.add(str);
					return true;
				}
				return false;
		}}).count() > 1){
			throw new RuntimeException("Multiple FIle ids detected, aborting");
		}
		Session first = null;
		for(ClassData d:data){
			//objects to handle
			if(first == null){
				first = d.getParent().getParent();
			}
			List<Object> inner = new ArrayList<>();
			inner.add(d.getMonday());
			inner.add(d.getTuesday());
			inner.add(d.getWednesday());
			inner.add(d.getThursday());
			inner.add(d.getFriday());
			inner.add(d.getSaturday());
			inner.add(d.getSunday());
			
			List<List<Object>> outer = new ArrayList<>();
			outer.add(inner);
			toInsert.add(outer);
			
			//location request
			LocationRequest request = new LocationRequest(first.getSeason(),CLASS_DATA_OFFSET_X
																			,CLASS_DATA_OFFSET_Y+(d.getTime()-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT,
																			CLASS_DATA_OFFSET_X+DAYS_IN_WEEK,
																			CLASS_DATA_OFFSET_Y+(d.getTime()-CLASS_START_TIME+NEXT_DAY_SLOT)%NEXT_DAY_SLOT);
			request.shiftY((d.getParent().getYPointer())*TOTAL_SIZE_Y);
			request.shiftX((first.getSession()-1)*TOTAL_SIZE_X);
			requests.add(request);
			//LocationRequest lr = new LocationRequest(parent.getParent().getSeason(),CLASS_DATA_OFFSET_X,CLASS_DATA_OFFSET_Y,CLASS_DATA_OFFSET_X+DAYS_IN_WEEK-1,CLASS_DATA_OFFSET_Y+DATA_SIZE_Y);
		}
		if(first != null){
			//SheetHandler.this.accessor.compileInsertIntoTable(first.getFileID(), requests,toInsert);
			accessor.compileInsertIntoTable(first.getFileID(), requests, toInsert);
		}
	}
	public void addRoom(String id,String room,String year) throws IOException{
		ArrayList<String> strings = new ArrayList<>(DriveHandler.getRoomByFileID((id)));
		
		int i;
		//sort strings by natural order
		for(i = 0;i < strings.size();i++){
			if(strings.get(i).compareTo(room) >0){
				break;
			}
			
			else if(strings.get(i).compareTo(room)== 0){
				throw new InvalidInputError("Room Already Exists");
			}
		}
		strings.add(i,room);
		
		LocationRequest request = new LocationRequest(SheetHandler.ROOM_LOCATION,0,1,0,strings.size()+1);
		LocationRequest requests = request;
		List<List<String>> toSend = new ArrayList<>();
		for(String str: strings){
			toSend.add(Arrays.asList(str));
		}
		
		
		RequestCompilePackage rcp = accessor.startRequests(id);
		//accessor.buildUpdateRowsRequests(rcp, requests, Arrays.asList(Collections.emptyList()));
		accessor.buildUpdateRowsRequests(rcp, Arrays.asList(requests), Arrays.asList(toSend),false);
		
		//template things sort of
		LocationRequest Fall = new LocationRequest(SheetHandler.SEASON_FALL_TAB,0,0,SheetHandler.TOTAL_SIZE_X,SheetHandler.TOTAL_SIZE_Y);
		Fall.shiftY(SheetHandler.TOTAL_SIZE_Y*i);
		
		//create copies, set to correct sheet
		LocationRequest Summer = Fall.createCopy();
		Summer.setSheet(SheetHandler.SEASON_SUMMER_TAB);
		
		LocationRequest Spring = Fall.createCopy();
		Spring.setSheet(SheetHandler.SEASON_SPRING_TAB);
		
		accessor.buildInsertDimensionRows(rcp, Arrays.asList(Spring,Summer,Fall));
		
		
		
		ArrayList<LocationRequest> copyToLocations = new ArrayList<>();
		//will be used in copy
		LocationRequest template = new LocationRequest(SheetHandler.TEMPLATE_PAGE,0,0,SheetHandler.TOTAL_SIZE_X,SheetHandler.TOTAL_SIZE_Y);
		
		//fall copies
		for(int j = 0; j < MAX_SESSIONS;j++){
			//fall
			LocationRequest toCopy = Fall.createCopy();
			toCopy.shiftX(j*TOTAL_SIZE_X);
			copyToLocations.add(toCopy);
			//spring
			toCopy = Spring.createCopy();
			toCopy.shiftX(j*TOTAL_SIZE_X);
			copyToLocations.add(toCopy);
			//summer
			toCopy = Summer.createCopy();
			toCopy.shiftX(j*TOTAL_SIZE_X);
			copyToLocations.add(toCopy);
		}
		//ArrayList<List<Object>> rooms
		accessor.buildCopyRequests(rcp, template, copyToLocations);
		//move time thing over now.
		
		LocationRequest templateTime = new LocationRequest(SheetHandler.TEMPLATE_PAGE,
														SheetHandler.TIME_DATA_LOC_X,SheetHandler.TIME_DATA_LOC_Y,SheetHandler.TIME_DATA_LOC_X_MAX,SheetHandler.TIME_DATA_LOC_Y_MAX);
		
		LocationRequest fallTime = templateTime.createCopy();
		fallTime.setSheet(SheetHandler.SEASON_FALL_TAB);
		fallTime.shiftY(i*SheetHandler.TOTAL_SIZE_Y);
		
		LocationRequest summerTime = fallTime.createCopy();
		summerTime.setSheet(SheetHandler.SEASON_SUMMER_TAB);
		
		LocationRequest springTime = fallTime.createCopy();
		springTime.setSheet(SheetHandler.SEASON_SPRING_TAB);
		
		accessor.buildCopyRequests(rcp, templateTime,Arrays.asList(springTime,fallTime,summerTime));
		
		//******** ROOM NUMBER ************
		LocationRequest fallHeaderRoom = new LocationRequest(SheetHandler.SEASON_FALL_TAB
																,SheetHandler.DATA_HEADER_ROOMNUMBER_X
																,SheetHandler.DATA_HEADERS_Y
																,SheetHandler.DATA_HEADER_ROOMNUMBER_X
																,SheetHandler.DATA_HEADER_Y_END);
		//fall
		fallHeaderRoom.shiftY(i*SheetHandler.TOTAL_SIZE_Y);
		
		//spring
		LocationRequest springHeaderRoom = fallHeaderRoom.createCopy();
		springHeaderRoom.setSheet(SheetHandler.SEASON_SPRING_TAB);
		
		//summer
		LocationRequest summerHeaderRoom = fallHeaderRoom.createCopy();
		summerHeaderRoom.setSheet(SheetHandler.SEASON_SUMMER_TAB);

		//******** SESSIONS ************
		LocationRequest fallHeaderSession = new LocationRequest(SheetHandler.SEASON_FALL_TAB
																	,SheetHandler.DATA_HEADER_SESSION
																	,SheetHandler.DATA_HEADERS_Y
																	,SheetHandler.DATA_HEADER_SESSION
																	,SheetHandler.DATA_HEADER_Y_END);
		//fall
		fallHeaderSession.shiftY(i*SheetHandler.TOTAL_SIZE_Y);
		
		//spring
		LocationRequest springHeaderSession = fallHeaderSession.createCopy();
		springHeaderSession.setSheet(SheetHandler.SEASON_SPRING_TAB);
		
		//summer
		LocationRequest summerHeaderSession= fallHeaderSession.createCopy();
		summerHeaderSession.setSheet(SheetHandler.SEASON_SUMMER_TAB);
		
		//******** Year ************
		LocationRequest fallHeaderYear = new LocationRequest(SheetHandler.SEASON_FALL_TAB
																,SheetHandler.DATA_HEADER_YEAR
																,SheetHandler.DATA_HEADERS_Y
																,SheetHandler.DATA_HEADER_YEAR
																,SheetHandler.DATA_HEADER_Y_END);
		//fall
		fallHeaderYear.shiftY(i*SheetHandler.TOTAL_SIZE_Y);
		
		//spring
		LocationRequest springHeaderYear = fallHeaderYear.createCopy();
		springHeaderYear.setSheet(SheetHandler.SEASON_SPRING_TAB);
		
		//summer
		LocationRequest summerHeaderYear = fallHeaderYear.createCopy();
		summerHeaderYear.setSheet(SheetHandler.SEASON_SUMMER_TAB);
		
		for(int j = 0; j < MAX_SESSIONS; j++){
			accessor.buildUpdateRowsRequests(rcp, Arrays.asList(	fallHeaderRoom,
																	summerHeaderRoom,
																	springHeaderRoom,
																	
																	fallHeaderYear,
																	summerHeaderYear,
																	springHeaderYear,
																	
																	fallHeaderSession,
																	summerHeaderSession,
																	springHeaderSession
						),
					
						
					
						Arrays.asList(
										//**********   ROOM NUMBER    ******
										  Arrays.asList(Arrays.asList(room)), //fall
										  Arrays.asList(Arrays.asList(room)), //summer
										  Arrays.asList(Arrays.asList(room)), //spring
										//**********   YEAR    ******
										  Arrays.asList(Arrays.asList(SheetHandler.SEASON_FALL_TAB+" "+year)), //fall
										  Arrays.asList(Arrays.asList(SheetHandler.SEASON_SUMMER_TAB+" "+year)), //summer
										  Arrays.asList(Arrays.asList(SheetHandler.SEASON_SPRING_TAB+" "+year)), //spring
										//**********   SESSION ******
										  Arrays.asList(Arrays.asList(""+(j+1))), //fall
										  Arrays.asList(Arrays.asList(""+(j+1))), //summer
										  Arrays.asList(Arrays.asList(""+(j+1)))//, //spring
									  ), 
						true); //sets borders visible
			
			
			fallHeaderRoom.shiftX(SheetHandler.TOTAL_SIZE_X);
			summerHeaderRoom.shiftX(SheetHandler.TOTAL_SIZE_X);
			springHeaderRoom.shiftX(SheetHandler.TOTAL_SIZE_X);
			
			fallHeaderYear.shiftX(SheetHandler.TOTAL_SIZE_X);
			summerHeaderYear.shiftX(SheetHandler.TOTAL_SIZE_X);
			springHeaderYear.shiftX(SheetHandler.TOTAL_SIZE_X);
			
			fallHeaderSession.shiftX(SheetHandler.TOTAL_SIZE_X);
			summerHeaderSession.shiftX(SheetHandler.TOTAL_SIZE_X);
			springHeaderSession.shiftX(SheetHandler.TOTAL_SIZE_X);
			
		}
		accessor.executeRequests(rcp);
	}
	/**
	 * room to be removed
	 * @param file to be removed
	 * @param room number to remove
	 * @throws IOException
	 */
	public void removeRoom(String file, String room) throws IOException{
		ArrayList<String> strings = new ArrayList<>(DriveHandler.getRoomByFileID(file));
		int i;
		for(i = 0;i < strings.size();i++){
			if(strings.get(i).compareTo(room)== 0){
				break;
			}
		}
		if(i == strings.size()){
			throw new InvalidInputError("Class Not Found");
		}
		strings.remove(i);

		LocationRequest request = new LocationRequest(SheetHandler.ROOM_LOCATION,0,1,0,strings.size()+1);
		LocationRequest requests = request;
		List<List<String>> toSend = new ArrayList<>();
		for(String str: strings){
			toSend.add(Arrays.asList(str));
		}
		
		
		RequestCompilePackage rcp = accessor.startRequests(file);
		//accessor.buildUpdateRowsRequests(rcp, requests, Arrays.asList(Collections.emptyList()));
		accessor.buildUpdateRowsRequests(rcp, Arrays.asList(requests), Arrays.asList(toSend),false);
		
		
		LocationRequest Fall = new LocationRequest(SheetHandler.SEASON_FALL_TAB,0,0,SheetHandler.TOTAL_SIZE_X,SheetHandler.TOTAL_SIZE_Y);
		Fall.shiftY(i*TOTAL_SIZE_Y);
		
		LocationRequest Spring = Fall.createCopy();
		Spring.setSheet(SheetHandler.SEASON_SPRING_TAB);

		LocationRequest Summer = Fall.createCopy();
		Summer.setSheet(SheetHandler.SEASON_SUMMER_TAB);
		accessor.buildDeleteRowsDimensionRequest(rcp, Arrays.asList(Summer,Spring,Fall));
		
		accessor.executeRequests(rcp);
		
	}
	/**
	 * 
	 * @param id of the file to be throughly cleaned
	 * @throws IOException 
	 */
	public void cleanSheet(String fileid) throws IOException{
		//pages to clean up
		ArrayList<String> removeLocations = new ArrayList<>();
		ArrayList<List<List<Object>>> dataToSwap = new ArrayList<>();
		List<String> rooms = this.getSessions(fileid, null).stream().map(s->s.getRoom()).collect(Collectors.toList());
		int size = accessor.queryCells(fileid,Arrays.asList(SheetHandler.REQUEST_PAGE_DIR)).get(0).size();
		dataToSwap.add(Utils.generateEmptyFakeDataList(9,size));
		dataToSwap.add(Utils.generateEmptyFakeDataList(5,size));
		removeLocations.add(SheetHandler.REQUEST_PAGE_DIR_SPLIT1);
		removeLocations.add(SheetHandler.REQUEST_PAGE_DIR_SPLIT2);
		
		LocationRequest lr = new LocationRequest("",CLASS_DATA_OFFSET_X,CLASS_DATA_OFFSET_Y,CLASS_DATA_OFFSET_X+DAYS_IN_WEEK-1,CLASS_DATA_OFFSET_Y+DATA_SIZE_Y);
		for(int y = 0; y < rooms.size();y++){
			for(int x = 0; x < SheetHandler.MAX_SESSIONS;x++){
				//'!values' expected
				String base = Utils.convertToPosition(lr);
				//dataToSwap.addAll(Arrays.asList(Arrays.asList(Arrays.asList(""))));
				//dataToSwap.addAll(Arrays.asList(Arrays.asList(Arrays.asList(""))));
				//dataToSwap.addAll(Arrays.asList(Arrays.asList(Arrays.asList(""))));
				dataToSwap.add(Utils.generateEmptyFakeDataList(SheetHandler.DAYS_IN_WEEK,96));
				dataToSwap.add(Utils.generateEmptyFakeDataList(SheetHandler.DAYS_IN_WEEK,96));
				dataToSwap.add(Utils.generateEmptyFakeDataList(SheetHandler.DAYS_IN_WEEK,96));
				
				removeLocations.add(SheetHandler.SEASON_FALL_TAB+base);
				removeLocations.add(SheetHandler.SEASON_SPRING_TAB+base);
				removeLocations.add(SheetHandler.SEASON_SUMMER_TAB+base);
				lr.shiftX(SheetHandler.TOTAL_SIZE_X);
			}
			lr.shiftX(-(SheetHandler.MAX_SESSIONS*SheetHandler.TOTAL_SIZE_X));
			lr.shiftY(SheetHandler.TOTAL_SIZE_Y);
		}
		this.accessor.insertIntoTable(fileid, removeLocations,dataToSwap);
	}
	public static void main(String... args) throws IOException, GeneralSecurityException{
		//https://drive.google.com/open?id=1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM
		//https://drive.google.com/open?id=1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM
		new SheetHandler().cleanSheet("1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM");
	}
}
