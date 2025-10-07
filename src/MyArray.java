import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MyArray {
    private final int[] data;

    public MyArray(int size) {
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        this.data = new int[size];
        for (int i = 0; i < size; i++) data[i] = i + 1;
    }

    public MyArray(int size, int bound) {
        if (size <= 0 || bound <= 0) throw new IllegalArgumentException("size/bound must be > 0");
        this.data = new int[size];
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) data[i] = r.nextInt(bound);
    }

    public void printArray() {
        System.out.println(Arrays.toString(data));
    }

    // sort a copy so original stays unchanged
    public int[] bubbleSortCopy() {
        int[] a = Arrays.copyOf(data, data.length);
        bubbleSort(a);
        return a;
    }

    public int[] quickSortCopy() {
        int[] a = Arrays.copyOf(data, data.length);
        quickSort(a, 0, a.length - 1);
        return a;
    }

    // bubble sort (in place)
    private static void bubbleSort(int[] a) {
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

    // quick sort (in place)
    private static void quickSort(int[] a, int l, int r) {
        if (l >= r) return;
        int i = l, j = r, p = a[l + (r - l) / 2];
        while (i <= j) {
            while (a[i] < p) i++;
            while (a[j] > p) j--;
            if (i <= j) {
                int t = a[i]; a[i] = a[j]; a[j] = t;
                i++; j--;
            }
        }
        if (l < j) quickSort(a, l, j);
        if (i < r) quickSort(a, i, r);
    }

    public static void main(String[] args) {
        MyArray arr = new MyArray(5000, 1_000_000);

        int[] b1 = Arrays.copyOf(arr.data, arr.data.length);
        long t0 = System.nanoTime();
        bubbleSort(b1);
        long t1 = System.nanoTime();

        int[] b2 = Arrays.copyOf(arr.data, arr.data.length);
        long t2 = System.nanoTime();
        quickSort(b2, 0, b2.length - 1);
        long t3 = System.nanoTime();

        System.out.println("Bubble: " + (t1 - t0) + " ns");
        System.out.println("Quick: " + (t3 - t2) + " ns");
        System.out.println("Original length: " + arr.data.length);
    }
}
