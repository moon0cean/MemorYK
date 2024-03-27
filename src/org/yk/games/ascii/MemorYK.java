package org.yk.games.ascii;

import java.util.ArrayList;
import java.util.Scanner;

public class MemorYK {
    protected final static int MAX_PLAYERS = 4;
    protected final static int MIN_PLAYERS = 1;
    protected final static int MAX_ROWS = 10;
    protected final static int MIN_ROWS = 2;
    protected final static int MAX_COLUMNS = 10;
    protected final static int MIN_COLUMNS = 2;

    public static void main(String[] args) {
        boolean debug = false;
        if (args[0] != null && args[0].equals("debug")) {
            debug = true;
        }

        System.out.println("Welcome to MemorYK");
        try {
            // Gather number of players from user
            System.out.println("Number of players:");
            int numberOfPlayers = 0;
            while (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
                try {
                    numberOfPlayers = Integer.parseInt(readLine());
                } catch (NumberFormatException nfe) {
                    numberOfPlayers = 0;
                }
                if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
                    System.err.println("Invalid number of players. Enter a value between " + MIN_PLAYERS + " and " + MAX_PLAYERS + ".");
                }
            }
            var players = new ArrayList<Player>();
            for (int i = 0; i < numberOfPlayers; i++) {
                players.add(new Player(i + 1));
            }
            System.out.println(numberOfPlayers + " players selected");

            // Gather number of rows from user
            System.out.println("Number of rows:");
            int rows = 0;
            while (rows < MIN_ROWS || rows > MAX_ROWS) {
                try {
                    rows = Integer.parseInt(readLine());
                } catch (NumberFormatException nfe) {
                    rows = 0;
                }
                if (rows < MIN_ROWS || rows > MAX_ROWS) {
                    System.err.println("Invalid number of rows. Enter a value between " + MIN_ROWS + " and " + MAX_ROWS + ".");
                }
            }

            // Gather number of columns from user
            System.out.println("Number of columns:");
            int columns = 0;
            while (columns < MIN_COLUMNS || columns > MAX_COLUMNS) {
                try {
                    columns = Integer.parseInt(readLine());
                } catch (NumberFormatException nfe) {
                    columns = 0;
                }
                if (columns < MIN_COLUMNS || columns > MAX_COLUMNS) {
                    System.err.println("Invalid number of columns. Enter a value between " + MIN_COLUMNS + " and " + MAX_COLUMNS + ".");
                }
                if ((rows * columns) % 2 != 0) {
                    System.err.println("Invalid number of tiles. rows * columns (currently = " + rows * columns + " ) must be divisible by 2");
                    columns = 0;
                }
            }

            // Build memory matrix
            MemoryMatrix matrix = new MemoryMatrix(rows, columns);
            matrix.buildMatrix();

            // Start game
            matrix.startGame(players);

        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }

    public static String readLine() {
        return new Scanner(System.in).nextLine();
    }
}