import java.util.Scanner;

public class Heap {
    public static void insert(int[] a, int x, int l) {
        a[l] = x;
        int i = l;
        if (a.length == 2) {
            if (a[1] > a[0]) {
                int k = a[1];
                a[1] = a[0];
                a[0] = k;
            }
        }
        if (a.length > 2) {
            while (i > 0 && a[i] > a[(i - 1) / 2]) {
                int k = a[i];
                a[i] = a[(i - 1) / 2];
                a[(i - 1) / 2] = k;
                i = (i - 1) / 2;
            }
        }
    }
    public static int extract(int[] a, int l) {
        int sym = a[0];
        a[0] = a[l - 1];
        a[l - 1] = 0;
        int i = 0;
        while (true) {
            int j = i;
            int ls = 2 * i + 1;
            int rs = 2 * i + 2;
            if (ls <= l - 2 && a[ls] > a[j]) {
                j = ls;
            }
            if (rs <= l - 2 && a[rs] > a[j]) {
                j = rs;
            }
            if (j == i) {
                break;
            }
            int k = a[i];
            a[i] = a[j];
            a[j] = k;
            i = j;
        }
        return sym;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] a = new int[100000];
        for (int i = 0; i < 100000; i++ ) {
            a[i] = 0;
        }
        int l = 0;
        int n = sc.nextInt();
        while (n > 0) {
            if (sc.nextInt() == 0) {
                insert(a, sc.nextInt(), l);
                l++;
            } else {
                if (l > 0) {
                    System.out.println(extract(a, l));
                    l--;
                }
            }
            n--;
        }
    }
}
