<%--
  Created by IntelliJ IDEA.
  User: REDTECH
  Date: 18/9/2024
  Time: 3:14 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p>${sessionScope.get("user").getPassword()}</p>
</body>
</html>
