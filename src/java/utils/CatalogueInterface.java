package utils;

import java.util.ArrayList;

/**
 *
 * @author Samuel Walladge
 */
public interface CatalogueInterface {

    public ArrayList<Book> getAllBooks() throws Exception;
    public ArrayList<Book> getUserBooks(String username) throws Exception;
    public Book getBook(Integer id) throws Exception;
    public void addBook(String username, String title, String author, Integer rating) throws Exception;
    public void updateBook(Integer id, String username, String title, String author, Integer rating) throws Exception;
    public void removeBook(Integer id) throws Exception;
    
}
