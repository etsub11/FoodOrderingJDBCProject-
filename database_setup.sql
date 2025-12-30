-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS food_ordering_db;
USE food_ordering_db;

-- 2. Create Tables

-- This table now represents a single, complete order
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_group_id VARCHAR(255) NOT NULL, -- Unique ID for the entire order
    table_number VARCHAR(50),             -- Can be NULL for take-away
    order_type VARCHAR(50) NOT NULL,       -- "Dine-In" or "Take-Away"
    order_status VARCHAR(50) NOT NULL,     -- "Preparing", "Ready", "Completed"
    total_price DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX(order_group_id) -- Index for faster lookups
);

-- This table stores the individual items within an order
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_group_id VARCHAR(255) NOT NULL,
    food_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price_per_item DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_group_id) REFERENCES orders(order_group_id)
);

-- Table for Food Items
CREATE TABLE IF NOT EXISTS food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    country VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    image_url TEXT,
    is_fasting BOOLEAN DEFAULT FALSE,
    ingredients TEXT,
    nutritional_value TEXT,
    preparation_time INT -- Estimated time in minutes
);

-- 3. Insert Data
-- Breakfast
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Pancakes', 'Breakfast', 'Pastry', 'USA', 300.00, 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Flour, Milk, Eggs, Sugar, Butter', 'Calories: 350, Carbs: 45g, Protein: 8g', 15),
('Omelette', 'Breakfast', 'Egg', 'France', 350.00, 'https://images.unsplash.com/photo-1510693206972-df098062cb71?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Eggs, Milk, Cheese, Salt, Pepper', 'Calories: 250, Carbs: 2g, Protein: 18g', 10),
('Chechebsa', 'Breakfast', 'Traditional', 'Ethiopia', 250.00, 'photo/chechabsa.jpg', FALSE, 'Flour, Butter, Berbere, Honey', 'Calories: 400, Carbs: 55g, Protein: 6g', 12),
('Ful', 'Breakfast', 'Bean', 'Ethiopia', 200.00, 'photo/ful.jpg', TRUE, 'Fava Beans, Onion, Tomato, Oil, Spices', 'Calories: 300, Carbs: 40g, Protein: 15g', 10);

-- Lunch
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Burger', 'Lunch', 'Fast Food', 'USA', 450.00, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Beef Patty, Bun, Lettuce, Tomato, Cheese', 'Calories: 600, Carbs: 50g, Protein: 30g', 15),
('Salad', 'Lunch', 'Healthy', 'Greece', 380.00, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', TRUE, 'Lettuce, Cucumber, Tomato, Olives, Feta', 'Calories: 200, Carbs: 15g, Protein: 5g', 8),
('Doro Wat', 'Lunch', 'Traditional', 'Ethiopia', 600.00, 'photo/Doro Wat.jpg', FALSE, 'Chicken, Onion, Berbere, Butter, Egg', 'Calories: 500, Carbs: 20g, Protein: 40g', 30),
('Macaroni', 'Lunch', 'Italian', 'Italy', 150.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', TRUE, 'Pasta, Tomato Sauce, Garlic, Basil', 'Calories: 450, Carbs: 80g, Protein: 12g', 20);

-- Dinner
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Pizza', 'Dinner', 'Italian', 'Italy', 650.00, 'https://images.unsplash.com/photo-1513104890138-7c749659a591?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Dough, Tomato Sauce, Cheese, Basil', 'Calories: 800, Carbs: 90g, Protein: 35g', 25),
('Sushi', 'Dinner', 'Japanese', 'Japan', 750.00, 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Rice, Fish, Seaweed, Vegetables', 'Calories: 400, Carbs: 60g, Protein: 20g', 20),
('Kitfo', 'Dinner', 'Traditional', 'Ethiopia', 550.00, 'photo/kitfo.jpg', FALSE, 'Minced Beef, Butter, Mitmita', 'Calories: 450, Carbs: 5g, Protein: 45g', 20),
('Steak', 'Dinner', 'Meat', 'USA', 800.00, 'https://images.unsplash.com/photo-1600891964092-4316c288032e?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Beef Steak, Salt, Pepper, Butter', 'Calories: 700, Carbs: 0g, Protein: 60g', 25);

-- Snack
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Sandwich', 'Snack', 'Fast Food', 'UK', 220.00, 'photo/sandwich.jpg', FALSE, 'Bread, Ham, Cheese, Lettuce', 'Calories: 350, Carbs: 40g, Protein: 15g', 5),
('Samosa', 'Snack', 'Pastry', 'India', 50.00, 'https://images.unsplash.com/photo-1601050690597-df0568f70950?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', TRUE, 'Flour, Potato, Peas, Spices', 'Calories: 150, Carbs: 20g, Protein: 3g', 10),
('Fries', 'Snack', 'Fast Food', 'Belgium', 150.00, 'photo/fries.png', FALSE, 'Potato, Oil, Salt', 'Calories: 300, Carbs: 45g, Protein: 4g', 8);

-- Drinks
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Coca-Cola', 'Drink', 'Soft Drink', 'USA', 60.00, 'photo/coca cola.jpg', FALSE, 'Carbonated Water, Sugar, Caffeine', 'Calories: 140, Carbs: 39g, Protein: 0g', 1),
('Pepsi', 'Drink', 'Soft Drink', 'USA', 60.00, 'photo/pepsi.jpg', FALSE, 'Carbonated Water, Sugar, Caffeine', 'Calories: 150, Carbs: 41g, Protein: 0g', 1),
('Mirinda', 'Drink', 'Soft Drink', 'Spain', 65.00, 'photo/mirnda (2).jpg', FALSE, 'Carbonated Water, Sugar, Orange Flavor', 'Calories: 160, Carbs: 42g, Protein: 0g', 1),
('Tea', 'Drink', 'Hot Drink', 'Ethiopia', 40.00, 'photo/tea.jpg', FALSE, 'Water, Tea Leaves, Sugar', 'Calories: 50, Carbs: 12g, Protein: 0g', 3),
('Macchiato', 'Drink', 'Hot Drink', 'Italy', 70.00, 'photo/macchiato.jpg', FALSE, 'Espresso, Milk Foam', 'Calories: 100, Carbs: 8g, Protein: 5g', 5),
('Coffee', 'Drink', 'Hot Drink', 'Ethiopia', 50.00, 'photo/Coffee.jpg', FALSE, 'Coffee Beans, Water', 'Calories: 5, Carbs: 0g, Protein: 1g', 5),
('Orange Juice', 'Drink', 'Juice', 'USA', 120.00, 'https://images.unsplash.com/photo-1613478223719-2ab802602423?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Oranges, Water, Sugar', 'Calories: 120, Carbs: 28g, Protein: 2g', 3),
('Mango Juice', 'Drink', 'Juice', 'India', 130.00, 'photo/mango.jpg', FALSE, 'Mangoes, Water, Sugar', 'Calories: 130, Carbs: 30g, Protein: 1g', 3),
('Avocado Juice', 'Drink', 'Juice', 'Mexico', 150.00, 'photo/avocado.jpg', FALSE, 'Avocado, Milk, Sugar', 'Calories: 250, Carbs: 20g, Protein: 5g', 4);

-- Alcohol
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Walia Beer', 'Drink', 'Alcohol', 'Ethiopia', 85.00, 'photo/walia bera.jpg', FALSE, 'Barley, Hops, Water, Yeast', 'Calories: 150, Carbs: 13g, Protein: 1g', 1),
('Habesha Beer', 'Drink', 'Alcohol', 'Ethiopia', 90.00, 'photo/habash beera.jpg', FALSE, 'Barley, Hops, Water, Yeast', 'Calories: 160, Carbs: 14g, Protein: 1g', 1),
('Tej (Honey Wine)', 'Drink', 'Alcohol', 'Ethiopia', 150.00, 'photo/tej.jpg', FALSE, 'Honey, Water, Gesho', 'Calories: 200, Carbs: 25g, Protein: 0g', 2),
('Gouder Wine', 'Drink', 'Alcohol', 'Ethiopia', 500.00, 'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Grapes, Yeast', 'Calories: 125, Carbs: 4g, Protein: 0g', 2);

-- Desserts
INSERT INTO food_items (name, category, type, country, price, image_url, is_fasting, ingredients, nutritional_value, preparation_time) VALUES
('Ice Cream', 'Dessert', 'Dessert', 'Italy', 150.00, 'https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Milk, Cream, Sugar, Flavoring', 'Calories: 200, Carbs: 25g, Protein: 4g', 2),
('Chocolate Cake', 'Dessert', 'Cake', 'Germany', 200.00, 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Flour, Sugar, Cocoa, Eggs, Butter', 'Calories: 350, Carbs: 50g, Protein: 5g', 20),
('Tiramisu', 'Dessert', 'Dessert', 'Italy', 250.00, 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60', FALSE, 'Mascarpone, Espresso, Ladyfingers, Cocoa', 'Calories: 400, Carbs: 40g, Protein: 8g', 15);