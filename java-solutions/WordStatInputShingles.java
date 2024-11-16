import java.io.*;
import java.util.ArrayList;

public class WordStatInputShingles {
    public static boolean ask(char sy) {
        boolean t = Character.isLetter(sy) || Character.DASH_PUNCTUATION == Character.getType(sy) || sy == '\'';
        return t;
    }

    public static void main(String[] args) {
        try {
            File fi = new File(args[0]);
            Scanner fr = new Scanner(fi);
            StringBuilder str = new StringBuilder();
            ArrayList<String> words = new ArrayList<String>();
            ArrayList<Integer> a = new ArrayList<Integer>();
            int i = 0;
            while (fr.hasNext()) {
                char sy = fr.nextChar();
                if (ask(sy)) {
                    str.insert(i, sy);
                    i++;
                } else {
                    i = 0;
                    if (str.length() != 0){
                        str.setLength(0);
                    }
                }
                if (i == 3) {
                    if (words.contains(str.toString().toLowerCase())) {
                        int k = words.indexOf(str.toString().toLowerCase());
                        a.set(k, a.get(k) + 1);
                    } else {
                        words.add(str.toString().toLowerCase());
                        a.add(1);
                    }
                    str.delete(0, 1);
                    i--;
                }
            }
            File fil = new File(args[1]);
            OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream(fil), "UTF-8");
            BufferedWriter wri = new BufferedWriter(wr);
            try {
                while (i < a.size()) {
                    wri.write(words.get(i) + " " + a.get(i));
                    wri.newLine();
                    i++;
                }
            } finally {
                wri.close();               
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Required file not found");
        } catch (IOException e) {
            System.out.println("Incorrect input or output");
        }
    }
}