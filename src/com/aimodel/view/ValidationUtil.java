
package com.aimodel.view;

import com.aimodel.model.AiModel;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.application.Platform;
import javafx.stage.Window;

/**
 * Utility class for validating input fields in the application.
 */
public class ValidationUtil {

    private static final String ERROR_COLOR = "#e74c3c";

    /**
     * Validates the ID field to ensure it's not empty, contains only digits, and is unique.
     *
     * @param field   The TextField representing the ID field.
     * @param aiModel The AiModel instance to check for ID uniqueness.
     * @return True if the ID is valid and unique, false otherwise.
     */
    public static boolean validateIdField(TextField field, AiModel aiModel) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "ID cannot be empty.");
            return false;
        } else if (!input.matches("\\d+")) {
            showTooltip(field, "ID must contain only digits.");
            return false;
        } else if (isIdExists(input, aiModel)) {
            showTooltip(field, "ID already exists.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Checks if an ID already exists in the AiModel.
     *
     * @param id      The ID to check.
     * @param aiModel The AiModel instance to search within.
     * @return True if the ID exists, false otherwise.
     */
    private static boolean isIdExists(String id, AiModel aiModel) {
        for (AiModel.ModelData model : aiModel.getModelDataList()) {
            if (model.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the Name field to ensure it's not empty and is within the length limit.
     *
     * @param field The TextField representing the Name field.
     * @return True if the Name is valid, false otherwise.
     */
    public static boolean validateNameField(TextField field) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "Name cannot be empty.");
            return false;
        } else if (input.length() > 50) {
            showTooltip(field, "Name must be at most 50 characters.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Validates the Modality field to ensure it's not empty.
     *
     * @param field The TextField representing the Modality field.
     * @return True if the Modality is valid, false otherwise.
     */
    public static boolean validateModalityField(TextField field) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "Modality cannot be empty.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Validates the Latency field to ensure it's not empty and is a positive number.
     *
     * @param field The TextField representing the Latency field.
     * @return True if the Latency is valid, false otherwise.
     */
    public static boolean validateLatencyField(TextField field) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "Latency cannot be empty.");
            return false;
        } else if (!input.matches("\\d+")) {
            showTooltip(field, "Latency must be a positive number.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Validates the Cost field to ensure it's not empty and is a valid number.
     *
     * @param field The TextField representing the Cost field.
     * @return True if the Cost is valid, false otherwise.
     */
    public static boolean validateCostField(TextField field) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "Cost cannot be empty.");
            return false;
        } else if (!input.matches("\\d*\\.?\\d+")) {
            showTooltip(field, "Cost must be a valid number.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Validates the Provider field to ensure it's not empty.
     *
     * @param field The TextField representing the Provider field.
     * @return True if the Provider is valid, false otherwise.
     */
    public static boolean validateProviderField(TextField field) {
        String input = field.getText();
        if (input.isEmpty()) {
            showTooltip(field, "API Provider cannot be empty.");
            return false;
        } else {
            clearTooltipAndErrorStyle(field);
            return true;
        }
    }

    /**
     * Shows a tooltip on the specified control with the given error message.
     * The tooltip will be shown only after the control has been added to a scene and has a window.
     * If the control is not in a scene yet, it waits until the scene is available.
     *
     * @param control The Control to attach the tooltip to.
     * @param message The error message to display.
     */
    public static void showTooltip(Control control, String message) {
        Platform.runLater(() -> {
            Tooltip tooltip = new Tooltip(message);
            tooltip.setStyle(
                    "-fx-background-color: " + ERROR_COLOR + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 12px;"
            );

            // Check if the control is part of a scene and has a window
            if (control.getScene() != null && control.getScene().getWindow() != null) {
                Window window = control.getScene().getWindow();
                control.setTooltip(tooltip);
                control.setStyle("-fx-border-color: " + ERROR_COLOR + ";");
                tooltip.show(window, window.getX() + control.localToScene(control.getBoundsInLocal()).getMinX() + 5,
                        window.getY() + control.localToScene(control.getBoundsInLocal()).getMaxY() + 5);
            } else {
                // If not in a scene yet, wait for the scene to be available
                control.sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null && newScene.getWindow() != null) {
                        Window window = newScene.getWindow();
                        control.setTooltip(tooltip);
                        control.setStyle("-fx-border-color: " + ERROR_COLOR + ";");
                        tooltip.show(window, window.getX() + control.localToScene(control.getBoundsInLocal()).getMinX() + 5,
                                window.getY() + control.localToScene(control.getBoundsInLocal()).getMaxY() + 5);
                    }
                });
            }
        });
    }

    /**
     * Clears the tooltip and resets the error style of the specified control.
     *
     * @param control The Control to clear the tooltip and reset the style for.
     */
    private static void clearTooltipAndErrorStyle(Control control) {
        Platform.runLater(() -> {
            control.setTooltip(null);
            control.setStyle("");
        });
    }
}