



package com.aimodel.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;

/**
 *
 * @author Aaviskar
 */

public class Navbar extends HBox {

   // UI Components
   private ImageView logo;
   private TextField searchField;
   private ComboBox<String> sortByComboBox;

   // Modern UI Constants
   private static final String MODERN_FONT = "SF Pro Display, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Open Sans, Helvetica Neue, sans-serif";
   private static final String PRIMARY_COLOR = "#1a73e8";
   private static final String TEXT_SECONDARY = "#5f6368";
   private static final String BORDER_COLOR = "#dadce0";

   public interface NavigationListener {
       void onHomeClicked();
       void onAboutUsClicked();
       void onDashboardClicked();
       void onViewmodelsClicked();
       void onLogoutClicked();
   }

   private final NavigationListener navigationListener;

   public Navbar(NavigationListener navigationListener) {
       this.navigationListener = navigationListener;
       setupUI();
   }

   private void setupUI() {
       setPadding(new Insets(10, 20, 10, 20));
       setAlignment(Pos.CENTER_LEFT);
       setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");
       setSpacing(20);

       // Logo
       logo = new ImageView(new Image(getClass().getResource("/com/aimodel/resources/brandlogo.png").toExternalForm()));
       logo.setFitHeight(40);
       logo.setPreserveRatio(true);

       // Nav buttons
       String[] navItems = {"Home", "About Us", "Dashboard", "View Models", "Logout"};
       for (String item : navItems) {
           Button navButton = createNavButton(item);
           getChildren().add(navButton);
       }

       Region spacer = new Region();
       HBox.setHgrow(spacer, Priority.ALWAYS);

       // Search field
       searchField = new TextField();
       searchField.setPromptText("Quick action...");
       searchField.setPrefWidth(100);
       searchField.setStyle(
               "-fx-background-color: #f1f3f4;" +
                       "-fx-background-radius: 20;" +
                       "-fx-padding: 8 16;" +
                       "-fx-font-family: '" + MODERN_FONT + "';"
       );

       // Sort by dropdown
       sortByComboBox = new ComboBox<>(FXCollections.observableArrayList("Sort By", "Name", "Latency", "Cost"));
       sortByComboBox.getSelectionModel().selectFirst();
       sortByComboBox.setStyle(
               "-fx-background-color: white;" +
                       "-fx-border-color: " + BORDER_COLOR + ";" +
                       "-fx-border-radius: 4;" +
                       "-fx-padding: 4 8;" +
                       "-fx-font-family: '" + MODERN_FONT + "';"
       );

       getChildren().addAll(logo, spacer, searchField, sortByComboBox);
   }

   private Button createNavButton(String text) {
       Button button = new Button(text);
       button.setStyle(
               "-fx-background-color: transparent;" +
                       "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                       "-fx-font-family: '" + MODERN_FONT + "';" +
                       "-fx-font-size: 14px;" +
                       "-fx-cursor: hand;"
       );

       button.setOnMouseEntered(e -> button.setStyle(
               "-fx-background-color: rgba(26,115,232,0.08);" +
                       "-fx-text-fill: " + PRIMARY_COLOR + ";" +
                       "-fx-font-family: '" + MODERN_FONT + "';" +
                       "-fx-font-size: 14px;" +
                       "-fx-cursor: hand;"
       ));

       button.setOnMouseExited(e -> button.setStyle(
               "-fx-background-color: transparent;" +
                       "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                       "-fx-font-family: '" + MODERN_FONT + "';" +
                       "-fx-font-size: 14px;" +
                       "-fx-cursor: hand;"
       ));

       // Add click handlers
       button.setOnAction(e -> {
           if (navigationListener != null) {
               switch (text) {
                   case "Home" -> navigationListener.onHomeClicked();
                   case "About Us" -> navigationListener.onAboutUsClicked();
                   case "Dashboard" -> navigationListener.onDashboardClicked();
                   case "View Models" -> navigationListener.onViewmodelsClicked();
                   case "Logout" -> navigationListener.onLogoutClicked();
               }
           }
       });

       return button;
   }
}


