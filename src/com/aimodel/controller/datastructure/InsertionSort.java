
package com.aimodel.controller.datastructure;

import com.aimodel.model.AiModel.ModelData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaviskar  23048648
 */

public class InsertionSort {
    public static List<ModelData> sort(List<ModelData> list, String columnName, boolean ascending) {
        System.out.println("Starting InsertionSort - Column: " + columnName + ", Ascending: " + ascending);
        
        List<ModelData> sortedList = new ArrayList<>(list);
        int n = sortedList.size();

        for (int i = 1; i < n; ++i) {
            ModelData key = sortedList.get(i);
            int j = i - 1;

            // Debug print for comparison
            if (j >= 0) {
                System.out.println("Comparing: " + 
                    sortedList.get(j).getLatency() + " with " + key.getLatency());
            }

            while (j >= 0 && compare(sortedList.get(j), key, columnName, ascending) > 0) {
                sortedList.set(j + 1, sortedList.get(j));
                j = j - 1;
            }
            sortedList.set(j + 1, key);
        }
        
        System.out.println("Finished sorting");
        return sortedList;
    }

    private static int compare(ModelData a, ModelData b, String columnName, boolean ascending) {
        int result;
        
        System.out.println("Comparing with column: " + columnName);
        
        switch (columnName) {
            case "Latency (ms)":
                System.out.println("Comparing latencies: " + a.getLatency() + " vs " + b.getLatency());
                result = Integer.compare(a.getLatency(), b.getLatency());
                break;
            case "Model Name":
                result = compareStrings(a.getName(), b.getName());
                break;
            case "Modality":
                result = compareStrings(a.getModality(), b.getModality());
                break;
            case "Cost Per Token":
                result = Double.compare(a.getCostPerToken(), b.getCostPerToken());
                break;
            case "API Provider":
                result = compareStrings(a.getApiProvider(), b.getApiProvider());
                break;
            default:
                System.out.println("Warning: Unknown column name: " + columnName);
                result = 0;
                break;
        }
        
        return ascending ? result : -result;
    }

    private static int compareStrings(String str1, String str2) {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";
        return str1.toLowerCase().compareTo(str2.toLowerCase());
    }
}