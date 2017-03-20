/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 *
 * @author Samuel Walladge
 */
public class DerbyBackend implements CatalogueInterface {
    private String jdbcConnectionString;

    public DerbyBackend(String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
        this.init();
    }

    public DerbyBackend() {
        this.jdbcConnectionString = "jdbc:derby://localhost:1527/s265679HIT337A1;create=true";
        this.init();
    }

    private void init() throws RuntimeException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("failed to load the driver", ex);
        }

        Connection connection = null;
        try {
            // TODO: tidy this - also should only be run once
            //       check if table exists before trying to re-create
            //       maybe do this once in a separate method to be called by the servlet init?
            connection = DriverManager.getConnection(this.jdbcConnectionString);
            Statement s = connection.createStatement();
            s.execute("create table books (id integer not null generated always as identity (start with 1, increment by 1), username varchar(50), title varchar(100), author varchar(100), rating integer, constraint primary_key primary key (id))");
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            // check if table already created
            // otherwise, rethrow the error
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            }
        }
    }

    private Connection getConnection() throws Exception {
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection con = DriverManager.getConnection(this.jdbcConnectionString);
        return con;
    }

    @Override
    public ArrayList<Book> getAllBooks() throws Exception {
        ArrayList<Book> results = new ArrayList<Book>();
        Connection connection = this.getConnection();
        try {
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
            boolean res = statement.execute();
        } catch (Exception ex) {
            Logger.getLogger(DerbyBackend.class.getName()).log(Level.INFO, ex.getMessage());
            throw ex;
        } finally {
            connection.close();
        }

    }

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
