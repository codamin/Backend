package servlets;

import repository.IeatRepository;
import repository.Restaurant;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/restaurants/*")
public class Restaurants extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI().substring(request.getContextPath().length());

        if(url.equals("/restaurants")) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/restaurants.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            String[] urlList = url.split("/");
            Restaurant restaurant = null;
            if(urlList.length < 3 | (restaurant = IeatRepository.getInstance().findRestaurantById(urlList[2])) == null) {
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("");
                response.sendError(404, "invalid url");
            }
            else {
                request.setAttribute("restaurant", restaurant);
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/restaurant.jsp");
                requestDispatcher.forward(request, response);
            }
        }
    }
}