public class ReverseMin {
    public static void main(String[] args) {
        Scann sc = new Scann(System.in);
        int [] min = new int[1000000]; // минимальный элемент строки
        int [] minn = new int[1000000]; // минимальный элемент столбца
        int [] f = new int[1000000]; //количество столбцов в строке
        String str;
        for (int i = 0; i < 1000000; i++){
            min[i] = Integer.MAX_VALUE;
            minn[i] = Integer.MAX_VALUE;
            f[i] = 0;
        }
        int k = 0; // подсчёт количества строк
		int i;
		int r;
		char sy;
		int j;
		boolean t;
        while (sc.hasNextLine()){
            str = sc.nextLine();
            str = str + " ";
            i = 0; // номер элемернта строки, с которым работает цикл
            r = 0; // номер столбца элемента строки
            while (i < str.length() - 1)  {
                sy = str.charAt(i);
                t = Character.isWhitespace(sy);
                if (t) {
                    i++;
                } else {
                    j = i;
                    while (!t) {
                        sy = str.charAt(j);
                        t = Character.isWhitespace(sy);
                        j++;
                    }
                    f[k]++;
                    if (Integer.parseInt(str, i, j - 1, 10) < min[k]){
                        min[k] = Integer.parseInt(str, i, j - 1, 10);
                    }
                    if (Integer.parseInt(str, i, j - 1, 10) < minn[r]){
                        minn[r] = Integer.parseInt(str, i, j - 1, 10);
                    }
                    r++;
                    i = j - 1;
                }
            }
            k++;
        }
        if (k == 0) {
            System.out.println();
        }
        for (int y = 0; y < k; y++){
            r = 0;
            while (r < f[y]){
                if (min[y] < minn[r]) {
                    System.out.print(min[y] + " ");
                } else {
                    System.out.print(minn[r] + " ");
                }
                r++;
            }
            System.out.println();
        }
    }
}