<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<%
		Enumeration headers = request.getHeaderNames();
		if(headers != null){
			while(headers.hasMoreElements()){
				Object header = headers.nextElement();
				out.print(String.valueOf(header) + ": " + request.getHeader(String.valueOf(header)));
				out.print("<br>");
				String key = "X-SSL-PEER-SUBJECT";
				out.print(key + ": " + request.getHeader(key));
				out.print("<br>");
			}
		}
	
	%>

</body>
</html>