/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        boolean admin = (boolean) session.getAttribute("isadmin");
        DerbyBackend db = new DerbyBackend();

        // if no book id, then we're adding a book
        // otherwise, we're editing a book - show edit and delete forms
        String bookId = (String) request.getParameter("id");
        boolean edit = true;
        if (bookId == null) {
            edit = false;
        }

        Logger.getLogger(book.class.getName()).log(Level.INFO, String.format("requested book id: %s", bookId));

        // print the header stuff
        PrintWriter out = response.getWriter();

        snippets.writeHead(out);

        // links
        snippets.writeLogoutButton(out, username, base);
        out.println("<a href=\"" + base + "/\">Book List</a>");

        this.writeHeader(out, edit);

        // different title depending on whether admin and/or editing or not
        if (admin) {
            out.println("<p>You are admin and can edit/add a book for any user!</p>");
        } else {
          out.printf("<p>Max number of books allowed: %s</p>\n", this.getMaxRecords());
          try {
              Integer n = db.getUserBooks(username).size();
              if (n >= this.getMaxRecords() && !edit) {
                out.println("<p>You already have the maximum number of books. Please delete some books before trying to add more.</p>");
                snippets.writeEnd(out);
                return;
              } else {
                out.println("<p>Number of books you have: " + n.toString() + "</p>");
              }
          } catch(Exception e){
              out.println(e.getMessage());
              snippets.writeEnd(out);
          }
        }

        // display an error if there is one
        String error = (String) session.getAttribute("error");
        if (error != null) {
            out.println("<p>Error: " + error + "</p>");
            session.removeAttribute("error");
        }

        Book book = null;
        if (edit) {
            try {
                book = db.getBook(bookId);
            } catch (Exception e) {
                out.println(e.getMessage());
                snippets.writeEnd(out);
                return;
            }
        } else {
            book = new Book(null, "", "", "", 0);
        }

        if (edit && book == null) {
          out.println("<p>Requested book not found!</p>");
        } else {
            // write out the form
            this.writeEditForm(out, edit, admin, book);
        }

        snippets.writeEnd(out);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        boolean admin = (boolean) session.getAttribute("isadmin");

        ServletContext context = this.getServletContext();
        String base = context.getContextPath();

        // if no book id, then we're adding a book
        // otherwise, show edit and delete forms
        String bookId = request.getParameter("id");
        boolean edit = true;
        if (bookId == null) {
            edit = false;
        }

        DerbyBackend db = new DerbyBackend();

        try {

            ArrayList<Book> allBooks = db.getUserBooks(username);
            // make sure still have space for more books
            if (allBooks.size() >= getMaxRecords()) {
                throw new RuntimeException("You already have the maximum number of books!");
            }

            // make sure users can only edit their own books (or any if admin)
            String bookUsername = request.getParameter("username");
            if (bookUsername == null) {
                bookUsername = username;
            }
            if (! (admin || username.equals(bookUsername))) {
                throw new RuntimeException("Not allowed to edit book belonging to this user.");
            }

            // make sure data sensible
            String bookTitle = request.getParameter("title").trim();
            String bookAuthor = request.getParameter("author").trim();
            if (bookTitle.length() < 1 || bookAuthor.length() < 1) {
              throw new RuntimeException("Fields can't be empty!");
            }
            Integer bookRating = Integer.parseInt(request.getParameter("rating"));
            if (bookRating < 1 || bookRating > 10) {
              throw new RuntimeException("Invalid rating value!");
            }

            if (edit) {
                db.updateBook(bookId, bookUsername, bookTitle, bookAuthor, bookRating);
            } else {
                db.addBook(bookUsername, bookTitle, bookAuthor, bookRating);
            }
            response.sendRedirect(base + "/");
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
            if (edit) {
                response.sendRedirect(base + "/book?id=" + bookId);
            } else {
                response.sendRedirect(base + "/book");
            }
        }

    }

    private void writeHeader(PrintWriter out, boolean edit) {
        if (edit) {
            out.println("<h2>Edit Book</h2>");
        } else {
            out.println("<h2>Add Book</h2>");
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

        out.println("<form method=\"POST\">");
        if (admin) {
            out.println("<div><label for=\"username\">Username: </label> <input id=\"username\" name=\"username\" type=\"text\" value=\"" + book.getUsername() + "\" required></div>");
        }

        out.println("<div><label for=\"title\">Title: </label> <input id=\"title\" name=\"title\" type=\"text\" value=\"" + book.getTitle() + "\" required></div>");
        out.println("<div><label for=\"author\">Author: </label> <input id=\"author\" name=\"author\" type=\"text\" value=\"" + book.getAuthor() + "\" required></div>");

        out.println("<div><label for=\"rating\">Rating: </label> <select name=\"rating\" id=\"rating\"></div>");
        for (int i = 1; i <= 10; i++) {
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
}