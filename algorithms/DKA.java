import java.io.*;

public class DKA {
    public static void main(String[] args) throws IOException{
        File fi = new File("problem1.in");
        File fil = new File("problem1.out");
        //File fi = new File("C:\\Users\\Кирилл\\IdeaProjects\\VR\\src\\test.txt");
        //File fil = new File("C:\\Users\\Кирилл\\IdeaProjects\\VR\\src\\testOut.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fi), "UTF-8"));
        OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream(fil), "UTF-8");
        BufferedWriter wri = new BufferedWriter(wr);
        String string = reader.readLine();
        String str = reader.readLine();
        int n = Integer.parseInt(str, 0, str.indexOf(" "), 10);
        int m = Integer.parseInt(str, str.indexOf(" ") + 1, str.lastIndexOf(" "), 10);
        int k = Integer.parseInt(str, str.lastIndexOf(" ") + 1, str.length(), 10);
        int[][] map = new int[n][26];
        int[] terminal = new int[n];
        str = reader.readLine() + " ";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 26; j++) {
                map[i][j] = -1;
            }
        }
        for (int i = 0; i < k; i++) {
            int temp = Integer.parseInt(str, 0, str.indexOf(" "), 10);
            str = str.substring(str.indexOf(" ") + 1);
            terminal[temp - 1] = 1;
        }
        for (int i = 0; i < m; i++) {
            str = reader.readLine();
            int l = Integer.parseInt(str, 0, str.indexOf(" "), 10);
            int r = Integer.parseInt(str, str.indexOf(" ") + 1, str.lastIndexOf(" "), 10);
            String sym = str.substring(str.lastIndexOf(" ") + 1);
            map[l - 1][sym.charAt(0) - 97] = r - 1;
        }
        /*System.out.println(string);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 26; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < n; i++) {
            System.out.print(terminal[i] + " ");
        }*/
        int real = 0;
        int check = 0;
        for (int i = 0; i < string.length(); i++) {
            char sym = string.charAt(i);
            if (map[real][sym - 97] != -1) {
                real = map[real][sym - 97];
            } else {
                check = 1;
                wri.write("Rejects");
                break;
            }
        }
        if (check == 0) {
            if (terminal[real] == 1) {
                wri.write("Accepts");
            } else {
                wri.write("Rejects");
            }
        }
        reader.close();
        wri.close();
    }
}
