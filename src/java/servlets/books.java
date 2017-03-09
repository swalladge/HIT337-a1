/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Samuel Walladge
 */
public class books extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("login");
            return;
        }
        ServletContext context = this.getServletContext();
        String base = context.getContextPath();

        // get admin username from context init param
        String adminUsername = context.getInitParameter("admin");

        // TODO: show different header if admin user

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out);
        out.println("<a href=\"" + base + "/logout\">logout</a>");
        out.println("<p>List of books here</p>");
        out.println("</body></html>");
    }

    private void writeHeader(PrintWriter out) {
        out.println("<h1>My Book List</h1>");
    }
        
}
