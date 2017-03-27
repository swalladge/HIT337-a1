/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DerbyBackend;
import utils.snippets;

/**
 * This servlet handles user login
 * @author Samuel Walladge
 */
public class login extends HttpServlet {

    /**
     * set some things up here - whatever should happen only once
     * @param config
     * @throws ServletException 
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // once off create tables in the database
        // calling here since login page has to load at least once before any user can get in and manage books
        DerbyBackend db = new DerbyBackend();
        try {
            db.createTables();
        } catch (Exception ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * display the login page with form
     * if user is already logged in, will redirect to the homepage
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
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
        snippets.writeHead(out);
        out.println("<p>Please login to continue.</p>");

        // print the error message if there is one
        String error = (String) session.getAttribute("errorMsg");
        if (error != null) {
            out.printf("<p><b>%s</b></p>", error);
            session.removeAttribute("errorMsg");
        }

        // write the form and end
        this.writeForm(out);
        snippets.writeEnd(out);
    }

    /**
     * handles actually logging in - called when the user submits the login form.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = this.getServletContext();
        String name = request.getParameter("username");
        HttpSession session = request.getSession();

        // validate the username
        if (name == null || name.trim().length() < 1) {
            // set session error message and redirect to home
            session.setAttribute("errorMsg", "Username is required!");
            response.sendRedirect(context.getContextPath());
            return;
        }

        // all good, let's login
        String username = name.trim();
        session.setAttribute("username", username);

        // check for admin and set attribute
        session.setAttribute("isadmin", username.equals(context.getInitParameter("adminUsername")));

        // redirect to home
        response.sendRedirect(context.getContextPath());
    }

    // helper method to printout the login form
    private void writeForm(PrintWriter out) {
        out.println("<form method=\"POST\">");
        out.println("<input type=\"text\" name=\"username\" placeholder=\"username\" required>");
        out.println("<input type=\"submit\" value=\"Login\" >");
        out.println("</form>");
    }
}
