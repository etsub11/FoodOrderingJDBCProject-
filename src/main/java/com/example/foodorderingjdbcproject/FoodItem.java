package com.example.foodorderingjdbcproject;

public class FoodItem {
    private int id;
    private String name;
    private String category;
    private String type;
    private String country;
    private double price;
    private String imageUrl;
    private boolean isFasting;
    private String ingredients;
    private String nutritionalValue;
    private int preparationTime;

    public FoodItem(int id, String name, String category, String type, String country, double price, String imageUrl, boolean isFasting, String ingredients, String nutritionalValue, int preparationTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.country = country;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFasting = isFasting;
        this.ingredients = ingredients;
        this.nutritionalValue = nutritionalValue;
        this.preparationTime = preparationTime;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getCountry() { return country; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFasting() { return isFasting; }
    public String getIngredients() { return ingredients; }
    public String getNutritionalValue() { return nutritionalValue; }
    public int getPreparationTime() { return preparationTime; }
}