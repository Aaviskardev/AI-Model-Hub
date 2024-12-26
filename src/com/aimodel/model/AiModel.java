package com.aimodel.model;

import java.util.ArrayList;

/**
 * Represents the main model for managing AI model data and tracking recent actions.
 */
public class AiModel {
    // List of listeners to be notified of model data changes
    private ArrayList<AiModelListener> listeners = new ArrayList<>();
    // List to store model data
    private ArrayList<ModelData> modelDataList;
    // List to track recent actions performed on the model
    private ArrayList<String> recentActions;

    /**
     * Constructor for AiModel.
     * Initializes the model data list and recent actions list.
     * Adds sample data to the model data list.
     */
    public AiModel() {
        modelDataList = new ArrayList<>();
        recentActions = new ArrayList<>();
        // Sample data for demonstration
        modelDataList.add(new ModelData("1", "GPT-01(preview)", "Multimodal", 3, 15, "OpenAI"));
        modelDataList.add(new ModelData("2", "Gemini 2.0 Flash", "Multimodal", 5, 5, "Google AI"));
        modelDataList.add(new ModelData("3", "GPT-03", "Multimodal", 4, 2000, "OpenAI"));
        modelDataList.add(new ModelData("4", "GPT-40", "Multimodal", 2, 0.15, "OpenAI"));
        modelDataList.add(new ModelData("5", "Sora", "Video", 6, 50, "OpenAI"));
    }

    /**
     * Interface for listeners that need to be notified of model data changes.
     */
    public interface AiModelListener {
        /**
         * Called when the model data changes.
         */
        void onModelDataChanged();
    }

    /**
     * Adds a new model to the model data list.
     *
     * @param modelData The ModelData object representing the new model.
     */
    public void addModel(ModelData modelData) {
        modelDataList.add(modelData);
        // Add action to recent actions list
        addRecentAction("Added model: " + modelData.getName());
        // Notify listeners of the change
        notifyListeners();
    }

    /**
     * Updates an existing model in the model data list.
     *
     * @param updatedModelData The ModelData object representing the updated model.
     */
    public void updateModel(ModelData updatedModelData) {
        // Find the model to update by ID
        for (int i = 0; i < modelDataList.size(); i++) {
            if (modelDataList.get(i).getId().equals(updatedModelData.getId())) {
                // Update the model
                modelDataList.set(i, updatedModelData);
                break;
            }
        }
        // Add action to recent actions list
        addRecentAction("Updated model: " + updatedModelData.getName());
        // Notify listeners of the change
        notifyListeners();
    }

    /**
     * Deletes a model from the model data list by its ID.
     *
     * @param modelId The ID of the model to delete.
     */
    public void deleteModel(String modelId) {
        ModelData modelToDelete = null;
        // Find the model to delete by ID
        for (ModelData model : modelDataList) {
            if (model.getId().equals(modelId)) {
                modelToDelete = model;
                break;
            }
        }
        // Remove the model if found
        if (modelToDelete != null) {
            modelDataList.remove(modelToDelete);
            // Add action to recent actions list
            addRecentAction("Deleted model: " + modelToDelete.getName());
        }
        // Notify listeners of the change
        notifyListeners();
    }

    /**
     * Adds a recent action to the recent actions list.
     * Maintains a maximum of 10 recent actions.
     *
     * @param action The action to add.
     */
    public void addRecentAction(String action) {
        // Remove the oldest action if the list exceeds 10 elements
        if (recentActions.size() > 10) {
            recentActions.remove(0);
        }
        // Add the new action
        recentActions.add(action);
    }

    /**
     * Retrieves a copy of the recent actions list.
     *
     * @return A new ArrayList containing the recent actions.
     */
    public ArrayList<String> getRecentActions() {
        // Return a copy to prevent external modification of the original list
        return new ArrayList<>(recentActions);
    }

    /**
     * Retrieves the list of model data.
     *
     * @return The ArrayList of ModelData objects.
     */
    public ArrayList<ModelData> getModelDataList() {
        return modelDataList;
    }

    /**
     * Adds a listener to the list of listeners.
     *
     * @param listener The AiModelListener to add.
     */
    public void addListener(AiModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the list of listeners.
     *
     * @param listener The AiModelListener to remove.
     */
    public void removeListener(AiModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners about a change in the model data.
     */
    private void notifyListeners() {
        for (AiModelListener listener : listeners) {
            listener.onModelDataChanged();
        }
    }

    /**
     * Represents data for a single AI model.
     */
    public static class ModelData {
        private String id;
        private String name;
        private String modality;
        private int latency;
        private double costPerToken;
        private String apiProvider;

        /**
         * Constructor for ModelData.
         *
         * @param id           The unique identifier for the model.
         * @param name         The name of the model.
         * @param modality     The modality of the model (e.g., text, image, multimodal).
         * @param latency      The latency of the model in milliseconds.
         * @param costPerToken The cost per token for using the model.
         * @param apiProvider  The API provider for the model.
         */
        public ModelData(String id, String name, String modality, int latency, double costPerToken, String apiProvider) {
            this.id = id;
            this.name = name;
            this.modality = modality;
            this.latency = latency;
            this.costPerToken = costPerToken;
            this.apiProvider = apiProvider;
        }

        /**
         * Gets the ID of the model.
         *
         * @return The model ID.
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the name of the model.
         *
         * @return The model name.
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the modality of the model.
         *
         * @return The model modality.
         */
        public String getModality() {
            return modality;
        }

        /**
         * Gets the latency of the model.
         *
         * @return The model latency.
         */
        public int getLatency() {
            return latency;
        }

        /**
         * Gets the cost per token for the model.
         *
         * @return The cost per token.
         */
        public double getCostPerToken() {
            return costPerToken;
        }

        /**
         * Gets the API provider for the model.
         *
         * @return The API provider.
         */
        public String getApiProvider() {
            return apiProvider;
        }

        /**
         * Sets the ID of the model.
         *
         * @param id The model ID to set.
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Sets the name of the model.
         *
         * @param name The model name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Sets the modality of the model.
         *
         * @param modality The model modality to set.
         */
        public void setModality(String modality) {
            this.modality = modality;
        }

        /**
         * Sets the latency of the model.
         *
         * @param latency The model latency to set.
         */
        public void setLatency(int latency) {
            this.latency = latency;
        }

        /**
         * Sets the cost per token for the model.
         *
         * @param costPerToken The cost per token to set.
         */
        public void setCostPerToken(double costPerToken) {
            this.costPerToken = costPerToken;
        }

        /**
         * Sets the API provider for the model.
         *
         * @param apiProvider The API provider to set.
         */
        public void setApiProvider(String apiProvider) {
            this.apiProvider = apiProvider;
        }
    }
}