import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MyArray {
    private final int[] data;

    @FunctionalInterface
    interface IntSearch { int apply(int[] arr, int target); }

    @FunctionalInterface
    interface InPlaceSorter { void sort(int[] arr); }

    // ordered values 1..size
    public MyArray(int size) {
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        this.data = new int[size];
        for (int i = 0; i < size; i++) data[i] = i + 1;
    }

    // random values in [0, bound)
    public MyArray(int size, int bound) {
        if (size <= 0 || bound <= 0) throw new IllegalArgumentException("size/bound must be > 0");
        this.data = new int[size];
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) data[i] = r.nextInt(bound);
    }

    public void printArray() { System.out.println(Arrays.toString(data)); }

    // ---- searches from Q1 ----
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == target) return i;
        return -1;
    }
    public static int binarySearchIterative(int[] arr, int target) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] == target) return m;
            if (arr[m] < target) l = m + 1; else r = m - 1;
        }
        return -1;
    }
    public static int binarySearchRecursive(int[] arr, int target) {
        return bs(arr, target, 0, arr.length - 1);
    }
    private static int bs(int[] a, int x, int l, int r) {
        if (l > r) return -1;
        int m = l + (r - l) / 2;
        if (a[m] == x) return m;
        if (x < a[m]) return bs(a, x, l, m - 1);
        return bs(a, x, m + 1, r);
    }

    // ---- non-mutating sort wrappers (return a sorted copy of data) ----
    public int[] bubbleSort() {
        int[] c = Arrays.copyOf(data, data.length);
        bubbleSortInPlace(c);
        return c;
    }
    public int[] insertionSort() {
        int[] c = Arrays.copyOf(data, data.length);
        insertionSortInPlace(c);
        return c;
    }
    public int[] mergeSort() {
        int[] c = Arrays.copyOf(data, data.length);
        mergeSortInPlace(c);
        return c;
    }
    public int[] quickSort() {
        int[] c = Arrays.copyOf(data, data.length);
        quickSortInPlace(c);
        return c;
    }

    // ---- in-place algorithms (used on copies only) ----
    public static void bubbleSortInPlace(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
    public static void insertionSortInPlace(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0 && a[j] > key) { a[j + 1] = a[j]; j--; }
            a[j + 1] = key;
        }
    }
    public static void mergeSortInPlace(int[] a) {
        int[] tmp = new int[a.length];
        ms(a, tmp, 0, a.length - 1);
    }
    private static void ms(int[] a, int[] t, int l, int r) {
        if (l >= r) return;
        int m = (l + r) >>> 1;
        ms(a, t, l, m);
        ms(a, t, m + 1, r);
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) t[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i <= m) t[k++] = a[i++];
        while (j <= r) t[k++] = a[j++];
        for (i = l; i <= r; i++) a[i] = t[i];
    }
    public static void quickSortInPlace(int[] a) { qs(a, 0, a.length - 1); }
    private static void qs(int[] a, int l, int r) {
        if (l >= r) return;
        int i = l, j = r, p = a[l + (r - l) / 2];
        while (i <= j) {
            while (a[i] < p) i++;
            while (a[j] > p) j--;
            if (i <= j) { int t = a[i]; a[i] = a[j]; a[j] = t; i++; j--; }
        }
        if (l < j) qs(a, l, j);
        if (i < r) qs(a, i, r);
    }

    // ---- timing helpers ----
    public long timeSearch(IntSearch fn, int target) {
        long t0 = System.nanoTime();
        int idx = fn.apply(this.data, target);
        long t1 = System.nanoTime();
        if (idx == Integer.MIN_VALUE) System.out.print("");
        return t1 - t0;
    }
    public long timeSort(InPlaceSorter sorter) {
        int[] c = Arrays.copyOf(this.data, this.data.length);
        long t0 = System.nanoTime();
        sorter.sort(c);
        long t1 = System.nanoTime();
        if (c.length == -1) System.out.print("");
        return t1 - t0;
    }

    public static void main(String[] args) {
        // For Q2 use a random array. Adjust size/bound if needed.
        MyArray my = new MyArray(20000, 1_000_000);

        long tBubble = my.timeSort(MyArray::bubbleSortInPlace);
        long tInsertion = my.timeSort(MyArray::insertionSortInPlace);
        long tMerge = my.timeSort(MyArray::mergeSortInPlace);
        long tQuick = my.timeSort(MyArray::quickSortInPlace);

        System.out.println("Sort timings (nanoseconds):");
        System.out.printf("Bubble:    %d%n", tBubble);
        System.out.printf("Insertion: %d%n", tInsertion);
        System.out.printf("Merge:     %d%n", tMerge);
        System.out.printf("Quick:     %d%n", tQuick);

        // Optional: quick search demo still works on the original array
        if (my.data.length <= 100) {
            System.out.print("Original array (unchanged): ");
            my.printArray();
        } else {
            System.out.println("Original array length: " + my.data.length);
        }

        // Uncomment to allow interactive search if you like:
        // Scanner sc = new Scanner(System.in);
        // System.out.print("Enter value to search: ");
        // int target = sc.nextInt();
        // System.out.printf("Linear idx=%d (%d ns)%n",
        //         linearSearch(my.data, target), my.timeSearch(MyArray::linearSearch, target));
