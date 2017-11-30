package com.cdrap.transit.UI;

import java.util.ArrayList;
import java.util.Collection;

import com.cdrap.UI.Controller.GridWindow.OnClick;
import com.cdrap.transit.Callback.Navigation;
import com.cdrap.transit.Callback.NavigationCapture;
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
public class CustomGridWindowBundle<T> extends ResourceList<T>{
	private ArrayList<ColumnHolder> columns = new ArrayList<>();
	private ArrayList<ButtonHolder> buttons = new ArrayList<>();
	private final String[] HeaderText;
    private Navigation[] navigation;
    private NavigationCapture capture;
	/**
	 * 
	 * @param objs collection that will be displayed
	 * @param headerText to display, notifying the user of what they are looking at
	 */
	public CustomGridWindowBundle(Collection<T> objs,String[] headerText) {
		super(objs);
		this.HeaderText = headerText;
	}
	/**
	 * 
	 * @return header text the user sees
	 */
	public String[] getHeaderText(){
		return HeaderText;
	}
	/**
	 * 
	 * @return navigation options the user can click on
	 */
	public Navigation[] getNavigation() {
		return navigation;
	}
	/**
	 * 
	 * @param navigation sets the navigation controls
	 */
	public void setNavigation(Navigation... navigation) {
		this.navigation = navigation;
	}
	/**
	 * 
	 * @return the navigation capture
	 */
	public NavigationCapture getCapture() {
		return capture;
	}
	/**
	 * 
	 * @param capture when a navigation button is clicked.
	 */
	public void setCapture(NavigationCapture capture) {
		this.capture = capture;
	}
	/**
	 * 
	 * @param text of this entry
	 * @param weight how much space it expected to take
	 * @param call factory of this method
	 */
	public void addColumn(String text,double weight, Callback<TableColumn<T,T>,TableCell<T,T>> call){
		this.columns.add(new ColumnHolder(text,weight,call));
	}
	/**
	 * 
	 * @param text of this entry
	 * @param click what happens when it clicked
	 */
	public void addButton(String text, OnClick<T> click){
		this.buttons.add(new ButtonHolder(text,click));
	}
	/**
	 * 
	 * @return get the columns
	 */
	public ArrayList<ColumnHolder> getColumnsHolders(){
		return this.columns;
	}
	/**
	 * 
	 * @return the buttons of this bundle
	 */
	public ArrayList<ButtonHolder> getButtonHolders(){
		return this.buttons;
	}
	/**
	 * 
	 * @author James
	 */
	public class ColumnHolder{
		private String topText;
		private double weight;
		private Callback<TableColumn<T,T>,TableCell<T,T>> callback;
		/**
		 * 
		 * @param text of this column
		 * @param weight of this column, how much space it takes
		 * @param call the factory of the column cells
		 */
		public ColumnHolder(String text,double weight, Callback<TableColumn<T,T>,TableCell<T,T>> call){
			this.callback = call;
			this.topText = text;
			this.weight = weight;
		}
		/**
		 * 
		 * @return text on top
		 */
		public String getTopText() {
			return topText;
		}
		/**
		 * 
		 * @return weight display of this object
		 */
		public double getWeight(){
			return weight;
		}
		/**
		 * 
		 * @param topText displayed on the top
		 */
		public void setTopText(String topText) {
			this.topText = topText;
		}
		/**
		 * 
		 * @return the factory of the table cells
		 */
		public Callback<TableColumn<T, T>, TableCell<T, T>> getCallback() {
			return callback;
		}
		/**
		 * 
		 * @param callback of this table cells factory
		 */
		public void setCallback(Callback<TableColumn<T, T>, TableCell<T, T>> callback) {
			this.callback = callback;
		}
	}
	public class ButtonHolder{
		private String text;
		private OnClick<T> click;
		/**
		 * 
		 * @param text this button displays
		 * @param click this button does when clicked
		 */
		public ButtonHolder(String text, OnClick<T> click){
			this.text = text;
			this.click = click;
		}
		/**
		 * 
		 * @return the text of this button
		 */
		public String getText() {
			return text;
		}
		/**
		 * 
		 * @param text to be displayed
		 */
		public void setText(String text) {
			this.text = text;
		}
		/**
		 * 
		 * @return action done when this button is clicked
		 */
		public OnClick<T> getClick() {
			return click;
		}
		/**
		 * 
		 * @param clickthat happens when clicked
		 */
		public void setClick(OnClick<T> click) {
			this.click = click;
		}
	}
}
