<%@page import="org.json.JSONArray,org.json.JSONObject,java.util.ArrayList"%>
<%@ page language="java" contentType="application/json; 
charset=ISO-8859-1" pageEncoding="ISO-8859-1"%><%
	ArrayList<ArrayList<ArrayList<String>>> tables = (ArrayList<ArrayList<ArrayList<String>>>) request.getAttribute("tables");
	JSONObject tablesJson = new JSONObject();
	JSONArray tableRowArray = new JSONArray();
	JSONObject objJson = new JSONObject();
	JSONArray objArray = new JSONArray();
	int tableCnt = 0;
	for (ArrayList<ArrayList<String>> tableRow : tables) {
		tableRowArray = new JSONArray();
		tableCnt++;
		for (ArrayList<String> columns : tableRow) {
			int colSize = columns.size();
			int colCnt = 0;
			objJson = new JSONObject();
			objArray = new JSONArray();
			for (String data : columns) {
		if (colCnt == 0) {
			objJson.put("attribute_name", data);
		} else if (colSize == 2 && colCnt != 0) {
			objArray.put(data);
		} else {
			objArray.put(data);
		}
		colCnt++;
			}
			objJson.put("attribute_value", objArray);
			tableRowArray.put(objJson);
		}
		tablesJson.put("table_" + tableCnt, tableRowArray);
	}
%><%=tablesJson.toString()%>