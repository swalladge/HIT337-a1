/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.snippets;

/**
 *
 * @author Samuel Walladge
 */
public class books extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // protected by filter; guaranteed to exist
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        ServletContext context = this.getServletContext();
        String base = context.getContextPath();

        // get admin username from context init param
        Boolean admin = false;
        if (username == context.getInitParameter("admin")) {
            admin = true;
        }

        // TODO: show different header if admin user

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");

        this.writeHeader(out);

        out.println("<a href=\"" + base + "/book\">Add Book</a>");
        snippets.writeLogoutButton(out, base);

        out.println("<p>Welcome " + username + "!</p>");
        if (admin) {
            out.println("<p>You are the admin user and can edit books of other users</p>");
        }

        // TODO: get from database
        ArrayList<Integer> bookList = new ArrayList<>();
        bookList.add(43);
        bookList.add(564);

        this.writeBooks(out, bookList);

        out.println("</body></html>");
    }

    private void writeHeader(PrintWriter out) {
        out.println("<h1>My Book List</h1>");
    }

    private void writeBooks(PrintWriter out, List<Integer> bookList) {
        out.println("<table>");
        out.println("<tr><th>Title</th><th>Author</th></tr>");
        for (Integer book : bookList) {
            out.println("<tr>");
            out.println("<td>" + book + "</td>");
            out.println("<td>" + book + "</td>");
            out.println("<td>" + book + "</td>");
            out.println("<td><a href=\"book?id=" + book +  "\">edit</a></td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }
        
}
