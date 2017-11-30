/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdrap.UI;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.GoogleAccessor.DriveHandler;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
public class FXHandler extends Application {
	public static final String FX_PAGE_CUSTOM_SHEET_WINDOW = "com/cdrap/UI/FXML/GridWindow.fxml";
	public static final String FX_POPUP_DIR_SEARCH_BY_CLASS_WINDOW_DEFAULT = "com/cdrap/UI/FXML/Popup/SearchByClass.fxml";
	public static final String FX_POPUP_DIR_SEARCH_BY_TIME_WINDOW_DEFAULT = "com/cdrap/UI/FXML/Popup/SearchByTimeFrame.fxml";
	public static final String FX_POPUP_DIR_ALERT_DEFAULT = "com/cdrap/UI/FXML/Popup/Alert.fxml";
	public static final String FX_POPUP_DIR_ROOM_ADD_DEFAULT = "com/cdrap/UI/FXML/Popup/RoomAdd.fxml";
	public static final String FX_POPUP_DIR_ROOM_REMOVE_DEFAULT = "com/cdrap/UI/FXML/Popup/RoomRemove.fxml";
    private Stage stage;
    private Stage popup;
    private onCloseAttempt oca = () -> {return true;};
    public static FXHandler handle;
    @Override
    public void start(final Stage s) throws Exception {
    	//handle is now set
        handle = this;
        this.stage = s;
        s.setTitle("Room Booking Application");
        //sets the default size of this stage
        stage.setWidth(1000);
        stage.setHeight(500);
        //tells the user this may take a while
        Stage popup = pushToast("loading files from google docs, this may take a while",5000000,100,100);
		stage.show();
		//prevents instant exists
		Platform.setImplicitExit(false);
		final EventDispatcher dispatch = stage.getEventDispatcher();
		stage.setEventDispatcher(new EventDispatcher(){
			@Override
			public Event dispatchEvent(Event event, EventDispatchChain tail) {
				if(FXHandler.this.popup != null){
					event.consume();
				}
				return dispatch.dispatchEvent(event, tail);
			}});
		//checks to see if it should close
		stage.setOnCloseRequest((se)->{
			if(oca.attempt()){
				System.exit(0);
			}
			else{
				//DO NOT EXIT
				se.consume();
			}
		});
		//time to go to google services
        new Thread(()->{
        	try {
        		//check to see if we even have internet
        		FXHandler.checkInternetAccess();
        		//setup sheets
        		DriveHandler.refreshSheets();
        		//get bundle base from data
        		final ResourceBundle bundle = DriveHandler.getYearSeasonSessionSelector();
        		Platform.runLater(()->{ //back to javafx
					try {
						//setup stage
						switchStage(FX_PAGE_CUSTOM_SHEET_WINDOW,bundle);
						popup.hide();
						popup.close();
					} catch (IOException e) {
						//problems with harddrive
						e.printStackTrace();
					}
        		});
			} catch (Throwable e) {
				ErrorLog.writeError(e, ErrorLevel.INTERNET_ERROR);
				Platform.runLater(()->{
					stage.setOnCloseRequest((se)->{});//prevents closing while stage gives warning
					stage.close();
					popup.close();
					FXHandler.handle.pushToast("Error on loading->"+e.getMessage(), 5000,100,100);
				});
				//java will close down when all windows are down
			}
        }).start();
        
    }
    /**
     * 
     * @throws IOException if not connected to the internet
     */
    private static void checkInternetAccess() throws IOException{
    	try{
    		//google address, if google is down or out of reach, than google services are also probably down or out of reach
    		final URL url = new URL("http://www.google.com");
    		final URLConnection conn = url.openConnection();
    		conn.connect();
    	}
    	catch(Throwable e){
    		throw new IOException("Cannot connect to the internet");
    	}
    }
    /**
     * 
     * @param file dir to switch to
     * @param bundle to load the file with
     * @throws IOException if their are harddrive problems
     */
    public void switchStage(String file,ResourceBundle bundle) throws IOException{
    	//makes the new  scene have the same values
    	double curWidth = stage.getWidth();
    	double curHeight = stage.getHeight();
    	////build it
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file), bundle);
        //set it
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //same dimensions
        stage.setWidth(curWidth);
        stage.setHeight(curHeight);
        //stage.show();
    }
    /**
     * 
     * @param file to start the popup from
     * @param bundle to send to the popup
     * @throws IOException if something is wrong with the harddrive
     */
    public void performPopup(String file, ResourceBundle bundle) throws IOException{
    	//root to handle
    	Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file),bundle);
    	//popup for stage
    	popup = new Stage();
    	Scene scene = new Scene(root);
    	//scene it will have
    	popup.setScene(scene);
    	//show it now
    	popup.show();
    	//above the user head
    	popup.setAlwaysOnTop(true);
    	//cannot be resized, its a popup
    	popup.setResizable(false);
    	//make it disapear when closes
    	popup.setOnCloseRequest((e)->{popup = null;});
    	//fix some minor size problems
    	popup.sizeToScene();
    }
    /**
     * closes the popup the offical way
     */
    public void closePopup(){
    	if(popup!=null){
    		popup.close();
    		popup = null;
    	}
    }
   /**
    * 
    * @return true if the popup is active
    */
    public boolean isPopupActive(){
    	if(popup!=null){
    		return popup.isShowing();
    	}
    	return false;
    }
    /**
     * resizes popup, ussually used by popup to check size
     */
    public void checkPopupSize(){
    	popup.sizeToScene();
    }
    /**
     * 
     * @param toastMsg to display to user
     * @param toastDelay time it will last
     * @param fadeInDelay time it takes to fade in
     * @param fadeOutDelay time it takes to fade out at end
     * @return
     */
    public Stage pushToast(String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay)
    {
    	//stage it set onto
        Stage toastStage=new Stage();
        //toastStage.initOwner(stage);
        //cannot be resized, static
        toastStage.setResizable(false);
        //size of the thing
        toastStage.setWidth(500);
        toastStage.setHeight(50);
        //its transparent and see through
        toastStage.initStyle(StageStyle.TRANSPARENT);
        
        //text to display
        Text text = new Text(toastMsg);
        //size of the font
        text.setFont(Font.font("Verdana", 12));
        //text.setStyle("-fx-background-color: rgba(.5,.5,.5, 1.0);");
        //color of the text
        text.setFill(Color.RED);
        
        //stackpane to wrap it in
        StackPane root = new StackPane(text);
        //style of the pane
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0,0,0, .8); -fx-padding: 50px;");
        //see through
        root.setOpacity(0);

        //scene to plaster on
        Scene scene = new Scene(root);
        //make transparent also
        scene.setFill(Color.TRANSPARENT);
        //setup fx stuff
        toastStage.setScene(scene);
        toastStage.show();
        toastStage.setAlwaysOnTop(true);
        toastStage.requestFocus();
        //setup time focus
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(fadeInKey1);  
        //remove when finished
        fadeInTimeline.setOnFinished((ae) -> 
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(toastDelay);
                }
                catch (InterruptedException e)
                {

					ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
                }
                // fade out when ready
                   Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0)); 
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close()); 
                    fadeOutTimeline.play();
            }).start();
        }); 
        //start fadein property
        fadeInTimeline.play();
        return toastStage;
    }
    /**
     * Main start of this application, launchs javafx
     * @param strings inserted via cmd line
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * 
     * @param oca the OnCloseAttempt Lamdba interface to use
     */
    public void setOnCloseAttempt(onCloseAttempt oca){
    	this.oca = oca;
    }
    public static interface onCloseAttempt{
    	boolean attempt();
    }
}
