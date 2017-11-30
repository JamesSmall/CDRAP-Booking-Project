/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdrap.UI.Controller.Popup;

import java.net.URL;
import java.util.ResourceBundle;


import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.ErrorManager.InvalidInputError;
import com.cdrap.GoogleAccessor.DriveHandler;
import com.cdrap.UI.FXHandler;
import com.cdrap.transit.Session;
import com.cdrap.transit.UI.RoomHandlerBundle;

import javafx.scene.control.TextField;
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
public class RoomAddController implements Initializable {
	private Session session;
	@FXML
	private TextField room;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	if(rb instanceof RoomHandlerBundle && rb != null){
    		RoomHandlerBundle rooms = (RoomHandlerBundle) rb;
    		session = rooms.getSession();
    	}
    	else{
    		throw new RuntimeException("Invalid resource bundle");
    	}
    }
    @FXML
    private void onCancel(ActionEvent event){
    	FXHandler.handle.closePopup();
    }
    @FXML
    private void onSearch(ActionEvent event){
    	try {
    		String room = this.room.getText();
    		if(room != null && !room.trim().isEmpty()){
	    		DriveHandler.addRoom(session,room);
	    		FXHandler.handle.closePopup();
				FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW, DriveHandler.getClassSelector(session));
    		}
    		else{
        		FXHandler.handle.pushToast("Cannot add with empty string", 5000,100,100);
    		}
		} 
    	catch(InvalidInputError iir){
    		ErrorLog.writeError(iir, ErrorLevel.USER_ERROR);
    		FXHandler.handle.pushToast(iir.getMessage(), 5000,100,100);
    	}
    	catch (Exception e) {
			ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
    		FXHandler.handle.pushToast(e.getMessage(), 3000,100,100);
		}
    }
    
}
