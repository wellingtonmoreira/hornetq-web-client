<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
   <meta charset="UTF-8">
</head>
<body style="text-align:center;">
   <form method="GET" action="clearQueueServlet">
        <h3>Client web HornetQ</h3></br>
        <a href="index.jsp">Publish</a>&nbsp;&nbsp;
        <a href="consumir.jsp">Receive</a>&nbsp;&nbsp;
        <a href="stress.jsp">Stress-Test</a>&nbsp;&nbsp;
        <a href="limpar.jsp">Clear-Queue</a>&nbsp;&nbsp;
        <h4>Clear Queue</h4>
        <span>URL HornetQ: </span><input type="text" name="url"/></br>
        <span>Queue:</span><input type="text" name="queue"></br>
        <span style="color:green"><%= request.getAttribute("successMsg") == null ? "" : request.getAttribute("successMsg") %></span>
        <span style="color:red"><%= request.getAttribute("errorMsg") == null ? "" : request.getAttribute("errorMsg") %></span></br>
        <input type="submit" value="Submit"></input>
   </form>
</body>
</html>