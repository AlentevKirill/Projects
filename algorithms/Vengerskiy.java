import java.io.*;
import java.util.stream.IntStream;

public class Vengerskiy {

    public static void main(String[] args) throws IOException {
        File fi = new File("assignment.in");
        File fil = new File("assignment.out");
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fi), "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream(fil), "UTF-8");
        BufferedWriter wri = new BufferedWriter(wr);
        //Scanner scanner = new Scanner(System.in);
        String string = reader.readLine();
        //String string = scanner.nextLine();
        int n = Integer.parseInt(string);
        int[][] c = new int[n + 1][n + 1];
        for (int i = 1; i < n + 1; i++) {
            string = reader.readLine();
            //string = scanner.nextLine();
            int l = string.indexOf(' ');
            for (int j = 1; j < n + 1; j++) {
                if (l == -1) {
                    c[i][j] = Integer.parseInt(string);
                } else {
                    c[i][j] = Integer.parseInt(string.substring(0, l));
                }
                string = string.substring(l + 1);
                l = string.indexOf(' ');
            }
        }
        int[] diffRow = new int[n + 1];
        int[] diffCol = new int[n + 1];
        long[] minCol = new long[n + 1];
        int[] indexMinCol = new int[n + 1];
        int[] matching = new int[n + 1];
        boolean[] visited = new boolean[n + 1];
        IntStream.range(0, n + 1).forEach(i -> {
            diffRow[i] = 0;
            diffCol[i] = 0;
            matching[i] = 0;
        });
        for (int i = 1; i < n + 1; i++) {
            matching[0] = i;
            int k = 0;
            IntStream.range(0, n + 1).forEach(j -> {
                visited[j] = false;
                minCol[j] = Integer.MAX_VALUE;
            });
            while (true) {
                visited[k] = true;
                long min = Integer.MAX_VALUE;
                int l = 0;
                for (int j = 1; j < n + 1; j++) {
                    if (!visited[j]) {
                        int cIJ = c[matching[k]][j] - diffCol[j] - diffRow[matching[k]];
                        if (cIJ < minCol[j]) {
                            minCol[j] = cIJ;
                            indexMinCol[j] = k;
                        }
                        if (minCol[j] < min) {
                            min = minCol[j];
                            l = j;
                        }
                    }
                }
                for (int j = 0; j < n + 1; j++) {
                    if (!visited[j]) {
                        minCol[j] -= min;
                    } else {
                        diffRow[matching[j]] += min;
                        diffCol[j] -= min;
                    }
                }
                k = l;
                if (matching[k] == 0) {
                    break;
                }
            }
            while (true) {
                int x = indexMinCol[k];
                matching[k] = matching[x];
                k = x;
                if (k == 0) {
                    break;
                }
            }
        }
        int[] answer = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            answer[matching[i]] = i;
        }
        //System.out.println(-diffCol[0]);
        wri.write(Integer.toString(-diffCol[0]));
        wri.newLine();
        for (int i = 1; i < n + 1; i++) {
            wri.write(i + " " + answer[i]);
            wri.newLine();
            wri.flush();
            //System.out.println(i + " " + answer[i]);
        }
        reader.close();
        wri.close();
    }

}
