package ru.itmo.wp.web.page;

import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {

    private void action(Map<String, Object> view) {
        view.put("state", new State());
    }
    public static class State {
        private final int size = 3;
        private final String [][] cells = new String[size][size];
        private String phase = "RUNNING";
        private boolean crossesMove = true;
        public State() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    cells[i][j] = "";
                }
            }
        }

        public boolean getCrossesMove() {
            return crossesMove;
        }

        public String getPhase() {
            return phase;
        }
        public String[][] getCells() {
            return cells;
        }

        public int getSize() {
            return size;
        }
    }
}
