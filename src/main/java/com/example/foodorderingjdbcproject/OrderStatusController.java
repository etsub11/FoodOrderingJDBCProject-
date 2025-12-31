package com.example.foodorderingjdbcproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderStatusController {

    @FXML
    private Label orderIdLabel;
    @FXML
    private Label orderSummaryLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label prepTimeLabel;
    @FXML
    private Label ethiopianDateLabel;

    private Timeline countdownTimeline;

    public void setOrderDetails(String orderGroupId, List<MenuController.OrderItem> orderItems, double totalPrice, int prepTimeInMinutes) {
        orderIdLabel.setText("Order ID: " + orderGroupId);

        StringBuilder summary = new StringBuilder("Your Order:\n");
        for (MenuController.OrderItem item : orderItems) {
            summary.append(String.format("- %s x%d\n", item.foodItem.getName(), item.quantity));
        }
        orderSummaryLabel.setText(summary.toString());
        totalPriceLabel.setText(String.format("Total: %.2f ETB", totalPrice));
        ethiopianDateLabel.setText("Ordered on: " + EthiopianDateConverter.formatToEthiopian(LocalDateTime.now()));

        // --- Countdown Timer ---
        startCountdown(prepTimeInMinutes);

        // Status change timeline
        Timeline statusTimeline = new Timeline(
            new KeyFrame(Duration.minutes(prepTimeInMinutes), e -> {
                statusLabel.setText("Ready");
                statusLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #43a047;"); // Green color
                if (countdownTimeline != null) {
                    countdownTimeline.stop();
                }
                prepTimeLabel.setText("Your order is ready!");
            })
        );
        statusTimeline.play();
    }

    private void startCountdown(int minutes) {
        long totalSeconds = minutes * 60;
        final AtomicLong remainingSeconds = new AtomicLong(totalSeconds);

        // Set the initial static text
        String initialText = "Estimated Time: " + minutes + " minutes ";
        
        countdownTimeline = new Timeline();
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), e -> {
                    long seconds = remainingSeconds.getAndDecrement();
                    if (seconds < 0) {
                        countdownTimeline.stop();
                        return;
                    }
                    long mins = seconds / 60;
                    long secs = seconds % 60;
                    // Append the countdown to the initial text
                    prepTimeLabel.setText(initialText + String.format("(%02d:%02d)", mins, secs));
                })
        );
        countdownTimeline.playFromStart();
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 750);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
}