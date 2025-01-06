//
//
//package com.aimodel.util;
//
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import com.aimodel.model.AiModel;
///**
// *
// * @author Aaviskar  23048648
// */
//
//public class ValidationUtil {
//
//    public static boolean validateIdField(TextField idField, AiModel aiModel, Label idValidationLabel) {
//        String id = idField.getText();
//        if (id.isEmpty()) {
//            idValidationLabel.setText("ID cannot be empty");
//            return false;
//        }
//        if (aiModel.getModelDataList().stream()
//                .anyMatch(existingModel -> existingModel.getId().equals(id))) {
//            idValidationLabel.setText("ID already exists");
//            return false;
//        }
//        if (!id.matches("[a-zA-Z0-9]+")) {
//            idValidationLabel.setText("ID can only contain letters and numbers");
//            return false;
//        }
//        if (id.length() > 10) {
//            idValidationLabel.setText("ID is too long (max 10 characters)");
//            return false;
//        }
//        idValidationLabel.setText("");
//        return true;
//    }
//
//    public static boolean validateNameField(TextField nameField, Label nameValidationLabel) {
//        String name = nameField.getText();
//
//        if (name.isEmpty()) {
//            nameValidationLabel.setText("Name cannot be empty");
//            return false;
//        }
//
//        // Check if the name contains at least one letter or special character
//        if (name.matches("\\d+")) { // Matches strings with only digits
//            nameValidationLabel.setText("Name cannot be only numbers");
//            return false;
//        }
//
//        if (name.length() > 50) {
//            nameValidationLabel.setText("Name is too long (max 50 characters)");
//            return false;
//        }
//
//        nameValidationLabel.setText(""); // Clear validation message if valid
//        return true;
//    }
//
//    public static boolean validateModalityField(TextField modalityField, Label modalityValidationLabel) {
//        String modality = modalityField.getText();
//        if (modality.isEmpty()) {
//            modalityValidationLabel.setText("Modality cannot be empty");
//            return false;
//        }
//        if (!modality.matches("[a-zA-Z]+")) {
//            modalityValidationLabel.setText("Modality must be a word");
//            return false;
//        }
//        if (modality.length() > 20) {
//            modalityValidationLabel.setText("Modality is too long (max 20 characters)");
//            return false;
//        }
//        modalityValidationLabel.setText("");
//        return true;
//    }
//
//    public static boolean validateLatencyField(TextField latencyField, Label latencyValidationLabel) {
//        String latency = latencyField.getText();
//        if (latency.isEmpty()) {
//            latencyValidationLabel.setText("Latency cannot be empty");
//            return false;
//        }
//        try {
//            int latencyValue = Integer.parseInt(latency);
//            if (latencyValue <= 0) {
//                latencyValidationLabel.setText("Latency must be a positive number");
//                return false;
//            }
//            if (latencyValue > 10000) {
//                latencyValidationLabel.setText("Latency is too high (max 10000 ms)");
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            latencyValidationLabel.setText("Invalid latency format");
//            return false;
//        }
//        latencyValidationLabel.setText("");
//        return true;
//    }
//
//    public static boolean validateCostField(TextField costField, Label costValidationLabel) {
//        String cost = costField.getText();
//        if (cost.isEmpty()) {
//            costValidationLabel.setText("Cost cannot be empty");
//            return false;
//        }
//        try {
//            double costValue = Double.parseDouble(cost);
//            if (costValue < 0) {
//                costValidationLabel.setText("Cost cannot be negative");
//                return false;
//            }
//            if (costValue > 100) {
//                costValidationLabel.setText("Cost is too high (max $100)");
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            costValidationLabel.setText("Invalid cost format");
//            return false;
//        }
//        costValidationLabel.setText("");
//        return true;
//    }
//
//    public static boolean validateProviderField(TextField providerField, Label providerValidationLabel) {
//        String provider = providerField.getText();
//        if (provider.isEmpty()) {
//            providerValidationLabel.setText("Provider cannot be empty");
//            return false;
//        }
//        if (!provider.matches("[a-zA-Z\\s]+")) {
//            providerValidationLabel.setText("Provider must be a word or words");
//            return false;
//        }
//        if (provider.length() > 30) {
//            providerValidationLabel.setText("Provider name is too long (max 30 characters)");
//            return false;
//        }
//        providerValidationLabel.setText("");
//        return true;
//    }
//}




package com.aimodel.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.aimodel.model.AiModel;

/**
 * Utility class for validating input fields in the AI model application.
 *
 * @author Aaviskar 23048648
 */
public class ValidationUtil {

    /**
     * Validates the ID field.
     *
     * @param idField           The TextField for the ID.
     * @param aiModel           The AiModel instance to check for ID uniqueness.
     * @param idValidationLabel The Label to display validation messages.
     * @return True if the ID is valid, false otherwise.
     */
    public static boolean validateIdField(TextField idField, AiModel aiModel, Label idValidationLabel) {
        String id = idField.getText();

        // Check if ID is empty
        if (id.isEmpty()) {
            idValidationLabel.setText("ID cannot be empty");
            return false;
        }

        // Check if ID already exists in the model data list
        if (aiModel.getModelDataList().stream()
                .anyMatch(existingModel -> existingModel.getId().equals(id))) {
            idValidationLabel.setText("ID already exists");
            return false;
        }

        // Check if ID contains only numeric characters
        if (!id.matches("[0-9]+")) {
            idValidationLabel.setText("ID can only contain numbers");
            return false;
        }

        // Check if ID length exceeds the maximum limit
        if (id.length() > 10) {
            idValidationLabel.setText("ID is too long (max 10 characters)");
            return false;
        }

        // Clear validation message if ID is valid
        idValidationLabel.setText("");
        return true;
    }

    /**
     * Validates the name field.
     *
     * @param nameField           The TextField for the name.
     * @param nameValidationLabel The Label to display validation messages.
     * @return True if the name is valid, false otherwise.
     */
    public static boolean validateNameField(TextField nameField, Label nameValidationLabel) {
        String name = nameField.getText();

        // Check if name is empty
        if (name.isEmpty()) {
            nameValidationLabel.setText("Name cannot be empty");
            return false;
        }

        // Check if the name contains only numbers
        if (name.matches("\\d+")) {
            nameValidationLabel.setText("Name cannot be only numbers");
            return false;
        }

        // Check if name length exceeds the maximum limit
        if (name.length() > 50) {
            nameValidationLabel.setText("Name is too long (max 50 characters)");
            return false;
        }

        // Clear validation message if name is valid
        nameValidationLabel.setText("");
        return true;
    }

    /**
     * Validates the modality field.
     *
     * @param modalityField           The TextField for the modality.
     * @param modalityValidationLabel The Label to display validation messages.
     * @return True if the modality is valid, false otherwise.
     */
    public static boolean validateModalityField(TextField modalityField, Label modalityValidationLabel) {
        String modality = modalityField.getText();

        // Check if modality is empty
        if (modality.isEmpty()) {
            modalityValidationLabel.setText("Modality cannot be empty");
            return false;
        }

        // Check if modality contains only letters
        if (!modality.matches("[a-zA-Z]+")) {
            modalityValidationLabel.setText("Modality must be a word");
            return false;
        }

        // Check if modality length exceeds the maximum limit
        if (modality.length() > 20) {
            modalityValidationLabel.setText("Modality is too long (max 20 characters)");
            return false;
        }

        // Clear validation message if modality is valid
        modalityValidationLabel.setText("");
        return true;
    }

    /**
     * Validates the latency field.
     *
     * @param latencyField           The TextField for the latency.
     * @param latencyValidationLabel The Label to display validation messages.
     * @return True if the latency is valid, false otherwise.
     */
    public static boolean validateLatencyField(TextField latencyField, Label latencyValidationLabel) {
        String latency = latencyField.getText();

        // Check if latency is empty
        if (latency.isEmpty()) {
            latencyValidationLabel.setText("Latency cannot be empty");
            return false;
        }

        // Check if latency is a valid positive integer within the allowed range
        try {
            int latencyValue = Integer.parseInt(latency);
            if (latencyValue <= 0) {
                latencyValidationLabel.setText("Latency must be a positive number");
                return false;
            }
            if (latencyValue > 10000) {
                latencyValidationLabel.setText("Latency is too high (max 10000 ms)");
                return false;
            }
        } catch (NumberFormatException e) {
            latencyValidationLabel.setText("Invalid latency format");
            return false;
        }

        // Clear validation message if latency is valid
        latencyValidationLabel.setText("");
        return true;
    }

    /**
     * Validates the cost field.
     *
     * @param costField           The TextField for the cost.
     * @param costValidationLabel The Label to display validation messages.
     * @return True if the cost is valid, false otherwise.
     */
    public static boolean validateCostField(TextField costField, Label costValidationLabel) {
        String cost = costField.getText();

        // Check if cost is empty
        if (cost.isEmpty()) {
            costValidationLabel.setText("Cost cannot be empty");
            return false;
        }

        // Check if cost is a valid non-negative double within the allowed range
        try {
            double costValue = Double.parseDouble(cost);
            if (costValue < 0) {
                costValidationLabel.setText("Cost cannot be negative");
                return false;
            }
            if (costValue > 10000) {
                costValidationLabel.setText("Cost is too high (max $10000)");
                return false;
            }
        } catch (NumberFormatException e) {
            costValidationLabel.setText("Invalid cost format");
            return false;
        }

        // Clear validation message if cost is valid
        costValidationLabel.setText("");
        return true;
    }

    /**
     * Validates the provider field.
     *
     * @param providerField           The TextField for the provider.
     * @param providerValidationLabel The Label to display validation messages.
     * @return True if the provider is valid, false otherwise.
     */
    public static boolean validateProviderField(TextField providerField, Label providerValidationLabel) {
        String provider = providerField.getText();

        // Check if provider is empty
        if (provider.isEmpty()) {
            providerValidationLabel.setText("Provider cannot be empty");
            return false;
        }

        // Check if provider contains only letters and spaces
        if (!provider.matches("[a-zA-Z\\s]+")) {
            providerValidationLabel.setText("Provider must be a word or words");
            return false;
        }

        // Check if provider length exceeds the maximum limit
        if (provider.length() > 30) {
            providerValidationLabel.setText("Provider name is too long (max 30 characters)");
            return false;
        }

        // Clear validation message if provider is valid
        providerValidationLabel.setText("");
        return true;
    }
}