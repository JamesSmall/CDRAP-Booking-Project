/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdrap.UI.Controller.Popup;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.ErrorManager.InvalidInputError;
import com.cdrap.GoogleAccessor.DriveHandler;
import com.cdrap.GoogleAccessor.SheetHandler;
import com.cdrap.UI.FXHandler;
import com.cdrap.transit.ClassData;
import com.cdrap.transit.TimeData;
import com.cdrap.transit.UI.CustomGridWindowBundle;

import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;

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
public class SearchByTimeFrameController implements Initializable {

	@FXML
	private ComboBox<String> yearBox;
	@FXML
	private ComboBox<String> seasonBox;
	@FXML
	private ComboBox<String> sessionBox;
	@FXML
	private ComboBox<String> day;
	@FXML
	private ComboBox<String> timeStart;
	@FXML 
	private ComboBox<String> classTime;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

		ObservableList<String> toSee = FXCollections.observableArrayList(DriveHandler.getFileContainerKeys());
    	yearBox.setItems(toSee); //setting items
    	yearBox.setEditable(true);
    	seasonBox.setEditable(true);
    	sessionBox.setEditable(true);
    	timeStart.setEditable(true);
    	classTime.setEditable(true);
    	day.setEditable(true);

        ObservableList<String> l;
        ArrayList<String> start = new ArrayList<>();
        for(int i = 0; i < SheetHandler.NEXT_DAY_SLOT;i++){
        	start.add(ClassData.convertIntoMilitaryTime((i+SheetHandler.CLASS_START_TIME) % SheetHandler.NEXT_DAY_SLOT));
        }
    	l = FXCollections.observableArrayList(start);
    	timeStart.setItems(l);
        ArrayList<String> items = new ArrayList<>();
        for(int i = 1; i < SheetHandler.NEXT_DAY_SLOT;i++){
        	items.add(ClassData.convertIntoMilitaryTime(i));
        }
    	l = FXCollections.observableArrayList(items);
    	classTime.setItems(l);
        
        
    	l = FXCollections.observableArrayList(Arrays.asList("Monday","Tuesday","Wednesday","thursday","friday","Saturday","Sunday"));
    	day.setItems(l);
        //setup shown items
		//try {
			//l = FXCollections.observableArrayList(StubTester.getClassDataDefault());
			//classBox.setItems(l);
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
    	l = FXCollections.observableArrayList(Arrays.asList(SheetHandler.SEASON_FALL_TAB,SheetHandler.SEASON_SUMMER_TAB,SheetHandler.SEASON_SPRING_TAB));
    	seasonBox.setItems(l);
    	//yearBox.setItems(FXCollections.observableArrayList(Arrays.asList("2018")));
    	sessionBox.setItems(FXCollections.observableArrayList(Arrays.asList(""+1,""+2,""+3,""+4,""+5)));
    }    
    @FXML
    private void onCancel(ActionEvent event){
    	FXHandler.handle.closePopup();
    }
    public void onYearBoxUpdated(){
    	
    }
    @FXML
    private void onSearch(ActionEvent event){
    	boolean error = false;
    	int session = 1;
    	int timeStart = 0,timeFrame = 0;
    	String season, day;
    	String strYear,strSession;
    	strYear = yearBox.getSelectionModel().getSelectedItem();
    	if(strYear == null || strYear.isEmpty()){
    		yearBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	else{
    		yearBox.setStyle("");
    	}
    	strSession = sessionBox.getSelectionModel().getSelectedItem();
    	if(strSession == null || strSession.isEmpty()){
    		sessionBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	else{
    		try{
    			session = Integer.parseInt(strSession);
    			sessionBox.setStyle("");
    		}
    		catch(Exception ex){
    			sessionBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    			error = true;
    		}
    	}
    	season = seasonBox.getSelectionModel().getSelectedItem();
    	if(season == null || season.isEmpty()){
    		seasonBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	else{
    		seasonBox.setStyle("");
    	}
    	day = this.day.getSelectionModel().getSelectedItem();
    	if(day == null || day.isEmpty()){
    		this.day.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	else{
    		this.day.setStyle("");
    	}
    	try{
    		timeStart = ClassData.convertMilitaryToInt(this.timeStart.getSelectionModel().getSelectedItem());
    		timeFrame = ClassData.convertMilitaryToInt(this.classTime.getSelectionModel().getSelectedItem());
    		this.timeStart.setStyle("");
    		this.classTime.setStyle("");
    	}
    	catch(Exception ex){
    		this.timeStart.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		this.classTime.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	if(!error){
    		
	    	try{
	    		CustomGridWindowBundle<TimeData> b = DriveHandler.createDataFormatBundle(strYear, season, session, day, timeStart, timeFrame);
	    		if(b!=null){
	    			FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW, b);
	    			FXHandler.handle.closePopup();
	    			return;
	    		}
	    		else{
	    			throw new RuntimeException("Invalid date selection exception");
	    		}
	    		//FXHandler.handle.switchStage(FXHandler.FX_PAGE_DIR_EXCEL_DOC_PAGE,FakeDatabase.findAndWrapDateTimeRow(year, season, session, room));
	    	}

	    	catch(IOException ex){
	    		FXHandler.handle.pushToast("Error, problem with the internet -> possible reason: "+ex.getMessage(), 4000, 100,100);
	    		ErrorLog.writeError(ex, ErrorLevel.INTERNET_ERROR);
	    	}
	    	catch(InvalidInputError ex){
	    		FXHandler.handle.pushToast("Error, data not found -> possible reason:"+ex.getMessage(), 4000, 100,100);
	    		ErrorLog.writeError(ex, ErrorLevel.USER_ERROR);
	    	}
	    	catch(Exception ex){
	    		
	    		FXHandler.handle.pushToast("Error, unexpected error -> possible reason:"+ex.getMessage(), 4000, 100,100);
	    		ErrorLog.writeError(ex, ErrorLevel.UNKNOWN_ERROR);
	    	}
    	}
		Toolkit.getDefaultToolkit().beep();
    	FXHandler.handle.checkPopupSize();
    }
    
}
