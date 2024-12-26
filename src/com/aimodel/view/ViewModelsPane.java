package com.aimodel.view;

import com.aimodel.model.AiModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.SVGPath;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;

/**
 *
 * @author Aaviskar
 */

/**
 * ViewModelsPane is a JavaFX BorderPane that displays a table of AI models.
 * It includes a navigation bar, a header, and a table view to show model data.
 * This class also listens to changes in the AiModel and updates the table accordingly.
 */
public class ViewModelsPane extends BorderPane implements AiModel.AiModelListener {
    private TableView<AiModel.ModelData> modelTable;
    private AiModel aiModel;
    private HomePage.NavigationHandler navHandler;
    private VBox sideNav;

    // Modern UI Constants
    private static final String FONT_FAMILY = "SF Pro Display, -apple-system, BlinkMacSystemFont, Segoe UI";
    
    // Updated color scheme
    private static final Color LIGHTEST_BLUE = Color.web("#E5F0FA");
    private static final Color LIGHT_BLUE = Color.web("#D1E8FC");
    private static final Color MID_BLUE = Color.web("#BDE0FE");
    private static final Color DEEP_BLUE = Color.web("#A2D2FF");
    private static final Color DARKEST_BLUE = Color.web("#8FC9FF");
    
    private static final Color TEXT_COLOR = Color.web("#2C3E50");
    private static final Color HOVER_COLOR = Color.web("#A2D2FF");

    /**
     * Constructor for ViewModelsPane.
     *
     * @param aiModel    The AiModel instance containing the data.
     * @param navHandler The NavigationHandler for handling navigation actions.
     */
    public ViewModelsPane(AiModel aiModel, HomePage.NavigationHandler navHandler) {
        this.aiModel = aiModel;
        this.navHandler = navHandler;
        aiModel.addListener(this); // Register ViewModelsPane as a listener
        initializeUI();
    }

    /**
     * Called when the model data changes. Refreshes the table view.
     */
    @Override
    public void onModelDataChanged() {
        Platform.runLater(() -> {
            refreshModels();
        });
    }
    
    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        // Create main background gradient
        Stop[] stops = new Stop[]{
            new Stop(0, LIGHTEST_BLUE),
            new Stop(0.3, LIGHT_BLUE),
            new Stop(0.6, MID_BLUE),
            new Stop(0.8, DEEP_BLUE),
            new Stop(1, DARKEST_BLUE)
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        
        // Apply gradient to background
        setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Create content area with frosted glass effect
        createModelTable();
        HBox header = createHeader();
        sideNav = createViewModelsNavBar();

        // Main content container with glass effect
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().addAll(header, modelTable);
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Layout setup
        setCenter(content);
        setLeft(sideNav);
        setPadding(new Insets(20));
    }

    /**
     * Creates the navigation bar for the ViewModelsPane.
     *
     * @return A VBox containing the navigation bar.
     */
    private VBox createViewModelsNavBar() {
        VBox navBar = new VBox(20);
        navBar.setPrefWidth(150);
        navBar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); " +
                       "-fx-background-radius: 20; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        navBar.setPadding(new Insets(30, 20, 30, 20));

        VBox navItems = new VBox(8);
        navItems.setPadding(new Insets(40, 0, 0, 0));

        Button homeBtn = createNavButton("Home", "M8 20H4a2 2 0 01-2-2V9a2 2 0 012-2h1");
        Button aboutUsBtn = createNavButton("About Us", "M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z");
        Button viewModelsBtn = createNavButton("View Models", "M4 6h16M4 12h8m-8 6h16");
        Button adminBtn = createNavButton("Admin", "M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z");

        homeBtn.setOnAction(e -> navHandler.navigateTo("main"));
        aboutUsBtn.setOnAction(e -> navHandler.navigateTo("aboutUs"));
        viewModelsBtn.setOnAction(e -> navHandler.navigateTo("viewModels"));
        adminBtn.setOnAction(e -> navHandler.navigateTo("login"));

        navItems.getChildren().addAll(homeBtn, aboutUsBtn, viewModelsBtn, adminBtn);
        navBar.getChildren().add(navItems);

        return navBar;
    }

    /**
     * Creates a navigation button with the specified text and SVG icon.
     *
     * @param text     The text to display on the button.
     * @param svgPath  The SVG path for the button icon.
     * @return         A styled Button for navigation.
     */
    private Button createNavButton(String text, String svgPath) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPadding(new Insets(12, 16, 12, 16));

        SVGPath icon = new SVGPath();
        icon.setContent(svgPath);
        icon.setFill(TEXT_COLOR);

        btn.setGraphic(icon);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(12);

        String defaultStyle = String.format(
                "-fx-background-color: rgba(255, 255, 255, 0.3); " +
                "-fx-text-fill: #2C3E50; " +
                "-fx-font-family: '%s'; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 8;",
                FONT_FAMILY
        );

        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: '%s'; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 8;",
                toHexString(HOVER_COLOR),
                FONT_FAMILY
        );

        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));

        return btn;
    }

    /**
     * Creates the table view for displaying AI models.
     */
    private void createModelTable() {
        modelTable = new TableView<>();
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));

        TableColumn<AiModel.ModelData, String> idColumn = createColumn("ID", "id", 0.1);
        TableColumn<AiModel.ModelData, String> nameColumn = createColumn("Model Name", "name", 0.25);
        TableColumn<AiModel.ModelData, String> modalityColumn = createColumn("Modality", "modality", 0.15);
        TableColumn<AiModel.ModelData, Integer> latencyColumn = createColumn("Latency (ms)", "latency", 0.15);
        TableColumn<AiModel.ModelData, Double> costColumn = createColumn("Cost Per Token", "costPerToken", 0.15);
        TableColumn<AiModel.ModelData, String> providerColumn = createColumn("API Provider", "apiProvider", 0.2);

        modelTable.getColumns().addAll(idColumn, nameColumn, modalityColumn, latencyColumn, costColumn, providerColumn);

        modelTable.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3); " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: transparent; " +
                "-fx-table-cell-border-color: " + toHexString(LIGHT_BLUE) + ";" +
                "-fx-font-family: '" + FONT_FAMILY + "';"
        );

        // Add row hover effect
        modelTable.setRowFactory(tv -> {
            TableRow<AiModel.ModelData> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;");

            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: " + toHexString(MID_BLUE) + "50;");
                }
            });

            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });

            return row;
        });
    }

    /**
     * Creates a table column with specified title, property, and width percentage.
     *
     * @param <T>             The type of the table column.
     * @param title           The title of the column.
     * @param property        The property to display in the column.
     * @param widthPercentage The width percentage of the column relative to the table width.
     * @return                A styled TableColumn.
     */
    private <T> TableColumn<AiModel.ModelData, T> createColumn(String title, String property, double widthPercentage) {
        TableColumn<AiModel.ModelData, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.prefWidthProperty().bind(modelTable.widthProperty().multiply(widthPercentage));

        column.setCellFactory(col -> {
            TableCell<AiModel.ModelData, T> cell = new TableCell<>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.toString());
                        setStyle(String.format(
                                "-fx-font-family: '%s'; " +
                                "-fx-font-size: 14px; " +
                                "-fx-text-fill: #2C3E50; " +
                                "-fx-padding: 16 24;",
                                FONT_FAMILY
                        ));
                    }
                }
            };
            return cell;
        });

        return column;
    }

    /**
     * Creates the header section of the ViewModelsPane.
     *
     * @return An HBox containing the header elements.
     */
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("View Models");
        title.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                "-fx-font-size: 32px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #2C3E50;",
                FONT_FAMILY
        ));

        header.getChildren().add(title);
        return header;
    }

    /**
     * Converts a Color object to its hexadecimal string representation.
     *
     * @param color The Color object to convert.
     * @return      The hexadecimal string representation of the color.
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Creates a new JavaFX Scene with this ViewModelsPane.
     *
     * @return A new Scene object.
     */
    public Scene createScene() {
        return new Scene(this, 1000, 600);
    }

    /**
     * Refreshes the models in the table view.
     */
    public void refreshModels() {
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));
        modelTable.refresh();
    }
}