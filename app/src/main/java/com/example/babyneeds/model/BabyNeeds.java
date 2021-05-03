package com.example.babyneeds.model;

public class BabyNeeds {
    private int id;
    private String item;
    private int quantity;
    private String color;
    private int size;
    private String date;

    public BabyNeeds() { this("", 0, "", 0, ""); }

    public BabyNeeds(String item, int quantity, String color, int size, String date) {
        this.item = item;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
