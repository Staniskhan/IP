package com.staniskhan;

public class IssuedBook {
    private int bookId;
    private int issuedAmount;

    public IssuedBook(int bookId, int issuedAmount) {
        this.bookId = bookId;
        this.issuedAmount = issuedAmount;
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getIssuedAmount() { return issuedAmount; }
    public void setIssuedAmount(int issuedAmount) { this.issuedAmount = issuedAmount; }
    public void incrementIssuedAmount() { this.issuedAmount++; }
    public void decrementIssuedAmount() { 
        if (this.issuedAmount > 0) this.issuedAmount--; 
    }
}