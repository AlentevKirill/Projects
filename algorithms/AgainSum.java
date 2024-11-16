import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AgainSum {

    private static long sum = 0;

    public static class List {
        private int x;
        private int y;
        private List left;
        private List right;
        private long sum;

        public List(List left, List right, int x, int y, long sum) {
            this.left = left;
            this.right = right;
            this.x = x;
            this.y = y;
            this.sum = sum;
        }
    }

    private static void recalc(List x) {
        if (x.left != null) {
            if (x.right != null) {
                x.sum = x.left.sum + x.right.sum + x.x;
            } else {
                x.sum = x.left.sum + x.x;
            }
        } else if (x.right != null) {
            x.sum = x.right.sum + x.x;
        } else {
            x.sum = x.x;
        }
    }

    private static boolean exists(List root, int x) {
        List real = root;
        if (real == null) {
            return false;
        }
        while (true) {
            if (x == real.x) {
                return true;
            }
            if (x > real.x) {
                if (real.right != null) {
                    real = real.right;
                } else {
                    return false;
                }
            } else {
                if (real.left != null) {
                    real = real.left;
                } else {
                    return false;
                }
            }
        }
    }

    private static List[] split(List A, int x) {
        if (A == null) {
            return (new List[] {null, null});
        }
        if (A.x < x) {
            List[] p = split(A.right, x);
            A.right = p[0];
            recalc(A);
            return (new List[] {A, p[1]});
        } else {
            List[] p = split(A.left, x);
            A.left = p[1];
            recalc(A);
            return (new List[] {p[0], A});
        }
    }

    private static List add(List A, List plus) {
        if (A == null) {
            return plus;
        }
        if (plus.y > A.y) {
            List[] p = split(A, plus.x);
            plus.left = p[0];
            plus.right = p[1];
            recalc(plus);
            return plus;
        }
        if (plus.x < A.x) {
            A.left = add (A.left, plus);
        } else {
            A.right = add(A.right, plus);
        }
        recalc(A);
        return A;
    }

    private static List merge (List A, List B) {
        if (A == null) {
            return B;
        }
        if (B == null) {
            return A;
        }
        if (A.y > B.y) {
            A.right = merge(A.right, B);
            recalc(A);
            return A;
        } else {
            B.left = merge(A, B.left);
            recalc(B);
            return B;
        }
    }

    private static List sum(List A, int l, int r) {
        List[] p = split(A, l);
        List[] k = split(p[1], r);
        if (k[0] != null) {
            sum += k[0].sum;
        }
        return merge(p[0], merge(k[0], k[1]));
    }

    public static void main(String[] args) throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
        List root = null;
        int n = Integer.parseInt(read.readLine());
        int flag = 0;
        for (int j = 0; j < n; j++) {
            String string = read.readLine();
            String operation = string.substring(0, 1);
            int i = 0;
            int l = 0;
            int r = 0;
            if (operation.equals("+")) {
                i = Integer.parseInt(string, 2, string.length(), 10);
                if (flag == 1) {
                    i = (int) ((i + sum) % 1000000000);
                }
                flag = 0;
            } else {
                l = Integer.parseInt(string, 2, string.lastIndexOf(' '), 10);
                r = Integer.parseInt(string, string.lastIndexOf(' ') + 1, string.length(), 10);
                flag = 1;
            }
            if (operation.equals("+")) {
                if (!exists(root, i)) {
                    int random = (int) (1000000 * Math.random());
                    List plus = new List(null, null, i, random, i);
                    if (root != null) {
                        root = add(root, plus);
                    } else {
                        root = plus;
                    }
                }
            } else {
                sum = 0;
                if (root == null) {
                    System.out.println(0);
                } else {
                    if (exists(root, r)) {
                        sum += r;
                    }
                    root = sum(root, l, r);
                    System.out.println(sum);
                }
            }
        }
    }
}
