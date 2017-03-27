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
import utils.security;

/**
 * Main page of the application - displays the book list and links to edit and delete them
 * POSTing to this servlet allows deleting a book
 * @author Samuel Walladge
 */
public class books extends HttpServlet {

    /**
     * Should be protected by authFilter - displays the book list for a user, or all books in the case of the admin user.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // protected by filter; guaranteed to exist
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        boolean admin = (boolean) session.getAttribute("isadmin");

        ServletContext context = this.getServletContext();
        String base = context.getContextPath();
        String dbErrorMsg = null;

        // get the books from the database
        DerbyBackend db = new DerbyBackend();
        ArrayList<Book> books = new ArrayList<>();
        try {
            if (admin) {
                books = db.getAllBooks();
            } else {
                books = db.getUserBooks(username);
            }
        } catch (Exception e) {
            dbErrorMsg = "Database Error: " + e.getMessage();
        }

        PrintWriter out = response.getWriter();
        // write headers
        snippets.writeHead(out);

        // write links
        snippets.writeLogoutButton(out, username, base);
        out.println("<a href=\"" + base + "/book\">Add Book</a>");

        // write a welcome message
        out.println("<p>Welcome " + security.escapeHtml(username) + "!</p>");

        // note to admins
        if (admin) {
            out.println("<p>You are the admin user and can edit books of other users</p>");
        }

        // display a flashed session message
        String message = (String) session.getAttribute("message");
        if (message != null) {
            out.println("<p>Notice: " + message + "</p>");
            session.removeAttribute("message");
        }

        // display a db error message if getting books failed...
        if (dbErrorMsg != null) {
            // dberrormsg could contain nasty characters
            out.println("<p>" + security.escapeHtml(dbErrorMsg) + "</p>");
        } else {
            // ... otherwise write out the table of books!
            this.writeBooks(out, books, admin);
        }

        // write the footer and end tags
        snippets.writeEnd(out);
    }

    /**
     * handles deleting a book - the book table provides links (actually styled POST forms) to delete books directly from the list
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = this.getServletContext();
        String contextPath = context.getContextPath();

        String id = request.getParameter("id");
        String method = request.getParameter("method");

        // sanity check
        if (id == null || method == null || !"delete".equals(method)) {
            response.sendRedirect(contextPath);
            return;
        }

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        boolean admin = (boolean) session.getAttribute("isadmin");

        String message = "Successfully deleted!";
        DerbyBackend db = new DerbyBackend();
        try {
            // check if the book exists and the user is allowed to edit it
            Book book = db.getBook(id);
            if (book == null || (!book.getUsername().equals(username) && !admin)) {
                throw new RuntimeException("Cannot delete that book!");
            }
            // actually remove the book
            db.removeBook(id);
        } catch (Exception e) {
            Logger.getLogger(book.class.getName()).log(Level.INFO, e.getMessage());
            // if there was an exception, set the message accordingly
            message = e.getMessage();
        }

        // set the session message and redirect back to the booklist
        session.setAttribute("message", message);
        response.sendRedirect(contextPath);

    }

    /**
     * writes out the list of books as an html table
     * @param out
     * @param bookList the list of book objects to display
     * @param admin whether the user is an admin or not
     */
    private void writeBooks(PrintWriter out, ArrayList<Book> bookList, Boolean admin) {

        // no point displaying an empty table
        if (bookList.isEmpty()) {
          out.println("<p>You have no books! Try adding one!</p>");
          return;
        }

        // write the actual table
        // note that an extra column is added for displaying the book's owner if the logged-in user is admin
        out.println("<h2>My Book List</h2>");
        out.println("<table><tr>");
        if (admin) {
            out.println("<th>Username</th>");
        }
        out.println("<th>Title</th><th>Author</th><th>Rating</th><th>Actions</th></tr>");
        for (Book book : bookList) {
            out.println("<tr>");
            if (admin) {
                out.println("<td>" + security.escapeHtml(book.getUsername()) + "</td>");
            }
            out.println("<td>" + security.escapeHtml(book.getTitle()) + "</td>");
            out.println("<td>" + security.escapeHtml(book.getAuthor()) + "</td>");
            out.println("<td>" + book.getRating().toString() + "</td>");
            out.println("<td>");
            out.println("<a href=\"book?id=" + security.escapeHtml(book.getId()) + "\">edit</a>");
            out.println("<form style=\"display:inline;\" method=\"POST\">");
            out.println("<input type=\"hidden\" name=\"method\" value=\"delete\" >");
            out.println("<input type=\"hidden\" name=\"id\" value=\"" + book.getId() + "\" >");
            out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"submit\" value=\"delete\" >");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }
}
