<%@ page import="repository.Restaurant" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="repository.Food" %>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

<%--
  Created by IntelliJ IDEA.
  User: ho
  Date: 2/20/20
  Time: 4:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% Restaurant restaurant = (Restaurant) request.getAttribute("restaurant"); %>;

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant</title>
    <style>
        img {
            width: 50px;
            height: 50px;
        }
        li {
            display: flex;
            flex-direction: row;
            padding: 0 0 5px;
        }
        div, form {
            padding: 0 5px
        }
    </style>
</head>
<body>
<ul>
    <li>id: <%=restaurant.getId()%>/res</li>
    <li>name: <%=restaurant.getName()%></li>
    <li>location: <%=restaurant.getLocation().StringFormat()%></li>
    <li>logo: <img src="<%=restaurant.getLogo()%>" alt="logo"></li>

    <!-- IN CASE YOU WANT SOME BONUS : -->
    <!-- <li>estimated delivery time: 10 min 2 sec </li> -->
    <% ArrayList<Food> menu = (ArrayList<Food>) restaurant.getMenu(); %>
    <li>menu:
        <ul>
            <% for(Food food: menu) { %>
            <li>
                <img src="<%=food.getImage()%>" alt="logo">
                <div><%=food.getName()%></div>
                <div><%=food.getPrice()%></div>
                <form action="/addToCart" method="POST">
                    <input type="hidden" name="restaurantId" value="<%=food.getRestaurantId()%>" />
                    <input type="hidden" name="foodName" value="<%=food.getName()%>" />
                    <button type="submit">addToCart</button>
                </form>
            </li>
            <% } %>
        </ul>
    </li>
</ul>
</body>
</html>