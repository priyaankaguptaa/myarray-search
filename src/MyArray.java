import java.util.Arrays;
import java.util.Scanner;

public class MyArray {
    private final int[] data;

    @FunctionalInterface
    interface IntSearch {
        int apply(int[] arr, int target);
    }

    public MyArray(int size) {
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        this.data = new int[size];
        for (int i = 0; i < size; i++) data[i] = i + 1;
    }

    public void printArray() {
        System.out.println(Arrays.toString(data));
    }

    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == target) return i;
        return -1;
    }

    public static int binarySearchIterative(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    public static int binarySearchRecursive(int[] arr, int target) {
        return binarySearchRecursive(arr, target, 0, arr.length - 1);
    }

    private static int binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left > right) return -1;
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (target < arr[mid]) return binarySearchRecursive(arr, target, left, mid - 1);
        return binarySearchRecursive(arr, target, mid + 1, right);
    }

    public long timeSearch(IntSearch fn, int target) {
        long t0 = System.nanoTime();
        int idx = fn.apply(data, target);
        long t1 = System.nanoTime();
        if (idx == Integer.MIN_VALUE) System.out.print("");
        return t1 - t0;
    }

    public static void main(String[] args) {
        MyArray my = new MyArray(100000);
        if (my.data.length <= 100) my.printArray();
        else System.out.println("Array size: " + my.data.length);

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter value to search: ");
        int target = sc.nextInt();

        int idxL = linearSearch(my.data, target);
        long tL = my.timeSearch(MyArray::linearSearch, target);

        int idxBi = binarySearchIterative(my.data, target);
        long tBi = my.timeSearch(MyArray::binarySearchIterative, target);

        int idxBr = binarySearchRecursive(my.data, target);
        long tBr = my.timeSearch(MyArray::binarySearchRecursive, target);

        System.out.printf("Linear: index=%d, time=%d ns%n", idxL, tL);
        System.out.printf("Binary (iter): index=%d, time=%d ns%n", idxBi, tBi);
        System.out.printf("Binary (rec): index=%d, time=%d ns%n", idxBr, tBr);
        if (idxL == -1 && idxBi == -1 && idxBr == -1) System.out.println("Item not found");
    }
}
