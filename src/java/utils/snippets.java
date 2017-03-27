/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.PrintWriter;

/**
 * contains some snippets of html that can be reused across various servlets
 * @author Samuel Walladge
 */
public class snippets {

    /**
     * writes the logout button (and surrounding stuff) to the printwriter
     * @param out
     * @param username username of the currently logged in user
     * @param base the base url of the app - required for printing links
     */
    public static void writeLogoutButton(PrintWriter out, String username, String base) {
        out.printf("<div><span>(%s)</span>", username);
        out.println("<form style=\"display:inline;\" action=\"" + base + "/logout\" method=\"POST\">");
        out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"submit\" value=\"Logout\" >");
        out.println("</form></div>");
    }

    /**
     * writes the beginning tags and head of the html document
     * @param out 
     */
    public static void writeHead(PrintWriter out) {
        out.println("<!DOCTYPE html><html lang=\"en\"><head>");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><meta charset=\"utf-8\">");
        out.println("<style>table{border-collapse:collapse;}th,td{border:1px solid grey;padding:10px;}</style>");
        out.println("<title>Book Catalogue</title>");
        out.println("</head><body>");
        out.println("<h1>Books Catalogue</h1>");
    }

    /**
     * writes the end of the document - footer and end tags
     * @param out 
     */
    public static void writeEnd(PrintWriter out) {
        out.println("<p><small>Copyright &copy; 2017 Samuel Walladge</small></p>");
        out.println("</body></html>");
    }
}
