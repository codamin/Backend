package servlets;

import repository.IeatRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/finalize")
public class Finalize extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        boolean result = IeatRepository.getInstance().finalizeCart();
        if(!result) {
            try {
                response.sendError(508, "charge your account");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                response.sendRedirect("/restaurants");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}