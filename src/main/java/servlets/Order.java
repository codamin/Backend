package servlets;

import repository.IeatRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order/*")
public class Order extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getRequestURI().substring(request.getContextPath().length());
        String[] urlList = url.split("/");
        repository.Order order;
        if(urlList.length < 3 | (order = IeatRepository.getInstance().getOrderRepository().findOrderById(Integer.parseInt(urlList[2]))) == null)
            response.sendError(850, "no such order found");
        else {
            request.setAttribute("order", order);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/order.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
