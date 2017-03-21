/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Samuel Walladge
 */
public class authFilter implements Filter {

    FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ServletContext context = filterConfig.getServletContext();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String username = (String) session.getAttribute("username");

        // redirect to login if not logged in
        if (username == null) {
            ((HttpServletResponse) response).sendRedirect(context.getContextPath() + "/login");
            return;
        }

        // make sure isadmin attribute set correctly
        session.setAttribute("isadmin", username.equals(context.getInitParameter("adminUsername")));

        chain.doFilter(request, response);

    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
