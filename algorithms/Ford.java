import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ford {

    public static class Scanner {
        private final Reader r;
        private int sy = 0;
        private int check = 0;

        public Scanner(InputStream in) throws IOException {
            r = new BufferedReader(new InputStreamReader(new BufferedInputStream(in, 4096)));
        }
        public int nextInt() throws IOException{
            StringBuilder str = new StringBuilder();
            int flag = 0;
            if (sy == 0 && check == 0) {
                sy = r.read();
            }
            char sym = (char) sy;
            if (!Character.isWhitespace(sym) && sy != -1) {
                str.append(sym);
                flag = 1;
            }
            while (sy != -1) {
                sy = r.read();
                if (sy == -1) {
                    break;
                }
                sym = (char) sy;
                if (!Character.isWhitespace(sym)) {
                    str.append(sym);
                    flag = 1;
                } else {
                    if (flag == 1) {
                        break;
                    }
                }
            }
            return Integer.parseInt(str.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] arr = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i * n + j] = -1;
            }
        }
        for (int i = 0; i < m; i++) {
            int a = sc.nextInt() - 1;
            int b = sc.nextInt() - 1;
            int c = sc.nextInt();
            if (a != b) {
                arr[a * n + b] = c;
            }
        }
        int[] d = new int[n];
        boolean[] use = new boolean[n];
        for (int i = 0; i < n; i++) {
            d[i] = 1000000;
            use[i] = false;
        }
        d[0] = 0;
        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int j = 0; j < n; j++) {
                if (!use[j] && v == -1) {
                    v = j;
                } else if (v != -1) {
                    if (!use[j] && d[j] < d[v]) {
                        v = j;
                    }
                }
            }
            if (v == -1) {
                continue;
            }
            if (d[v] == 100000) {
                break;
            }
            use[v] = true;
            for (int j = 0; j < n; j++) {
                if (d[v] + arr[v * n + j] < d[j]) {
                    d[j] = d[v] + arr[v * n + j];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.print(d[i] + " ");
        }
    }
}
