/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This provides a database backend which uses Derby - implements the CatalogueInterface.
 * @author Samuel Walladge
 */
public class DerbyBackend implements CatalogueInterface {
    private final String jdbcConnectionString;

    public DerbyBackend(String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
    }

    public DerbyBackend() {
        this.jdbcConnectionString = "jdbc:derby://localhost:1527/s265679HIT337A1;create=true";
    }

    /**
     * Creates the table in the database if it doesn't exist
     * @throws Exception
     */
    public void createTables() throws Exception {
        Connection connection = this.getConnection();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet res = meta.getTables(null, null, "BOOKS", null);
            // create tables if doesn't exist
            if (!res.next()) {
                Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, "table doesn't exist");
                Statement s = connection.createStatement();
                s.execute("create table books (id integer not null generated always as identity (start with 1, increment by 1), username varchar(50), title varchar(100), author varchar(100), rating integer, constraint primary_key primary key (id))");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            connection.close();
        }
    }

    /**
     * gets a connection to the database as configured by jdbcConnectionString
     * @return
     * @throws Exception 
     */
    private Connection getConnection() throws Exception {
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection con = DriverManager.getConnection(this.jdbcConnectionString);
        return con;
    }

    /**
     * gets a list containing all the books in the database
     * used when displaying all the books to the admin user
     * @return
     * @throws Exception 
     */
    @Override
    public ArrayList<Book> getAllBooks() throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        Connection connection = null;
        try {
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from books");
            ResultSet res = statement.executeQuery();
            while(res.next()){
                results.add(new Book(res.getString("id"), res.getString("username"), res.getString("title"), res.getString("author"), res.getInt("rating")));
            }
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

        return results;
    }

    /**
     * gets a list containing all the books from the database that belong to the given user
     * @param username
     * @return
     * @throws Exception 
     */
    @Override
    public ArrayList<Book> getUserBooks(String username) throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        Connection connection = this.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("select * from books where username = ?");
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, "getting user books");
            statement.setString(1, username);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                results.add(new Book(res.getString("id"), res.getString("username"), res.getString("title"), res.getString("author"), res.getInt("rating")));
            }

        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

        return results;
        
    }

    /**
     * adds a book to the database with the given data
     * @param username
     * @param title
     * @param author
     * @param rating
     * @throws Exception 
     */
    @Override
    public void addBook(String username, String title, String author, Integer rating) throws Exception {
        Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, username + title);
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("insert into books (username, title, author, rating) values (?, ?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setInt(4, rating);
            statement.execute();
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.SEVERE, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }
    }

    /**
     * updates an already existing book (identified by the id) with the given data
     * @param id
     * @param username
     * @param title
     * @param author
     * @param rating
     * @throws Exception 
     */
    @Override
    public void updateBook(String id, String username, String title, String author, Integer rating) throws Exception {
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("update books set username = ?, title = ?, author = ?, rating = ? where id = ?");
            statement.setString(5, id);
            statement.setString(1, username);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setInt(4, rating);
            statement.execute();
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

    }

    /**
     * deletes a book from the database
     * @param id
     * @throws Exception 
     */
    @Override
    public void removeBook(String id) throws Exception {
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("delete from books where id = ?");
            statement.setString(1, id);
            boolean res = statement.execute();
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

    }

    /**
     * retrieves a book from the database by id, and returns it. Raises an exception on error, and returns null if book not found.
     * @param id
     * @return
     * @throws Exception 
     */
    @Override
    public Book getBook(String id) throws Exception {
        Book book = null;
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from books where id = ?");
            statement.setString(1, id);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                book = new Book(res.getString("id"), res.getString("username"), res.getString("title"), res.getString("author"), res.getInt("rating"));
            }
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

        return book;

    }
    
}
