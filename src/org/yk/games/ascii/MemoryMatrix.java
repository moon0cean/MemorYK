package org.yk.games.ascii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class MemoryMatrix {
    protected final static char MASK = '*';
    protected final static char SEPARATOR = '|';
    private final int rows;
    private final int columns;
    private List<List<Character>> matrix;
    private List<List<Boolean>> matrixResult;
    private final List<Character> items;

    private final boolean debug;

    List<Integer[]> selectionMatrixCoords = new ArrayList<Integer[]>(2);

    public MemoryMatrix(int rows, int columns) {
        this(rows, columns, false);
    }

    public MemoryMatrix(int rows, int columns, boolean debug) {
        this.rows = rows;
        this.columns = columns;
        this.items = generateItems();
        this.debug = debug;
    }

    protected void buildMatrix() {
        if (matrix != null && !matrix.isEmpty()) {
            System.out.println("MemorYK already initialized");
        }

        var memorYKMatrix = new ArrayList<List<Character>>(rows);
        var memorYKMatrixResult = new ArrayList<List<Boolean>>(rows);
        int index = 0;
        for (int i = 0; i < rows; i++) {
            memorYKMatrix.add(i, new ArrayList<>(columns));
            memorYKMatrixResult.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                memorYKMatrix.get(i).add(j, items.get(index));
                memorYKMatrixResult.get(i).add(j, Boolean.FALSE);
                index++;
            }
        }
        this.matrix = memorYKMatrix;
        this.matrixResult = memorYKMatrixResult;
    }

    private List<Character> generateItems() {
        int numberOfItemsToBeGenerated = (rows * columns) / 2;

        var items = new ArrayList<Character>();
        for (int i = 0; i < numberOfItemsToBeGenerated; i++) {
            char randomItem;
            do {
                randomItem = Character.toChars(Random.from(RandomGenerator.getDefault()).nextInt(33, 126))[0];
            } while (items.contains(randomItem) && randomItem != MASK && randomItem != SEPARATOR);
            items.add(randomItem);
        }
        items.addAll(items);
        Collections.shuffle(items, RandomGenerator.getDefault());
        return List.copyOf(items);
    }

    @Override
    public String toString() {
        StringBuilder matrixBuilder = new StringBuilder();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                Character value = MASK;
                if (debug || matrixResult.get(i).get(j) ||
                        (!selectionMatrixCoords.isEmpty() &&
                                ((i == selectionMatrixCoords.get(0)[0] && j == selectionMatrixCoords.get(0)[1])
                                        || (i == selectionMatrixCoords.get(1)[0] && j == (selectionMatrixCoords.get(1)[1]))))) {
                    value = matrix.get(i).get(j);
                }

                if (j == 0) {
                    matrixBuilder
                            .append(SEPARATOR)
                            .append("\u0020")
                            .append(value)
                            .append("\u0020")
                            .append(SEPARATOR)
                            .append("\u0020");
                } else if (j + 1 >= columns) {
                    matrixBuilder
                            .append(value)
                            .append("\u0020")
                            .append(SEPARATOR)
                            .append("\n");
                } else {
                    matrixBuilder
                            .append(value)
                            .append("\u0020")
                            .append(SEPARATOR)
                            .append("\u0020");
                }
            }
        }
        return matrixBuilder.toString();
    }

    public void startGame(List<Player> players) {
        Score score = new Score(players);
        int round = 1;
        while (matrixResult.stream().noneMatch(r -> Boolean.TRUE && r.stream().noneMatch(c -> Boolean.TRUE))) {
            for (Player player : players) {
                System.out.println(this);
                System.out.println("Round [" + round + "]");
                for (int i = 0; i < 2; i++) {
                    int selectedRow = 0;
                    while (selectedRow == 0 && matrixResult.get(selectedRow).stream().allMatch(r -> Boolean.TRUE)) {
                        selectedRow = getMatrixLocation(player, i, rows, "row");
                    }
                    int selectedColumn = 0;
                    Integer[] previousMatrixCoordsSelection = (!selectionMatrixCoords.isEmpty()) ? selectionMatrixCoords.get(0) : null;
                    while (selectedColumn == 0 || (previousMatrixCoordsSelection != null
                            && (previousMatrixCoordsSelection[0] == (selectedRow - 1) && previousMatrixCoordsSelection[1] == (selectedColumn - 1)))) {
                        selectedColumn = getMatrixLocation(player, i, columns, "column");
                    }
                    Integer[] matrixCoords = new Integer[]{selectedRow - 1, selectedColumn - 1};
                    selectionMatrixCoords.add(i, matrixCoords);
                }

                Character selectValue1 = matrix.get(selectionMatrixCoords.get(0)[0]).get(selectionMatrixCoords.get(0)[1]);
                Character selectValue2 = matrix.get(selectionMatrixCoords.get(1)[0]).get(selectionMatrixCoords.get(1)[1]);
                if (selectValue1.equals(selectValue2)) {
                    matrixResult.get(selectionMatrixCoords.get(0)[0]).set(selectionMatrixCoords.get(0)[1], Boolean.TRUE);
                    matrixResult.get(selectionMatrixCoords.get(1)[0]).set(selectionMatrixCoords.get(1)[1], Boolean.TRUE);
                    System.out.println("Congratulations player [" + player.getId() + "] you scored one point!");
                    score.addPoint(player);
                    if (matrixResult.stream().allMatch(r -> Boolean.TRUE && r.stream().allMatch(c -> Boolean.TRUE))) {
                        break;
                    }
                }
                System.err.println("Yikes! Player [" + player.getId() + "] has failed, selection doesn't match!");
            }
            round++;
        }
    }

    private int getMatrixLocation(Player player, int selectionIndex, int maxLocation, String type) {
        System.out.println("Player [" + player.getId() + "] select a " + type + " " + selectionIndex + ":");
        int selectedLocation = 0;
        while (selectedLocation < 1 || selectedLocation > maxLocation) {
            try {
                selectedLocation = Integer.parseInt(MemorYK.readLine());
            } catch (NumberFormatException nfe) {
                selectedLocation = 0;
            }
            if (selectedLocation < 0 || selectedLocation > maxLocation) {
                System.err.println("Invalid selected " + type + ". Enter a value between 1 and " + maxLocation + ".");
            }
        }
        return selectedLocation;
    }
}