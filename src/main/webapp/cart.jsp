<%@ page import="repository.OrderItem" %>
<%@ page import="repository.IeatRepository" %><%--
  Created by IntelliJ IDEA.
  User: ho
  Date: 2/20/20
  Time: 12:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li, div, form {
            padding: 5px
        }
    </style>
</head>
<body>
<div>Restaurant = <%=IeatRepository.getInstance().getCart().getRestaurantName()%></div>
<ul>
    <%for(OrderItem item: IeatRepository.getInstance().getCart().getOrderItems()) { %>
        <li> <%=item.getFood().getName()%>: <%=item.getNumber()%> </li>
    <%}%>
</ul>
<form action="/finalize" method="POST">
    <button type="submit">finalize</button>
</form>
</body>
</html>