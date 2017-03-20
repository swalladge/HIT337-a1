package utils;

import java.util.ArrayList;

/**
 *
 * @author Samuel Walladge
 */
public interface CatalogueInterface {

    public ArrayList<Book> getAllBooks() throws Exception;
    public ArrayList<Book> getUserBooks(String username) throws Exception;
    public Book getBook(String id) throws Exception;
    public boolean addBook(String username, String title, String author, Integer rating) throws Exception;
    public void updateBook(String id, String username, String title, String author, Integer rating) throws Exception;
    public void removeBook(String id) throws Exception;
    
}
