package com.staniskhan;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book 
{
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty author;
    private IntegerProperty year;
    private DoubleProperty price;
    private StringProperty category;
    private IntegerProperty amount;
    private IntegerProperty realAmount;

    public Book(int id, String title, String author, int year, double price, String category, int amount, int realAmount) 
    {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleIntegerProperty(amount);
        this.realAmount = new SimpleIntegerProperty(realAmount);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }

    public String getAuthor() { return author.get(); }
    public void setAuthor(String author) { this.author.set(author); }
    public StringProperty authorProperty() { return author; }

    public int getYear() { return year.get(); }
    public void setYear(int year) { this.year.set(year); }
    public IntegerProperty yearProperty() { return year; }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    public int getAmount() { return amount.get(); }
    public void setAmount(int amount) { this.amount.set(amount); }
    public IntegerProperty amountProperty() { return amount; }

    public int getRealAmount() { return realAmount.get(); }
    public void setRealAmount(int realAmount) { this.realAmount.set(realAmount); }
    public IntegerProperty realAmountProperty() { return realAmount; }

    @Override
    public String toString() {
        return title.get() + " - " + author.get();
    }
}