package com.example.foodorderingjdbcproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    // Update these with your MySQL credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/food_ordering_db";
    private static final String USER = "root";
    private static final String PASS = "eyasu5800"; // Change this to your MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        // This method is less critical now as the SQL script should be run manually.
        // However, it can still be useful for ensuring tables exist.
    }

    public static void saveOrder(String orderGroupId, String orderType, String tableNumber, List<MenuController.OrderItem> orderItems, double totalPrice) {
        String orderSQL = "INSERT INTO orders(order_group_id, order_type, table_number, order_status, total_price) VALUES(?, ?, ?, ?, ?)";
        String itemSQL = "INSERT INTO order_items(order_group_id, food_name, quantity, price_per_item) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Set auto-commit to false to handle transaction manually
            conn.setAutoCommit(false);

            // Insert into parent 'orders' table
            try (PreparedStatement orderPstmt = conn.prepareStatement(orderSQL)) {
                orderPstmt.setString(1, orderGroupId);
                orderPstmt.setString(2, orderType);
                orderPstmt.setString(3, "Dine-In".equals(orderType) ? tableNumber : null);
                orderPstmt.setString(4, "Preparing"); // Initial status
                orderPstmt.setDouble(5, totalPrice);
                orderPstmt.executeUpdate();
            }

            // Insert each item into 'order_items' table
            try (PreparedStatement itemPstmt = conn.prepareStatement(itemSQL)) {
                for (MenuController.OrderItem item : orderItems) {
                    itemPstmt.setString(1, orderGroupId);
                    itemPstmt.setString(2, item.foodItem.getName());
                    itemPstmt.setInt(3, item.quantity);
                    itemPstmt.setDouble(4, item.foodItem.getPrice());
                    itemPstmt.addBatch();
                }
                itemPstmt.executeBatch();
            }

            // If everything is successful, commit the transaction
            conn.commit();

        } catch (SQLException e) {
            System.out.println("Save Order Transaction Error: " + e.getMessage());
            // Consider rolling back the transaction here if something failed
        }
    }

    public static List<FoodItem> getAllFoodItems() {
        List<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("type"),
                    rs.getString("country"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getBoolean("is_fasting"),
                    rs.getString("ingredients"),
                    rs.getString("nutritional_value"),
                    rs.getInt("preparation_time")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Fetch Food Error: " + e.getMessage());
        }
        return items;
    }
}