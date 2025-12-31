package com.example.foodorderingjdbcproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuController {

    @FXML
    private FlowPane foodContainer;
    @FXML
    private TextField searchField;
    @FXML
    private VBox orderList;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private ToggleGroup dietaryGroup;
    @FXML
    private ToggleGroup categoryGroup;
    @FXML
    private ToggleGroup orderTypeGroup;
    @FXML
    private VBox tableNumberBox;
    @FXML
    private TextField tableNumberField;
    @FXML
    private MediaView menuChefAnimationView;

    private List<FoodItem> allFoodItems = new ArrayList<>();
    private List<OrderItem> currentOrder = new ArrayList<>();
    private double totalOrderPrice = 0.0;
    private int totalOrderQuantity = 0;

    public static class OrderItem {
        FoodItem foodItem;
        int quantity;

        public OrderItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }
    }

    @FXML
    public void initialize() {
        DatabaseHandler.initializeDatabase();
        loadFoodDataFromDB();
        
        if (allFoodItems.isEmpty()) {
            System.out.println("Database is empty. Loading fallback data.");
            loadFoodData();
        }
        
        setInitialFilter();
        searchField.textProperty().addListener((obs, old, val) -> updateDisplayedFood());
        
        orderTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selected = (ToggleButton) newValue;
                boolean isDineIn = "Dine-In".equals(selected.getText());
                tableNumberBox.setVisible(isDineIn);
                tableNumberBox.setManaged(isDineIn);
            }
        });

        ToggleButton selected = (ToggleButton) orderTypeGroup.getSelectedToggle();
        if (selected != null && "Dine-In".equals(selected.getText())) {
            tableNumberBox.setVisible(true);
            tableNumberBox.setManaged(true);
        } else {
            tableNumberBox.setVisible(false);
            tableNumberBox.setManaged(false);
        }

        // --- Chef Animation Logic for Menu Screen ---
        String videoPath = "photo/chef-animation.gif.mp4";
        File videoFile = new File(videoPath);

        if (videoFile.exists()) {
            Media media = new Media(videoFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            menuChefAnimationView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } else {
            System.err.println("Animation file not found at path: " + videoFile.getAbsolutePath());
        }
    }

    private void loadFoodDataFromDB() {
        allFoodItems = DatabaseHandler.getAllFoodItems();
    }

    private void loadFoodData() {
        // Breakfast
        allFoodItems.add(new FoodItem(1, "Pancakes", "Breakfast", "Pastry", "USA", 300.00, new File("photo/Pancakes.jpg").toURI().toString(), false, "Flour, Milk, Eggs, Sugar, Butter", "Calories: 350, Carbs: 45g, Protein: 8g", 15));
        allFoodItems.add(new FoodItem(2, "Omelette", "Breakfast", "Egg", "France", 350.00, "https://images.unsplash.com/photo-1510693206972-df098062cb71?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Eggs, Cheese, Milk, Butter, Salt, Pepper", "Calories: 250, Carbs: 2g, Protein: 18g", 10));
        allFoodItems.add(new FoodItem(3, "Chechebsa", "Breakfast", "Traditional", "Ethiopia", 250.00, new File("photo/chechabsa.jpg").toURI().toString(), false, "Flatbread (Kita), Berbere, Niter Kibbeh (Spiced Butter), Honey", "Calories: 400, Carbs: 55g, Protein: 6g", 12));
        allFoodItems.add(new FoodItem(4, "Ful", "Breakfast", "Bean", "Ethiopia", 200.00, new File("photo/ful.jpg").toURI().toString(), true, "Fava Beans, Onion, Tomato, Green Chili, Oil, Spices", "Calories: 300, Carbs: 40g, Protein: 15g", 10));

        // Lunch
        allFoodItems.add(new FoodItem(5, "Burger", "Lunch", "Fast Food", "USA", 450.00, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Beef Patty, Bun, Lettuce, Tomato, Cheese, Sauce", "Calories: 600, Carbs: 50g, Protein: 30g", 15));
        allFoodItems.add(new FoodItem(6, "Salad", "Lunch", "Healthy", "Greece", 380.00, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", true, "Lettuce, Cucumber, Tomato, Olives, Feta Cheese, Olive Oil", "Calories: 200, Carbs: 15g, Protein: 5g", 8));
        allFoodItems.add(new FoodItem(7, "Doro Wat", "Lunch", "Traditional", "Ethiopia", 600.00, new File("photo/Doro Wat.jpg").toURI().toString(), false, "Chicken, Onion, Berbere, Niter Kibbeh, Eggs, Garlic, Ginger", "Calories: 500, Carbs: 20g, Protein: 40g", 30));
        allFoodItems.add(new FoodItem(8, "Macaroni", "Lunch", "Italian", "Italy", 150.00, new File("photo/Macaroni.jpg").toURI().toString(), true, "Macaroni, Tomato Sauce, Garlic, Basil, Olive Oil", "Calories: 450, Carbs: 80g, Protein: 12g", 20));

        // Dinner
        allFoodItems.add(new FoodItem(9, "Pizza", "Dinner", "Italian", "Italy", 650.00, "https://images.unsplash.com/photo-1513104890138-7c749659a591?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Dough, Tomato Sauce, Mozzarella Cheese, Basil", "Calories: 800, Carbs: 90g, Protein: 35g", 25));
        allFoodItems.add(new FoodItem(10, "Sushi", "Dinner", "Japanese", "Japan", 750.00, "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Rice, Nori (Seaweed), Fresh Fish (Salmon/Tuna), Vegetables", "Calories: 400, Carbs: 60g, Protein: 20g", 20));
        allFoodItems.add(new FoodItem(11, "Kitfo", "Dinner", "Traditional", "Ethiopia", 550.00, new File("photo/kitfo.jpg").toURI().toString(), false, "Minced Beef, Mitmita, Niter Kibbeh, Ayibe (Cottage Cheese), Gomen", "Calories: 450, Carbs: 5g, Protein: 45g", 20));
        allFoodItems.add(new FoodItem(12, "Steak", "Dinner", "Meat", "USA", 800.00, "https://images.unsplash.com/photo-1600891964092-4316c288032e?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Beef Steak, Salt, Pepper, Butter, Herbs", "Calories: 700, Carbs: 0g, Protein: 60g", 25));

        // Snack
        allFoodItems.add(new FoodItem(13, "Sandwich", "Snack", "Fast Food", "UK", 220.00, new File("photo/sandwich.jpg").toURI().toString(), false, "Bread, Ham, Cheese, Lettuce, Tomato, Mayo", "Calories: 350, Carbs: 40g, Protein: 15g", 5));
        allFoodItems.add(new FoodItem(14, "Samosa", "Snack", "Pastry", "India", 50.00, "https://images.unsplash.com/photo-1601050690597-df0568f70950?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", true, "Flour, Potato, Peas, Spices, Oil", "Calories: 150, Carbs: 20g, Protein: 3g", 10));
        allFoodItems.add(new FoodItem(15, "Fries", "Snack", "Fast Food", "Belgium", 150.00, new File("photo/fries.png").toURI().toString(), false, "Potatoes, Oil, Salt", "Calories: 300, Carbs: 45g, Protein: 4g", 8));

        // Drinks
        allFoodItems.add(new FoodItem(16, "Coca-Cola", "Drink", "Soft Drink", "USA", 60.00, new File("photo/coca cola.jpg").toURI().toString(), false, "Carbonated Water, Sugar, Caramel Color, Phosphoric Acid, Caffeine", "Calories: 140, Carbs: 39g, Protein: 0g", 1));
        allFoodItems.add(new FoodItem(17, "Pepsi", "Drink", "Soft Drink", "USA", 60.00, new File("photo/pepsi.jpg").toURI().toString(), false, "Carbonated Water, Sugar, Caramel Color, Phosphoric Acid, Caffeine", "Calories: 150, Carbs: 41g, Protein: 0g", 1));
        allFoodItems.add(new FoodItem(18, "Mirinda", "Drink", "Soft Drink", "Spain", 65.00, new File("photo/mirnda (2).jpg").toURI().toString(), false, "Carbonated Water, Sugar, Orange Flavor, Citric Acid", "Calories: 160, Carbs: 42g, Protein: 0g", 1));
        allFoodItems.add(new FoodItem(19, "Tea", "Drink", "Hot Drink", "Ethiopia", 40.00, new File("photo/tea.jpg").toURI().toString(), false, "Tea Leaves, Water, Sugar, Spices (optional)", "Calories: 50, Carbs: 12g, Protein: 0g", 3));
        allFoodItems.add(new FoodItem(20, "Macchiato", "Drink", "Hot Drink", "Italy", 70.00, new File("photo/macchiato.jpg").toURI().toString(), false, "Espresso, Steamed Milk, Foam", "Calories: 100, Carbs: 8g, Protein: 5g", 5));
        allFoodItems.add(new FoodItem(21, "Orange Juice", "Drink", "Juice", "USA", 120.00, "https://images.unsplash.com/photo-1613478223719-2ab802602423?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Fresh Oranges, Sugar (optional)", "Calories: 120, Carbs: 28g, Protein: 2g", 3));
        allFoodItems.add(new FoodItem(22, "Mango Juice", "Drink", "Juice", "India", 130.00, new File("photo/mango.jpg").toURI().toString(), false, "Fresh Mangoes, Water, Sugar", "Calories: 130, Carbs: 30g, Protein: 1g", 3));
        allFoodItems.add(new FoodItem(23, "Avocado Juice", "Drink", "Juice", "Mexico", 150.00, new File("photo/avocado.jpg").toURI().toString(), false, "Avocado, Milk, Sugar, Lime (optional)", "Calories: 250, Carbs: 20g, Protein: 5g", 4));
        allFoodItems.add(new FoodItem(31, "Coffee", "Drink", "Hot Drink", "Ethiopia", 50.00, new File("photo/Coffee.jpg").toURI().toString(), false, "Coffee Beans, Water", "Calories: 5, Carbs: 0g, Protein: 1g", 5));

        // Alcohol
        allFoodItems.add(new FoodItem(24, "Walia Beer", "Drink", "Alcohol", "Ethiopia", 85.00, new File("photo/walia bera.jpg").toURI().toString(), false, "Barley Malt, Hops, Water, Yeast", "Calories: 150, Carbs: 13g, Protein: 1g", 1));
        allFoodItems.add(new FoodItem(25, "Habesha Beer", "Drink", "Alcohol", "Ethiopia", 90.00, new File("photo/habash beera.jpg").toURI().toString(), false, "Barley Malt, Hops, Water, Yeast", "Calories: 160, Carbs: 14g, Protein: 1g", 1));
        allFoodItems.add(new FoodItem(26, "Tej (Honey Wine)", "Drink", "Alcohol", "Ethiopia", 150.00, new File("photo/tej.jpg").toURI().toString(), false, "Honey, Water, Gesho (Rhamnus prinoides)", "Calories: 200, Carbs: 25g, Protein: 0g", 2));
        allFoodItems.add(new FoodItem(27, "Gouder Wine", "Drink", "Alcohol", "Ethiopia", 500.00, "https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Grapes, Yeast", "Calories: 125, Carbs: 4g, Protein: 0g", 2));

        // Desserts
        allFoodItems.add(new FoodItem(28, "Ice Cream", "Dessert", "Dessert", "Italy", 150.00, "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Milk, Cream, Sugar, Flavoring", "Calories: 200, Carbs: 25g, Protein: 4g", 2));
        allFoodItems.add(new FoodItem(29, "Chocolate Cake", "Dessert", "Cake", "Germany", 200.00, "https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Flour, Sugar, Cocoa Powder, Eggs, Butter, Milk", "Calories: 350, Carbs: 50g, Protein: 5g", 20));
        allFoodItems.add(new FoodItem(30, "Tiramisu", "Dessert", "Dessert", "Italy", 250.00, "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", false, "Ladyfingers, Mascarpone Cheese, Espresso, Cocoa Powder, Eggs, Sugar", "Calories: 400, Carbs: 40g, Protein: 8g", 15));
    }
    
    private void setInitialFilter() {
        LocalTime now = LocalTime.now();
        String timeCategory;

        if (now.isBefore(LocalTime.of(11, 0))) {
            timeCategory = "Breakfast";
        } else if (now.isBefore(LocalTime.of(16, 0))) {
            timeCategory = "Lunch";
        } else if (now.isBefore(LocalTime.of(22, 0))) {
            timeCategory = "Dinner";
        } else {
            timeCategory = "Snack";
        }

        for (Toggle toggle : categoryGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            if (button.getText().equalsIgnoreCase(timeCategory)) {
                button.setSelected(true);
                break;
            }
        }
        updateDisplayedFood();
    }

    private void displayFood(List<FoodItem> items) {
        foodContainer.getChildren().clear();
        for (FoodItem item : items) {
            VBox card = createFoodCard(item);
            foodContainer.getChildren().add(card);
        }
    }

    private VBox createFoodCard(FoodItem item) {
        VBox card = new VBox(10);
        card.getStyleClass().add("food-card");
        card.setPrefWidth(220);
        card.setAlignment(Pos.CENTER);

        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showFoodDetails(item);
            }
        });

        ImageView imageView = new ImageView();
        try {
             Image image = new Image(item.getImageUrl(), 200, 140, true, true, true);
             imageView.setImage(image);
             
             image.errorProperty().addListener((obs, old, isError) -> {
                 if (isError) {
                     imageView.setImage(new Image("https://via.placeholder.com/200x140?text=No+Image"));
                 }
             });
        } catch (Exception e) {
            imageView.setImage(new Image("https://via.placeholder.com/200x140?text=No+Image"));
        }
        imageView.setFitWidth(200);
        imageView.setFitHeight(140);

        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label detailLabel = new Label(item.getType() + " | " + item.getCountry());
        detailLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Label ingredientsLabel = new Label();
        if (item.getIngredients() != null && !item.getIngredients().isEmpty()) {
            ingredientsLabel.setText("Ingredients: " + item.getIngredients());
            ingredientsLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 10px; -fx-wrap-text: true;");
            ingredientsLabel.setMaxWidth(200);
            
            Tooltip tooltip = new Tooltip(item.getIngredients());
            tooltip.setPrefWidth(200);
            tooltip.setWrapText(true);
            Tooltip.install(ingredientsLabel, tooltip);
        }

        Label nutritionLabel = new Label();
        if (item.getNutritionalValue() != null && !item.getNutritionalValue().isEmpty()) {
            nutritionLabel.setText("Nutrition: " + item.getNutritionalValue());
            nutritionLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 10px; -fx-wrap-text: true;");
            nutritionLabel.setMaxWidth(200);
            
            Tooltip tooltip = new Tooltip(item.getNutritionalValue());
            tooltip.setPrefWidth(200);
            tooltip.setWrapText(true);
            Tooltip.install(nutritionLabel, tooltip);
        }

        Label priceLabel = new Label(String.format("%.2f ETB", item.getPrice()));
        priceLabel.getStyleClass().add("price-label");

        Button addButton = new Button("Add to Order");
        addButton.getStyleClass().add("button");
        addButton.setOnAction(e -> addToOrder(item));

        card.getChildren().addAll(imageView, nameLabel, detailLabel, ingredientsLabel, nutritionLabel, priceLabel, addButton);
        return card;
    }

    private void showFoodDetails(FoodItem item) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle(item.getName() + " Details");
        try {
            detailStage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/1830/1830839.png"));
        } catch (Exception e) {
            System.out.println("Could not load icon for details window.");
        }

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        ImageView mainImageView = new ImageView();
        try {
            Image mainImage = new Image(item.getImageUrl());
            mainImageView.setImage(mainImage);
            mainImageView.setPreserveRatio(true);
            mainImageView.setFitWidth(350);
            mainImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-background-radius: 10;");
        } catch (Exception e) {
            mainImageView.setImage(new Image("https://via.placeholder.com/350x233?text=No+Image"));
        }

        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 28));
        nameLabel.setStyle("-fx-text-fill: #333;");

        Label priceLabel = new Label(String.format("%.2f ETB", item.getPrice()));
        priceLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 22));
        priceLabel.setStyle("-fx-text-fill: #ef6c00;");

        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.CENTER_LEFT);

        Label ingredientsHeader = new Label("Ingredients");
        ingredientsHeader.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        Label ingredientsText = new Label(item.getIngredients());
        ingredientsText.setWrapText(true);
        ingredientsText.setFont(Font.font("Poppins", 14));

        Label nutritionHeader = new Label("Nutritional Value");
        nutritionHeader.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        Label nutritionText = new Label(item.getNutritionalValue());
        nutritionText.setWrapText(true);
        nutritionText.setFont(Font.font("Poppins", 14));

        detailsBox.getChildren().addAll(ingredientsHeader, ingredientsText, new Separator(), nutritionHeader, nutritionText);

        layout.getChildren().addAll(mainImageView, nameLabel, priceLabel, detailsBox);

        Scene scene = new Scene(layout, 400, 650);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private void addToOrder(FoodItem item) {
        boolean found = false;
        for (OrderItem orderItem : currentOrder) {
            if (orderItem.foodItem.getId() == item.getId()) {
                orderItem.quantity++;
                found = true;
                break;
            }
        }
        
        if (!found) {
            currentOrder.add(new OrderItem(item, 1));
        }
        
        totalOrderPrice += item.getPrice();
        totalOrderQuantity++;
        updateOrderDisplay();
    }

    private void removeFromOrder(OrderItem orderItem) {
        if (orderItem.quantity > 1) {
            orderItem.quantity--;
        } else {
            currentOrder.remove(orderItem);
        }
        
        totalOrderPrice -= orderItem.foodItem.getPrice();
        totalOrderQuantity--;
        updateOrderDisplay();
    }

    private void updateOrderDisplay() {
        orderList.getChildren().clear();
        for (OrderItem orderItem : currentOrder) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            
            ImageView itemImageView = new ImageView();
            try {
                Image itemImage = new Image(orderItem.foodItem.getImageUrl(), 40, 40, true, true);
                itemImageView.setImage(itemImage);
            } catch (Exception e) {
                itemImageView.setImage(new Image("https://via.placeholder.com/40x40?text=N/A"));
            }
            
            Label name = new Label(orderItem.foodItem.getName() + " x" + orderItem.quantity);
            name.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(name, Priority.ALWAYS);
            
            Label price = new Label(String.format("%.2f ETB", orderItem.foodItem.getPrice() * orderItem.quantity));
            price.setStyle("-fx-font-weight: bold;");

            Button removeButton = new Button("x");
            removeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2 5; -fx-background-radius: 10;");
            removeButton.setOnAction(e -> removeFromOrder(orderItem));

            row.getChildren().addAll(itemImageView, name, price, removeButton);
            orderList.getChildren().add(row);
        }
        
        totalPriceLabel.setText(String.format("Total (%d items): %.2f ETB", totalOrderQuantity, totalOrderPrice));
    }

    @FXML
    private void updateDisplayedFood() {
        Stream<FoodItem> stream = allFoodItems.stream();

        String searchQuery = searchField.getText().toLowerCase();
        if (!searchQuery.isEmpty()) {
            stream = stream.filter(item -> item.getName().toLowerCase().contains(searchQuery) || item.getCountry().toLowerCase().contains(searchQuery));
        }

        ToggleButton selectedCategory = (ToggleButton) categoryGroup.getSelectedToggle();
        if (selectedCategory != null && !"All".equals(selectedCategory.getText())) {
            String categoryFilter = selectedCategory.getText();
            stream = stream.filter(item -> categoryFilter.equalsIgnoreCase(item.getCategory()) || categoryFilter.equalsIgnoreCase(item.getType()));
        }

        ToggleButton selectedDietary = (ToggleButton) dietaryGroup.getSelectedToggle();
        if (selectedDietary != null && !"Any".equals(selectedDietary.getText())) {
            String dietaryFilter = selectedDietary.getText();
            if ("Fasting".equals(dietaryFilter)) {
                stream = stream.filter(FoodItem::isFasting);
            } else if ("Non-Fasting".equals(dietaryFilter)) {
                stream = stream.filter(item -> !item.isFasting());
            }
        }

        displayFood(stream.collect(Collectors.toList()));
    }

    @FXML
    public void filterCategory() {
        updateDisplayedFood();
    }

    @FXML
    public void filterDietary() {
        updateDisplayedFood();
    }

    @FXML
    public void placeOrder(ActionEvent event) throws IOException {
        ToggleButton selectedOrderType = (ToggleButton) orderTypeGroup.getSelectedToggle();
        if (selectedOrderType == null) {
            showAlert(Alert.AlertType.WARNING, "Order Error", "Please select an order type (Dine-In or Take-Away).");
            return;
        }

        String orderType = selectedOrderType.getText();
        String tableNumber = tableNumberField.getText();

        if ("Dine-In".equals(orderType) && (tableNumber == null || tableNumber.trim().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Order Error", "Please enter a table number for Dine-In orders.");
            return;
        }

        if (currentOrder.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Order", "Please add items to your order first.");
            return;
        }

        String orderGroupId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        DatabaseHandler.saveOrder(orderGroupId, orderType, tableNumber, currentOrder, totalOrderPrice);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("order-status-view.fxml"));
        Parent root = loader.load();

        OrderStatusController controller = loader.getController();
        int prepTime = currentOrder.stream().mapToInt(item -> item.foodItem.getPreparationTime()).max().orElse(5);
        controller.setOrderDetails(orderGroupId, new ArrayList<>(currentOrder), totalOrderPrice, prepTime);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

        currentOrder.clear();
        totalOrderPrice = 0;
        totalOrderQuantity = 0;
        updateOrderDisplay();
        tableNumberField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onBackClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 750);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Food Ordering App");
        stage.setScene(scene);
        stage.show();
    }
}