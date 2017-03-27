package utils;

import java.util.ArrayList;

/**
 *
 * @author Samuel Walladge
 */
public interface CatalogueInterface {

    // read methods - should either return what they are asked for, or throw an exception.
    public ArrayList<Book> getAllBooks() throws Exception;
    public ArrayList<Book> getUserBooks(String username) throws Exception;

    // should return null if no book with that id exists
    public Book getBook(String id) throws Exception;

    // write methods - these methods modify the database, and they should be atomic
    public void addBook(String username, String title, String author, Integer rating) throws Exception;
    public void updateBook(String id, String username, String title, String author, Integer rating) throws Exception;
    public void removeBook(String id) throws Exception;
    
}
