package com.example.foodorderingjdbcproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class HelloController {
    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private StackPane rootPane;

    @FXML
    private MediaView chefAnimationView;

    private double hue = 0;

    @FXML
    public void initialize() {
        try {
            // Set the main background image
            Image image = new Image("https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80");
            backgroundImage.setImage(image);
            
            // Ensure the background covers the whole area
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

            // --- Chef Animation Logic ---
            String videoPath = "photo/chef-animation.gif.mp4";
            File videoFile = new File(videoPath);

            if (videoFile.exists()) {
                Media media = new Media(videoFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                chefAnimationView.setMediaPlayer(mediaPlayer);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            } else {
                System.err.println("Animation file not found at path: " + videoFile.getAbsolutePath());
            }


            addGlowingBorderAnimation();

        } catch (Exception e) {
            System.err.println("Could not load resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addGlowingBorderAnimation() {
        Rectangle border = new Rectangle();
        border.setFill(Color.TRANSPARENT);
        border.setStrokeWidth(4);


        border.setMouseTransparent(true);


        border.widthProperty().bind(rootPane.widthProperty());
        border.heightProperty().bind(rootPane.heightProperty());


        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(16), e -> {

                hue = (hue + 0.5) % 360;

                Color color = Color.hsb(hue, 1.0, 1.0, 0.9); // Saturation, Brightness, Opacity


                border.setStroke(color);
            })
        );
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        rootPane.getChildren().add(border);
    }

    @FXML
    protected void onOrderNowClick(ActionEvent event) throws IOException {
        loadMenuScene(event);
    }

    private void loadMenuScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 750);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
}