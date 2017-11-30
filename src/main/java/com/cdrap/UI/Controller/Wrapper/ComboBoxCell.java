package com.cdrap.UI.Controller.Wrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
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
public class ComboBoxCell<T> extends TableCell<T,T>{
	//internal box
	private ComboBox<String> box;
	//reader
	private boolean alwaysVisible;
	private StringConverter<T> stringConvert;
	
	public ComboBoxCell(StringConverter<T> c){
		this(c,false);
	
	}public ComboBoxCell(StringConverter<T> c,boolean alwaysVisiable){
		this.stringConvert = c;
		this.alwaysVisible = alwaysVisiable;
	}
	@Override
	public void startEdit(){
		super.startEdit();
		if(box == null){
			createComboBox();
		}

        setGraphic(box);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}
	@Override
    public void cancelEdit() {
		super.cancelEdit();
		if(!this.alwaysVisible){
	        setContentDisplay(ContentDisplay.TEXT_ONLY);
	        handleSelection();
	        setText(this.stringConvert.getString(getItem()));
	        //super.cancelEdit();
		}
    }
	public void handleSelection(){
		String selected = box.getSelectionModel().getSelectedItem();
		if(selected !=null){
			this.stringConvert.onSelected(getItem(), selected);
		}
	}
	private void createComboBox() {
        // ClassesController.getLevelChoice() is the observable list of String
        box = new ComboBox<>(FXCollections.observableArrayList(this.stringConvert.getOptions(getItem())));
        box.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
        box.getSelectionModel().select(stringConvert.getString(getItem()));
        box.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE || t.getCode() == KeyCode.ENTER) {
                    //cancelEdit();
                	handleSelection();
                	commitEdit(getItem());
                }
            }
        });
        box.valueProperty().addListener((e)->{
        	handleSelection();
        	commitEdit(getItem());
        });
    }
	public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing() || this.alwaysVisible) {
                if (box == null) {
                	createComboBox();
                }
            	ObservableList<String> observableStrings = FXCollections.observableArrayList(
    			this.stringConvert.getOptions(
    					getItem()));
        		box.setItems(observableStrings);
                setGraphic(box);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(this.stringConvert.getString(item));
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }
	public static interface StringConverter<T>{
		String getString(T t);
		String[] getOptions(T t);
		void onSelected(T base,String str);
	}
}
