package com.cdrap.UI;

import com.cdrap.UI.Controller.Wrapper.SelfCallBack;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
public class FXUtils {
	//cannot init, util class
	private FXUtils(){
		
	}
	/**
	 *
	 * @param table to add to
	 * @param transfereNode factory to be set
	 */
	public static <T> void setupTableColumnView(TableColumn<T,T> col, Callback<TableColumn<T,T>,TableCell<T,T>> transfereNode){
		//skip this factory and use hte getValueFactory
		col.setCellValueFactory(new SelfCallBack<T>());
		//factory!
		col.setCellFactory(transfereNode);
		
	}
}
