package search;

//import java.util.Scanner;

public class BinarySearchMin {

    private static int result;

    // Pred: a.length > 1 && (forall i = 0..n, j = 0..n: a[i] == a[j] -> i == j) && Существует единственный i = 0..n : начиная с него a[(j + 1) % n] > a[j % n] j = 0..n
    // Post: (result <= a[i]) && (i >= 0 && i < n)
    public static int binSearchIterate(int[] a) {
        // a.length > 1
        int n = a.length;
        int left = -1;
        int right = n;
        // a.length > 1 && left < right
        // Inv a.length > 1 && left <= right && (a - со сдвигом) -> ((a[left] > a[n - 1]) && (a[right] <= a[n - 1])) && (a - без сдвига) -> (left == -1)
        while (left < right - 1) {
            // a.length > 1 && left < right - 1
            int mid = (left + right) / 2;
            // a.length > 1 && left < right - 1 && (mid < right) && (mid > length)
            if (a[mid] > a[n - 1]) {
                // a.length > 1 && left < right - 1 && (mid < right) && (mid > length) && (a[mid] > a[n - 1])
                left = mid;
            } else {
                // a.length > 1 && left < right - 1 && (mid < right) && (mid > length) && (a[mid] < a[n - 1])
                right = mid;
            }
        }
        // a.length > 1 && (a - без сдвигом) -> left == -1
        if (left == -1) {
            // a.length > 1 && (a - без сдвига) && left == -1
            left = 0;
            // a.length > 1 && (a - без сдвига) && left == 0
        }
        // a.length > 1 && left >= 0 && ((left == 0) || (a[left] < a[left - 1] && a[left] < a[left + 1]) || (a[left + 1] < a[left] && a[left + 1] < a[left + 2])
        if (a[left] > a[left + 1]) {
            // a.length > 1 && (a[left + 1] < a[left] && a[left + 1] < a[left + 2])
            result = a[left + 1];
        } else {
            // a.length > 1 && left >= 0 && ((left == 0) || (a[left] < a[left - 1] && a[left] < a[left + 1])
            result = a[left];
        }
        // a.length > 1 && ((a[result] < a[i]) && ((i >= 0) && (i < n)))
        return result;
    }

    // Pred: a.length > 1 && left < right && left >= -1 && right <= a.length && && (forall i = 0..n, j = 0..n: a[i] == a[j] -> i == j) && Существует единственный i = 0..n : начиная с него a[(j + 1) % n] > a[j % n] j = 0..n
    // Post: (result <= a[i]) && (i > left && i < right)
    public static void binSearchRecurs(int[] a, int left, int right) {
        int n = a.length;
        // n > 1
        int mid = (left + right) / 2;
        // n > 1 && mid > left && mid < right
        if (left < right - 1) {
            // n > 1 && left < right - 1 && (mid > left && mid < right)
            if (a[mid] > a[n - 1]) {
                // n > 1 && a[mid] > a[n - 1] && mid > left && mid < right && (a[mid] > a[i] && (i >= 0 && i < mid))
                binSearchRecurs(a, mid, right);
            } else {
                // n > 1 && a[mid] < a[n - 1] && mid > left && mid < right && (a[mid] < a[i] && (i > mid && i < n - 1))
                binSearchRecurs(a, left, mid);
            }
        } else {
            // a.length > 1 && (a - без сдвигом) -> left == -1
            if (left == -1) {
                // a.length > 1 && (a - без сдвига) && left == -1
                left = 0;
                // a.length > 1 && (a - без сдвига) && left == 0
            }
            // a.length > 1 && left >= 0 && ((left == 0) || (a[left] < a[left - 1] && a[left] < a[left + 1]) || (a[left + 1] < a[left] && a[left + 1] < a[left + 2])
            if (a[left] > a[left + 1]) {
                // a.length > 1 && (a[left + 1] < a[left] && a[left + 1] < a[left + 2])
                result = a[left + 1];
            } else {
                // a.length > 1 && left >= 0 && ((left == 0) || (a[left] < a[left - 1] && a[left] < a[left + 1])
                result = a[left];
            }
            // a.length > 1 && ((a[result] < a[i]) && ((i >= 0) && (i < n)))
        }
    }



    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        int n = args.length;
        //int n = 5;
        int [] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(args[i]);
            //a[i] = sc.nextInt();
        }
        if (n < 2) {
            if (n == 0) {
                System.out.println(0);
            } else {
                System.out.println(a[0]);
            }
        } else {
            //binSearchRecurs(a, -1, n);
            //System.out.println(result);
            System.out.println(binSearchIterate(a));
        }
    }
}
