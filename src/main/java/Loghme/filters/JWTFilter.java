package Loghme.filters;

import Loghme.Utilities.JwtUtils;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class JWTFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        String method = request.getMethod();

        if((url.equals("/user") && method.equals("POST")) || url.equals("/auth/login") || url.equals("/auth/tokenIDLogin"))
            chain.doFilter(request, response);
        else {
            String token = request.getHeader("Authorization");
            if(token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("You have not authorized yet!");
            }
            else {
                String username = JwtUtils.verifyJWT(token);
                if(username == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().println("The JWT token is invalidated!");
                }
                else {
                    try {
                        User user = UserMapper.getInstance().find(username);
                        request.setAttribute("user", user.getEmail());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
