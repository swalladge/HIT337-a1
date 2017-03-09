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
public class edit extends HttpServlet {

    // TODO: get max number of books from servlet init param (used in doGet and doPost)

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

        // TODO: get id from query params

        // TODO: show different header if admin user
        // get admin username from context init param
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out);
        out.println("<a href=\"" + base + "/logout\">logout</a>");
        out.println("<a href=\"" + base + "/\">book list</a>");
        out.println("<p>Book to edit here.</p>");
        out.println("</body></html>");
    }

    // TODO: doPost method to handle actually editing

    private void writeHeader(PrintWriter out) {
        // TODO: tailor header based on action
        out.println("<h1>Edit/Add entry</h1>");
    }
        
}
