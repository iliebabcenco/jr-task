<%--
  Created by IntelliJ IDEA.
  User: AveZomer
  Date: 5/2/2021
  Time: 1:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <% String message = request.getAttribute("error").toString(); %>
    <h3><%= message %>></h3>
</body>
</html>
