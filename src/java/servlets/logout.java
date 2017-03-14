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
public class logout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // remove the session value and redirect to the login page
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        response.sendRedirect("login");
    }
}
