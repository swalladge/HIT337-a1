/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * handles logging out the user
 * @author Samuel Walladge
 */
public class logout extends HttpServlet {

    /**
     * logs the user out - invalidates the session and redirects to the login page
     * this is performed only for a POST request, due to this changing session state
     * also this avoids accidentally being logged out by a browser pre-fetching page links
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // invalidate the session
        HttpSession session = request.getSession();
        session.invalidate();

        response.sendRedirect("login");
    }
}
