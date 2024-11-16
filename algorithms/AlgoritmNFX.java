import java.io.*;

public class AlgoritmNFX {

    public static void main(String[] args) throws IOException{
        File fi = new File("nfc.in");
        File fil = new File("nfc.out");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(System.out)));
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fi), "UTF-8"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fil), "UTF-8"));
        String string = reader.readLine();
        int n = Integer.parseInt(string, 0, string.indexOf(' '), 10);
        int first = string.charAt(string.length() - 1) - 65;
        String [][] move = new String[26][n];
        int [] mas = new int[26];
        for (int i = 0; i < n; i++) {
            string = reader.readLine();
            if (string.length() < 6) {
                continue;
            }
            boolean flag = false;
            for (int j = 0; j < mas[string.charAt(0) - 65]; j++) {
                if (move[string.charAt(0) - 65][j].equals(string.substring(5))) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            move[string.charAt(0) - 65][mas[string.charAt(0) - 65]] = string.substring(5);
            mas[string.charAt(0) - 65]++;
        }
        String word = reader.readLine();
        int[][][] dp = new int[26][word.length()][word.length()];
        for (int i = 0; i < 26; i++) {
            if (move[i][0] == null) {
                continue;
            }
            for (int j = 0; j < word.length(); j++) {
                for (int k = 0; k < mas[i]; k++) {
                    if (move[i][k].equals(Character.toString(word.charAt(j)))) {
                        dp[i][j][j] = 1;
                    }
                }
            }
        }
        for (int i = 1; i < word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                if (move[j][0] == null) {
                    continue;
                }
                for (int k = 0; k < mas[j]; k++) {
                    if (move[j][k].length() <= 1) {
                        continue;
                    }
                    int left = move[j][k].charAt(0) - 65;
                    int right = move[j][k].charAt(1) - 65;
                    for (int p = 0; p < word.length() - i; p++) {
                        if (i == 1) {
                            dp[j][p][p + 1] += (dp[left][p][p] * dp[right][p + 1][p + 1]) % 1000000007;;
                        } else {
                            for (int t = p; t < p + i; t++) {
                                if (word.substring(p, t + 1).equals(word.substring(t + 1, p + i + 1))) {
                                    dp[j][p][p + i] = dp[left][p][t];
                                    break;
                                } else if (t - p > 2 && t - p < p + i - t) {
                                    if (word.substring(t + 1, p + i + 1).contains(word.substring(p, t + 1))) {
                                        dp[j][p][p + i] = dp[left][p][t];
                                        break;
                                    }
                                } else if (p + i - t > 2 && t - p > p + i - t) {
                                    if (word.substring(p, t + 1).contains(word.substring(t + 1, p + i + 1))) {
                                        dp[j][p][p + i] = dp[left][p][t];
                                        break;
                                    }
                                }
                                dp[j][p][p + i] = (dp[j][p][p + i] + ((dp[left][p][t] * dp[right][t + 1][p + i]) % 1000000007)) % 1000000007;
                            }
                        }
                    }
                }
            }
        }
        writer.write(Integer.toString(dp[first][0][word.length() - 1]));
        writer.close();
        reader.close();
    }
}
