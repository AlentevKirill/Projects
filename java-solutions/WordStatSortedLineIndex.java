import java.io.*;
import java.util.Map;
import java.util.TreeMap;


public class WordStatSortedLineIndex {

    public static boolean ask(char sy) {
        boolean t = Character.isLetter(sy) || Character.DASH_PUNCTUATION == Character.getType(sy) || sy == '\'';
        return t;
    }

    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            Scanner fileRead = new Scanner(file);
            try {
			StringBuilder string = new StringBuilder();
            Map<String, StringBuilder> word = new TreeMap<>();
            StringBuilder count = new StringBuilder();
            int i = 0;
            int j = 0;
            int wordNumber = 1;
            int stringNumber = 1;
            while (fileRead.hasNext()) {
                char sym = fileRead.nextChar();
                if (ask(sym)) {
                    string.append(sym);
                    i = 1;
                    j = i;
                } else {
                    i = 0;
                }
                if ((j - i > 0)) {
                    if (word.containsKey(string.toString().toLowerCase())) {
                        count = word.get(string.toString().toLowerCase());
						int lengthNumber = 0;
						while (Character.isDigit(count.charAt(lengthNumber))) {
							lengthNumber++;
						}
                        int k = Integer.parseInt(count.substring(0, lengthNumber)) + 1;
                        count.replace(0, lengthNumber, Integer.toString(k));
                        count.append(" ");
                        count.append(stringNumber);
                        count.append(":");
                        count.append(wordNumber);
                        word.replace(string.toString().toLowerCase(), count);
                    } else {
                        count.append("1 ");
                        count.append(stringNumber);
                        count.append(":");
                        count.append(wordNumber);
                        word.put(string.toString().toLowerCase(), count);
                    }
                    string = new StringBuilder();
                    count = new StringBuilder();
                    wordNumber++;
                    j = 0;
                }
                if (sym == '\n') {
                    stringNumber++;
                    wordNumber = 1;
                }
            }
            File file2 = new File(args[1]);
            OutputStreamWriter writeFile = new OutputStreamWriter(new FileOutputStream(file2), "UTF-8");
            BufferedWriter writer = new BufferedWriter(writeFile);
            try {
                String[] save = new String[word.size()];
                i = 0;
                for (Map.Entry<String, StringBuilder> entry : word.entrySet()) {
                    save[i] = entry.getKey();
                    i++;
                }
                for (j = 0; j < word.size(); j++) {
                    writer.write(save[j] + " " + word.get(save[j]));
                    writer.newLine();
                }
            } finally {
                try {
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Error: Required file not found");
                }
            }
			} finally {
					try {
                       fileRead.close();
                    } catch (IOException e) {
                        System.out.println("Error: Input or Output");
                    }
                }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Required file not found");
        } catch (IOException e) {
            System.out.println("Incorrect input or output");
        }
    }
}
