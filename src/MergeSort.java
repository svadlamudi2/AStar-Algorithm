import java.lang.reflect.Array;
import java.util.ArrayList;

public class MergeSort {
    public MergeSort(){};

    public void merge(ArrayList<Node> arr, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        Node[] L = new Node[n1];
        Node[] R = new Node[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; i++)
            L[i] = arr.get(l + i);
        for (int j = 0; j < n2; j++)
            R[j] = arr.get(m + 1 + j);

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i].fCost <= R[j].fCost) {
                arr.set(k, L[i]);
                i++;
            }
            else {
                arr.set(k, R[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr.set(k, L[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr.set(k, R[j]);
            j++;
            k++;
        }
    }

    public void sort(ArrayList<Node> arr, int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    public void printArray(ArrayList<Integer> arr) {
        for(Integer aint: arr) {
            System.out.printf("%d ", aint);
        }
        System.out.println();
    }
}
