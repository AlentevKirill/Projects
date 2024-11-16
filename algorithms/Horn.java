import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Horn {
    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
        int pr = s.indexOf(" ");
        int k = Integer.parseInt(s.substring(pr + 1));
        String[] str = new String[k];
        int avoid = 0;
        Map<Integer, Integer> a = new HashMap<>();
        int flag = 0;
        for (int i = 0; i < k; i++) {
            str[i] = r.readLine();
            str[i] = str[i].replace("-1", "2");
            int v1 = str[i].indexOf('0');
            int v2 = str[i].indexOf('1');
            if (((v1 != -1) && (v1 != str[i].lastIndexOf('0'))) || (v1 != -1 && v2 != -1)) {
                flag++;
            } else {
                if (v1 != -1) {
                    if (a.containsKey(v1)){
                        if (a.get(v1) != 0) {
                            System.out.println("YES");
                            flag = -1;
                            break;
                        }
                    }
                    a.put(v1, 0);
                } else {
                    if (a.containsKey(v2)){
                        if (a.get(v2) != 1) {
                            System.out.println("YES");
                            flag = -1;
                            break;
                        }
                    }
                    a.put(v2, 1);
                }
                str[i] = "";
                avoid++;
            }
        }
        if (flag == k) {
            System.out.println("NO");
            flag = -1;
        }
        if (flag != -1) {
            while (true) {
                for (int i = 0; i < k; i++) {
                    if (str[i].length() == 0) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(str[i]);
                    for (Entry e : a.entrySet()) {
                        int k1 = (int) e.getKey();
                        int k2 = (int) e.getValue();
                        if (k2 == 0) {
                            if (sb.charAt(k1) == '0') {
                                str[i] = "";
                                avoid++;
                                break;
                            } else {
                                if (sb.charAt(k1) == '1') {
                                    sb.replace(k1, k1 + 1, "2");
                                    continue;
                                }
                            }
                        }
                        if (k2 == 1) {
                            if (sb.charAt(k1) == '1') {
                                str[i] = "";
                                avoid++;
                                break;
                            } else {
                                if (sb.charAt(k1) == '0') {
                                    sb.replace(k1, k1 + 1, "2");
                                }
                            }
                        }
                    }
                    str[i] = sb.toString();
                }
                a.clear();
                flag = 0;
                for (int i = 0; i < k; i++) {
                    if (str[i].length() == 0) {
                        continue;
                    }
                    int v1 = str[i].indexOf('0');
                    int v2 = str[i].indexOf('1');
                    if (v1 == -1 && v2 == -1) {
                        System.out.println("YES");
                        flag = -1;
                        break;
                    }
                    if (!(((v1 != -1) && (v1 != str[i].lastIndexOf('0'))) || (v1 != -1 && v2 != -1))) {
                        if (v1 != -1) {
                            if (a.containsKey(v1)){
                                if (a.get(v1) != 0) {
                                    System.out.println("YES");
                                    flag = -1;
                                    break;
                                }
                            }
                            a.put(v1, 0);
                        } else {
                            if (a.containsKey(v2)){
                                if (a.get(v2) != 1) {
                                    System.out.println("YES");
                                    flag = -1;
                                    break;
                                }
                            }
                            a.put(v2, 1);
                        }
                        str[i] = "";
                        avoid++;
                    }
                }
                if (flag == -1) {
                    break;
                }
                if (avoid == k || a.size() == 0) {
                    System.out.println("NO");
                    break;
                }
            }
        }
    }
}
