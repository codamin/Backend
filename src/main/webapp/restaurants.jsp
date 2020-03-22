<%--
  Created by IntelliJ IDEA.
  User: ho
  Date: 2/20/20
  Time: 12:25 AM
  To change this template use File | Settings | File Templates.
--%>

<%@page import="repository.IeatRepository"%>
<%@ page import="repository.Restaurant" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="repository.Location" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Restaurant> restaurants = IeatRepository.getInstance().findNearRestaurants(170);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurants</title>
    <style>
        table {
            text-align: center;
            margin: auto;
        }
        th, td {
            padding: 5px;
            text-align: center;
        }
        .logo{
            width: 100px;
            height: 100px;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>id</th>
        <th>logo</th>
        <th>name</th>
        <th>location</th>
        <th>estimated_delivery_time</th>
    </tr>
    <% int deliverySpeedAvg = 5;%>
    <% for(Restaurant restaurant: restaurants) {%>
    <tr>
        <td><a href="<%="http://localhost:8080/restaurants/" + restaurant.getId() %>"> <%=restaurant.getId()%> </a></td>
        <td><img class="logo" src="<%=restaurant.getLogo()%>" alt="logo" /></td>
        <td> <%=restaurant.getName()%> </td>
        <td> <%=restaurant.getLocation().StringFormat()%> </td>
        <% int timeSec = (int) Math.floor(60 + restaurant.getLocation().getDistance(new Location(0, 0)) * 3 / 2 / deliverySpeedAvg);%>
        <% int minutes = (int)(timeSec/60);%>
        <% int seconds = (int)(timeSec%60);%>
        <td><%=minutes%> minutes <%=seconds%> seconds</td>
    </tr>
    <% } %>
</table>
</body>
</html>


<jsp:include page="footer.jsp" />