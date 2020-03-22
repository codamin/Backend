<%@ page import="repository.PartyFood" %>
<%@ page import="repository.IeatRepository" %><%--
  Created by IntelliJ IDEA.
  User: ho
  Date: 2/20/20
  Time: 8:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Food Party</title>
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
        .old-price {
            text-decoration: line-through;
        }
    </style>
</head>
<body>
<ul>
    <% for(PartyFood food: IeatRepository.getInstance().getParty()) { %>
    <li>menu:
        <ul>
            <li>
                <img src="<%= food.getImage() %>" alt="logo" />
                <div><%=food.getRestaurantName()%></div>
                <div><%=food.getName()%></div>
                <div><%=food.getDescription()%></div>
                <div class="old-price"><%=food.getOldPrice()%></div>
                <div><%=food.getPrice()%> Toman</div>
                <div>remaining count: <%=food.getCount()%></div>
                <div>popularity: <%=food.getPopularity()%></div>
                <form action="/addToCart" method="POST">
                    <input type="hidden" name="restaurantId" value="<%=food.getRestaurantId()%>" />
                    <input type="hidden" name="foodName" value="<%=food.getName()%>" />
                    <button type="submit">addToCart</button>
                </form>
            </li>
        </ul>
    </li>
    <% } %>
</ul>
</body>
</html>