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
 * ensures that the user is logged in before allowing access to protected pages
 * the servlets or urls that this protects are configured in web.xml
 * Note that the logout, books, and book servlets require this filter protecting them, otherwise they will attempt to use session variables which may not exist
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

        // check if logged in, and redirect to login if not
        if (username == null) {
            ((HttpServletResponse) response).sendRedirect(context.getContextPath() + "/login");
            return;
        }

        // pass on to the servlet (or next filter)
        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) {
        // we need this set to get the context
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        // nothing required
    }

}
