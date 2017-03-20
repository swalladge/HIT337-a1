/*
 * Copyright (c) 2017 Samuel Walladge
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        if (username.equals(context.getInitParameter("adminUsername"))) {
            admin = true;
        }

        // Logger.getLogger(books.class.getName()).log(Level.INFO, "getting db");
        DerbyBackend db = new DerbyBackend();
        ArrayList<Book> books = new ArrayList<>();
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

        String message = (String) session.getAttribute("message");
        if (message != null) {
            out.println("<p>Notice: " + message + "</p>");
            session.removeAttribute("message");
        }

        this.writeBooks(out, books, admin);

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = this.getServletContext();
        String contextPath = context.getContextPath();

        String id = request.getParameter("id");
        String method = request.getParameter("method");

        if (id == null || method == null || !"delete".equals(method)) {
            response.sendRedirect(contextPath);
            return;
        }

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // get admin username from context init param
        String adminUsername = context.getInitParameter("adminUsername");
        Boolean admin = false;
        if (username.equals(adminUsername)) {
            admin = true;
        }

        String message = "Successfully deleted!";
        DerbyBackend db = new DerbyBackend();
        try {
            Book book = db.getBook(id);
            if (!book.getUsername().equals(username) && !admin) {
                throw new RuntimeException("Unable to access that book!");
            }
            db.removeBook(id);
        } catch (Exception e) {
            Logger.getLogger(book.class.getName()).log(Level.INFO, e.getMessage());
            message = e.getMessage();
        }

        session.setAttribute("message", message);
        response.sendRedirect(contextPath);

    }
    private void writeHeader(PrintWriter out) {
        out.println("<h1>My Book List</h1>");
    }

    private void writeBooks(PrintWriter out, ArrayList<Book> bookList, Boolean admin) {
        out.println("<table>");
        out.println("<tr>");
        if (admin) {
            out.println("<th>Username</th>");
        }
        out.println("<th>Title</th><th>Author</th><th>Rating</th><th>Action</th></tr>");
        for (Book book : bookList) {
            out.println("<tr>");
            if (admin) {
                out.println("<td>" + book.getUsername() + "</td>");
            }
            out.println("<td>" + book.getTitle() + "</td>");
            out.println("<td>" + book.getAuthor() + "</td>");
            out.println("<td>" + book.getRating().toString() + "</td>");
            out.println("<td>");
            out.println("<a href=\"book?id=" + book.getId() + "\">edit</a>");
            out.println("<form style=\"display:inline;\" action=\"\" method=\"POST\">");
            out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"hidden\" name=\"method\" value=\"delete\" >");
            out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"hidden\" name=\"id\" value=\"" + book.getId() + "\" >");
            out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"submit\" value=\"delete\" >");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }
        
}
