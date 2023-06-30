package game;

import java.util.ArrayList;

public class Tournament {

    public int[][] tournament(int people, int circle, int n, int m, int k, ArrayList<Player> list) {
        int[][] table = new int[people][circle];
        for (int c = 0; c < circle; c++) {
            for (int i = 0; i < people - 1; i++) {
                for (int p = i + 1; p < people; p++) {
                    System.out.println("Game, Player " + i + " VS" + " Player " + p);
                    int result;
                    final Game game = new Game(true, list.get(i), list.get(p));
                    result = game.play(new NMKBoard(n, m, k));
                    if (result == 1) {
                        table[i][c] += 3;
                    }
                    if (result == 2) {
                        table[p][c] += 3;
                    }
                    if (result == 0) {
                        table[i][c] += 1;
                        table[p][c] += 1;
                    }
                }
            }
        }
        return table;
    }
}
