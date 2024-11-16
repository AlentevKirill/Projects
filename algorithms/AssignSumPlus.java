import java.io.*;

public class AssignSumPlus {

    public static long[] treeSum = new long[1000000];
    public static long[] treeAdd = new long[1000000];
    public static long[] treeAssign = new long[1000000];
    public static final long noOperation = Long.MIN_VALUE;
    private static final long noElement = -1;

    public static class Scanner {
        private final Reader r;
        private int sy = 0;
        private int check = 0;

        public Scanner(InputStream in) throws IOException {
            r = new BufferedReader(new InputStreamReader(new BufferedInputStream(in, 4096)));
        }

        public int nextInt() throws IOException {
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

    public static void propagate(int x, int lx, int rx, int n, int nb) {
        if (rx - lx <= 1) {
            if (treeAssign[x] == noOperation) {
                //System.out.println("Hell");
                treeAssign[x] = 0;
            }
            if (treeAdd[x] == noElement) {
                treeAdd[x] = 0;
            }
            treeSum[x] = treeAssign[x] + treeAdd[x];
            return;
        }
        if (rx > n - 1 + nb) {
            rx = nb + n - 1;
            if (lx >= rx) {
                return;
            }
        }
        if (treeAdd[x] == noElement) {
            treeAdd[2 * x + 1] = noElement;
            treeAdd[2 * x + 2] = noElement;
        } else {
            if (treeAdd[2 * x + 1] == noElement && treeAdd[x] != 0) {
                treeAdd[2 * x + 1] = treeAdd[x];
            } else {
                treeAdd[2 * x + 1] = treeAdd[2 * x + 1] != noElement ? treeAdd[2 * x + 1] + treeAdd[x] : treeAdd[2 * x + 1];
            }
            if (treeAdd[2 * x + 2] == noElement && treeAdd[x] != 0) {
                treeAdd[2 * x + 2] = treeAdd[x];
            } else {
                treeAdd[2 * x + 2] = treeAdd[2 * x + 2] != noElement ? treeAdd[2 * x + 2] + treeAdd[x] : treeAdd[2 * x + 2];
            }
            if (treeAdd[2 * x + 1] != noElement) {
                treeSum[2 * x + 1] += treeAdd[2 * x + 1] * (rx - lx) / 2;
            }
            if (treeAdd[2 * x + 2] != noElement) {
                treeSum[2 * x + 2] += treeAdd[2 * x + 2] * (rx - lx) / 2;
            }
        }
        if (treeAssign[x] != noOperation) {
            treeAssign[2 * x + 1] = treeAssign[x];
            treeAssign[2 * x + 2] = treeAssign[x];
            if (treeAdd[x] == noElement) {
                treeSum[x] = treeAssign[x] * (rx - lx);
            } else {
                treeSum[x] = (treeAssign[x] + treeAdd[x]) * (rx - lx);
            }
            treeAssign[x] = noOperation;
        }
        treeAdd[x] = 0;
    }

    public static void setAndAdd(int l, int r, int v, int x, int lx, int rx, int flag, int n, int nb) {
        propagate(x, lx, rx, n, nb);
        if ((lx >= r) || (l >= rx)) {
            return;
        }
        if (lx >= l && rx <= r) {
            /*for (int k = 0; k < 2 * Math.pow(2, n) - 1; k++) {
                System.out.print(treeAdd[k] + " ");
            }
            System.out.println();
            System.out.println("!");
            for (int k = 0; k < 2 * Math.pow(2, n) - 1; k++) {
                System.out.print(treeAssign[k] + " ");
            }
            System.out.println();*/
            if (flag == 1) {
                treeAssign[x] = v;
                treeAdd[x] = noElement;
            } else {
                treeAdd[x] = treeAdd[x] != noElement ? treeAdd[x] + v : v;
            }
            if (treeAdd[x] == noElement) {
                treeSum[x] = treeAssign[x] != noOperation ? treeAssign[x] * (rx - lx) : treeSum[x];
            } else {
                treeSum[x] = treeAssign[x] != noOperation ? (treeAdd[x] + treeAssign[x]) * (rx - lx) : treeSum[x] + treeAdd[x] * (rx - lx);
            }
            return;
        }
        int mid = (lx + rx) / 2;
        //System.out.println("x = " + (2 * x + 1) + " lx = " + lx + " rx = " + mid);
        setAndAdd(l, r, v, 2 * x + 1, lx, mid, flag, n, nb);
        //System.out.println("!x = " + (2 * x + 2) + " lx = " + mid + " rx = " + rx);
        setAndAdd(l, r, v,2 * x + 2,  mid, rx, flag, n, nb);
        treeSum[x] = treeSum[2 * x + 1]  + treeSum[2 * x + 2];
    }

    public static long sumSegment(int l, int r, int x, int lx, int rx, int size, int nb) {
        propagate(x, lx, rx, size, nb);
        if ((lx >= r) || (l >= rx)) {
            return 0;
        }
        if (lx >= l && rx <= r) {

            return treeSum[x];
        }
        int mid = (lx + rx) / 2;
        //System.out.println(s1 + " " + s2 + " " + treeMin[x]);
        /*for (int k = 0; k < 2 * size - 1; k++) {
            System.out.print(treeAssign[k] + " ");
        }
        System.out.println();
        System.out.println("!");
        for (int k = 0; k < 2 * size - 1; k++) {
            System.out.print(treeAdd[k] + " ");
        }
        System.out.println();
        System.out.println("! !");
        for (int k = 0; k < 2 * size - 1; k++) {
            System.out.print(treeSum[k] + " ");
        }
        System.out.println();*/
        return sumSegment(l, r, 2 * x + 1, lx, mid, size, nb) + sumSegment(l, r, 2 * x + 2, mid, rx, size, nb);
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int nb = sc.nextInt();
        int m = sc.nextInt();
        int n = nb;
        int save = 0;
        if ((Math.log(nb) / Math.log(2)) > (int) (Math.log(nb) / Math.log(2)) ) {
            save = ((int) (Math.log(nb)/Math.log(2)) + 1);
            n = (int) Math.pow(2, save);
        }
        for (int i = 0; i < 2 * n - 1; i++) {
            treeAssign[i] = noOperation;
            treeSum[i] = 0;
            treeAdd[i] = 0;
        }
        /*for (int i = 0; i < 2 * n - 1; i++) {
            System.out.print(treeAssign[i] + " ");
        }
        System.out.println();*/
        for (int j = 0; j < m; j++) {
            int flag = sc.nextInt();
            if (flag != 3) {
                int l = sc.nextInt() + n - 1;
                int r = sc.nextInt() + n - 1;
                int v = sc.nextInt();
                if (flag == 2 && v == 0) {
                    continue;
                }
                //System.out.println("i := " + i + " v := " + v);
                setAndAdd(l, r, v, 0, n - 1, n * 2 - 1, flag, n, nb);
                /*for (int k = 0; k < 2 * n - 1; k++) {
                    System.out.print(treeSum[k] + " ");
                }
                System.out.println();
                for (int k = 0; k < 2 * n - 1; k++) {
                    System.out.print(treeAssign[k] + " ");
                }
                System.out.println();
                for (int k = 0; k < 2 * n - 1; k++) {
                    System.out.print(treeAdd[k] + " ");
                }
                System.out.println();*/
            } else {
                int l = sc.nextInt() + n - 1;
                int r = sc.nextInt() + n - 1;
                System.out.println(sumSegment(l, r, 0, n - 1, n * 2 - 1, n, nb));
            }
        }
    }
}
