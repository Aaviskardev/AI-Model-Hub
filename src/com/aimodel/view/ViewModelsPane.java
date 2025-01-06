
package com.aimodel.view;

import com.aimodel.model.AiModel;
import com.aimodel.controller.datastructure.InsertionSort;
import com.aimodel.controller.datastructure.MergeSort;
import com.aimodel.controller.datastructure.SelectionSort;
import com.aimodel.controller.datastructure.BinarySearch;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Aaviskar  23048648
 */

/**
 * ViewModelsPane is a JavaFX BorderPane that displays a table of AI models.
 * It demonstrates the use of binary search (for exact name matches) and
 * sorting algorithms (Insertion Sort, Merge Sort, Selection Sort).
 */
public class ViewModelsPane extends BorderPane implements AiModel.AiModelListener {
    private TableView<AiModel.ModelData> modelTable;
    private AiModel aiModel;
    private HomePage.NavigationHandler navHandler;
    private VBox sideNav;
    private ComboBox<String> sortOptions;
    private ComboBox<String> columnOptions;
    private CheckBox ascendingCheckBox;
    private TextField searchField;
    private Button searchButton;
    private ScrollPane modelTableScrollPane;

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
    private static final String MODERN_TABLE_STYLE = """
            .table-view {
                -fx-background-color: rgba(255, 255, 255, 0.95);
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);
                -fx-padding: 0;
            }
            
            .table-view .column-header-background {
                -fx-background-color: #f8f9fa;
                -fx-background-radius: 15 15 0 0;
                -fx-padding: 5 0 5 0;
            }
            
            .table-view .column-header {
                -fx-background-color: transparent;
                -fx-size: 45px;
                -fx-border-color: transparent;
                -fx-font-family: 'SF Pro Display';
                -fx-font-size: 14px;
                -fx-font-weight: bold;
            }
            
            .table-view .table-cell {
                -fx-alignment: CENTER_LEFT;
                -fx-padding: 15 20;
                -fx-font-family: 'SF Pro Display';
                -fx-font-size: 14px;
                -fx-text-fill: #2C3E50;
                -fx-border-color: transparent;
            }
            
            .table-row-cell {
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                -fx-table-cell-border-color: transparent;
            }
            
            .table-row-cell:hover {
                -fx-background-color: rgba(162, 210, 255, 0.2);
                -fx-transition: background-color 0.3s;
            }
            
            .table-row-cell:selected {
                -fx-background-color: rgba(162, 210, 255, 0.4);
            }
            
            .table-view .virtual-flow .scroll-bar:vertical {
                -fx-background-color: transparent;
                -fx-padding: 0 0 0 0;
            }
            
            .table-view .virtual-flow .scroll-bar:vertical .track {
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                -fx-background-radius: 0;
            }
            
            .table-view .virtual-flow .scroll-bar:vertical .thumb {
                -fx-background-color: #A2D2FF;
                -fx-background-radius: 10;
                -fx-opacity: 0.6;
            }
            
            .table-view .virtual-flow .scroll-bar:vertical .thumb:hover {
                -fx-opacity: 0.8;
            }
            
            .table-view .placeholder .label {
                -fx-text-fill: #666;
                -fx-font-size: 14px;
            }
            
            .table-view .filler {
                -fx-background-color: transparent;
            }
            """;

    public ViewModelsPane(AiModel aiModel, HomePage.NavigationHandler navHandler) {
        this.aiModel = aiModel;
        this.navHandler = navHandler;
        aiModel.addListener(this);
        initializeUI();
    }

    @Override
    public void onModelDataChanged() {
        Platform.runLater(() -> {
            refreshModels();
        });
    }

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

        // Create content area
        createModelTable();
        HBox header = createHeader();
        sideNav = createViewModelsNavBar();

        // Add sorting dropdown
        sortOptions = new ComboBox<>();
        sortOptions.getItems().addAll("None", "Insertion Sort", "Merge Sort", "Selection Sort");
        sortOptions.setValue("None");
        sortOptions.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); " +
                        "-fx-border-color: transparent; " +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;"
        );
        sortOptions.setOnAction(e -> sortAndRefreshTable());

        columnOptions = new ComboBox<>();
        columnOptions.getItems().addAll("Model Name", "Modality", "Latency (ms)", "Cost Per Token", "API Provider");
        columnOptions.setValue("Model Name");
        columnOptions.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.7); " +
            "-fx-border-color: transparent; " +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );

        // Add this listener to trigger resort when column selection changes
        columnOptions.setOnAction(e -> {
            System.out.println("Column selection changed to: " + columnOptions.getValue());
            sortAndRefreshTable();
        });

        // Also update the sortOptions listener to ensure it triggers properly
        sortOptions.setOnAction(e -> {
            System.out.println("Sort method changed to: " + sortOptions.getValue());
            sortAndRefreshTable();
        });

        // Add ascending/descending CheckBox
        ascendingCheckBox = new CheckBox("Ascending");
        ascendingCheckBox.setSelected(true); // Default to ascending
        ascendingCheckBox.setStyle(
                "-fx-text-fill: " + toHexString(TEXT_COLOR) + ";" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 14px;"
        );

        // Add this listener to trigger resort when checkbox is clicked
        ascendingCheckBox.setOnAction(e -> {
            System.out.println("Sort direction changed to: " + (ascendingCheckBox.isSelected() ? "ascending" : "descending"));
            sortAndRefreshTable();
        });

        // Add search field and button
        searchField = new TextField();
        searchField.setPromptText("Search by Name (exact)...");
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); " +
                        "-fx-border-color: transparent; " +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;"
        );

        searchButton = new Button("Search");
        searchButton.setStyle(
                "-fx-background-color: " + toHexString(DEEP_BLUE) + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        searchButton.setOnAction(e -> searchModel());

        HBox topBar = new HBox(10);
        topBar.getChildren().addAll(sortOptions, columnOptions, ascendingCheckBox, searchField, searchButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        // Wrap the table in a ScrollPane
        modelTableScrollPane = new ScrollPane(modelTable);
        modelTableScrollPane.setFitToWidth(true);
        modelTableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        modelTableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        modelTableScrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );
        modelTableScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        modelTableScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        // Main content container
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().addAll(header, topBar, modelTableScrollPane);
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); " +
                "-fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Layout setup
        setCenter(content);
        setLeft(sideNav);
        setPadding(new Insets(20));
    }

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

    private void createModelTable() {
        modelTable = new TableView<>();
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));

        modelTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AiModel.ModelData, String> idColumn = createColumn("ID", "id", 0.1);
        TableColumn<AiModel.ModelData, String> nameColumn = createColumn("Model Name", "name", 0.25);
        TableColumn<AiModel.ModelData, String> modalityColumn = createColumn("Modality", "modality", 0.15);
        TableColumn<AiModel.ModelData, Integer> latencyColumn = createColumn("Latency (ms)", "latency", 0.15);
        TableColumn<AiModel.ModelData, Double> costColumn = createColumn("Cost Per Token", "costPerToken", 0.15);
        TableColumn<AiModel.ModelData, String> providerColumn = createColumn("API Provider", "apiProvider", 0.2);

        idColumn.setReorderable(false);
        nameColumn.setReorderable(false);
        modalityColumn.setReorderable(false);
        latencyColumn.setReorderable(false);
        costColumn.setReorderable(false);
        providerColumn.setReorderable(false);

        modelTable.getColumns().addAll(idColumn, nameColumn, modalityColumn, latencyColumn, costColumn, providerColumn);

        modelTable.setStyle(MODERN_TABLE_STYLE);

        modelTable.setSelectionModel(null);

        Label placeholderLabel = new Label("No models available");
        placeholderLabel.setStyle("-fx-font-family: 'SF Pro Display'; -fx-font-size: 14px; -fx-text-fill: #666;");
        modelTable.setPlaceholder(placeholderLabel);

        modelTable.setRowFactory(tv -> {
            TableRow<AiModel.ModelData> row = new TableRow<>();
            row.setFocusTraversable(false);
            return row;
        });

        modelTable.setBackground(new Background(new BackgroundFill(
                Color.rgb(255, 255, 255, 0.95),
                new CornerRadii(15),
                Insets.EMPTY
        )));

        modelTable.setFixedCellSize(50);
        modelTable.prefHeightProperty().bind(modelTable.fixedCellSizeProperty().multiply(10.5));
    }

    private <T> TableColumn<AiModel.ModelData, T> createColumn(String title, String property, double widthPercentage) {
        TableColumn<AiModel.ModelData, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.prefWidthProperty().bind(modelTable.widthProperty().multiply(widthPercentage));

        column.setSortable(false);

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
                        setStyle("-fx-font-family: 'SF Pro Display'; -fx-font-size: 14px; -fx-text-fill: #2C3E50;");
                    }
                }
            };
            return cell;
        });

        return column;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

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

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }



    private void sortAndRefreshTable() {
        String selectedSort = sortOptions.getValue();
        String selectedColumn = columnOptions.getValue();
        boolean ascending = ascendingCheckBox.isSelected();

       

        List<AiModel.ModelData> sortedList = new ArrayList<>(aiModel.getModelDataList());

 

        switch (selectedSort) {
            case "Insertion Sort":
                sortedList = InsertionSort.sort(sortedList, selectedColumn, ascending);
                break;
            case "Merge Sort":
                sortedList = MergeSort.sort(sortedList, selectedColumn, ascending);
                break;
            case "Selection Sort":
                sortedList = SelectionSort.sort(sortedList, selectedColumn, ascending);
                break;
            case "None":
            default:
                // No sorting needed
                break;
        }


        modelTable.setItems(FXCollections.observableArrayList(sortedList));
        modelTable.refresh();  
    }   

    /**
     * Searches for a model using binary search (for exact name matches only).
     * The search is case-insensitive.
     */
    private void searchModel() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Input Empty");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a search term.");
            alert.showAndWait();
            return;
        }

        // Sort the list by name for binary search
        List<AiModel.ModelData> sortedList = new ArrayList<>(aiModel.getModelDataList());
        Collections.sort(sortedList, Comparator.comparing(model -> model.getName().toLowerCase()));

        // Perform binary search on the 'name' field (case-insensitive, exact match)
        int index = BinarySearch.search(sortedList, searchText);

        // Create a list with the found item (or an empty list if not found)
        List<AiModel.ModelData> filteredList = new ArrayList<>();
        if (index != -1) {
            filteredList.add(sortedList.get(index));
        }

        // Update the table with the filtered list
        modelTable.setItems(FXCollections.observableArrayList(filteredList));

        // If no results are found, show an alert
        if (filteredList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Results Found");
            alert.setHeaderText(null);
            alert.setContentText("No models matched your exact search criteria.");
            alert.showAndWait();
        }
    }

    public Scene createScene() {
        return new Scene(this, 1000, 600);
    }

    public void refreshModels() {
        modelTable.setItems(FXCollections.observableArrayList(aiModel.getModelDataList()));
        modelTable.refresh();
    }
}

