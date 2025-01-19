
package com.aimodel.controller.datastructure;

import com.aimodel.model.AiModel.ModelData;
import java.util.List;

/**
 *
 * @author Aaviskar  23048648
 */

/**
 * Implements the Binary Search algorithm for searching a sorted list of ModelData objects.
 * The search is case-insensitive and finds exact matches only.
 */
public class BinarySearch {

    /**
     * Searches for a ModelData object with the exact name in a sorted list (case-insensitive).
     *
     * @param list The sorted list of ModelData objects (sorted by name).
     * @param name The name to search for (case-insensitive).
     * @return The index of the ModelData object if found, or -1 if not found.
     */
    public static int search(List<ModelData> list, String name) {
        int l = 0;
        int r = list.size() - 1;
        name = name.toLowerCase(); // Convert search string to lowercase

        while (l <= r) {
            int m = l + (r - l) / 2;
            ModelData midModel = list.get(m);
            String midName = midModel.getName().toLowerCase(); // Convert model name to lowercase

            // Check for an exact match
            if (midName.equals(name)) {
                return m;
            }

            // If the search string is lexicographically greater, ignore the left half
            if (midName.compareTo(name) < 0) {
                l = m + 1;
            }
            // If the search string is lexicographically smaller, ignore the right half
            else {
                r = m - 1;
            }
        }

        return -1; // Not found
    }
}


