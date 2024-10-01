package student;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.Random;
import java.util.Stack;

public class Grid extends AbstractGrid {
    private List<List<Integer>> grid;
    private Stack<List<List<Integer>>> gridHistoryStack = new Stack<>();

    public Grid(BufferedImage image, long seed) {
        super(image, seed);
        grid = new ArrayList<>();
        gridInitialization(image);
    }

    public Grid(BufferedImage image, long seed, boolean testMode) {
        super(image, seed, testMode);
        gridInitialization(image);
    }

    private void gridInitialization(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        grid = new ArrayList<>(height);

        for (int y = 0; y < height; y++) {
            List<Integer> row = new ArrayList<>(width);
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                row.add(pixel);
            }
            grid.add(row);
        }
    }
    @Override
    BufferedImage convertToBufferedImage() {
        int height = grid.size();
        int width = grid.get(0).size();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            List<Integer> row = grid.get(y);
            for (int x = 0; x < width; x++) {
                int pixel = row.get(x);
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }

    @Override
    int getBluestColumnIndex() throws RequestFailedException {
        if (grid.isEmpty()) {
            throw new RequestFailedException("The Grid has no columns.");
        }

        int bluestColumnIndex = 0;
        int maxBlueSum = getColumnBlueSum(0);

        for (int columnIndex = 1; columnIndex < grid.get(0).size(); columnIndex++) {
            int columnBlueSum = getColumnBlueSum(columnIndex);
            if (columnBlueSum > maxBlueSum) {
                maxBlueSum = columnBlueSum;
                bluestColumnIndex = columnIndex;
            }
        }
        return bluestColumnIndex;
    }
    // A Helper method that calculates the sum of blue comp. in a column
    private int getColumnBlueSum(int columnIndex) {
        int totalBlueSum = 0;
        for (List<Integer> currentRow : grid) {
            int pixel = currentRow.get(columnIndex);
            Color pixelColor = new Color(pixel);
            totalBlueSum += pixelColor.getBlue();
        }
        return totalBlueSum;
    }

    @Override
    void removeBluestColumn() throws RequestFailedException {
        if (grid.size() <= 1) {
            throw new RequestFailedException("Grid has one or less columns.");
        }
        List<List<Integer>> prevGrid = new ArrayList<>();
        for (List<Integer> row : grid) {
            prevGrid.add(new ArrayList<>(row));
        }
        int bluestColumnIndex = getBluestColumnIndex();
        for (List<Integer> row : grid) {
            row.remove(bluestColumnIndex);
        }
        gridHistoryStack.push(prevGrid);
    }
     void removeRandomColumn() throws RequestFailedException {
        if (grid.isEmpty() || grid.get(0).isEmpty()) {
            throw new RequestFailedException("The Grid has no columns.");
        }
        Random random = new Random();
        int columnIndexToRemove = random.nextInt(grid.get(0).size());
        List<List<Integer>> prevGrid = new ArrayList<>();
        for (List<Integer> row : grid) {
            prevGrid.add(new ArrayList<>(row));
        }
        List<Integer> removedColumn = new ArrayList<>();
        for (List<Integer> row : grid) {
            removedColumn.add(row.remove(columnIndexToRemove));
        }
        gridHistoryStack.push(prevGrid);
    }
    public void undo() throws RequestFailedException {
        if (gridHistoryStack.isEmpty()) {
            throw new RequestFailedException("There is nothing to undo.");
        }
        List<List<Integer>> prevGrid = gridHistoryStack.pop();
        grid.clear();
        for (List<Integer> row : prevGrid) {
            grid.add(new ArrayList<>(row));
        }
    }
}
