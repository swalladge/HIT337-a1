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
import utils.Book;
import utils.DerbyBackend;
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
        String adminUsername = context.getInitParameter("adminUsername");
        Boolean admin = false;

        if (username.equals(adminUsername)) {
            admin = true;
        }

        // if no book id, then we're adding a book
        // otherwise, show edit and delete forms
        String bookId = (String) request.getParameter("id");
        boolean edit = true;
        if (bookId == null) {
            edit = false;
        }

        Logger.getLogger(book.class.getName()).log(Level.INFO, String.format("requested book id: %s", bookId));

        // print the header stuff
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("</head><body>");
        this.writeHeader(out, edit);

        // links
        out.println("<a href=\"" + base + "/\">book list</a>");
        snippets.writeLogoutButton(out, base);

        // different title depending on whether admin and/or editing or not
        if (admin) {
            out.println("<p>You are admin and can edit/add a book for any user!</p>");
        }

        if (edit) {
            out.println("<p>Editing book</p>");
        } else {
            out.println("<p>Adding book</p>");
        }


        // TODO: customize based on number of books already have
        out.printf("<p>Max number of books allowed: %s</p>\n", this.getMaxRecords());

        // display an error if there is one
        String error = (String) session.getAttribute("error");
        if (error != null) {
            out.println("<p>Error: " + error + "</p>");
            session.removeAttribute("error");
        }

        DerbyBackend db = new DerbyBackend("todo");
        Book book = null;
        if (edit) {
            try {
                book = db.getBook(bookId);
            } catch (Exception e) {
                // TODO: handle error
                this.writeErrorMessage(out);
                out.println("</body></html>");
                return;
            }
        } else {
            book = new Book(null, "", "", "", 0);
        }

        // write out the form
        this.writeEditForm(out, edit, admin, book);

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
        String adminUsername = context.getInitParameter("adminUsername");
        Boolean admin = false;
        if (username.equals(adminUsername)) {
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
        String errorMessage = null;

        DerbyBackend db = new DerbyBackend("todo");

        // TODO: sanitize and verify

        try {
            String bookUsername = request.getParameter("username");
            if (bookUsername == null) {
                bookUsername = username;
            }
            if (! (admin || username.equals(bookUsername))) {
                throw new RuntimeException("Not allowed to edit book belonging to this user.");
            }
            String bookTitle = request.getParameter("title");
            String bookAuthor = request.getParameter("author");
            Integer bookRating = Integer.parseInt(request.getParameter("rating"));
            if (edit) {
                db.updateBook(bookId, bookUsername, bookTitle, bookAuthor, bookRating);
            } else {
                db.addBook(bookUsername, bookTitle, bookAuthor, bookRating);
            }
            success = true;
        } catch (Exception ex) {
            Logger.getLogger(book.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
            errorMessage = ex.getMessage();
        }

        if (success) {
            response.sendRedirect(base + "/");
        } else {
            session.setAttribute("error", errorMessage);
            if (edit) {
                response.sendRedirect(base + "/book?id=" + bookId);
            } else {
                response.sendRedirect(base + "/book");
            }
        }

    }

    private void writeHeader(PrintWriter out, boolean edit) {
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

    private void writeEditForm(PrintWriter out, boolean edit, boolean admin, Book book) {
        String editText;
        if (edit) {
            editText = "Update";
        } else {
            editText = "Create";
        }

        out.println("<form action=\"\" method=\"POST\">");
        if (admin) {
            out.println("<div><label for=\"username\">Username: </label> <input id=\"username\" name=\"username\" type=\"text\" value=\"" + book.getUsername() + "\"></div>");
        }

        out.println("<div><label for=\"title\">Title: </label> <input id=\"title\" name=\"title\" type=\"text\" value=\"" + book.getTitle() + "\"></div>");
        out.println("<div><label for=\"author\">Author: </label> <input id=\"author\" name=\"author\" type=\"text\" value=\"" + book.getAuthor() + "\"></div>");

        out.println("<div><label for=\"rating\">Rating: </label> <select name=\"rating\" id=\"rating\"></div>");
        for (int i = 0; i < 10; i++) {
            if (i == book.getRating()) {
                out.printf("<option selected=\"selected\">%s</option>\n", i);
            } else {
                out.printf("<option>%s</option>\n", i);
            }
        }
        out.println("</select>");

        out.println("<div><input type=\"submit\" value=\"" + editText + "\"></div>");
        out.println("</form>");
    }

    private void writeErrorMessage(PrintWriter out) {
        out.println("<h2>Could not retrieve a book with the requested id!</h2>");
        out.println("You may want to <a href=\"book\">add a new book</a>.");
    }
}
