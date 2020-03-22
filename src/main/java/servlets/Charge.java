package servlets;

import repository.IeatRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/charge")
public class Charge extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String amountText = request.getParameter("amount");
        if(amountText == null)
            response.sendError(505, "empty input");

        int amount = Integer.parseInt(amountText);
        if(amount < 0)
            response.sendError(805, "amount is invalid");

        IeatRepository.getInstance().getUser().chargeCredit(amount);
        response.sendRedirect("/user");
    }
}
