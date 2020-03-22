package servlets;

import repository.Food;
import repository.IeatRepository;
import repository.Restaurant;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/addToCart")
public class addToCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String item = request.getParameter("restaurantId");
        byte[] bytes = item.getBytes(StandardCharsets.ISO_8859_1);
        String restaurantId = new String(bytes, StandardCharsets.UTF_8);

        item = request.getParameter("foodName");
        bytes = item.getBytes(StandardCharsets.ISO_8859_1);
        String foodName = new String(bytes, StandardCharsets.UTF_8);
        boolean result = IeatRepository.getInstance().addToCart(restaurantId, foodName);
        if(!result) {
            response.sendError(404, "invalid restaurant and food");
        }
        else {
            response.sendRedirect("/restaurants/" + restaurantId);
        }

    }
}