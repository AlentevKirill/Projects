import java.io.*;

public class Scanner {
    private final Reader r;
    private int sy = 0;
    private int check = 0;
    private int checkL = 0;
    public Scanner(InputStream in) throws IOException {
        r = new BufferedReader(new InputStreamReader(in));
    }

    public Scanner(File fi) throws IOException {
        r = new InputStreamReader(new FileInputStream(fi), "UTF-8");
    }

    public String nextLine() throws IOException {
        StringBuilder str = new StringBuilder();
        if (sy == 0 && check == 0) {
            sy = r.read();
        }
        char sym = (char) sy;
        if (sym != '\n' && sy != -1) {
            str.append(sym);
        }
        while (sym != '\n' && sy != -1) {
            sy = r.read();
            if (sy == -1) {
                break;
            }
            sym = (char) sy;
            if (sym != '\n') {
                str.append(sym);
            }
        }
        if ((char) sy == '\n') {
            checkL = 1;
        }
        return str.toString();
    }

    public boolean hasNextLine() throws IOException {
        sy = r.read();
        if (sy == 0) {
            check = 1;
        }
        if ((char) sy == '\n') {
            checkL = 1;
        }
        if (sy == -1) {
            return false;
        } else {
            return true;
        }
    }

    public char nextChar() throws IOException {
        sy = r.read();
        return (char) sy;
    }

    public boolean hasNext() throws IOException {
        return r.ready();
    }
    public void close() throws IOException {
        r.close();
    }

    public int nextInt() throws IOException{
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

    public String nextWord() throws IOException{
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
        while (sy != -1 && (char) sy != '\n') {
            sy = r.read();
            if (sy == -1 || (char) sy == '\n') {
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
        if ((char) sy == '\n') {
            checkL = 1;
        }
        return str.toString();
    }

    public boolean checkLine(){
        if (checkL == 1) {
            checkL = 0;
            return false;
        }
        if ((char) sy == '\n') {
            return false;
        } else {
            return true;
        }
    }

    public StringBuilder nextWord3(StringBuilder str) throws IOException{
        int flag = 0;
        if (sy == 0 && check == 0) {
            sy = r.read();
        }
        int length = str.length();
        if (length != 0) {
            flag = 1;
        }
        char sym = (char) sy;
        if (!Character.isWhitespace(sym) && sy != -1) {
            str.append(sym);
            flag = 1;
        }
        while (length < 3 && sy != -1 && (char) sy != '\n') {
            sy = r.read();
            if (sy == -1 || (char) sy == '\n') {
                break;
            }
            sym = (char) sy;
            if (!Character.isWhitespace(sym)) {
                str.append(sym);
                flag = 1;
            } else {
                str = new StringBuilder();
                if (flag == 1) {
                    break;
                }
            }
        }
        if ((char) sy == '\n') {
            checkL = 1;
        }
        return str;
    }
}