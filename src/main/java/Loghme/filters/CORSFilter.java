package Loghme.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CORSFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        try {
            Thread.sleep(850);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals("OPTIONS")) {
            ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "true");
            ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

            resp = (HttpServletResponse) servletResponse;
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);

            return;
        }

        chain.doFilter(request, servletResponse);
    }
}