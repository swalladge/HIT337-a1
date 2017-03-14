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
        Boolean admin = false;
        if (adminUsername == username) {
            admin = true;
        }

        // if no book id, then we're adding a book
        // otherwise, show edit and delete forms
        String bookId = request.getParameter("id");
        boolean edit = true;
        if (bookId == null) {
            edit = false;
        }

        Logger.getLogger(book.class.getName()).log(Level.INFO, String.format("requested book id: %s", bookId));

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out, edit);

        // links
        out.println("<a href=\"" + base + "/\">book list</a>");
        snippets.writeLogoutButton(out, base);

        if (admin && edit) {
            out.println("<p>Editing PLACEHOLDER'S book</p>");
        }

        // TODO: customize based on number of books already have
        out.printf("<p>Max number of books allowed: %s</p>\n", this.getMaxRecords());

        String error = (String) session.getAttribute("error");
        if (error != null) {
            out.println("<p>Error: " + error + "</p>");
            session.removeAttribute("error");
        }

        if (edit) {
            out.println("<p>Editing book</p>");
        } else {
            out.println("<p>Adding book</p>");
        }

        this.writeEditForm(out, edit);

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        ServletContext context = this.getServletContext();
        String base = context.getContextPath();

        // get admin username from context init param
        String adminUsername = context.getInitParameter("admin");
        Boolean admin = false;
        if (adminUsername == username) {
            admin = true;
        }

        // if no book id, then we're adding a book
        // otherwise, show edit and delete forms
        String bookId = request.getParameter("id");
        boolean edit = true;
        if (bookId == null) {
            edit = false;
        }

        boolean success = true;
        String errorMessage = "FAIL";
        // TODO: add/update in database
        // TODO: use result below

        if (success) {
            response.sendRedirect(base);
        } else {
            session.setAttribute("error", errorMessage);
            response.sendRedirect(base + "/book?id=" + bookId);
        }

    }

    private void writeHeader(PrintWriter out, boolean edit) {
        // TODO: tailor header based on action
        if (edit) {
            out.println("<h1>Edit Book</h1>");
        } else {
            out.println("<h1>Add Book</h1>");
        }
    }
        

    private int getMaxRecords() {
        ServletConfig config = this.getServletConfig();
        int maxRecords = Integer.parseInt(config.getInitParameter("maxRecords"));
        return maxRecords;
    }

    private void writeEditForm(PrintWriter out, boolean edit) {
        String editText;
        if (edit) {
            editText = "Update";
        } else {
            editText = "Create";
        }

        out.println("<form action=\"\" method=\"POST\">");
        out.println("<input type=\"submit\" value=\"" + editText + "\">");
        out.println("</form>");
    }
}
