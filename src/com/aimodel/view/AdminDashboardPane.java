
package com.aimodel.view;

import com.aimodel.model.AiModel;
import com.aimodel.controller.datastructure.CustomStack;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.util.Duration;
import com.aimodel.util.ValidationUtil;
import javafx.application.Platform;

import java.util.*;

/**
 * AdminDashboardPane is a JavaFX BorderPane that represents the admin dashboard.
 * It provides an interface for managing AI models, viewing recent actions, and logging out.
 */
public class AdminDashboardPane extends BorderPane {
    // UI Components
    private VBox mainContent;
    private TableView<AiModel.ModelData> modelTable;
    private final Runnable onLogout;
    private ListView<String> recentActionsList;
    private final AiModel aiModel;

    private StackPane contentWrapper;
    private VBox sideNav;
    private final HomePage.NavigationHandler navHandler;
    private ScrollPane scrollPane;
    private Label totalModelsValueLabel; // Label to display total models
    private Label activeModelsValueLabel; // Label to display active models
    private ModelUpdateListener modelUpdateListener;
    private final CustomStack<String> recentActionsStack;

    // Modern UI Constants
    private static final String FONT_FAMILY = "SF Pro Display, -apple-system, BlinkMacSystemFont, Segoe UI";
    private static final Color PRIMARY_COLOR = Color.web("#6366F1");
    private static final Color ACCENT_COLOR = Color.web("#4F46E5");
    private static final Color SUCCESS_COLOR = Color.web("#10B981");
    private static final Color ERROR_COLOR = Color.web("#EF4444");
    private static final Color TEXT_PRIMARY = Color.web("#111827");
    private static final Color TEXT_SECONDARY = Color.web("#6B7280");
    private static final Color BORDER_COLOR = Color.web("#E5E7EB");
    private static final Color BACKGROUND_COLOR = Color.web("#E5F0FA");

    /**
     * Constructor for AdminDashboardPane.
     *
     * @param onLogout    A Runnable to be executed when the user logs out.
     * @param aiModel     The AiModel instance containing the data.
     * @param navHandler  The NavigationHandler for handling navigation actions.
     */
    public AdminDashboardPane(Runnable onLogout, AiModel aiModel, HomePage.NavigationHandler navHandler) {
        this.onLogout = onLogout;
        this.aiModel = aiModel;

        this.recentActionsStack = new CustomStack<>();
        this.navHandler = navHandler;
        setPrefSize(1200, 800);
        initializeUI();
    }

    /**
     * Interface for listening to model updates.
     */
    public interface ModelUpdateListener {
        void onModelUpdated();
    }

    /**
     * Sets the model update listener.
     *
     * @param listener The listener to be set.
     */
    public void setModelUpdateListener(ModelUpdateListener listener) {
        this.modelUpdateListener = listener;
    }

    /**
     * Initializes the user interface.
     */
    private void initializeUI() {
        // Set modern background
        setBackground(new Background(new BackgroundFill(
                BACKGROUND_COLOR,
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        // Create main layout
        contentWrapper = new StackPane();
        contentWrapper.setPadding(new Insets(40));

        // Create and setup side navigation
        createSideNavigation();

        // Create main content area
        createMainContent();

        // Wrap mainContent in a ScrollPane
        scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        contentWrapper.getChildren().add(scrollPane);

        // Set up layout
        setLeft(sideNav);
        setCenter(contentWrapper);

        // Add initial animation
        animateContentOnLoad();
    }

    /**
     * Creates the side navigation panel.
     */
    private void createSideNavigation() {
        sideNav = new VBox(20);
        sideNav.setPrefWidth(200);
        sideNav.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 0);");
        sideNav.setPadding(new Insets(20, 15, 20, 15));

        // Logo/Brand section
        Label brandLabel = new Label("Admin");
        brandLabel.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY_COLOR.toString().replace("0x", "#") + ";");

        // Navigation items
        VBox navItems = new VBox(8);
        navItems.setPadding(new Insets(40, 0, 0, 0));

        // Create navigation buttons (without Settings)
        String[][] navButtons = {
                {"Dashboard", "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"},
                {"Models", "M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"}
        };

        for (String[] navButton : navButtons) {
            Button btn = createNavButton(navButton[0], navButton[1]);
            if (navButton[0].equals("Dashboard")) {
                btn.setOnAction(e -> navHandler.navigateTo("dashboard"));
            } else if (navButton[0].equals("Models")) {
                // Update models before navigating
                btn.setOnAction(e -> {
                    navHandler.navigateTo("viewModels");
                });
            }

            navItems.getChildren().add(btn);
        }

        // Logout button at bottom
        Button logoutBtn = createNavButton("Logout", "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1");
        logoutBtn.setOnAction(e -> onLogout.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideNav.getChildren().addAll(brandLabel, navItems, spacer, logoutBtn);
    }

    /**
     * Creates a navigation button with the given text and SVG path.
     *
     * @param text     The text to display on the button.
     * @param svgPath  The SVG path for the button icon.
     * @return         A styled Button.
     */
    private Button createNavButton(String text, String svgPath) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPadding(new Insets(12, 16, 12, 16));

        // Create SVG icon
        SVGPath icon = new SVGPath();
        icon.setContent(svgPath);
        icon.setFill(TEXT_SECONDARY);

        // Style button
        btn.setGraphic(icon);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(12);

        String defaultStyle = String.format(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: %s; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 8;",
                TEXT_SECONDARY.toString().replace("0x", "#"),
                FONT_FAMILY
        );

        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: %s; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 8;",
                PRIMARY_COLOR.toString().replace("0x", "#") + "22",
                PRIMARY_COLOR.toString().replace("0x", "#"),
                FONT_FAMILY
        );

        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));

        return btn;
    }

    /**
     * Creates the main content area of the dashboard.
     */
    private void createMainContent() {
        mainContent = new VBox(30);

        // Create header section
        HBox header = createHeader();

        // Create stats cards
        FlowPane statsContainer = createStatsContainer();

        // Create main table section
        VBox tableSection = createTableSection();

        // Create recent actions sidebar
        VBox recentActions = createRecentActionsCard();

        // Layout
        HBox mainSection = new HBox(30);
        mainSection.getChildren().addAll(tableSection, recentActions);
        HBox.setHgrow(tableSection, Priority.ALWAYS);

        mainContent.getChildren().addAll(header, statsContainer, mainSection);
    }

    /**
     * Creates the header section of the dashboard.
     *
     * @return An HBox containing the header elements.
     */
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 0, 0, 0));
        header.setSpacing(20);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addModelBtn = createPrimaryButton("+ Add New Model", e -> showAddModelDialog());

        header.getChildren().addAll(spacer, addModelBtn);
        return header;
    }

    /**
     * Creates a primary action button.
     *
     * @param text   The text to display on the button.
     * @param action The action to perform when the button is clicked.
     * @return       A styled Button.
     */
    private Button createPrimaryButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setOnAction(action);

        String defaultStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 12 24; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);",
                PRIMARY_COLOR.toString().replace("0x", "#"),
                FONT_FAMILY
        );

        String hoverStyle = defaultStyle + String.format(
                "-fx-background-color: %s;",
                ACCENT_COLOR.toString().replace("0x", "#")
        );

        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));

        return btn;
    }

    /**
     * Creates the container for statistics cards.
     *
     * @return A FlowPane containing the statistics cards.
     */
    private FlowPane createStatsContainer() {
        FlowPane container = new FlowPane();
        container.setHgap(20);
        container.setVgap(20);
        container.setAlignment(Pos.CENTER_LEFT);

        // Add padding to the container
        container.setPadding(new Insets(20, 20, 20, 20)); // Add 20 pixels padding on all sides

        // Create labels for total and active models (initialize to 0)
        totalModelsValueLabel = new Label("0");
        activeModelsValueLabel = new Label("0");

        // Update stats cards to use these labels
        VBox totalModelsCard = createStatsCard("Total Models", totalModelsValueLabel, "M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z");
        VBox activeModelsCard = createStatsCard("Active Models", activeModelsValueLabel, "M13 10V3L4 14h7v7l9-11h-7z");
        VBox pendingReviewsCard = createStatsCard("Pending Reviews", new Label("0"), "M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z");

        // Add cards to the container
        container.getChildren().addAll(totalModelsCard, activeModelsCard, pendingReviewsCard);

        // Update the labels with actual values
        updateModelCounts();

        return container;
    }

    /**
     * Updates the model counts in the statistics cards.
     */
    private void updateModelCounts() {
        int totalModels = aiModel.getModelDataList().size();
        int activeModels = calculateActiveModelsCount(); 

        totalModelsValueLabel.setText(String.valueOf(totalModels));
        activeModelsValueLabel.setText(String.valueOf(activeModels));
    }

    /**
     * Calculates the number of active models.
     *
     * @return The number of active models.
     */
    private int calculateActiveModelsCount() {
        // Assuming all models are active for now
        return aiModel.getModelDataList().size();
    }

    /**
     * Creates a statistics card with an icon, value, and title.
     *
     * @param title      The title of the card.
     * @param valueLabel The label to display the value.
     * @param svgPath    The SVG path for the card icon.
     * @return           A styled VBox representing the statistics card.
     */
    private VBox createStatsCard(String title, Label valueLabel, String svgPath) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20, 20, 20, 40));
        card.setPrefWidth(200);
        card.setStyle(String.format(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 2); " +
                        "-fx-border-color: %s; " +
                        "-fx-padding: 8 16; " +
                        "-fx-border-radius: 16; " +
                        "-fx-border-width: 1;",
                BORDER_COLOR.toString().replace("0x", "#")
        ));

        // Create icon
        SVGPath icon = new SVGPath();
        icon.setContent(svgPath);
        StackPane iconContainer = new StackPane(icon);
        iconContainer.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-background-radius: 8;",
                PRIMARY_COLOR.toString().replace("0x", "#") + "22"
        ));
        iconContainer.setPadding(new Insets(8));
        icon.setFill(PRIMARY_COLOR);

        // Style for the value label
        valueLabel.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: 28px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: %s;",
                FONT_FAMILY,
                TEXT_PRIMARY.toString().replace("0x", "#")
        ));

        // Label for the title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-fill: %s;",
                FONT_FAMILY,
                TEXT_SECONDARY.toString().replace("0x", "#")
        ));

        card.getChildren().addAll(iconContainer, valueLabel, titleLabel);

        // Add hover effect
        addHoverEffect(card);

        return card;
    }

    /**
     * Adds a hover effect to the given node.
     *
     * @param node The node to which the hover effect is added.
     */
    private void addHoverEffect(Region node) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.02);
        scaleUp.setToY(1.02);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        node.setOnMouseEntered(e -> {
            node.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.15)));
            scaleUp.play();
        });

        node.setOnMouseExited(e -> {
            node.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.08)));
            scaleDown.play();
        });
    }

    /**
     * Creates the table section for displaying AI models.
     *
     * @return A VBox containing the table section.
     */
    private VBox createTableSection() {
        VBox section = new VBox(20);
        section.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 2); " +
                        "-fx-padding: 24;"
        );

        Label title = new Label("AI Models");
        title.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: %s;",
                FONT_FAMILY,
                TEXT_PRIMARY.toString().replace("0x", "#")
        ));

        createModernTable();
        VBox.setVgrow(modelTable, Priority.ALWAYS);

        section.getChildren().addAll(title, modelTable);
        return section;
    }

    /**
     * Creates the modern-styled table for displaying AI models.
     */
    private void createModernTable() {
        modelTable = new TableView<>();
        modelTable.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent; " +
                        "-fx-table-cell-border-color: transparent;"
        );

        // Define columns (only ID, Name, and Actions)
        TableColumn<AiModel.ModelData, String> idColumn = createColumn("ID", "id", 100);
        TableColumn<AiModel.ModelData, String> nameColumn = createColumn("Model Name", "name", 200);
        TableColumn<AiModel.ModelData, Void> actionsColumn = createActionsColumn();

        modelTable.getColumns().addAll(idColumn, nameColumn, actionsColumn);
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));

        // Apply modern styling
        modelTable.setFixedCellSize(60);
        modelTable.setRowFactory(tv -> {
            TableRow<AiModel.ModelData> row = new TableRow<>();

            row.setStyle("-fx-background-color: transparent;");

            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: " + PRIMARY_COLOR.toString().replace("0x", "#") + "11;");
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
     * Creates a table column with specified title, property, and width.
     *
     * @param <T>      The type of the table column.
     * @param title    The title of the column.
     * @param property The property to display in the column.
     * @param width    The preferred width of the column.
     * @return         A styled TableColumn.
     */
    private <T> TableColumn<AiModel.ModelData, T> createColumn(String title, String property, double width) {
        TableColumn<AiModel.ModelData, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        column.setStyle("-fx-alignment: CENTER-LEFT;");

        // Style cells
        column.setCellFactory(col -> {
            TableCell<AiModel.ModelData, T> cell = new TableCell<>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };

            cell.setStyle(String.format(
                    "-fx-font-family: '%s'; " +
                            "-fx-font-size: 14px; " +
                            "-fx-text-fill: %s; " +
                            "-fx-padding: 16 24;",
                    FONT_FAMILY,
                    TEXT_PRIMARY.toString().replace("0x", "#")
            ));

            return cell;
        });

        return column;
    }

    /**
     * Creates the actions column for the table.
     *
     * @return A TableColumn with Edit and Delete action buttons.
     */
    private TableColumn<AiModel.ModelData, Void> createActionsColumn() {
        TableColumn<AiModel.ModelData, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final HBox container = new HBox(12);
            private final Button editButton = createActionButton("Edit", ACCENT_COLOR);
            private final Button deleteButton = createActionButton("Delete", ERROR_COLOR);

            {
                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().addAll(editButton, deleteButton);

                editButton.setOnAction(event -> {
                    AiModel.ModelData data = getTableRow().getItem();
                    if (data != null) showEditModelDialog(data);
                });

                deleteButton.setOnAction(event -> {
                    AiModel.ModelData data = getTableRow().getItem();
                    if (data != null) showDeleteConfirmation(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        return actionsColumn;
    }

    /**
     * Creates an action button for table rows.
     *
     * @param text  The text to display on the button.
     * @param color The color of the button.
     * @return      A styled Button for table actions.
     */
    private Button createActionButton(String text, Color color) {
        Button button = new Button(text);

        String defaultStyle = String.format(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: %s; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 16; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-radius: 6; " +
                        "-fx-cursor: hand;",
                color.toString().replace("0x", "#"),
                FONT_FAMILY,
                color.toString().replace("0x", "#")
        );

        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 16; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-radius: 6; " +
                        "-fx-cursor: hand;",
                color.toString().replace("0x", "#"),
                FONT_FAMILY,
                color.toString().replace("0x", "#")
        );

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        return button;
    }
    

    /**
     * Creates the card for displaying recent actions.
     *
     * @return A VBox containing the recent actions card.
     */
    private VBox createRecentActionsCard() {
        VBox card = new VBox(20);
        card.setPrefWidth(390);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 2); " +
                        "-fx-padding: 24;"
        );

        // Create header with title and clear button
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Actions");
        title.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: %s;",
                FONT_FAMILY,
                TEXT_PRIMARY.toString().replace("0x", "#")
        ));

        Button clearButton = new Button("Clear");
        clearButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FF5252; " +
                        "-fx-font-family: '" + FONT_FAMILY + "'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand;"
        );

        clearButton.setOnAction(e -> {
            recentActionsStack.clear();
            refreshRecentActionsList();
        });

        header.getChildren().addAll(title, new Region(), clearButton);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        recentActionsList = new ListView<>();
        recentActionsList.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-background-insets: 0; " +
                        "-fx-padding: 8; " +
                        "-fx-border-color: transparent;"
        );

        recentActionsList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle(String.format(
                            "-fx-font-family: '%s'; " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-text-fill: %s; " +
                                    "-fx-padding: 12 8; " +
                                    "-fx-background-color: transparent;",
                            FONT_FAMILY,
                            TEXT_PRIMARY.toString().replace("0x", "#")
                    ));
                }
            }
        });

        VBox.setVgrow(recentActionsList, Priority.ALWAYS);
        card.getChildren().addAll(header, recentActionsList);

        refreshRecentActionsList();
        return card;
    }

    /**
     * Adds a recent action to the stack and updates the UI.
     *
     * @param action The action to be added.
     */
    private void addRecentAction(String action) {
        if (action != null && !action.trim().isEmpty()) {
            recentActionsStack.push(action);
            refreshRecentActionsList();
        }
    }

    /**
     * Gets recent actions from the stack in correct order.
     * @return A list of recent actions.
     */
    private List<String> getRecentActionsFromStack() {
        List<String> actions = recentActionsStack.getRecentItems();
        return actions;
    }

    /**
     * Refreshes the ListView with current stack contents.
     */
    private void refreshRecentActionsList() {
        Platform.runLater(() -> {
            recentActionsList.setItems(FXCollections.observableArrayList(getRecentActionsFromStack()));
        });
    }

    /**
     * Animates the content when the dashboard is loaded.
     */
    private void animateContentOnLoad() {
        // Fade in animation for main content
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), mainContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Slide in animation for side navigation
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(800), sideNav);
        slideIn.setFromX(-280);
        slideIn.setToX(0);

        // Play animations
        ParallelTransition parallel = new ParallelTransition(fadeIn, slideIn);
        parallel.play();
    }

    
    
    
    

    /**
     * Shows the dialog for editing an existing model.
     *
     * @param model The model to be edited.
     */
 
    private HBox createFormField(String labelText, TextField field, Label validationLabel) {
        // Create container
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(15);
        hbox.setPrefHeight(40); // Fixed height for consistent spacing

        // Create and configure label
        Label label = new Label(labelText);
        label.setStyle(String.format(
            "-fx-font-family: '%s'; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: %s;",
            FONT_FAMILY,
            TEXT_PRIMARY.toString().replace("0x", "#")
        ));
        label.setPrefWidth(120); // Fixed width for label alignment
        label.setAlignment(Pos.CENTER_LEFT);

        // Configure field
        field.setPrefWidth(250); // Fixed width for text fields

        // Configure validation label
        validationLabel.setPrefWidth(200); // Fixed width for validation messages
        validationLabel.setTextFill(ERROR_COLOR);
        validationLabel.setWrapText(true);

        // Add all components
        hbox.getChildren().addAll(label, field, validationLabel);

        return hbox;
    }



    private void showAddModelDialog() {
        Dialog<AiModel.ModelData> dialog = new Dialog<>();
        dialog.setTitle("Add New Model");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Create the custom dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(700);
        dialogPane.setStyle(String.format(
            "-fx-background-color: white; " +
            "-fx-padding: 24; " +
            "-fx-font-family: '%s';",
            FONT_FAMILY
        ));

        // Form fields
        TextField idField = createStyledTextField("Enter model ID");
        Label idValidationLabel = new Label();

        TextField nameField = createStyledTextField("Enter model name");
        Label nameValidationLabel = new Label();

        TextField modalityField = createStyledTextField("Enter modality");
        Label modalityValidationLabel = new Label();

        TextField latencyField = createStyledTextField("Enter latency in ms");
        Label latencyValidationLabel = new Label();

        TextField costPerTokenField = createStyledTextField("Enter cost per token");
        Label costValidationLabel = new Label();

        TextField apiProviderField = createStyledTextField("Enter API provider");
        Label apiProviderValidationLabel = new Label();

        // Create map to track validation status
        Map<TextField, Boolean> validationStatus = new HashMap<>();
        validationStatus.put(idField, false);
        validationStatus.put(nameField, false);
        validationStatus.put(modalityField, false);
        validationStatus.put(latencyField, false);
        validationStatus.put(costPerTokenField, false);
        validationStatus.put(apiProviderField, false);

        // Function to check if all fields are valid
        Runnable updateSaveButtonState = () -> {
            Button saveButton = (Button) dialogPane.lookupButton(
                dialogPane.getButtonTypes().stream()
                    .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    .findFirst()
                    .get()
            );
            boolean allValid = validationStatus.values().stream().allMatch(valid -> valid);
            saveButton.setDisable(!allValid);
        };

        // Add validation listeners
        idField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(idField, 
                ValidationUtil.validateIdField(idField, aiModel, idValidationLabel));
            updateSaveButtonState.run();
        });

        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(nameField, 
                ValidationUtil.validateNameField(nameField, nameValidationLabel));
            updateSaveButtonState.run();
        });

        modalityField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(modalityField, 
                ValidationUtil.validateModalityField(modalityField, modalityValidationLabel));
            updateSaveButtonState.run();
        });

        latencyField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(latencyField, 
                ValidationUtil.validateLatencyField(latencyField, latencyValidationLabel));
            updateSaveButtonState.run();
        });

        costPerTokenField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(costPerTokenField, 
                ValidationUtil.validateCostField(costPerTokenField, costValidationLabel));
            updateSaveButtonState.run();
        });

        apiProviderField.textProperty().addListener((obs, oldVal, newVal) -> {
            validationStatus.put(apiProviderField, 
                ValidationUtil.validateProviderField(apiProviderField, apiProviderValidationLabel));
            updateSaveButtonState.run();
        });

        // Create content layout
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: white;");

        // Add form fields
        content.getChildren().addAll(
            createFormField("Model ID", idField, idValidationLabel),
            createFormField("Model Name", nameField, nameValidationLabel),
            createFormField("Modality", modalityField, modalityValidationLabel),
            createFormField("Latency (ms)", latencyField, latencyValidationLabel),
            createFormField("Cost Per Token", costPerTokenField, costValidationLabel),
            createFormField("API Provider", apiProviderField, apiProviderValidationLabel)
        );

        // Add content to dialog
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        dialogPane.setContent(scrollPane);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Style buttons
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        styleDialogButton(saveButton, PRIMARY_COLOR);
        styleDialogButton(cancelButton, TEXT_SECONDARY);

        // Initially disable save button
        saveButton.setDisable(true);

        // Trigger initial validation for all fields
        idField.setText(idField.getText());
        nameField.setText(nameField.getText());
        modalityField.setText(modalityField.getText());
        latencyField.setText(latencyField.getText());
        costPerTokenField.setText(costPerTokenField.getText());
        apiProviderField.setText(apiProviderField.getText());

        // Handle result conversion
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType && !saveButton.isDisabled()) {
                try {
                    return new AiModel.ModelData(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        modalityField.getText().trim(),
                        Integer.parseInt(latencyField.getText().trim()),
                        Double.parseDouble(costPerTokenField.getText().trim()),
                        apiProviderField.getText().trim()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // Show dialog and handle result
        Optional<AiModel.ModelData> result = dialog.showAndWait();
        result.ifPresent(newModel -> {
            aiModel.addModel(newModel);
            modelTable.getItems().add(newModel);
            addRecentAction("Added new model: " + newModel.getName());
            showSuccessAlert("Model added successfully!");
            updateModelCounts();

            if (modelUpdateListener != null) {
                modelUpdateListener.onModelUpdated();
            }
        });
    }


    // Helper method to create styled text field
    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle(
            "-fx-padding: 8 12; " +
            "-fx-background-radius: 4; " +
            "-fx-border-radius: 4; " +
            "-fx-border-color: #E0E0E0; " +
            "-fx-border-width: 1px;"
        );
        return field;
    }




    
    private void showEditModelDialog(AiModel.ModelData model) {
        Dialog<AiModel.ModelData> dialog = new Dialog<>();
        dialog.setTitle("Edit Model");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Create the custom dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(700);
        dialogPane.setStyle(String.format(
            "-fx-background-color: white; " +
            "-fx-padding: 24; " +
            "-fx-font-family: '%s';",
            FONT_FAMILY
        ));

        // Form fields with existing values
        TextField idField = createStyledTextField("Model ID");
        idField.setText(model.getId());
        idField.setDisable(true);
        Label idValidationLabel = new Label();

        TextField nameField = createStyledTextField("Model name");
        nameField.setText(model.getName());
        Label nameValidationLabel = new Label();

        TextField modalityField = createStyledTextField("Modality");
        modalityField.setText(model.getModality());
        Label modalityValidationLabel = new Label();

        TextField latencyField = createStyledTextField("Latency");
        latencyField.setText(String.valueOf(model.getLatency()));
        Label latencyValidationLabel = new Label();

        TextField costPerTokenField = createStyledTextField("Cost per token");
        costPerTokenField.setText(String.valueOf(model.getCostPerToken()));
        Label costValidationLabel = new Label();

        TextField apiProviderField = createStyledTextField("API provider");
        apiProviderField.setText(model.getApiProvider());
        Label apiProviderValidationLabel = new Label();

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Style buttons
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        styleDialogButton(saveButton, PRIMARY_COLOR);
        styleDialogButton(cancelButton, TEXT_SECONDARY);

        // Initially disable save button
        saveButton.setDisable(true);

        // Create a method to check all validations
        Runnable validateAllFields = () -> {
            boolean isNameValid = ValidationUtil.validateNameField(nameField, nameValidationLabel);
            boolean isModalityValid = ValidationUtil.validateModalityField(modalityField, modalityValidationLabel);
            boolean isLatencyValid = ValidationUtil.validateLatencyField(latencyField, latencyValidationLabel);
            boolean isCostValid = ValidationUtil.validateCostField(costPerTokenField, costValidationLabel);
            boolean isProviderValid = ValidationUtil.validateProviderField(apiProviderField, apiProviderValidationLabel);

            // Enable save button only if all fields are valid
            saveButton.setDisable(!(isNameValid && isModalityValid && isLatencyValid && 
                                  isCostValid && isProviderValid));
        };

        // Add validation listeners
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateAllFields.run());
        modalityField.textProperty().addListener((obs, oldVal, newVal) -> validateAllFields.run());
        latencyField.textProperty().addListener((obs, oldVal, newVal) -> validateAllFields.run());
        costPerTokenField.textProperty().addListener((obs, oldVal, newVal) -> validateAllFields.run());
        apiProviderField.textProperty().addListener((obs, oldVal, newVal) -> validateAllFields.run());

        // Run initial validation
        validateAllFields.run();

        // Create content layout
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: white;");

        // Add form fields
        content.getChildren().addAll(
            createFormField("Model ID", idField, idValidationLabel),
            createFormField("Model Name", nameField, nameValidationLabel),
            createFormField("Modality", modalityField, modalityValidationLabel),
            createFormField("Latency (ms)", latencyField, latencyValidationLabel),
            createFormField("Cost Per Token", costPerTokenField, costValidationLabel),
            createFormField("API Provider", apiProviderField, apiProviderValidationLabel)
        );

        // Add content to dialog
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        dialogPane.setContent(scrollPane);

        // Handle result conversion
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new AiModel.ModelData(
                        idField.getText(),
                        nameField.getText(),
                        modalityField.getText(),
                        Integer.parseInt(latencyField.getText()),
                        Double.parseDouble(costPerTokenField.getText()),
                        apiProviderField.getText()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // Show dialog and handle result
        Optional<AiModel.ModelData> result = dialog.showAndWait();
        result.ifPresent(updatedModel -> {
            aiModel.updateModel(updatedModel);
            refreshTable();
            addRecentAction("Updated model: " + updatedModel.getName());
            showSuccessAlert("Model updated successfully!");
            updateModelCounts();

            if (modelUpdateListener != null) {
                modelUpdateListener.onModelUpdated();
            }
        });
    }
    

    

    /**
     * Styles a dialog button.
     *
     * @param button The button to be styled.
     * @param color  The color of the button.
     */
    private void styleDialogButton(Button button, Color color) {
        String defaultStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: '%s'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 12 24; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;",
                color.toString().replace("0x", "#"),
                FONT_FAMILY
        );

        String hoverStyle = defaultStyle + "-fx-opacity: 0.9;";

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }

    /**
     * Shows a success alert.
     *
     * @param message The message to be displayed in the alert.
     */
    private void showSuccessAlert(String message) {
        showAlert("Success", message, SUCCESS_COLOR);
    }

    
    

    /**
     * Shows a custom alert dialog.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     * @param color   The color of the OK button in the alert.
     */
    private void showAlert(String title, String message, Color color) {
        Dialog<Void> alert = new Dialog<>();
        alert.setTitle(title);
        alert.initModality(Modality.APPLICATION_MODAL);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(String.format(
                "-fx-background-color: white; " +
                        "-fx-padding: 24; " +
                        "-fx-font-family: '%s';",
                FONT_FAMILY
        ));

        Label messageLabel = new Label(message);
        messageLabel.setStyle(String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: 16px; " +
                        "-fx-text-fill: %s;",
                FONT_FAMILY,
                TEXT_PRIMARY.toString().replace("0x", "#")
        ));

        dialogPane.setContent(messageLabel);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().add(okButtonType);

        Button okButton = (Button) dialogPane.lookupButton(okButtonType);
        styleDialogButton(okButton, color);

        alert.showAndWait();
    }
    
    /**
 * Shows a confirmation dialog for deleting a model.
 *
 * @param data The ModelData to be deleted.
 */
    private void showDeleteConfirmation(AiModel.ModelData data) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Model");
        alert.setHeaderText("Are you sure you want to delete this model?");
        alert.setContentText("Model ID: " + data.getId() + "\nModel Name: " + data.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            aiModel.deleteModel(data.getId()); 
            modelTable.getItems().remove(data); 
            addRecentAction("Deleted model: " + data.getName());
            showSuccessAlert("Model deleted successfully!");
            updateModelCounts();

            if (modelUpdateListener != null) {
                modelUpdateListener.onModelUpdated();
            }
        }
    }

    /**
     * Refreshes the model table.
     */
    private void refreshTable() {
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));
    }

    /**
     * Creates a JavaFX scene from this pane.
     *
     * @return A new JavaFX scene containing this pane.
     */
    public Scene createScene() {
        return new Scene(this);
    }

}






