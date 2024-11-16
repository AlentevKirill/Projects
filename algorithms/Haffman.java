import java.util.Scanner;

public class Haffman {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] p = new long[n];
        long[] save = new long[n];
        long[] bit = new long[n];
        long[] seq = new long[n];
        long[] html = new long[n];
        for (int i = 0; i < n; i++) {
            p[i] = sc.nextLong();
            bit[i] = 0;
            save[i] = p[i];
        }
        long val = Long.MAX_VALUE;
        int k = 0;
        while (true) {
            int flag = 0;
            int dopFlag = 0;
            int pMin1 = 0;
            int pMin2 = 1;
            if (p[pMin1] > p[pMin2]) {
                pMin1 = 1;
                pMin2 = 0;
            }
            for (int i = 2; i < n; i++) {
                if (p[i] < p[pMin1]) {
                    pMin2 = pMin1;
                    pMin1 = i;
                } else if (p[i] < p[pMin2]) {
                    pMin2 = i;
                }
            }
            int seqMin1 = 0;
            int seqMin2 = 1;
            if (k > 1) {
                if (seq[seqMin1] > seq[seqMin2]) {
                    seqMin1 = 1;
                    seqMin2 = 0;
                }
                for (int i = 2; i < k; i++) {
                    if (seq[i] < seq[seqMin1]) {
                        seqMin2 = seqMin1;
                        seqMin1 = i;
                    } else if (seq[i] < seq[seqMin2]) {
                        seqMin2 = i;
                    }
                }
            }
            if (p[pMin1] == val && p[pMin2] == val && k == 1) {
                break;
            }
            if (k == 0) {
                seq[0] = p[pMin1] + p[pMin2];
                p[pMin1] = val;
                p[pMin2] = val;
                html[pMin1] = 1;
                html[pMin2] = 1;
                k++;
                flag = 1;
            } else if (k == 1) {
                if (p[pMin2] < seq[0]) {
                    seq[1] = p[pMin1] + p[pMin2];
                    p[pMin1] = val;
                    p[pMin2] = val;
                    html[pMin1] = 2;
                    html[pMin2] = 2;
                    k++;
                    flag = 2;
                } else {
                    seq[0] += p[pMin1];
                    p[pMin1] = val;
                    html[pMin1] = 1;
                    flag = 1;
                }
            } else {
                if (p[pMin2] < seq[seqMin1]) {
                    seq[k] = p[pMin1] + p[pMin2];
                    p[pMin1] = val;
                    p[pMin2] = val;
                    k++;
                    html[pMin1] = k;
                    html[pMin2] = k;
                    flag = k;
                }else if (p[pMin1] < seq[seqMin2]) {
                    seq[seqMin1] += p[pMin1];
                    p[pMin1] = val;
                    html[pMin1] = seqMin1 + 1;
                    flag = seqMin1 + 1;
                } else {
                    if (seqMin1 < seqMin2) {
                        seq[seqMin1] += seq[seqMin2];
                        k--;
                        flag = seqMin1 + 1;
                        dopFlag = seqMin2 + 1;
                    } else {
                        seq[seqMin2] += seq[seqMin1];
                        k--;
                        flag = seqMin2 + 1;
                        dopFlag = seqMin1 + 1;
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                if (dopFlag == 0) {
                    if (html[i] == flag) {
                        bit[i]++;
                    }
                } else {
                    if (html[i] == dopFlag) {
                        html[i] = flag;
                    }
                    if (html[i] > dopFlag) {
                        html[i]--;
                    }
                    if (html[i] == flag) {
                        bit[i]++;
                    }
                }
            }
            if (dopFlag != 0) {
                for (int i = dopFlag - 1; i < k; i++) {
                    seq[i] = seq[i + 1];
                }
            }
        }
        long result = 0;
        for (int i = 0; i < n; i++) {
           // System.out.print(bit[i] + " ");
            result += bit[i] * save[i];
        }
        /*System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(seq[i] + " ");
        }
        System.out.println();*/
        System.out.println(result);
    }
}
