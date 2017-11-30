package com.cdrap.GoogleAccessor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGet;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchUpdate;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Borders;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.CopyPasteRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DeleteRangeRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.InsertRangeRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
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
public class SheetsAccessor {
	
	private Sheets handler;
	public SheetsAccessor() throws IOException, GeneralSecurityException{
		handler = DriveEssentials.getSheetsService();
	}
	public ArrayList<List<List<String>>> compileQueryCells(String id, List<LocationRequest> requests) throws IOException{
		ArrayList<String> items = new ArrayList<>(requests.size());
		for(LocationRequest request:requests){
			items.add(Utils.convertToPosition(request));
		}
		return queryCells(id,items);
	}
	public ArrayList<List<List<String>>> queryCells(String id, List<String> PositionRequest) throws IOException{
		//****************** use batch to get items ******************
		BatchGet get = handler.spreadsheets().values().batchGet(id)
			.setRanges(PositionRequest);
		BatchGetValuesResponse ret = get.execute();
		//***********************returned items ***************
		ArrayList<List<List<String>>> returned = new ArrayList<>(PositionRequest.size());
		
		//*****************convert them into useable format and collect data, it is underline string but for some reason doesn't like to be. .toString() converts string.... back into string*********************
		for(ValueRange parse0 :ret.getValueRanges()){
			if(parse0 != null&& parse0.getValues()!=null){
				List<List<String>> external = new ArrayList<>();
					for(List<Object> parse1: parse0.getValues()){
						if(parse1 !=null){
						//internal storage
						List<String> internal = new ArrayList<>(parse1.size());
						for(Object parse2:parse1){
							//will make the string...... become a string
							internal.add(parse2.toString());
						}
						//add to outerarray
						external.add(internal);
					}//add this returned group to list
					
				}
				returned.add(external);
			}
			else{
				returned.add(Collections.emptyList());
			}
			
		}
		return returned;
	}
	//			
	
	public void compileInsertIntoTable(String id,List<LocationRequest> ranges, List<List<List<Object>>> content) throws IOException{
		//********* convert Location Request to String of proper ids *********
		ArrayList<String> retRanges= new ArrayList<>(ranges.size());
		for(LocationRequest request:ranges){
			retRanges.add(Utils.convertToPosition(request));
		}
		//********* Call master method ***********
		insertIntoTable(id,retRanges,content);
	}
	
	public void insertIntoTable(String id, List<String> ranges, List<List<List<Object>>> values) throws IOException{
		ArrayList<ValueRange> toInsert = new ArrayList<>();
		if(ranges.size() != values.size()){
			throw new RuntimeException("Invalid batch!, incorrect number of value lists with ranges");
		}
		for(int i = 0; i < ranges.size();i++){
			toInsert.add(new ValueRange().setRange(ranges.get(i)).setValues(values.get(i)));
		}
		BatchUpdateValuesRequest batch = new BatchUpdateValuesRequest().setData(toInsert).setValueInputOption("USER_ENTERED");
		BatchUpdate updater = this.handler.spreadsheets().values().batchUpdate(id, batch);
		System.out.println(updater.execute());
	}
	private ArrayList<List<RowData>> compileDataToRowData(List<List<List<String>>> dataSets,boolean includeBorder){
		ArrayList<List<RowData>> returns = new ArrayList<>();
		//Sets of data being added
		for(List<List<String>> stringsOfStrings: dataSets){
			List<RowData> rowData = new ArrayList<>();
			//Rows of those sets
			for(List<String> strings:stringsOfStrings){
				List<CellData> cellData = new ArrayList<>();
				//each cell of those sets
				for(String str:strings){
					CellData data = new CellData()
							.setUserEnteredValue(new ExtendedValue().setStringValue(str));
							if(includeBorder){
								data.setUserEnteredFormat(new CellFormat()
									.setBorders(new Borders()
											.setBottom(
													new Border()
														.setStyle(includeBorder ? "solid": "none")
														.setColor(new Color()
																.setAlpha(1f)
																.setRed(0f)
																.setBlue(0f)
																.setGreen(0f)
														
													)
											)
											.setLeft(new Border()
													.setStyle(includeBorder ? "solid": "none")
													.setColor(new Color()
															.setAlpha(1f)
															.setRed(0f)
															.setBlue(0f)
															.setGreen(0f)
												)
											)
											.setTop(new Border()
													.setStyle(includeBorder ? "solid": "none")
													.setColor(new Color()
															.setAlpha(1f)
															.setRed(0f)
															.setBlue(0f)
															.setGreen(0f)
												)
											)
											.setRight(new Border()
													.setStyle(includeBorder ? "solid": "none")
													.setColor(new Color()
															.setAlpha(1f)
															.setRed(0f)
															.setBlue(0f)
															.setGreen(0f)
												)
											)
									)
							);
						}
					cellData.add(data);
				}
				//add row just completed to column
				rowData.add(new RowData()
						.setValues(cellData));
			}
			//add columns just completed to returns
			returns.add(rowData);
		}
		
		return returns;
	}
	private ArrayList<GridRange> compileToGridRanges(RequestCompilePackage pack,List<LocationRequest> req) throws IOException{
		
		//found items, used in stage 3
		HashMap<LocationRequest,GridRange> grids = new HashMap<>();
		
		//*********** stage 1, precompile ***********************
		//to set to reduce amount of lookups
		Set<String> sheetsToFind = new HashSet<>();
		ArrayList<LocationRequest> remainders = new ArrayList<>();
		//grid ranges
		for(LocationRequest r: req){
			Integer found = pack.sheetids.get(r.getSheet());
			if(found != null){
				//assing grid if already found from previous iteration
				grids.put(r,new GridRange()
					.setSheetId(found)
					.setStartRowIndex(r.getY1())
					.setStartColumnIndex(r.getX1())
					.setEndColumnIndex(r.getX2())
					.setEndRowIndex(r.getY2()));
			}
			else{
				//prepare to find the sheet
				sheetsToFind.add(r.getSheet());
				remainders.add(r);
			}
		}
		//********* stage 2, look up ids*************
		//string to id connections
		if(!sheetsToFind.isEmpty()){//if nothing to find, skip this operation
			HashMap<String,Integer> foundPages = new HashMap<>();
			Spreadsheet foundSSID = handler.spreadsheets().get(pack.fileid).setRanges(new ArrayList<>(sheetsToFind)).execute();
			for(Sheet sheet:foundSSID.getSheets()){
				foundPages.put(sheet.getProperties().getTitle(), sheet.getProperties().getSheetId());
			}
			
			//*********** assing ids ***************
			for(LocationRequest r:remainders){
				Integer found = foundPages.get(r.getSheet());
				if(found != null){
					grids.put(r,new GridRange()
							.setSheetId(found)
							.setStartRowIndex(r.getY1())
							.setStartColumnIndex(r.getX1())
							.setEndColumnIndex(r.getX2())
							.setEndRowIndex(r.getY2()));
				}
				else{
					throw new IOException("Page:"+r.getSheet()+" was not found");
				}
			}
		}
		//***************stage 3, reassign items order via insertion order**************
		//reemsure sort integerity
		ArrayList<GridRange> ranges = new ArrayList<>();
		for(LocationRequest r:req){
			//should never hit a case of 'not found'
			ranges.add(grids.get(r));
		}
		
		return ranges;
	}
	/*
	 * 
	 * 
	 *  REQUEST being made area
	 * 
	 * 
	 * 
	 */
	public RequestCompilePackage startRequests(String id){
		return new RequestCompilePackage(id);
	}
	
	public void buildUpdateRowsRequests(RequestCompilePackage pack, List<LocationRequest> requests,List<List<List<String>>> datasets,boolean includeBorder) throws IOException{
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, requests);
		ArrayList<List<RowData>> data = compileDataToRowData(datasets,includeBorder);
		if(requests.size()!= datasets.size()){
			throw new IndexOutOfBoundsException("Data and Location Requests sizes do not match");
		}
		
		for(int i =0; i < ranges.size();i++){
			ranges.get(i).setEndColumnIndex(ranges.get(i).getEndColumnIndex()+1);
			ranges.get(i).setEndRowIndex(ranges.get(i).getEndRowIndex()+1);
			pack.requests.add(new Request()
						.setUpdateCells(new UpdateCellsRequest()
							.setRange(ranges.get(i))
							.setRows(data.get(i))
							.setFields("*")
						)
			);
		}
	}
	/*
	 * remove because not used, but kept as a reference because why not
	 *
	public void buildAppendRowsRequestsAtEndRequests(RequestCompilePackage pack, List<LocationRequest> requests,List<List<List<String>>> datasets) throws IOException{
		
		/**** sort in order so insertions dont get shifted, but everything else will ******//*
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, requests);
		
		ArrayList<List<RowData>> data = compileDataToRowData(datasets,false);
		//indexer for getting ranges
		int i = -1;
		for(GridRange range:ranges){
			i++;
			pack.requests.add(new Request().setAppendCells(
					new AppendCellsRequest()
						.setFields("*")
						.setRows(data.get(i))
						.setSheetId(range.getSheetId())));
		}
	}*/
	public void buildShiftRowsRequest(RequestCompilePackage pack, List<LocationRequest> requests) throws IOException{
		
		/**** sort in order so insertions dont get shifted, but everything else will ******/
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, requests);
		
		//indexer for getting ranges
		//int i = -1;
		for(GridRange range:ranges){
			//i++;
			pack.requests.add(new Request().setInsertRange(new InsertRangeRequest()
													.setRange(range)
													.setShiftDimension("ROWS")
													)
												);
		}
	}
	public void buildCopyRequests(RequestCompilePackage pack,LocationRequest original, List<LocationRequest> requests) throws IOException{
		/**** sort in order so insertions dont get shifted, but everyt */

		//indexer for getting ranges
		//int i = -1;
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, requests);
		GridRange src  = this.compileToGridRanges(pack, Arrays.asList(original)).get(0);
		for(GridRange range:ranges){
			//i++;
			pack.requests.add(new Request().setCopyPaste(new CopyPasteRequest()
								.setDestination(range).setPasteType("PASTE_NORMAL")
								.setSource(src)
						));
		}
	}
	public void buildDeleteRowsRequest(RequestCompilePackage pack, List<LocationRequest> requests) throws IOException{
		
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, new ArrayList<>(requests));
		
		for(GridRange range:ranges){
			//i++;
			pack.requests.add(new Request().setDeleteRange(new DeleteRangeRequest()
													.setRange(range)
													.setShiftDimension("ROWS")));
		}
	}
	public void buildDeleteRowsDimensionRequest(RequestCompilePackage pack, List<LocationRequest> requests) throws IOException{
		
		ArrayList<GridRange> ranges = this.compileToGridRanges(pack, requests);
		
		//int i = -1;
		for(GridRange range:ranges){
			//i++;
			pack.requests.add(new Request()
					.setDeleteDimension(new DeleteDimensionRequest()
							.setRange(new DimensionRange()
									.setSheetId(range.getSheetId())
									.setDimension("ROWS")
									.setStartIndex(range.getStartRowIndex())
									.setEndIndex(range.getEndRowIndex()))));
		}
		
		
		
	}
	public void buildInsertDimensionRows(RequestCompilePackage pack, List<LocationRequest> ranges) throws IOException{
		
		/**** sort in order so insertions dont get shifted, but everything else will ******/
		//indexer for getting ranges
		//int i = -1;
		for(GridRange range:this.compileToGridRanges(pack,ranges)){
			//i++;
			pack.requests.add(new Request()
					.setInsertDimension(new InsertDimensionRequest()
							.setRange(new DimensionRange()
								.setSheetId(range.getSheetId())
								.setDimension("ROWS")
								.setStartIndex(range.getStartRowIndex())
								.setEndIndex(range.getEndRowIndex()))));
		}
	}
	public void executeRequests(RequestCompilePackage pack) throws IOException{
		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(pack.requests);
		if(pack.requests.isEmpty()){
			throw new RuntimeException("No Requests have been made");
		}
		handler.spreadsheets()
		        .batchUpdate(pack.fileid, body)
		        .execute();
	}
	
	public final class RequestCompilePackage{
		private final String fileid;
		private final HashMap<String, Integer> sheetids = new HashMap<>();
		private final LinkedList<Request> requests = new LinkedList<>();
		//*********** Effective object for handling ids, requests, and sheet ids of spreadsheet**********************
		private RequestCompilePackage(String id){
			this.fileid = id;
		}
	}
}
