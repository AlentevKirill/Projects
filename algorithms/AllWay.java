import java.util.Scanner;

public class AllWay {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = sc.nextInt();
            }
        }
        if (n != 1) {
            int c = (int) Math.pow(2, n);
            int[][] d = new int[c][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < c; j++) {
                    d[j][i] = Integer.MAX_VALUE;
                }
            }
            d[0][0] = 0;
            int[][] way = new int[c][n];
            for (int i = 0; i < c; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][j] == Integer.MAX_VALUE || d[i][j] < 0) {
                        continue;
                    }
                    for (int k = 0; k < n; k++) {
                        if (((i & (1 << k)) == 0)) {
                            if (d[i][j] + a[j][k] < d[i | (1 << k)][k]) {
                                way[i | (1 << k)][k] = j;
                            }
                            d[i | (1 << k)][k] = Math.min(d[i | (1 << k)][k], d[i][j] + a[j][k]);
                        }
                    }
                }
            }
            int iMin = 0;
            for (int i = 0; i < n; i++) {
                System.out.print(d[c - 1][i] + " ");
                iMin = d[c - 1][i] < d[c - 1][iMin] ? i : iMin;
            }
            System.out.println();
            System.out.println(iMin);
            System.out.println(d[c - 1][iMin]);
            StringBuilder string = new StringBuilder();
            int beg = iMin;
            while (true) {
                string.append((beg + 1) + " ");
                int temp = 1 << beg;
                beg = way[c - 1][beg];
                c = c - temp;
                if (c == 0) {
                    break;
                }
            }
            int r = string.lastIndexOf(" ", string.length() - 2);
            int p = string.lastIndexOf(" ", r - 1);
            for (int i = 0; i < n; i++) {
                int l = string.lastIndexOf(" ", r - 1);
                System.out.print(string.substring(l + 1, r) + " ");
                r = l;
            }
        } else {
            System.out.println(0);
            System.out.println(1);
        }
    }
}
