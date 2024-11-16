import java.io.*;

public class AssignAndMin {

    public static long[] treeMin = new long[1000000];
    public static long[] treeAssign = new long[1000000];
    public static long noOperation = Long.MAX_VALUE;

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

    public static void propagate(int x, int lx, int rx) {
        if (rx - lx <= 1) {
            return;
        }
        if (treeAssign[x] != noOperation) {
            treeAssign[2 * x + 1] = treeAssign[x];
            treeMin[2 * x + 1] = treeAssign[x];
            treeAssign[2 * x + 2] = treeAssign[x];
            treeMin[2 * x + 2] = treeAssign[x];
            treeAssign[x] = noOperation;
        }
    }

    public static void assign(int l, int r, int v, int x, int lx, int rx) {
        propagate(x, lx, rx);
        if ((lx >= r) || (l >= rx)) {
            return;
        }
        if (lx >= l && rx <= r) {
            treeAssign[x] = v;
            treeMin[x] = v;
            return;
        }
        int mid = (lx + rx) / 2;
        //System.out.println("x = " + (2 * x + 1) + " lx = " + lx + " rx = " + mid);
        assign(l, r, v, 2 * x + 1, lx, mid);
        //System.out.println("!x = " + (2 * x + 2) + " lx = " + mid + " rx = " + rx);
        assign(l, r, v,2 * x + 2,  mid, rx);
        treeMin[x] = Math.min(treeMin[2 * x + 1], treeMin[2 * x + 2]);
    }

    public static long minSegment(int l, int r, int x, int lx, int rx) {
        propagate(x, lx, rx);
        if ((lx >= r) || (l >= rx)) {
            return noOperation;
        }
        if (lx >= l && rx <= r) {
            return treeMin[x];
        }
        int mid = (lx + rx) / 2;
        //System.out.println(s1 + " " + s2 + " " + treeMin[x]);
        return Math.min(minSegment(l, r, 2 * x + 1, lx, mid), minSegment(l, r, 2 * x + 2, mid, rx));
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int nb = sc.nextInt();
        int m = sc.nextInt();
        int n = nb;
        if ((Math.log(nb) / Math.log(2)) > (int) (Math.log(nb) / Math.log(2)) ) {
            n = (int) Math.pow(2, ((int) (Math.log(nb)/Math.log(2))) + 1);
        }
        for (int i = 0; i < 2 * n - 1; i++) {
            //treeMin[i] = noOperation;
            treeAssign[i] = noOperation;
        }
        /*for (int i = 0; i < 2 * n - 1; i++) {
            System.out.print(treeAssign[i] + " ");
        }
        System.out.println();*/
        for (int j = 0; j < m; j++) {
            int flag = sc.nextInt();
            if (flag == 1) {
                int l = sc.nextInt() + n - 1;
                int r = sc.nextInt() + n - 1;
                int v = sc.nextInt();
                //System.out.println("i := " + i + " v := " + v);
                assign(l, r, v, 0, n - 1, n * 2 - 1);
                /*for (int k = 0; k < 2 * n - 1; k++) {
                    System.out.print(treeAssign[k] + " ");
                }
                System.out.println();*/
            } else {
                int l = sc.nextInt() + n - 1;
                int r = sc.nextInt() + n - 1;
                long save = minSegment(l, r, 0, n - 1, n * 2 - 1);
                if (save == noOperation) {
                    System.out.println(0);
                } else {
                    System.out.println(save);
                }
            }
        }
    }
}