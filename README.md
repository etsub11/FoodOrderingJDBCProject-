# Food Ordering JDBC Project

Simple JavaFX desktop app that demonstrates a food ordering UI with MySQL persistence.

## What it is

- A JavaFX client that shows a menu, allows adding items to an order, selecting order type (Dine-In / Take-Away), and placing orders.
- Orders are saved to a MySQL database (orders + order_items). Sample DB schema and seed data are in [database_setup.sql](database_setup.sql).

## Key files

- App entry: [`com.example.foodorderingjdbcproject.HelloApplication`](src/main/java/com/example/foodorderingjdbcproject/HelloApplication.java) — [file](src/main/java/com/example/foodorderingjdbcproject/HelloApplication.java)
- Menu UI / logic: [`com.example.foodorderingjdbcproject.MenuController`](src/main/java/com/example/foodorderingjdbcproject/MenuController.java) — [file](src/main/java/com/example/foodorderingjdbcproject/MenuController.java)
- Order status UI / logic: [`com.example.foodorderingjdbcproject.OrderStatusController`](src/main/java/com/example/foodorderingjdbcproject/OrderStatusController.java) — [file](src/main/java/com/example/foodorderingjdbcproject/OrderStatusController.java)
- Database access: [`com.example.foodorderingjdbcproject.DatabaseHandler`](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java) — [file](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java)
- Domain model: [`com.example.foodorderingjdbcproject.FoodItem`](src/main/java/com/example/foodorderingjdbcproject/FoodItem.java) — [file](src/main/java/com/example/foodorderingjdbcproject/FoodItem.java)
- Date helper shown on order screen: [`com.example.foodorderingjdbcproject.EthiopianDateConverter`](src/main/java/com/example/foodorderingjdbcproject/EthiopianDateConverter.java) — [file](src/main/java/com/example/foodorderingjdbcproject/EthiopianDateConverter.java)
- FXML views and styles:
  - [com/example/foodorderingjdbcproject/hello-view.fxml](com/example/foodorderingjdbcproject/hello-view.fxml)
  - [com/example/foodorderingjdbcproject/menu-view.fxml](com/example/foodorderingjdbcproject/menu-view.fxml)
  - [com/example/foodorderingjdbcproject/order-status-view.fxml](com/example/foodorderingjdbcproject/order-status-view.fxml)
  - [com/example/foodorderingjdbcproject/styles.css](com/example/foodorderingjdbcproject/styles.css)

## How it works (high level)

1. App starts at [`HelloApplication`](src/main/java/com/example/foodorderingjdbcproject/HelloApplication.java) and loads the welcome FXML.
2. User navigates to the menu (FXML + [`MenuController`](src/main/java/com/example/foodorderingjdbcproject/MenuController.java)). Food items are loaded from the DB via [`DatabaseHandler.getAllFoodItems()`](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java) or fallback sample data.
3. User builds an order (in-memory list of `MenuController.OrderItem`). On "Place Order", [`DatabaseHandler.saveOrder(...)`](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java) stores the order and items in MySQL.
4. After placing an order, [`OrderStatusController`](src/main/java/com/example/foodorderingjdbcproject/OrderStatusController.java) shows an estimated countdown and a formatted date via [`EthiopianDateConverter`](src/main/java/com/example/foodorderingjdbcproject/EthiopianDateConverter.java).

## Setup & run

1. Install JDK 17+ and MySQL.
2. Create DB and tables:  
   mysql -u root -p < database_setup.sql  
   (Or run the SQL with your DB tool.) See [database_setup.sql](database_setup.sql).
3. Update DB credentials if needed in [`DatabaseHandler`](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java) or replace with a safer configuration.
4. Build & run:
   - Using the wrapper: ./mvnw clean javafx:run
   - Or with Maven: mvn clean javafx:run  
     Project Maven config: [pom.xml](pom.xml)

## Notes

- Media files and local images are referenced under `photo/` — verify paths if media fails to load.
- UI FXML resources are packaged with the app (see the listed FXML files).
- The DB code uses basic JDBC transactions; enhance error handling and connection management for production.

## Contribution / debugging

- To debug UI flows, open: [`MenuController`](src/main/java/com/example/foodorderingjdbcproject/MenuController.java) and the FXML it controls: [menu-view.fxml](com/example/foodorderingjdbcproject/menu-view.fxml).
- To inspect/save order logic: see [`DatabaseHandler.saveOrder`](src/main/java/com/example/foodorderingjdbcproject/DatabaseHandler.java).
