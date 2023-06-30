package game;

import java.util.Arrays;
import java.util.Map;

public class NMKBoard implements Board, Position {

    private final int n;
    private final int m;
    private final int k;
    private static int saveN;
    private static int saveM;
    private final Cell[][] cells;
    private Cell turn;
    private static int saveNumMotion = 0;

    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    public NMKBoard(int n, int m, int k) {
        this.n = n;
        this.m = m;
        this.k = k;
        saveN = n;
        saveM = m;
        cells = new Cell[saveN][saveM];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    public static int getN() {
        return saveN;
    }

    public static int getM() {
        return saveM;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();

        saveNumMotion++;
        int x = move.getRow();
        int y = move.getColumn();
        int[] dx = {0, 1, 1, 1};
        int[] dy = {1, 0, 1, -1};
        int[] countPlus = new int[4];
        int[] countMinus = new int[4];
        for (int dir = 0; dir < 4; dir++) {
            int flag = 0;
            for (int i = 0; i < k; i++) {
                int nx = x + dx[dir] * i;
                int ny = y + dy[dir] * i;
                if (nx < n && ny < m && nx >= 0 && ny >= 0 && cells[nx][ny] == turn) {
                    countPlus[dir]++;
                    if (countPlus[dir] >= k) {
                        return Result.WIN;
                    }
                } else {
                    flag = 1;
                }
                if (i == 0) {
                    continue;
                }
                int unx = x - dx[dir] * i;
                int uny = y - dy[dir] * i;
                if (unx >= 0 && unx < n && uny >= 0 && uny < m && cells[unx][uny] == turn) {
                    countMinus[dir]++;
                    if (countMinus[dir] >= k) {
                        return Result.WIN;
                    }
                } else {
                    if (flag == 1) {
                        break;
                    }
                }
            }
            if (countPlus[dir] + countMinus[dir] >= k) {
                return Result.WIN;
            }
        }
        if (saveNumMotion == n * m) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getColumn() && move.getColumn() < m
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < m; i++) {
            sb.append(i);
        }
        for (int r = 0; r < n; r++) {
            sb.append("\n");
            sb.append(r);
            sb.append(" ");
            for (int c = 0; c < m; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
