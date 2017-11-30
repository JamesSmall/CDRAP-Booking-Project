package com.cdrap.UI.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.UI.FXHandler;
import com.cdrap.UI.FXUtils;
import com.cdrap.transit.Callback.Navigation;
import com.cdrap.transit.Callback.NavigationCapture;
import com.cdrap.transit.UI.CustomGridWindowBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
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
public class GridWindow<T> implements Initializable{
	@FXML
    private HBox HeaderText;
    @FXML
    private TableView<T> TableViewer;
    @FXML
    private HBox ButtonsContainers;
    @FXML
    private Menu Navigation;
    private ArrayList<ChangingWidthColumn<T>> columns = new ArrayList<>();
    //private ArrayList<Button> actions = new ArrayList<>();
    private Navigation[] navigation;
    private NavigationCapture capture;
    private CustomGridWindowBundle<T> bundle;
    //navigation buttons
    
    
    //to be streamlined in the future
    @FXML
    private void onNewDocument(ActionEvent event) {
    	
    }
    //to be removed in future release
    @FXML
    private void onReturnClick(ActionEvent event){
    	
    }
    /**
     * this method will activate searchByTime
     * @param event passed by javafx
     */
    @FXML
    private void onSearchByTime(ActionEvent event){
    	try {
    		//checks to se if capture exists
    		if(this.capture!=null){
    			this.capture.onNavigationAttempt(()->{
    				//accepted by capture
        			try {
						FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_SEARCH_BY_TIME_WINDOW_DEFAULT, null);
					} catch (IOException e) {
						ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
					}
    			});
    		}
    		else{//run instantly
    			FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_SEARCH_BY_TIME_WINDOW_DEFAULT, null);
    		}
		} catch (IOException e) {
			ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
		}
    }
    @FXML
    private void onSearchByClass(ActionEvent event){
    	if(this.capture!=null){//checks to see if capture exists
    		capture.onNavigationAttempt(()->{//fire off event
    			try {
    				FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_SEARCH_BY_CLASS_WINDOW_DEFAULT, null);
    			} catch (IOException e) {
					ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
    			}
    		});
    	}
    	else{//fire off event, no capture
	    	try {
				FXHandler.handle.performPopup(FXHandler.FX_POPUP_DIR_SEARCH_BY_CLASS_WINDOW_DEFAULT, null);
			} catch (IOException e) {
				ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
			}
    	}
    }
    /**
     * gets bundle of this window
     * @return
     */
    public CustomGridWindowBundle<T> getBundle(){
    	return bundle;
    }
    /**
     * method passed via javafx, it setups
     */
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb) {
    	if(rb instanceof CustomGridWindowBundle<?>){
	    	bundle = (CustomGridWindowBundle<T>) rb;
	    	//clear holders
	    	this.HeaderText.getChildren().clear();
	    	this.ButtonsContainers.getChildren().clear();
	    	//add buttons
	        for(CustomGridWindowBundle<T>.ButtonHolder b:bundle.getButtonHolders()){
	        	this.addButton(b.getText(), b.getClick());
	        }
	        //add columns
	        for(CustomGridWindowBundle<T>.ColumnHolder b:bundle.getColumnsHolders()){
	        	this.addColumn(b.getTopText(),b.getWeight(),b.getCallback());
	        }
	        //set items of this window display
	        this.setItems(bundle.getList());
	        //add on resize event listener
	        this.TableViewer.widthProperty().addListener((ob,oldVal,newVal)->{resizeEvent(newVal.doubleValue());});
	        Platform.runLater(()->{
	        	resizeEvent();
	        });
	        
	        //add navigation and its capture
	        this.navigation = bundle.getNavigation();
	        this.capture = bundle.getCapture();
	        if(navigation !=null){
	        	
	        	//makes menu items
	        	List<MenuItem> items = (this.Navigation.getItems());
		        for(final Navigation nav: navigation){
		        	//menu items
		        	MenuItem m = new MenuItem(nav.getDisplayText());
		        	m.setOnAction((e)->{
		        		if(capture != null){
		        			capture.onNavigationAttempt(nav);
		        		}
		        		else{
		        			nav.approve();
		        		}
		        	});
		        	items.add(0,m);
		        }
	        }
	        //makes sure cells can be selected
	        this.TableViewer.setEditable(true);
	        for(String text:bundle.getHeaderText()){
	        	addHeaderText(text);
	        }
	        FXHandler.handle.setOnCloseAttempt(()->{
	        		if(capture == null){
	        			return true;
	        		}
	        		else{
	        			//exit the system
	        			capture.onNavigationAttempt(()->{
	        				System.exit(0);
	        			});
	        			return false;
	        		}
	        });
    	}
    	else{
    		throw new RuntimeException("Invalid input type, expected a CustomGridWindowBundle");
    	}
    }
    /**
     * 
     * @param text to be added at the header
     */
    private void addHeaderText(String text){
    	TextField field = new TextField();
    	//make it resizable pretty large
    	field.setPrefWidth(10000000);
    	field.setText(text);
    	field.setEditable(false);
    	//field adding
    	this.HeaderText.getChildren().add(field);
    }
    /**
     * 
     * @param text to be set
     * @param evt action when clicked
     */
    private void addButton(String text,OnClick<T> evt){
    	Button b = new Button();
    	b.setPrefWidth(10000000);
    	b.setText(text);
    	b.setOnMouseClicked((e)->{evt.onClick(this.TableViewer.getItems());});
    	this.ButtonsContainers.getChildren().add(b);
    }
    /**
     * 
     * @param columnName to be set
     * @param weight of the column
     * @param factory the colum uses when resized
     */
    private void addColumn(String columnName,double weight, Callback<TableColumn<T,T>,TableCell<T,T>> factory){
    	ChangingWidthColumn<T> c = new ChangingWidthColumn<>(weight);
    	c.setText(columnName);
    	//c.set
    	this.columns.add(c);
    	FXUtils.setupTableColumnView(c, factory);
    	resizeEvent();
    	this.TableViewer.getColumns().add(c);
        this.TableViewer.refresh();
        c.setEditable(true);
        c.setSortable(false);
    }
    private void resizeEvent(){
    	resizeEvent(this.TableViewer.getWidth());
    }
    /**
     * 
     * @param size what the size of this value
     */
    private void resizeEvent(double size){
    	for(int i = 0; i < columns.size();i++){
    		columns.get(i).setMinWidth(Math.min(size,size*columns.get(i).width));
    		columns.get(i).setPrefWidth(Math.min(size,size*columns.get(i).width));
    		columns.get(i).setMaxWidth(size);
    	}
    }
    /**
     * 
     * @param items to be displayed
     */
    public void setItems(Collection<T> items){
        ObservableList<T> l = FXCollections.observableArrayList(items);
        this.TableViewer.setItems(l);
    }
    public static interface OnClick<T>{
    	void onClick(List<T> items);
    }
    private class ChangingWidthColumn<J> extends TableColumn<J,J>{
    	private double width;
    	public ChangingWidthColumn(double width){
    		this.width = width;
    	}
    }
}
