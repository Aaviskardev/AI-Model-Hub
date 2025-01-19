

package com.aimodel.controller.datastructure;

import com.aimodel.model.AiModel.ModelData;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Selection Sort algorithm for a list of ModelData objects.
 *
 * @author Aaviskar 23048648
 */
public class SelectionSort {

    /**
     * Sorts a list of ModelData objects based on the specified column and sort order.
     *
     * @param list       The list of ModelData objects to be sorted.
     * @param columnName The name of the column to sort by (e.g., "Model Name", "Latency (ms)").
     * @param ascending  True for ascending order, false for descending order.
     * @return A new list containing the sorted ModelData objects.
     */
    public static List<ModelData> sort(List<ModelData> list, String columnName, boolean ascending) {
        // Create a copy of the list to avoid modifying the original list.
        List<ModelData> sortedList = new ArrayList<>(list);
        int n = sortedList.size();

        // One by one move boundary of unsorted subarray.
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted array.
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (compare(sortedList.get(j), sortedList.get(minIdx), columnName, ascending) < 0) {
                    minIdx = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part.
            ModelData temp = sortedList.get(minIdx);
            sortedList.set(minIdx, sortedList.get(i));
            sortedList.set(i, temp);
        }
        return sortedList;
    }

    /**
     * Compares two ModelData objects based on the specified column and sort order.
     *
     * @param a          The first ModelData object.
     * @param b          The second ModelData object.
     * @param columnName The name of the column to compare.
     * @param ascending  True for ascending order, false for descending order.
     * @return A negative integer, zero, or a positive integer as the first argument
     *         is less than, equal to, or greater than the second, considering the sort order.
     */
    private static int compare(ModelData a, ModelData b, String columnName, boolean ascending) {
        int result = 0;
        switch (columnName) {
            case "Model Name":
                result = compareStrings(a.getName(), b.getName());
                break;
            case "Modality":
                result = compareStrings(a.getModality(), b.getModality());
                break;
            case "Latency (ms)":
                result = Integer.compare(a.getLatency(), b.getLatency());
                break;
            case "Cost Per Token":
                result = Double.compare(a.getCostPerToken(), b.getCostPerToken());
                break;
            case "API Provider":
                result = compareStrings(a.getApiProvider(), b.getApiProvider());
                break;
        }
        // Reverse the result if descending order is specified.
        return ascending ? result : -result;
    }

    /**
     * Compares two strings lexicographically, ignoring case.
     *
     * @param str1 The first string.
     * @param str2 The second string.
     * @return A negative integer, zero, or a positive integer as the first string
     *         is less than, equal to, or greater than the second, ignoring case.
     */
    private static int compareStrings(String str1, String str2) {
        // Convert strings to lowercase for case-insensitive comparison.
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        int len1 = str1.length();
        int len2 = str2.length();
        int lim = Math.min(len1, len2);
        
        // Compare characters one by one.
        for (int k = 0; k < lim; k++) {
            char c1 = str1.charAt(k);
            char c2 = str2.charAt(k);
            if (c1 != c2) {
                return c1 - c2;
            }
        }
        
        // If all characters are equal up to the minimum length, the shorter string is considered smaller.
        return len1 - len2;
    }
}

