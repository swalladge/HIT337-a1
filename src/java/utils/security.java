/*
 * Copyright (c) 2017 Samuel Walladge
 */
package utils;

/**
 * provides security methods for use
 * @author Samuel Walladge
 */
public class security {

    /**
     * Does some simple escaping for security - avoid XSS and weirdly formatted output.
     * @param s the string to be sanitized
     * @return  the sanitized string
     */
    public static String escapeHtml(String s) {
        // replace & first otherwise we get doublely sanitized text! :P
        s = s.replace("&", "&amp;");

        // then the rest
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        s = s.replace("\"", "&quot;");
        return s;
    }
    
}
