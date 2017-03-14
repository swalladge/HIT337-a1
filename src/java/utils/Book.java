package utils;

/**
 *
 * @author Samuel Walladge
 * class for storing a single book object
 */
public class Book {
    private String id;
    private String username;
    private String title;
    private String author;
    private Integer rating;

    public Book(String id, String username, String title, String author, Integer rating) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    public String getId()
    {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating){
        this.rating = rating;
    }
}
