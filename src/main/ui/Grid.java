package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import model.Maze;
import java.util.ArrayList;
import java.util.List;

import model.Tile;

//represents the grid viewed by the user as an amalgamation of Cells; conveys information from Maze
public class Grid extends GridPane {
    int rows;
    int cols;
    double height;
    double width;
    private List<Cell> cells;
    public Maze mazeSource;
    private VisualisationStatus status;

    public enum VisualisationStatus {
        NOT_IN_PROGRESS, IN_PROGRESS, DONE,
    }

    //EFFECTS: constructs a new Grid object given dimensions and col/row quantity
    public Grid(double w, double h, int r, int c) {
        this.rows = r;
        this.cols = c;
        initializeGrid(w, h);
        this.initializeEmptyCells();
    }

    //EFFECTS: constructs a new Grid object given dimensions and a source maze
    public Grid(double w, double h, Maze mazeData) {
        this.rows = mazeData.dimensionX;
        this.cols = mazeData.dimensionY;
        initializeGrid(w, h);
        this.initializeCells(mazeData);
        this.mazeSource = mazeData;
    }

    //MODIFIES: this
    //EFFECTS: initializes grid with default values and those pertaining to width/height
    private void initializeGrid(double w, double h) {
        this.status = VisualisationStatus.NOT_IN_PROGRESS;
        cells = new ArrayList<>();
        this.setMinHeight(h);
        this.setMaxHeight(h);
        this.setMinWidth(w);
        this.setMaxWidth(w);
        this.height = h;
        this.width = w;
        this.setHgap(0);
        this.setVgap(0);
        this.setAlignment(Pos.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: populates grid with all empty cells
    private void initializeEmptyCells() {
        double w = this.width / this.cols - 1;
        double h = this.height / this.rows - 1;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = new Cell(w, h);
                cells.add(cell);
                this.add(cell, j, i);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: populates grid in accordance to the source maze
    private void initializeCells(Maze mazeData) {
        this.initializeEmptyCells();

        this.initializeStartAndTargetCells(mazeData);
        this.initializeObstacleCells(mazeData);
    }

    //MODIFIES: this
    //EFFECTS: populates grid's start and end cells
    private void initializeStartAndTargetCells(Maze mazeData) {
        int startIndex = mazeData.getStartX() + mazeData.getStartY() * cols;
        int targetIndex = mazeData.getTargetX() + mazeData.getTargetY() * cols;
        Cell startCell = this.cells.get(startIndex);
        Cell targetCell = this.cells.get(targetIndex);
        startCell.setToStart();
        targetCell.setToTarget();
    }

    //MODIFIES: this
    //EFFECTS: populates grid's obstacle cells
    private void initializeObstacleCells(Maze mazeData) {
        ArrayList<Tile.TileType> tiles = mazeData.toTileTypesArrayList();
        for (int i = 0; i < this.rows * this.cols; i++) {
            if (tiles.get(i) == Tile.TileType.OBSTACLE) {
                Cell obstacleCell = this.cells.get(i);
                obstacleCell.setToObstacle();
            }
        }
    }

    //EFFECTS: get the cell at position (col, row) in grid
    public Cell getCell(int col, int row) {
        return cells.get(row * cols + col);
    }

    //MODIFIES: this
    //EFFECTS: change the source maze and re-render accordingly
    public void changeMaze(Maze maze) {
        this.cols = maze.dimensionX;
        this.rows = maze.dimensionY;
        this.getChildren().removeAll(this.getChildren());
        initializeGrid(this.width, this.height);
        this.initializeCells(maze);
        this.mazeSource = maze;
    }

    //MODIFIES: this
    //EFFECTS: adds obstacle to the grid and the maze data source
    public void addObstacle(int col, int row) {
        this.mazeSource.addObstacle(col, row);
        Cell selectedCell = this.getCell(col, row);
        selectedCell.setToObstacle();
    }

    //MODIFIES: this
    //EFFECTS: remove obstacle from the grid and the maze data source
    public void removeObstacle(int col, int row) {
        this.mazeSource.removeObstacle(col, row);
        Cell selectedCell = this.getCell(col, row);
        selectedCell.setToEmpty();
    }

    //MODIFIES: this
    //EFFECTS: changes the cell which is to become the new start node
    public void setStart(Cell cell) {
        cell.setToStart();
        int col = GridPane.getColumnIndex(cell);
        int row = GridPane.getRowIndex(cell);
        this.mazeSource.changeStart(col, row);
    }

    //MODIFIES: this
    //EFFECTS: changes the cell which is to become the new target node
    public void setTarget(Cell cell) {
        cell.setToTarget();
        int col = GridPane.getColumnIndex(cell);
        int row = GridPane.getRowIndex(cell);
        this.mazeSource.changeTarget(col, row);
    }

    //MODIFIES: this
    //EFFECTS: removes every obstacle from this grid and its maze data source
    public void removeAllObstacles() {
        for (Cell cell : cells) {
            if (cell.type.equals(Tile.TileType.OBSTACLE)) {
                int col = GridPane.getColumnIndex(cell);
                int row = GridPane.getRowIndex(cell);
                removeObstacle(col, row);
            }
        }
    }

    public void setStatus(VisualisationStatus newStatus) {
        this.status = newStatus;
    }

    public VisualisationStatus getStatus() {
        return this.status;
    }

    //MODIFIES: this
    //EFFECTS: reset cells to their default appearance
    public void unanimateCells() {
        for (Cell cell : cells) {
            cell.resetToDefaultState();
        }
    }
}
