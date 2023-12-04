package com.cs370.group4.goodspredict;

public class Product {
    private String name;
    private String imageUrl;
    private double price;
    private String description;

    public Product(String name, String imageUrl, double price, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    // toString 方法用于在列表中显示产品名称
    @Override
    public String toString() {
        return name;
    }
}
