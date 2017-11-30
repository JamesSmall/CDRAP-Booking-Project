/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdrap.UI.Controller.Popup;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.ErrorManager.InvalidInputError;
import com.cdrap.GoogleAccessor.DriveHandler;
import com.cdrap.GoogleAccessor.SheetHandler;
import com.cdrap.UI.FXHandler;
import com.cdrap.transit.ClassData;
import com.cdrap.transit.UI.CustomGridWindowBundle;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
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
public class SearchByClassController implements Initializable {
	
	@FXML
	private ComboBox<String> yearBox;
	@FXML
	private ComboBox<String> seasonBox;
	@FXML
	private ComboBox<String> sessionBox;
	@FXML
	private ComboBox<String> classBox;
	@FXML
	private AnchorPane back;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	yearBox.getItems(); //setting items
    	yearBox.setEditable(true);
    	seasonBox.setEditable(true);
    	sessionBox.setEditable(true);
    	classBox.setEditable(true);
        ObservableList<String> l;
        
    	l = FXCollections.observableArrayList(Arrays.asList(SheetHandler.SEASON_FALL_TAB,SheetHandler.SEASON_SUMMER_TAB,SheetHandler.SEASON_SPRING_TAB));
    	seasonBox.setItems(l);
    	//selectable options
    	sessionBox.setItems(FXCollections.observableArrayList(Arrays.asList(""+1,""+2,""+3,""+4,""+5)));
		ObservableList<String> toSee = FXCollections.observableArrayList(DriveHandler.getFileContainerKeys());
    	yearBox.setItems(toSee);
    	yearBox.valueProperty().addListener((o,s1,s2)->{
    		if(s1==null || (s2!=null &&!s1.equals(s2))){
    			try{
	    			//int parsed = Integer.parseInt(s2);
	    			DriveHandler.ASycnRoomGet(s2, (list)->{
	    				ObservableList<String> toSee2 = FXCollections.observableArrayList(list);
	    				classBox.setItems(toSee2);
	    			});
	    			
    			}
    			catch(Exception ex){
    				
    			}
    		}
    	});
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
    	int session = 0;
    	String season, room;
    	String strYear,strSession;
    	strYear = yearBox.getSelectionModel().getSelectedItem();
    	//year checker
    	if(strYear != null && !strYear.isEmpty()){
    		
    		//year = Integer.parseInt(strYear);
    		yearBox.setStyle("");
    	}
    	else {
    		error = true;
    		yearBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px;");
    	}
    	
    	//session checker
    	strSession = sessionBox.getSelectionModel().getSelectedItem();
    	if(strSession != null && !strSession.isEmpty()){
    		try{
    			session = Integer.parseInt(strSession);
    			sessionBox.setStyle("");
    		}
    		catch(Exception ex){
    			sessionBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		}
    	}
    	else{
    		sessionBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    		error = true;
    	}
    	//season checker
    	season = seasonBox.getSelectionModel().getSelectedItem();
    	if(season != null && !season.isEmpty()){
    		seasonBox.setStyle("");
    	}
    	else{
    		seasonBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px;");
    		error = true;
    	}
    	//room checker
    	room = classBox.getSelectionModel().getSelectedItem();
    	if(room != null && !room.isEmpty()){
    		classBox.setStyle("");
    	}
    	else{
    		error = true;
    		classBox.setStyle("-fx-border-color: red ; -fx-border-width: 1px;");
    	}
    	if(!error){
	    	try{
	    		CustomGridWindowBundle<ClassData> b = DriveHandler.getClassData(strYear,season,session,room);
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
    	FXHandler.handle.checkPopupSize();
    	Toolkit.getDefaultToolkit().beep();
    	
    }
    
}
