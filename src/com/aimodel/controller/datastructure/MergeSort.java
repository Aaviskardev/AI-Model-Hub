

package com.aimodel.controller.datastructure;

import com.aimodel.model.AiModel.ModelData;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Merge Sort algorithm for a list of ModelData objects.
 *
 * @author Aaviskar 23048648
 */
public class MergeSort {

    /**
     * Sorts a list of ModelData objects based on the specified column and sort order.
     *
     * @param list       The list of ModelData objects to be sorted.
     * @param columnName The name of the column to sort by (e.g., "Model Name", "Latency (ms)").
     * @param ascending  True for ascending order, false for descending order.
     * @return A new list containing the sorted ModelData objects.
     */
    public static List<ModelData> sort(List<ModelData> list, String columnName, boolean ascending) {
        List<ModelData> sortedList = new ArrayList<>(list);
        mergeSort(sortedList, 0, sortedList.size() - 1, columnName, ascending);
        return sortedList;
    }

    /**
     * Recursively sorts a sublist of ModelData objects using the Merge Sort algorithm.
     *
     * @param list       The list containing the sublist to be sorted.
     * @param l          The starting index of the sublist.
     * @param r          The ending index of the sublist.
     * @param columnName The name of the column to sort by.
     * @param ascending  True for ascending order, false for descending order.
     */
    private static void mergeSort(List<ModelData> list, int l, int r, String columnName, boolean ascending) {
        if (l < r) {
            // Find the middle point.
            int m = l + (r - l) / 2;

            // Sort first and second halves recursively.
            mergeSort(list, l, m, columnName, ascending);
            mergeSort(list, m + 1, r, columnName, ascending);

            // Merge the sorted halves.
            merge(list, l, m, r, columnName, ascending);
        }
    }

    /**
     * Merges two sorted sublists of ModelData objects.
     *
     * @param list       The list containing the sublists to be merged.
     * @param l          The starting index of the first sublist.
     * @param m          The ending index of the first sublist and the middle point.
     * @param r          The ending index of the second sublist.
     * @param columnName The name of the column to sort by.
     * @param ascending  True for ascending order, false for descending order.
     */
    private static void merge(List<ModelData> list, int l, int m, int r, String columnName, boolean ascending) {
        // Find sizes of two subarrays to be merged.
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temporary lists.
        List<ModelData> L = new ArrayList<>(n1);
        List<ModelData> R = new ArrayList<>(n2);

        // Copy data to temporary lists.
        for (int i = 0; i < n1; ++i)
            L.add(list.get(l + i));
        for (int j = 0; j < n2; ++j)
            R.add(list.get(m + 1 + j));

        // Initial indexes of the two subarrays and the merged array.
        int i = 0, j = 0, k = l;

        // Merge the two subarrays back into the original list.
        while (i < n1 && j < n2) {
            if (compare(L.get(i), R.get(j), columnName, ascending) <= 0) {
                list.set(k, L.get(i));
                i++;
            } else {
                list.set(k, R.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[], if any.
        while (i < n1) {
            list.set(k, L.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of R[], if any.
        while (j < n2) {
            list.set(k, R.get(j));
            j++;
            k++;
        }
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
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        int len1 = str1.length();
        int len2 = str2.length();
        int lim = Math.min(len1, len2);

        for (int k = 0; k < lim; k++) {
            char c1 = str1.charAt(k);
            char c2 = str2.charAt(k);
            if (c1 != c2) {
                return c1 - c2;
            }
        }
        return len1 - len2;
    }
}
