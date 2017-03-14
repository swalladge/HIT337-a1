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
import utils.snippets;

/**
 *
 * @author Samuel Walladge
 */
public class book extends HttpServlet {

    // TODO: get max number of books from servlet init param (used in doGet and doPost)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // protected by filter - guaranteed
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        ServletContext context = this.getServletContext();
        String base = context.getContextPath();

        // get admin username from context init param
        String adminUsername = context.getInitParameter("admin");

        // if no book id, then we're adding a book
        // otherwise, show edit and delete forms
        String bookId = request.getParameter("id");

        Logger.getLogger(book.class.getName()).log(Level.INFO, String.format("requested book id: %s", bookId));

        // TODO: show different header if admin user
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out);
        snippets.writeLogoutButton(out, base);
        out.println("<a href=\"" + base + "/\">book list</a>");
        out.printf("<p>Max number of books allowed: %s</p>\n", this.getMaxRecords());

        if (bookId == null) {
            out.println("<p>Add book</p>");
        } else {
            out.println("<p>Edit book</p>");
        }

        out.println("</body></html>");
    }

    // TODO: doPost method to handle actually editing

    private void writeHeader(PrintWriter out) {
        // TODO: tailor header based on action
        out.println("<h1>Edit/Add entry</h1>");
    }
        

    private int getMaxRecords() {
        ServletConfig config = this.getServletConfig();
        int maxRecords = Integer.parseInt(config.getInitParameter("maxRecords"));
        return maxRecords;
    }
}
