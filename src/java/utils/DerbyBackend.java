/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuel
 */
public class DerbyBackend implements CatalogueInterface {
    private String jdbcConnectionString;
    private ArrayList<Book> dummyBooks = null;

    public DerbyBackend(String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
        this.dummyBooks = new ArrayList<Book>();
        dummyBooks.add(new Book(0, "sam", "test book 1", "nobody", 3));
    }

    @Override
    public ArrayList<Book> getAllBooks() throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : dummyBooks) {
            if (book != null) {
                results.add(book);
            }
        }
        return results;
    }

    @Override
    public ArrayList<Book> getUserBooks(String username) throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, String.format("Getting books for %s", username));
        for (Book book : dummyBooks) {
            if (book != null && book.getUsername().equals(username)) {
                results.add(book);
            }
        }
        Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, String.format("Found %s books", results.size()));
        return results;
    }

    @Override
    public void addBook(String username, String title, String author, Integer rating) throws Exception {
        dummyBooks.add(new Book(dummyBooks.size(), username, title, author, rating));
    }

    @Override
    public void updateBook(Integer id, String username, String title, String author, Integer rating) throws Exception {
        dummyBooks.set(id, new Book(id, username, title, author, rating));
    }

    @Override
    public void removeBook(Integer id) throws Exception {
        dummyBooks.set(id, null);
    }

    @Override
    public Book getBook(Integer id) throws Exception {
        return dummyBooks.get(id);
    }
    
}
