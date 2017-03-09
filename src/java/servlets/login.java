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
public class login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // check if already logged in - if so, redirect to home
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username != null) {
            // redirect
            ServletContext context = this.getServletContext();
            String contextPath = context.getContextPath();
            response.sendRedirect(contextPath);
            return;
        }

        // not logged in, so show the form
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out);
        this.writeForm(out);
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("username");

        if (name == null || name.trim().length() < 1) {
            PrintWriter out = response.getWriter();
            out.println("<html><head>");
            out.println("</head><body>");
            out.println("<p><b>Username is required!</b></p>");
            this.writeForm(out);
            out.println("</body></html>");
        }

        HttpSession session = request.getSession();
        session.setAttribute("username", name.trim());
        response.sendRedirect("./");
    }

    private void writeForm(PrintWriter out) {
        out.println("<form method=\"POST\" action=\"\">");
        out.println("<input type=\"text\" name=\"username\" placeholder=\"username\" required>");
        out.println("<input type=\"submit\" value=\"Login\" >");
        out.println("</form>");
    }

    private void writeHeader(PrintWriter out) {
        out.println("<h1>Welcome to the Books Catalogue!</h1>");
        out.println("<p>Please login to continue.</p>");
    }
        
}
