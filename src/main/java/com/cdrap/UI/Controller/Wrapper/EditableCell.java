package com.cdrap.UI.Controller.Wrapper;
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
public class EditableCell<T> extends TableCell<T,T>{
 	private TextField textField;
 	private CellEditHandler<T> handler;
 	private T current;

    public EditableCell(CellEditHandler<T> event) {
    	this.handler = event;
    }

    @Override
    public void startEdit() {
        super.startEdit();

        super.textProperty().addListener((e)->{
        	super.getTooltip().setText(getText());
        });
        super.setTooltip(new Tooltip(""));
        if (textField == null && current != null) {
            createTextField(current);
        }
        textField.setText(getString(current));
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();
    }
    public void cancelEditTrue(){
    	super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
    @Override
    public void cancelEdit() {
        //super.cancelEdit();
    	if(this.current!=null){
    		this.handler.change(current, textField.getText());
    		//commitEdit(current);
    		setText(handler.getText(current));
    	}
		super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        this.current = item;
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
           // setGraphic(textField);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString(item));
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString(item));
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }
    private void createTextField(T item) {
        textField = new TextField(getString(item));
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) {
            	this.handler.change(item, textField.getText());
                commitEdit(item);
            	setText(handler.getText(item));
                
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEditTrue();
            }
        });
    }

    private String getString(T item) {
        return getItem() == null ? "" :this.handler.getText(item);
    }
    public interface CellEditHandler<T>{
    	void change(T item,String str);
    	String getText(T item);
    }
}
