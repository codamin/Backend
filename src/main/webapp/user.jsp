<%@page import="repository.IeatRepository"%>
<%@page import="repository.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="repository.OrderItem" %>
<%@ page import="repository.Order" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
    </style>
</head>

<body>

<ul>
    <% User user = IeatRepository.getInstance().getUser();%>
    <li>full name: <%=user.getFirstName() + " " + user.getLastName()%> </li>
    <li>phone number: <%=user.getPhone()%></li>
    <li>email: <%=user.getEmail()%>></li>
    <li>credit: <%=user.getCredit() + " "%> Tomans</li>

    <form action="/charge" method="POST">
        <input type="text" name="amount" />
        <button type="submit">increase</button>
    </form>

    <li>
        Orders :
        <% if(IeatRepository.getInstance().getOrders() != null) { %>
            <% for(Order order: IeatRepository.getInstance().getOrders()) { %>
                <ul>
                    <li><a href="<%="/order/" + order.getId()%>"> order id : <%=order.getId()%></a></li>
                </ul>
            <% } %>
        <% } %>
    </li>
</ul>
</body>

</html>