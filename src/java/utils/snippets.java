/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.PrintWriter;

/**
 *
 * @author Samuel Walladge
 */
public class snippets {

    public static void writeLogoutButton(PrintWriter out, String base) {
        out.println("<form style=\"display:inline;\" action=\"" + base + "/logout\" method=\"POST\">");
        out.println("<input style=\"display:inline;background:none;border:none;padding:0;font-family:inherit;font-size:inherit;text-decoration:underline;cursor:pointer;\" type=\"submit\" value=\"Logout\" >");
        out.println("</form>");
    }

    public static void writeHead(PrintWriter out) {
        out.println("<html><head>");
        out.println("</head><body>");
        out.println("<h1>Books Catalogue</h1>");
    }

    public static void writeEnd(PrintWriter out) {
        out.println("</body></html>");
    }
}
