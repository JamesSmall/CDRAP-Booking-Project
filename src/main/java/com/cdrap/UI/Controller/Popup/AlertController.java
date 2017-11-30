package com.cdrap.UI.Controller.Popup;

import java.awt.Toolkit;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.cdrap.transit.UI.AlertBundle;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
public class AlertController implements Initializable {
	@FXML
	private Label Text;
	@FXML
	private HBox buttonsArea;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//alert bundle to be displayed
		AlertBundle b = (AlertBundle) arg1;
		java.util.List<Node> btns = buttonsArea.getChildren();
		btns.clear();
		//buttons to be added
		for(Entry<String,Runnable> btnData: b.getButtons().entrySet()){
			//buttons to be added
			Button btn = new Button(btnData.getKey());
			btns.add(btn);
			btn.setPrefWidth(1000000);
			//buttons to be selected
			btn.setOnMouseClicked((e)->{
				btnData.getValue().run();
			});
		}
		//button text to set
		Text.setText(b.getDisplayText());
    	Toolkit.getDefaultToolkit().beep();
	}

}
