<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>    
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Upload word file</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=MML_CHTML"></script>	
</head>
<body>
	<div class="container">
		<div class="row m-5">
			<div class="col-12">
				<h1>Uploaded File Text Here</h1>
<%-- 				<p>User Email: ${email}</p>
				<p>User Mobile: ${mobile}</p>
 --%>			
		<%  ArrayList<ArrayList<ArrayList<String>>> tables = (ArrayList<ArrayList<ArrayList<String>>>)request.getAttribute("tables");
		for(ArrayList<ArrayList<String>> tableRow:tables) { %>
			<table class="table table-bordered">
		<%	for(ArrayList<String> columns:tableRow) { %>
		<tr>
	    <%
	    		int colSize = columns.size();
	            int colCnt = 0;
				for(String data:columns) { 
				if(colCnt == 0) {
					%><td><%=data %></td><%
				} else if(colSize==2 && colCnt != 0) {
					%><td colspan="2"><%=data %></td><%
				} else {
					%><td><%=data %></td><%
				}
				colCnt++;	
				}
	    %>
		</tr>
	    <%
			}
		%>
			</table>
			<hr />
		<%
		}
		%>
		</div>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>