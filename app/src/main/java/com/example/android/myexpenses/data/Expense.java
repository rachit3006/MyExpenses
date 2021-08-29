package com.example.android.myexpenses.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;

    private String description;

    private String receiver;

    private String date;

    public Expense(double amount, String description, String receiver, String date) {
        this.amount = amount;
        this.description = description;
        this.receiver = receiver;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDate() { return date; }

}
