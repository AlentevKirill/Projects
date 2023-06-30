package game;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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
        if (count == 5) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Please enter the number of rows, columns, \nand the required number of items in the row to win. \nAlso enter the number of participants and laps in the tournament.");
        Scanner scan = new Scanner(System.in);
        String strings;
        ArrayList<Player> player = new ArrayList<>();
        int error = 0;
        int n, m, k, people, c;
        while (true) {
            if (error != 0) {
                System.out.println("You entered incorrect values, \nplease re-enter them according to the instructions.");
            }
            strings = scan.nextLine();
            if (verification(strings)) {
                error++;
                continue;
            }
            Scanner scanner = new Scanner(strings);
            n = scanner.nextInt();
            m = scanner.nextInt();
            k = scanner.nextInt();
            people = scanner.nextInt();
            c = scanner.nextInt();
            if ((k > n && k > m) || people == 1) {
                error++;
            } else {
                System.out.println("Please select the options of the player: \"Human\" or \"Random\" or \"Sequential\"");
                for (int i = 0; i < people; i++) {
                    String s = scan.nextLine();
                    if (s.equals("Human")) {
                        player.add(new HumanPlayer());
                    }
                    if (s.equals("Random")) {
                        player.add(new RandomPlayer());
                    }
                    if (s.equals("Sequential")) {
                        player.add(new SequentialPlayer());
                    }
                }
                break;
            }
        }
        Tournament tournament = new Tournament();
        int[][] table = tournament.tournament(people, c, n, m, k, player);
        System.out.println("Final table of results:");
        int winner = 0;
        int max2 = 0;
        int maxNum = 0;
        for (int i = 0; i < people; i++) {
            int max1 = 0;
            for (int j = 0; j < c; j++) {
                max1 += table[i][j];
                if (table[i][j] > maxNum) {
                    maxNum = table[i][j];
                }
            }
            if (max1 == max2) {
                winner = -1;
            }
            if (max1 > max2) {
                winner = i;
                max2 = max1;
            }
        }
        for (int i = -2; i < people; i++) {
            if (i >= 0) {
                System.out.print("Player " + i + " : ");
                System.out.print(" ".repeat(Integer.toString(people).length() - Integer.toString(i).length()));
            }
            if (i == -1) {
                System.out.print("          ");
                System.out.print(" ".repeat(Integer.toString(people).length()));
            }
            if (i == -2) {
                System.out.print("          ");
                System.out.print(" ".repeat(Integer.toString(people).length()));
                System.out.println("Number of circle:");
                continue;
            }
            for (int j = 0; j < c; j++) {
                if (i == -1) {
                    System.out.print(j + " ");
                    System.out.print(" ".repeat((Integer.toString(maxNum).length()) - Integer.toString(j).length()));
                } else {
                    System.out.print(table[i][j] + " ");
                    System.out.print(" ".repeat((Integer.toString(maxNum).length()) - Integer.toString(table[i][j]).length()));
                }
            }
            System.out.println();
        }
        if (winner == -1) {
            System.out.println("This game does not have a single winner");
        } else {
            System.out.println("This game was won by player number: " + winner);
        }
    }
}
