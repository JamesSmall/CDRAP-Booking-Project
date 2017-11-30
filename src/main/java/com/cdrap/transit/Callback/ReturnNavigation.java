package com.cdrap.transit.Callback;

import java.io.IOException;

import com.cdrap.ErrorManager.ErrorLevel;
import com.cdrap.ErrorManager.ErrorLog;
import com.cdrap.UI.FXHandler;
import com.cdrap.transit.UI.CustomGridWindowBundle;
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
public class ReturnNavigation<T> implements Navigation{
	private CustomGridWindowBundle<T> bundle;
	/**
	 * 
	 * @param b bundle to returnto
	 */
	public ReturnNavigation(CustomGridWindowBundle<T> b){
		this.bundle = b;
	}
	/**
	 * approved thing to return to
	 */
	@Override
	public void approve() {
		try {
			FXHandler.handle.switchStage(FXHandler.FX_PAGE_CUSTOM_SHEET_WINDOW, bundle);
		} catch (IOException e) {
			ErrorLog.writeError(e, ErrorLevel.UNKNOWN_ERROR);
		}
	}
	/**
	 * returns dispalytext as RETURN
	 */
	@Override
	public String getDisplayText() {
		return "Return";
	}

}
