package game;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    public static boolean verification(String strings) {
        int flag = 0;
        int count = 0;
        strings = strings + " ";
        for (int i = 0; i < strings.length(); i++) {
            char symbol = strings.charAt(i);
            if (!Character.isDigit(symbol) && symbol != ' ') {
                return true;
            } else {
                if (flag == 0 && Character.isDigit(symbol)) {
                    flag = 1;
                }
                if (flag == 1 && symbol == ' ') {
                    count++;
                    flag = 0;
                }
            }
        }
        if (count == 2) {
            return false;
        }
        return true;
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            int n = NMKBoard.getN();
            int m = NMKBoard.getM();
            int x, y;
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            while (true) {
                String strings = in.nextLine();
                if (verification(strings)) {
                    out.println("Move " + strings + " is invalid number format");
                    continue;
                }
                Scanner scan = new Scanner(strings);
                x = scan.nextInt();
                y = scan.nextInt();
                break;
            }
            final Move move = new Move(x, y, cell);
            if (position.isValid(move)) {
                return move;
            }
            final int row = move.getRow();
            final int column = move.getColumn();
            if (row >= n || row < 0 || column >= m || column < 0) {
                out.println("Move " + move + " is invalid, out of board bounds");
            } else {
                out.println("Move " + move + " is invalid, because the selected position is occupied");
            }
        }
    }
}
