



package com.aimodel.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.image.Image;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import java.util.Objects;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javafx.animation.Interpolator;

/**
 *
 * @author Aaviskar
 */

/**
 * FXLoginPane is a custom JavaFX StackPane that provides a login interface with a modern UI.
 * It includes features such as a background image, glassmorphism effect, and input validation.
 */
public class FXLoginPane extends StackPane {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Consumer<String> onLoginSuccess;

    /**
     * Constructor for FXLoginPane.
     *
     * @param onLoginSuccess A Consumer that accepts a String (user type) upon successful login.
     */
    public FXLoginPane(Consumer<String> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        setupUI();
    }

    /**
     * Sets up the user interface components and layout.
     */
    private void setupUI() {
        // Set the preferred size for the StackPane
        setPrefSize(1000, 600);
        setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // Load and set background image
        try {
            Image backgroundImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/com/aimodel/resources/loginpage.png")));
            BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1000, 600, false, false, false, false)
            );
            setBackground(new Background(background));
        } catch (Exception e) {
            setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        // Create main content VBox with glassmorphism effect
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(40));
        loginBox.setMaxWidth(450);
        loginBox.setMaxHeight(500);

        // Glassmorphism style
        loginBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-background-radius: 20; " +
            "-fx-border-radius: 20; " +
            "-fx-border-color: rgba(255, 255, 255, 0.3); " +
            "-fx-border-width: 1.5px;"
        );

        // Add glass effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setSpread(0.2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));

        BoxBlur blur = new BoxBlur();
        blur.setWidth(10);
        blur.setHeight(10);
        blur.setIterations(3);

        loginBox.setEffect(shadow);

        // Welcome text
        Label welcomeLabel = new Label("Welcome Back");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        // Message label for error/success messages
        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", 14));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setVisible(false);

        // Create label containers with proper spacing
        VBox usernameContainer = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        usernameLabel.setTextFill(Color.WHITE);
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        styleTextField(usernameField);
        usernameContainer.getChildren().addAll(usernameLabel, usernameField);

        VBox passwordContainer = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        passwordLabel.setTextFill(Color.WHITE);
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        styleTextField(passwordField);
        passwordContainer.getChildren().addAll(passwordLabel, passwordField);

        // Login button with glass effect
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        styleLoginButton(loginButton);
        loginButton.setPrefWidth(200);
        loginButton.setOnAction(e -> handleLogin());

        // Add all elements to the login box
        loginBox.getChildren().addAll(
            welcomeLabel,
            messageLabel,
            usernameContainer,
            passwordContainer,
            loginButton
        );

        // Center the login box in the stack pane
        setAlignment(Pos.CENTER);
        getChildren().add(loginBox);
    }

    /**
     * Styles the text fields with a modern look.
     *
     * @param field The TextField to be styled.
     */
    private void styleTextField(TextField field) {
        field.setFont(Font.font("Arial", 14));
        field.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1); " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
            "-fx-border-color: rgba(255, 255, 255, 0.2); " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 12; " +
            "-fx-border-width: 1px;"
        );
        field.setPrefWidth(300);
        field.setMaxWidth(300);

        // Add focus effect
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                field.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.15); " +
                    "-fx-text-fill: white; " +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
                    "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-padding: 12; " +
                    "-fx-border-width: 1px;"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.1); " +
                    "-fx-text-fill: white; " +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
                    "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-padding: 12; " +
                    "-fx-border-width: 1px;"
                );
            }
        });
    }

    /**
     * Styles the login button with a modern look.
     *
     * @param button The Button to be styled.
     */
    private void styleLoginButton(Button button) {
        String normalStyle =
            "-fx-background-color: rgba(115, 115, 222, 0.7); " +
            "-fx-text-fill: white; " +
            "-fx-padding: 12 30; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-border-color: rgba(255, 255, 255, 0.3); " +
            "-fx-border-width: 1px; " +
            "-fx-cursor: hand;";

        String hoverStyle =
            "-fx-background-color: rgba(115, 115, 222, 0.9); " +
            "-fx-text-fill: white; " +
            "-fx-padding: 12 30; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-border-color: rgba(255, 255, 255, 0.5); " +
            "-fx-border-width: 1px; " +
            "-fx-cursor: hand;";

        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    /**
     * Handles the login action. Validates the username and password and performs the login.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin")) {
            messageLabel.setTextFill(Color.rgb(150, 255, 150));
            messageLabel.setText("Login successful!");
            messageLabel.setVisible(true);
            if (onLoginSuccess != null) {
                onLoginSuccess.accept("admin");  // Pass user type to callback
            }
        } else {
            messageLabel.setTextFill(Color.rgb(255, 150, 150));
            messageLabel.setText("Invalid username or password");
            messageLabel.setVisible(true);
            shakeFields();
        }
    }

    /**
     * Applies a shake animation to the input fields when the login fails.
     */
    private void shakeFields() {
        // Shake animation for invalid login
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(usernameField.translateXProperty(), 0, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(100),
                new KeyValue(usernameField.translateXProperty(), -10, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(200),
                new KeyValue(usernameField.translateXProperty(), 10, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(300),
                new KeyValue(usernameField.translateXProperty(), 0, Interpolator.LINEAR))
        );
        timeline.play();

        Timeline timeline2 = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(passwordField.translateXProperty(), 0, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(100),
                new KeyValue(passwordField.translateXProperty(), -10, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(200),
                new KeyValue(passwordField.translateXProperty(), 10, Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(300),
                new KeyValue(passwordField.translateXProperty(), 0, Interpolator.LINEAR))
        );
        timeline2.play();
    }

    /**
     * Creates a new JavaFX Scene with this login pane.
     *
     * @return A new Scene object.
     */
    public Scene createScene() {
        Scene scene = new Scene(this, 1000, 600);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }
}