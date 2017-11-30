package com.cdrap.UI.Controller.Wrapper;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.*;
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
public class ButtonCell <T> extends TableCell<T,T>{
	//internal boxf
	
	private final Button btn = new Button();
	private final ButtonText<T> bt;
	T item;
	public ButtonCell(ButtonText<T> reader, EventHandler<? super MouseEvent> handler){
		bt = reader;
		btn.setOnMouseClicked(handler);
		
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setPrefWidth(Double.MAX_VALUE);
	}
	public ButtonCell(ButtonText<T> reader, OnClick<T> click){
		bt = reader;
		btn.setOnMouseClicked((e)->{
			click.onClick(item);
		});
		
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setPrefWidth(Double.MAX_VALUE);
	}
	
	@Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setGraphic(btn);
            setText(null);
            btn.setText(bt.getText(item));
            this.item = item;
        } else {
            setGraphic(null);
            setText(null);
            this.item = null;
        }
        
    }
	public static interface ButtonText<T>{
		String getText(T item);
	}
	public interface OnClick<T>{
		void onClick(T item);
	}
}
