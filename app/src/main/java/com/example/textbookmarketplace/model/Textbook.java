package com.example.textbookmarketplace.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "textbooks")
public class Textbook implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "isbn")
    private String isbn;

    @ColumnInfo(name = "edition")
    private String edition;

    @ColumnInfo(name = "copies")
    private int copies;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "seller_name")
    private String sellerName;

    @ColumnInfo(name = "seller_email")
    private String sellerEmail;

    @ColumnInfo(name = "bank_name")
    private String bankName;

    @ColumnInfo(name = "account_number")
    private String accountNumber;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "condition")
    private String condition;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "date_added")
    private long dateAdded;

    // Constructor
    public Textbook(String title, String author, String isbn, String edition,
                    int copies, double price, String sellerName, String sellerEmail,
                    String bankName, String accountNumber, String course,
                    String condition, String description, String imageUrl) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.edition = edition;
        this.copies = copies;
        this.price = price;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.course = course;
        this.condition = condition;
        this.description = description;
        this.imageUrl = imageUrl;
        this.dateAdded = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public int getCopies() { return copies; }
    public void setCopies(int copies) { this.copies = copies; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getDateAdded() { return dateAdded; }
    public void setDateAdded(long dateAdded) { this.dateAdded = dateAdded; }
}