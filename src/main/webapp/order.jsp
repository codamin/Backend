<%@ page import="repository.Order" %>
<%@ page import="repository.OrderItem" %><%--
  Created by IntelliJ IDEA.
  User: ho
  Date: 2/20/20
  Time: 2:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% repository.Order order = (Order) request.getAttribute("order"); %>

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
<div> <%=order.getRestaurantName()%> </div>
    <ul>
        <%for(OrderItem item: order.getOrderItems()) { %>
            <li> <%=item.getFood().getName()%> : <%=item.getNumber()%> </li>
        <% } %>
    </ul>

    <div>
        <div>status : <%=order.getState()%></div>
        <%if(order.getState() == "delivering") {%>
            <div>remained time : <%=order.getRemMin()%> Min <%=order.getRemSec()%> Sec </div>
        <% } %>
    </div>
</div>
</body>
</html>