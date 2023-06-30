package search;

public class BinarySearch {

    private static int result;
    // Pred: a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length))
    // Post: // (x >= a[0]) || (x < a[a.length - 1]) || ((x <= a[l]) && (x >= a[r]) && (l,r >= 0 && l,r < a.length) && (r - l < 2))
    public static int binSearchIterate(int[] a, int x) {
        // Pred: a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length))
        int n = a.length;
        // n > 1
        int m = n / 2;
        // m >= 1
        int l = 0;
        int r = n - 1;
        // r > l && m >= 1 && n > 1
        // r > l && m >= 1 && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length))
        if (x >= a[l]) {
            // r > l && m >= 1 && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[0]
            return 0;
        }
        // r > l && m >= 1 && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x < a[0]
        if (x < a[r]) {
            // r > l && m >= 1 && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x < a[n - 1]
            return n;
        }
        // r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[n - 1] && x < a[0]
        // Inv: r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l]
        while (true) {
            // r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l]
            if (x >= a[m]) {
                // r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l] && a[m] <= x
                r = m;
            } else {
                // r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l] && a[m] > x
                l = m;
            }
            // Inv
            m = (l + r) / 2;
            // Inv
            if (r - l < 2) {
                // ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l] && r - l < 2
                if (a[l] <= x) {
                    // && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x == a[l] && r - l < 2
                    return l;
                } else {
                    // ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x < a[l] && r - l < 2
                    return r;
                }
                // ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x < a[l] && r - l < 2
            }
            // r > l && ((m <= r) && (l <= m)) && n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x < a[l] && r - l >= 2
        }
        // n > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && x >= a[r] && x <= a[l] && r - l < 2
    }

    // Pred: a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && r > l
    // Post: (result == 0) || (result == a.length) || ((a[result] <= x) && ( a[result - 1] > x) && ((result > 0) && (result <= a.length)
    public static void binSearchRecurs(int[] a, int l, int r, int x) {
        int m = (l + r) / 2;
        // r > l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l)))
        if (r - l < 2) {
            // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2
            if (a[l] <= x) {
                // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[l] <= x
                result = l;
                // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[result] <= x
            } else {
                // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[l] > x
                if (x < a[r]) {
                    // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[r] > x
                    result = r + 1;
                    // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && (a[r] > x -> a[a.length - 1] > x -> result == a.length
                } else {
                    // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[r] <= x
                    result = r;
                    // r >= l && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && r - l < 2 && a[result] <= x
                }
            }
            return;
        }
        // r >= l + 2 && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l)))
        if (x >= a[m]) {
            // r >= l + 2 && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && x >= a[m]
            binSearchRecurs(a, l, m, x);
        } else {
            // r >= l + 2 && a.length > 1 && ((a[i] <= a[i - 1]) && (i > 0 && i < a.length)) && (((m <= r) && (m > l)) || ((m < r) && (m >= l))) && x < a[m]
            binSearchRecurs(a, m, r, x);
        }
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int n = args.length - 1;
        int [] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        if (n < 2) {
            if (n == 0) {
                System.out.println(n);
            }
            if (n == 1) {
                if (a[0] <= x) {
                    System.out.println(0);
                } else {
                    System.out.println(1);
                }
            }
        } else {
            System.out.println(binSearchIterate(a, x));
            //binSearchRecurs(a, 0, n - 1, x);
            //System.out.println(result);
        }
    }
}
