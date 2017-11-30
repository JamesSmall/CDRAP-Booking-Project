
package com.cdrap.GoogleAccessor;

import java.awt.Toolkit;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import com.cdrap.UI.Controller.Wrapper.ButtonCell.OnClick;
import com.cdrap.UI.Controller.Wrapper.ComboBoxCell;
import com.cdrap.UI.Controller.Wrapper.ComboBoxCell.StringConverter;
import com.cdrap.UI.Controller.Wrapper.EditableCell;
import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.ErrorManager.InvalidInputError;
import com.cdrap.UI.FXHandler;
import com.cdrap.UI.Controller.GridWindow;
import com.cdrap.UI.Controller.Wrapper.ButtonCell;
import com.cdrap.transit.ClassData;
import com.cdrap.transit.ClassFragment;
import com.cdrap.transit.ClassSession;
import com.cdrap.transit.RequestSession;
import com.cdrap.transit.Session;
import com.cdrap.transit.TimeData;
import com.cdrap.transit.Callback.Navigation;
import com.cdrap.transit.Callback.NavigationCapture;
import com.cdrap.transit.Callback.StringReturnCallback;
import com.cdrap.transit.UI.AlertBundle;
import com.cdrap.transit.UI.CustomGridWindowBundle;
import com.cdrap.transit.UI.RoomHandlerBundle;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
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
public class DriveHandler {
	public static final String GOTO_TEXT = "GOTO";
	public static final String NAVIGATE_TO_TEXT= "Navigate To";
	public static final String TO_ROOM_NAVIGATION = "To Room Selection";
	public static final String TO_PAGE_SELECTION = "To Year/Season/Session selection";
	
	//current file containers
	private static HashMap<String,SheetFileContainer> Selectors = new HashMap<>();
	
	//wrapper for the wrapper of google sheets (translates data into desired format)
	private static SheetHandler handler;
	//wrapper for google drive directly
	private static DriveAccessor accessor;
	//wrapper for google email
	private static EmailHandler mail;
	//https://drive.google.com/open?id=1l8jbc96t7QNQ3BDA9TpBmM88wN4ZChkUrdCFaS098NE
	/**
	 * master startup method
	 * @throws IOException if soemthign happens in transit
	 * @throws GeneralSecurityException if user declines login
	 */
	private static void setup() throws IOException, GeneralSecurityException{
		if(handler == null) {//it is exists, don't build it
			//create items that are needed
			handler = new SheetHandler();
			accessor = new DriveAccessor(handler);
			mail = new EmailHandler();
		} 
	}
	/**
	 * called when starting up, gets all relevent files and stores their ids, must be called when pages are refreshed!
	 * @throws IOException if something goes wrong with the internet
	 * @throws GeneralSecurityException 
	 */
	public static void refreshSheets() throws IOException, GeneralSecurityException{
		//call setup
		setup();
		//remove previous instances, if used (like with add new year)
		Selectors.clear();
		//containers.clear();
		//ArrayList of containers to look through
		ArrayList<SheetFileContainer> containers = accessor.getFileReferences();
		//marked files for files that share the same metadata
		boolean[] marks = new boolean[containers.size()];//defaults to false
		for(int i = 0; i < containers.size();i++){
			if(!marks[i]){//sees if it was already added to search list
				//items of the same year
				ArrayList<SheetFileContainer> sameYear = new ArrayList<>();
				//same year problems....
				sameYear.add(containers.get(i));
				for(int j = i+1; j < containers.size();j++){//look through items
					if(containers.get(i).getYear() == containers.get(j).getYear()){//welp, same name
						marks[j] = true;//mark
						sameYear.add(containers.get(j));
					}
				}
				if(sameYear.size() != 1){//check to make sure name isn't the same(in case file copied to another directory)
					for(int j = 0; j < sameYear.size();j++){
						boolean useID = false;
						for(int n = 0; n < sameYear.size();n++){//check brothers and sisters
							if(sameYear.get(j).getFileName().equals(sameYear.get(n).getFileName())&&j!=n){//well now its time to use internal id to not confuse it..... unforentially this is confusing.
								useID = true;
							}
						}
						if(useID){//set it to id using
							Selectors.put("Year:"+sameYear.get(j).getYear() +" FileName: "+ sameYear.get(j).getFileName() +"FileID: "+sameYear.get(j).getFileid(),sameYear.get(j));
						}
						else{//set it to showing name
							Selectors.put("Year:"+sameYear.get(j).getYear() +" FileName: "+ sameYear.get(j).getFileName(),sameYear.get(j));
						}
					}
				}
				else{ //set to using year only
					Selectors.put(""+sameYear.get(0).getYear(), sameYear.get(0));
				}
			}
		}
	}
	/**
	 * get file containers keyset
	 * @return set of File Containers
	 * @throws GeneralSecurityException 
	 * @throws IOException 
	 */
	public static void copyFile(String filename,String newFileName, int year,int summerSessions, int fallSessions, int springSessions) throws IOException, GeneralSecurityException{
		ArrayList<SheetFileContainer> containers = getFileContainersAsList();
		if(containers.stream().anyMatch(e->e.getFileName().equals(newFileName))){
			throw new InvalidInputError("File name already exists");
		}
		if(summerSessions > 5){
			throw new InvalidInputError("Summer sessions cannot be more than 5");
		}
		if(springSessions > 5){
			throw new InvalidInputError("Spring sessions cannot be more than 5");
		}
		if(fallSessions > 5){
			throw new InvalidInputError("Fall sessions cannot be more than 5");
			
		}
		for(SheetFileContainer c:containers){
			if(c.getFileName().equals(filename)){
				String id = DriveHandler.accessor.copyFile(c.getFileid(), newFileName);
				//temp operations to handle file creation
				try{
					
				}
				catch(Exception ex){
					//error in handling requests
					throw new IOException("Error in updating file, old data may be present still",ex);
				}
				
				refreshSheets();
				return;
			}
		}
		throw new InvalidInputError("Copy File was not found");
	}
	public static Set<String> getFileContainerKeys(){
		//get set of strings
		return Collections.unmodifiableSet(Selectors.keySet());
	}
	/**
	 * 
	 * @param fileid the id of the file to get these from
	 * @return classes as strings
	 * @throws IOException if an io error occurs while getting classes
	 */
	public static ArrayList<String> getClassData(String fileid) throws IOException{
		//summon up a bunch of strings
		ArrayList<String> ret = new ArrayList<>();
		//get Class Fragments
		ArrayList<ClassSession> classes = handler.getClassFragments(fileid);
		for(ClassSession s:classes){ //prepare to send to sender
			ret.add(s.getRoom());
		}
		//return to sender
		return ret;
	}
	/**
	 * requests a specific identifier to get data from
	 * @param identifier to request
	 * @return the SheetFileContainer of the requested identifier
	 */
	private static SheetFileContainer getFileContainer(String identifier){
		//find year/file container by an identifier
		return Selectors.get(identifier);
	}
	/**
	 * 
	 * @param id to be requested
	 * @return Strings of specified rooms
	 * @throws IOException if something happens in transit
	 */
	public static ArrayList<String> getRoomByFileID(String id) throws IOException{
		ArrayList<String> ret = new ArrayList<>();
		ret.addAll(getClassData(id));
		return ret;
	}
	/** 
	 * quick convertion to keep older code working correctly and inorder without fail and keep order correct
	 * uses java timsort to sort items
	 * @return expected formate for some internal code
	 */
	private static ArrayList<SheetFileContainer> getFileContainersAsList(){
		//convert Hashmap Values to ArrayList
		ArrayList<SheetFileContainer> container = new ArrayList<>(Selectors.values());
		
		container.sort((SheetFileContainer o1,SheetFileContainer o2)->{
			//sort via year
			if(o1.getYear() > o2.getYear()){
				return 1;
			}
			else if(o1.getYear() < o2.getYear()){
				return -1;
			}
			//compare with names string natural order if years not reliable
			return o1.getFileName().compareTo(o2.getFileName());
		});
		//return container
		return container;
	}
	/**
	 * sees if year exists
	 * @param year to request
	 * @return true if year is found, false otherwise
	 */
	public static boolean yearExists(int year){
		return Selectors.values().stream().anyMatch(s->s.getYear() == year);
	}
	/**
	 * 
	 * @param year specified for transit request
	 * @return room data
	 * @throws IOException if something happens in transit
	 */
	public static ArrayList<String> getRooms(String year) throws IOException{
		SheetFileContainer file = getFileContainer(year);
		return getRoomByFileID(file.getFileid());
	}
	/**
	 * an asynchronous get used by searchbox, this code has a callback
	 * @param year year requested
	 * @param run method of return
	 */
	public static void ASycnRoomGet(String year, StringReturnCallback run){
		new Thread(){ //starts new thread, lets old one goto rest
			public void run(){
				try {
					//run callback
					run.callback(getRooms(year));
				} catch (IOException e) {
					//wont record errors here, if invalid data exists on input it will throw an exception and techically speaking input box has invalid data
					//at the start, so its not even worth bothering with.
					e.printStackTrace();
				}
			}
		}.start();
	}
	/*
	 * 
	 * 
	 * ***********************************************************************************************************************************************************
	 * 														BUILDERS THAT REUSE OTHER BUILDERS TO BUILD
	 * ***********************************************************************************************************************************************************
	 */
	/**
	 * 
	 * @param session value to reference from
	 * @param room to add
	 * @throws IOException if something goes wrong
	 */
	public static void addRoom(Session session, String room) throws IOException{
		handler.addRoom(session.getFileID(), room, ""+session.getYear());
	}
	/**
	 * 
	 * @param session value to reference from
	 * @param room to remove
	 * @throws IOException if something goes wrong
	 */
	public static void removeRoom(Session session, String room) throws IOException{
		handler.removeRoom(session.getFileID(), room);
	}
	/**
	 * generates Session from user entered data
	 * @param year requested
	 * @param season requested
	 * @param session requested
	 * @return the Session based off entered data
	 */
	public static Session generateSession(String year, String season, int session){
		//for(SheetFileContainer container:getFileContainers(year)){
		SheetFileContainer container = getFileContainer(year);
		//if not found,generate correct error to send to user
		if(container == null){
			try{
				//sees if it exists
				int iyear = Integer.parseInt(year);
				if(yearExists(iyear)){//it exists, but has duplicate years
					throw new InvalidInputError(year +" is valid, but 2 or more sheets contain this year meta data");
				}
			}
			catch(Exception ex){
				
			}
			//does not exist, was not found, or just plain bad
			throw new InvalidInputError(year+" was not found");
			
		}
		//select based on season, not case sensitive
		switch(season.toLowerCase()){
			//FALL
			case SheetHandler.SEASON_FALL_TAB_LOWER_CASE:
				if(container.getFallSessions() >= session){
					//return session
					return new Session(container.getFileid(),container.getFileName(),container.getYear(),season,session);
				}
				else{
					//session had invalid session num
					throw new InvalidInputError(session+" is not a valid session");
				}
				//break; unreachable
			//SPRING
			case SheetHandler.SEASON_SPRING_TAB_LOWER_CASE:
				if(container.getSpringSessions() >= session){
					//return session
					return new Session(container.getFileid(),container.getFileName(),container.getYear(),season,session);
				}
				else{
					//session had invalid session num
					throw new InvalidInputError(session+" is not a valid session");
				}
				//break; unreachable
			//SUMMER
			case SheetHandler.SEASON_SUMMER_TAB_LOWER_CASE:
				if(container.getSummerSessions() >= session){
					//return session
					return new Session(container.getFileid(),container.getFileName(),container.getYear(),season,session);
				}
				else{
					//session had invalid session num
					throw new InvalidInputError(session+" is not a valid session");
				}
				//break; unreachable
		}
		//invalid season picked
		throw new InvalidInputError(season+" was not a valid season");
		
	}
	/**
	 * 
	 * @param year requested
	 * @param season requested
	 * @param session requested
	 * @param classBox requested
	 * @return Bundle of requested data
	 * @throws IOException if something happens in transit
	 */
	public static CustomGridWindowBundle<ClassData> getClassData(String year, String season,int session, String classBox) throws IOException{
		//get session from requested data
		Session s = generateSession(year,season,session);
		if(s != null){
			//get classSessions from File ID
			ArrayList<ClassSession> sessions = handler.getClassFragments(s.getFileID());
			//convert to sessions
			for(int i = 0; i < sessions.size();i++){
				//time to get correct data
				if(sessions.get(i).getRoom().equals(classBox)){
					//return class session data
					return getClassData(new ClassSession(s,classBox,i));
				}
			}
			//ClassSession s = new ClassSession(s,classBox);
		}
		//nope, throw out, was not found
		throw new InvalidInputError("Data was not found");
	}
	/*
	 * 
	 * 
	 * ***********************************************************************************************************************************************************
	 * 																		Prepare Switches
	 * ***********************************************************************************************************************************************************
	 */
	/**
	 * prepares to switch to this session data
	 * @param s the session desired to switch to
	 */
	public static void prepareSwitch(Session s){
		try {
			//tell FX handler to switch to this new data in the stage.
			FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW, getClassSelector(s));
		} catch (IOException e) {
			//error occured
			Toolkit.getDefaultToolkit().beep();
			ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
			FXHandler.handle.pushToast("Error in loading, possible reason->"+e.getMessage(), 4000,100,100);
		}
	}
	/**
	 * Builds Room Data from specific ClassSession
	 * @param session to build Rooms from
	 */
	public static void prepareSwitch(ClassSession session){
		try {
			//switchs room to switch,gets class data as needed
			FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,getClassData(session));
		} catch (IOException e) {
			//internet error. woops
			Toolkit.getDefaultToolkit().beep();
			ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
			FXHandler.handle.pushToast("Error in loading, possible reason->"+e.getMessage(), 4000,100,100);
		}
	}
	/*
	 * 
	 * 
	 * ***********************************************************************************************************************************************************
	 * 														BUNDLE BUILDERS
	 * ***********************************************************************************************************************************************************
	 */
	/**
	 * gets the year/season/session data to display
	 * @return Bundle containing the year/season/session data
	 */
	public static CustomGridWindowBundle<Session> getYearSeasonSessionSelector(){
		//sessions to select from
		ArrayList<Session> ret = new ArrayList<>();
		//************* DATA SETUP *****************
		//go through list
		for(SheetFileContainer c:getFileContainersAsList()){
			//fall stuff
			for(int i = 1; i <= c.getFallSessions();i++){ //go through active sessions
				//add 
				ret.add(new Session(c.getFileid(),c.getFileName(),c.getYear(),SheetHandler.SEASON_FALL_TAB,i));
			}
			//Spring
			for(int i = 1; i <= c.getSpringSessions();i++){ //go through active sessions
				ret.add(new Session(c.getFileid(),c.getFileName(),c.getYear(),SheetHandler.SEASON_SPRING_TAB,i));
			}
			//summer
			for(int i = 1; i <= c.getSummerSessions();i++){ //go through active sessions
				ret.add(new Session(c.getFileid(),c.getFileName(),c.getYear(),SheetHandler.SEASON_SUMMER_TAB,i));
			}
		}
		//build the bundle
		CustomGridWindowBundle<Session> b = new CustomGridWindowBundle<>(ret,new String[]{"Year/File Selection"});
		//********** Buttons *******************
		b.addButton("Stub button", (d)->{});
		
		//*********  Columns ******************
		b.addColumn("File",.222125,new Callback<TableColumn<Session,Session>,TableCell<Session,Session>>(){
			@Override
			public TableCell<Session,Session> call(TableColumn<Session,Session> arg0) {
				//create table cell
				TableCell<Session,Session> c = new TableCell<Session,Session>(){;
					@Override
                    public void updateItem(Session item, boolean empty) {
						//display nessassary items
                        super.updateItem(item, empty);
                        if(!empty){
                            setText(""+item.getFileName());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Year",.222125,new Callback<TableColumn<Session,Session>,TableCell<Session,Session>>(){
			@Override
			public TableCell<Session,Session> call(TableColumn<Session,Session> arg0) {
				//create table cell
				TableCell<Session,Session> c = new TableCell<Session,Session>(){
					@Override
                    public void updateItem(Session item, boolean empty) {
                        super.updateItem(item, empty);
                        //display nessassary items
                        if(!empty){
                            setText(""+item.getYear());
                            setTooltip(new Tooltip(getText()));
                        }
					}
				};
				
				return c;
			}
		});
		b.addColumn("Season",.222125,new Callback<TableColumn<Session,Session>,TableCell<Session,Session>>(){
			@Override
			public TableCell<Session,Session> call(TableColumn<Session,Session> arg0) {
				TableCell<Session,Session> c = new TableCell<Session,Session>(){
					//create table cell
					@Override
                    public void updateItem(Session item, boolean empty) {
                        super.updateItem(item, empty);
                        //display nessassary items
                        if(!empty){
                            setText(""+item.getSeason());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				return c;
			}
		});
		b.addColumn("Session",.222125,new Callback<TableColumn<Session,Session>,TableCell<Session,Session>>(){
			@Override
			public TableCell<Session,Session> call(TableColumn<Session,Session> arg0) {
				TableCell<Session,Session> c = new TableCell<Session,Session>(){
					//create table cell
					@Override
                    public void updateItem(Session item, boolean empty) {
                        super.updateItem(item, empty);
                        //display data
                        if(!empty){
                            setText(""+item.getSession());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				return c;
			}
		});
		b.addColumn(GOTO_TEXT,.1,new Callback<TableColumn<Session,Session>,TableCell<Session,Session>>(){
			@Override
			public TableCell<Session,Session> call(TableColumn<Session,Session> arg0) {
				//factory builds the goto button with the goto switch
				ButtonCell<Session> c = new ButtonCell<>((text)->{return NAVIGATE_TO_TEXT;},new OnClick<Session>(){
					@Override
					public void onClick(Session item) {
						//when clicked, go here
						prepareSwitch(item);
					}});
				return c;
			}
		});
		return b;
	}
	/**
	 * 
	 * @param parent gets parent class to spawn children class(the rooms) data from 
	 * @return children data (the rooms)
	 * @throws IOException if an error occurs in transit
	 */
	public static CustomGridWindowBundle<ClassData> getClassData(ClassSession parent) throws IOException{
		//class data built based off parent
		ArrayList<ClassData> data = handler.getClassData(parent.getParent().getFileID(), parent);
		CustomGridWindowBundle<ClassData> b = new CustomGridWindowBundle<>(data,new String[]{"Year:"+parent.getParent().getYear(),
																							"Season:"+parent.getParent().getSeason(),
																							"Session:"+parent.getParent().getSession(),
																							"Room:"+parent.getRoom()});
		//************ NAVIGATION ***************
		b.setNavigation((new Navigation(){

			@Override
			public void approve() {
				prepareSwitch(parent.getParent());
			}

			@Override
			public String getDisplayText() {
				return DriveHandler.TO_ROOM_NAVIGATION;
			}
			//
		}),new Navigation(){

			@Override
			public void approve() {
				try {
					FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,DriveHandler.getYearSeasonSessionSelector());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getDisplayText() {
				return DriveHandler.TO_PAGE_SELECTION;
			}});
		//************** LAMDA EXPRESSION USED BOTH BY NAVIGATION CONTROLLER AND SAVE BUTTON
		final com.cdrap.UI.Controller.GridWindow.OnClick<ClassData> c = (d)->{
			List<ClassData> cd = data.stream().filter(sh->{return sh.refreshHasUpdated();}).collect(Collectors.toList());
			try {
				handler.insertTimeData(cd);
			} catch (IOException e) {
				ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
				FXHandler.handle.pushToast("Error in operation, see error logs, basic message ->"+e.getMessage(),1000,100,100);
			}
		};
		//***************Navigation Capture, to allow for saving data *******************
		b.setCapture(new NavigationCapture(){

			@Override
			public void onNavigationAttempt(final Navigation nav) {
				Breakable:{
					//itterate through data, see if anything new
					for(ClassData d: data){
						//is it new?
						if(d.hasUpdated()){
							//creates an alert window popup bundle
							AlertBundle b = new AlertBundle("Save Changed data?");
							//yes and leave
							b.addButton("Yes", ()->{
								FXHandler.handle.closePopup();
								c.onClick(data);
								nav.approve();
							});
							//no and leave
							b.addButton("No", ()->{
								FXHandler.handle.closePopup();
								nav.approve();
							});
							//no and stay
							b.addButton("Cancel", ()->{
								FXHandler.handle.closePopup();
							});
							//setup popup
							try {
								FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_ALERT_DEFAULT, b);
							} catch (IOException e) {
								e.printStackTrace();
							}
							//leave to outer Breakablee, dont want to call nav.approve() from here.
							break Breakable;
						}
					}
					//all data was searched threw, nothing new, lets just leave
					nav.approve();
				}
			}
			
		});;
		//****************** BUTTONS **********************
		b.addButton("Save", c);
		//********************* COLUMNS************************
		b.addColumn("Time",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				TableCell<ClassData,ClassData> c = new TableCell<ClassData,ClassData>(){
					@Override
                    public void updateItem(ClassData item, boolean empty) {
						//dispaly time data to user
                        super.updateItem(item, empty);
                        if(!empty){
                            setText(""+item.getMilitaryTime());
                            setTooltip(new Tooltip(getText()));
                        }
					}
				};
				
				return c;
			}
		});
		//*************** COLUMNS*******************
		b.addColumn("Monday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					//on cell edit
					@Override
					public void change(ClassData item, String str) {
						item.setMonday(str);
					}
					//display data
					@Override
					public String getText(ClassData item) {
						return item.getMonday();
					}});
				
				return c;
			}
		});
		b.addColumn("Tuesday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					//on cell edit
					@Override
					public void change(ClassData item, String str) {
						item.setTuesday(str);
						
					}
					//display data
					@Override
					public String getText(ClassData item) {
						return item.getTuesday();
					}});
				
				return c;
			}
		});
		b.addColumn("Wednesday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					// on cell edit
					@Override
					public void change(ClassData item, String str) {
						item.setWednsday(str);
						
					}
					// get text
					@Override
					public String getText(ClassData item) {
						return item.getWednesday();
					}});
				
				return c;
			}
		});
		b.addColumn("Thursday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					//on cell edit
					@Override
					public void change(ClassData item, String str) {
						item.setThursday(str);
						
					}
					//get text
					@Override
					public String getText(ClassData item) {
						return item.getThursday();
					}});
				
				return c;
			}
		});
		b.addColumn("Friday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					//on cell edit
					@Override
					public void change(ClassData item, String str) {
						
						item.setFriday(str);
						
					}
					//get text
					@Override
					public String getText(ClassData item) {
						
						return item.getFriday();
					}});
				
				return c;
			}
		});
		b.addColumn("Saturday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
						//on cell edit
						@Override
						public void change(ClassData item, String str) {
							
							item.setSaturday(str);
							
						}
						//get text
						@Override
						public String getText(ClassData item) {
							
							return item.getSaturday();
						}});
					
					return c;
				};
		});
		b.addColumn("Sunday",.124,new Callback<TableColumn<ClassData,ClassData>,TableCell<ClassData,ClassData>>(){
			@Override
			public TableCell<ClassData,ClassData> call(TableColumn<ClassData,ClassData> arg0) {
				
				EditableCell<ClassData> c = new EditableCell<ClassData>(new EditableCell.CellEditHandler<ClassData>(){
					//on cell edit
					@Override
					public void change(ClassData item, String str) {
						
						item.setSunday(str);
						
					}
					//get text
					@Override
					public String getText(ClassData item) {
						
						return item.getSunday();
					}});
				
				return c;
			}
		});
		return b;
	}
	
	public static CustomGridWindowBundle<ClassSession> getClassSelector(final Session parent) throws IOException{
		//Class Sessions gotten from 
		ArrayList<ClassSession> sessions = handler.getSessions(parent.getFileID(), parent);
		//start operations
		CustomGridWindowBundle<ClassSession> b = new CustomGridWindowBundle<>(sessions, new String[]{"File:"+parent.getFileName(),
																									"Year:"+parent.getYear(),
																									"Season:"+parent.getSeason(),
																									"Session:"+parent.getSession()});
		//************ NAVIGATIONS ***********************
		b.setNavigation(new Navigation(){

			@Override
			public void approve() {
				 try {
					 //return home
					FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,DriveHandler.getYearSeasonSessionSelector());
				} catch (IOException e) {
	
					FXHandler.handle.pushToast("Error in loading, possible reason->"+e.getMessage(), 4000,100,100);
				}
			}

			@Override
			public String getDisplayText() {//we are going up a level
				return DriveHandler.TO_PAGE_SELECTION;
			}});
		//****************** Action Button ********************
		b.addButton("Show Request Page", (d)->{
				try {
					switchToRequestPage(parent);
				} catch (IOException e) {
					ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
				}
			});
		b.addButton("Add Room", (d)->{
			try {
				FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_ROOM_ADD_DEFAULT, new RoomHandlerBundle(parent));
			} catch (IOException e) {
				ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
			}
		});
		b.addButton("Remove Room", (d)->{
			try {
				FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_ROOM_REMOVE_DEFAULT, new RoomHandlerBundle(parent));
			} catch (IOException e) {
				ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
			}
		});
		//***************** COLUMNS***********************
		b.addColumn("room",.890,new Callback<TableColumn<ClassSession,ClassSession>,TableCell<ClassSession,ClassSession>>(){
			@Override
			public TableCell<ClassSession,ClassSession> call(TableColumn<ClassSession,ClassSession> arg0) {
				TableCell<ClassSession,ClassSession> c = new TableCell<ClassSession,ClassSession>(){
					//factory, I need this built for this item
					@Override
                    public void updateItem(ClassSession item, boolean empty) {
                        super.updateItem(item, empty);
                    	//set text and tooltip if item exists
                        if(!empty){
                            setText(""+item.getRoom());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn(GOTO_TEXT,.1,new Callback<TableColumn<ClassSession,ClassSession>,TableCell<ClassSession,ClassSession>>(){
			//factory for goto buttoncell
			@Override
			public TableCell<ClassSession,ClassSession> call(TableColumn<ClassSession,ClassSession> arg0) {
				//build Button cell that navigates to this rows ROOM number
				ButtonCell<ClassSession> c = new ButtonCell<>((text)->{return NAVIGATE_TO_TEXT;},new OnClick<ClassSession>(){
					@Override
					public void onClick(ClassSession item) {
						//time to goto that room
						prepareSwitch(item);
					}});
				return c;
			}
		});
		return b;
	}
	/**
	 * switches and setups to the master request page
	 * @param id requested
	 * @throws IOException if something happens in transit
	 */
	public static void switchToRequestPage(final Session classSession) throws IOException{
			//gets request data
			String id = classSession.getFileID();
			List<RequestSession> data = handler.getRequestData(id);
			//starts to build grid window
			CustomGridWindowBundle<RequestSession> b = new CustomGridWindowBundle<>(data,new String[]{"Request page"});
			//*********************************** LOWER BUTTON ON CLICK MEGA SAVE ACTION, WARNING MASSIVE MOUNT OF CHECKING AND DATA TYPE TRANSFERES AND SPLITS ***********************************
			GridWindow.OnClick<RequestSession> submit = (a)->{
				try{
					//collects noteworthy save data
					List<RequestSession> noteworthy = data.stream().filter(e->e.getStatus().equals("Accepted")||e.getStatus().equals("Not possible")).collect(Collectors.toList());
					//classData fragments to check, the integer represents session, this is done to check each session seperatly
					HashMap<Integer,ArrayList<ClassFragment>> classData = new HashMap<>();
					//go through note worthy, add to classData HashMap as nessassary
					for(RequestSession session:noteworthy){
						//split strings
						String[] sessionStringFragments = session.getSessions().split(",");
						for(String sessionString:sessionStringFragments){
							try{
								//create a session for every split, trin to its only the integer
								int sessionNum = Integer.parseInt(sessionString.trim());
								
								//ClassFragment ArrayList to grab
								ArrayList<ClassFragment> list = classData.get(sessionNum);
								//arraylist operations
									if(list == null){//if null, make a new one
										list = new ArrayList<>();
										classData.put(sessionNum, list);
									}
									//build classfragment from data
									ClassFragment frag = new ClassFragment(id,session.getStartTime(),session.getTimeFrame(),
											session.getSemester(),sessionNum,Utils.getDaySelect(session.getDay()),session.getName(),
											session.getCourseNum(),session.getRoom(),session.getStatus().equals("Accepted"),
											session.getEmail());
									//add to respective collection
									list.add(frag);
								//end of arraylist operations
							}
							catch(Throwable e){
								//error with parsing string to int.... most likely
								ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
							}
						}
					}
					//prepare for mass send
					ArrayList<ClassFragment> classFrags = new ArrayList<>();
					//check data consistancy
					for(ArrayList<ClassFragment> frag:classData.values()){
						for(int i = 0; i < frag.size();i++){
							for(int j = i+1; j < frag.size();j++){
								//double teir peek list to check every element agaisnt all next elements
								//items to compare
								ClassFragment one = frag.get(i);
								ClassFragment two = frag.get(j);
								//quick throw out, if these don't match, they arent even on the same plane of existance
								if(one.getSession() == two.getSession() && one.isAccepted() && two.isAccepted() && one.getSeason().equals(two.getSeason())&&one.getRoom().equals(two.getRoom())){
									//makes data easier to read than inside of all these fragments
									int time1 = one.getStartTime()+one.getDay()*SheetHandler.NEXT_DAY_SLOT;
									int time2 = two.getStartTime()+two.getDay()*SheetHandler.NEXT_DAY_SLOT;
									int endofweek = SheetHandler.NEXT_DAY_SLOT*SheetHandler.DAYS_IN_WEEK;
									int distance = Utils.getCircularDistance(time1,time2,endofweek);
									//if(getCircularDistance(o,t,SheetHandler.NEXT_DAY_SLOT*SheetHandler.DAYS_IN_WEEK) < one.getEndTime() + two.getEndTime()){
									//checks time distance, uses that to determine what is ahead of who, and see if one overreaches the other. by comparing min distance and max distance.
									if(((time1 + distance)%endofweek == time2 && (one.getEndTime() > distance) || ((two.getEndTime() > distance)))){
										throw new InvalidInputError("Invalid input, data being inserted has overlapping time slots");
									}
								}
							}
						}
						classFrags.addAll(frag);
						
					}
					ArrayList<ClassFragment> toSend= new ArrayList<>();
					classFrags.stream().filter((f)->f.isAccepted()).sequential().forEach(e->toSend.add(e));
					//now allow for validator to check to see if data already exists, and than send it if so.
					handler.insertNewData(id, toSend,noteworthy);
					mail.prepareEmails(classFrags);
					//switch to new version of page
					switchToRequestPage(classSession);
					
				}catch (MessagingException | IOException e1) {
					ErrorLog.writeError(e1, ErrorLevel.INTERNET_ERROR);
					FXHandler.handle.pushToast(e1.getMessage(),5000,100,100);
					
				}
				catch(InvalidInputError i){
					ErrorLog.writeError(i, ErrorLevel.USER_ERROR);
					FXHandler.handle.pushToast(i.getMessage(), 5000,100,100);
				}
		};
		//********************** ********************************************** END OF MASSIVE DATA CHECK HIKING********************************************************************
		//**************** BUTTTONS *******************************, 
		b.addButton("Submit Changes", submit);//reuses mount of code above
		//***************Navigation Capture **************************
		b.setCapture(new NavigationCapture(){

			@Override
			public void onNavigationAttempt(final Navigation nav) {
				//quick check seeing if anything matchs, using steam apis
				if(data.stream().anyMatch((e)->!e.getStatus().equals("Pending"))){
					AlertBundle bundle =new AlertBundle("Do you want to submit changes?");
					//save data, and leave
					bundle.addButton("Yes", ()->{
						FXHandler.handle.closePopup();
						submit.onClick(data);//fires mount of code above
						nav.approve();
					});
					//dont save data and leavae
					bundle.addButton("No", ()->{
						FXHandler.handle.closePopup();
						nav.approve();
					});
					//dont save, dont leavae
					bundle.addButton("Cancel Exit", ()->{
						FXHandler.handle.closePopup();
					});
					//prepare popup
					try {
						FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_ALERT_DEFAULT, bundle);
					} catch (IOException e1) {
						ErrorLog.writeError(e1, ErrorLevel.UNKNOWN_ERROR);
					}
				}
				else{
					//approve nav if conditions are met
					nav.approve();
				}
				
			}
			
		});
		//***************** NAVIGATION ************************
		b.setNavigation(new Navigation(){
				@Override
				public void approve() {
					 try {
						 //return home
						FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,DriveHandler.getClassSelector(classSession));
					} catch (IOException e) {
		
						FXHandler.handle.pushToast("Error in loading, possible reason->"+e.getMessage(), 4000,100,100);
					}
				}

				@Override
				public String getDisplayText() {//we are going up a level
					return DriveHandler.TO_ROOM_NAVIGATION;
				}},new Navigation(){

			@Override
			public void approve() {
				
				try {
					//switch back to this sage
					FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,DriveHandler.getYearSeasonSessionSelector());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getDisplayText() {
				
				return DriveHandler.TO_PAGE_SELECTION;
			}}
		);
		//********************* COLUMNS ****************************
		b.addColumn("Name",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory does stuff
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getName());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Email",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory does stuff
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getEmail());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Course Number",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory is out here
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getCourseNum());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		})
		;b.addColumn("Course CRN",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getCRN());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Semester",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory method
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getSemester());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Session",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory method
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getSessions());
                            setTooltip(new Tooltip(getText()));
                        };
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Day",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory method
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getDay());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Room Number",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory method
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+item.getRoom());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("start Time",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set text and tooltip
                            setText(""+ClassData.convertIntoMilitaryTime(item.getStartTime()));
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("duration",.08,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set factory and tooltip
                            setText(""+ClassData.convertIntoMilitaryTime(item.getTimeFrame()));
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Notes",.1,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//set factory
				TableCell<RequestSession,RequestSession> c = new TableCell<RequestSession,RequestSession>(){
					@Override
                    public void updateItem(RequestSession item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                        	//set factory and tooltip
                            setText(""+item.getNotes());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				
				return c;
			}
		});
		b.addColumn("Approve",.1,new Callback<TableColumn<RequestSession,RequestSession>,TableCell<RequestSession,RequestSession>>(){
			@Override
			public TableCell<RequestSession,RequestSession> call(TableColumn<RequestSession,RequestSession> arg0) {
				//factory for combo toolbox
				ComboBoxCell<RequestSession> box = new ComboBoxCell<RequestSession>(new StringConverter<RequestSession>(){
					public static final String APPROVED = "Accepted";
					public static final String PENDING = "Pending";
					public static final String NOT_POSSIBLE = "Not possible";
					@Override
					public String getString(RequestSession t) {
						//returns expected string
						return t.getStatus();
					}

					@Override
					public String[] getOptions(RequestSession t) {
						//shows options
						return new String[]{APPROVED,PENDING,NOT_POSSIBLE};
					}

					@Override
					public void onSelected(RequestSession base, String str) {
						//shows statues
						base.setStatus(str);
						System.out.println(str);
					}},
					true);//true so its always showing
				
				return box;
			}
		});
		//******************** ACTUALLY MADE IT SWITCH STAGE IS DOWN HERE ***************************
		FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,b);
	}

	/**
	 * 
	 * @param year to pick from
	 * @param season to pick from
	 * @param session to pick from
	 * @param day to pick from
	 * @param start time to start looking at
	 * @param timeFrame how many frames
	 * @return CustomGridWindowBundle that contains the requested data
	 * @throws IOException if something goes wrong
	 */
	public static CustomGridWindowBundle<TimeData> createDataFormatBundle(String year, String season, int session,String day, int start, int timeFrame) throws IOException{
		//************** data to start looking for **********************
		ArrayList<TimeData> data = new ArrayList<>();
		//get selected day from string
		int selectedDay = Utils.getDaySelect(day);
		
		//get correct file
		SheetFileContainer file = DriveHandler.getFileContainer(year);
		if(file == null){//wasn't found, may not exist
			throw new InvalidInputError("File or year not found");
		}
		//add all correct data from SheetHandler
		data.addAll(handler.getTimeData(file.getFileid(), season, session, selectedDay, start, timeFrame));
		//*********************Starting Container HERE ***********************************
		//build bundle header
		CustomGridWindowBundle<TimeData> bundle = new CustomGridWindowBundle<>(data,new String[]{year,"Season:"+season,"Session:"+session,"Day:"+day});
		//********************* ADDING NAVIGATION ****************************
		//add navigation to escape.
		bundle.setNavigation(new Navigation(){
			//
			@Override
			public void approve() {
				try {
					//when approved, its time to leave
					FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW,DriveHandler.getYearSeasonSessionSelector());
				} catch (IOException e) {
					//welp something went wrong
					ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
				}
			}

			@Override
			public String getDisplayText() {
				//name of this navigation
				return DriveHandler.TO_PAGE_SELECTION;
			}});
		//**********************ADDING COLUMS ***********************
		//add column room
		bundle.addColumn("Room",.2, new Callback<TableColumn<TimeData,TimeData>,TableCell<TimeData,TimeData>>(){
			@Override
			public TableCell<TimeData,TimeData> call(TableColumn<TimeData,TimeData> arg0) {
				//table cell generator for the column
				TableCell<TimeData,TimeData> c = new TableCell<TimeData,TimeData>(){
					//update item method
					@Override
                    public void updateItem(TimeData item, boolean empty) {
						//use inside calls
                        super.updateItem(item, empty);
                        if(!empty){//if not empty, set item
                        	//set text and tool tip
                        	setText(item.getRoom());
                            setTooltip(new Tooltip(getText()));
                        }
                    }
				};
				//return cell created
				return c;
			}
		});
		
		for(int i =0; i < timeFrame;i++){
			//loosly stored J for future reference in table.
			final int j = i;
			//add column using the day AND current time in military time
			bundle.addColumn(Utils.toDay(selectedDay+((start+i)/SheetHandler.NEXT_DAY_SLOT))+ClassData.convertIntoMilitaryTime((start+i)%SheetHandler.NEXT_DAY_SLOT),.2, new Callback<TableColumn<TimeData,TimeData>,TableCell<TimeData,TimeData>>(){
				@Override
				public TableCell<TimeData,TimeData> call(TableColumn<TimeData,TimeData> arg0) {
					//table cell created by the factory
					TableCell<TimeData,TimeData> c = new TableCell<TimeData,TimeData>(){
						//Session target;
						@Override
	                    public void updateItem(TimeData item, boolean empty) {
							//set items to update
	                        super.updateItem(item, empty);
	                        if(!empty){//if not empty, do requests
	                        	//set text and tooltip
	                        	setText(item.getElements()[j]);
	                            setTooltip(new Tooltip(getText()));
	                        }
	                    }
					};
					
					return c;
				}
			});
			
		}
		return bundle;
	}
	/**
	 * Creates a copy of a new sheet with the same classes, the new sheet will be clean after this operation
	 * @param srcPointer for the SheetFileContainer to be referenced from/to
	 * @param newFileName the chosen name for the new file
	 * @throws IOException if something occurs in transit
	 */
	public static void addNewSheet(String srcPointer, String newFileName) throws IOException{
		//get the respective pointer based on the file
		SheetFileContainer sfc = DriveHandler.Selectors.get(srcPointer);
		//see if it exists, if not, throw user error.
		if(sfc == null){
			throw new InvalidInputError("Src file data was not found");
		}
		//get src files id
		String fileid = sfc.getFileid();
		//create a copy of the file
		String newFileID = accessor.copyFile(fileid,newFileName);
		//clean the new file
		handler.cleanSheet(newFileID);
	}
}
