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
 * @author Samuel Walladge
 */
public class DerbyBackend implements CatalogueInterface {
    private String jdbcConnectionString;
    public static ArrayList<Book> dummyBooks = new ArrayList<Book>();

    public DerbyBackend(String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
        this.init();
    }

    public DerbyBackend() {
        this.jdbcConnectionString = "TODO: default";
        this.init();
    }

    private void init() {
        this.dummyBooks = new ArrayList<Book>();
        dummyBooks.add(new Book("0", "sam", "test book 1", "nobody", 3));
    }

    @Override
    public ArrayList<Book> getAllBooks() throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, "getting all books");
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
        dummyBooks.add(new Book(((Integer) dummyBooks.size()).toString(), username, title, author, rating));
    }

    @Override
    public void updateBook(String id, String username, String title, String author, Integer rating) throws Exception {
        Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, String.format("Updating book %s", id));
        dummyBooks.set(Integer.parseInt(id), new Book(id, username, title, author, rating));
    }

    @Override
    public void removeBook(String id) throws Exception {
        dummyBooks.set(Integer.parseInt(id), null);
    }

    @Override
    public Book getBook(String id) throws Exception {
        return dummyBooks.get(Integer.parseInt(id));
    }
    
}
