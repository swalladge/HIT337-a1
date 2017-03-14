/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.snippets;
import utils.Book;
import utils.DerbyBackend;

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
        if (username.equals(context.getInitParameter("admin"))) {
            admin = true;
        }

        // Logger.getLogger(books.class.getName()).log(Level.INFO, "getting db");
        DerbyBackend db = new DerbyBackend("todo");
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            if (admin) {
                books = db.getAllBooks();
            } else {
                books = db.getUserBooks(username);
            }
        } catch (Exception e) {
            // error
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

        this.writeBooks(out, books);

        out.println("</body></html>");
    }

    private void writeHeader(PrintWriter out) {
        out.println("<h1>My Book List</h1>");
    }

    private void writeBooks(PrintWriter out, ArrayList<Book> bookList) {
        out.println("<table>");
        out.println("<tr><th>Title</th><th>Author</th><th>Rating</th><th>Action</th></tr>");
        for (Book book : bookList) {
            out.println("<tr>");
            out.println("<td>" + book.getTitle() + "</td>");
            out.println("<td>" + book.getAuthor() + "</td>");
            out.println("<td>" + book.getRating().toString() + "</td>");
            out.println("<td><a href=\"book?id=" + book.getId().toString() + "\">edit</a></td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }
        
}
