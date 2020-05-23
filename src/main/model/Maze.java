package model;

import persistence.Reader;
import java.io.PrintWriter;
import java.util.ArrayList;
import model.Tile.*;
import persistence.Saveable;

// Represents a maze which behaves as a graph of Tiles and which serves as the input for all the pathfinding algorithms
public class Maze implements Saveable {
    public static final int DEFAULT_Y_DIMENSION = 6;
    public static final int DEFAULT_X_DIMENSION = 6;
    
    public int dimensionX;
    public int dimensionY;
    public Tile startTile;
    private int startX;
    private int startY;
    private int targetX;
    private int targetY;

    //EFFECTS: constructs brand new maze with default values
    public Maze() {
        this(Maze.DEFAULT_X_DIMENSION, Maze.DEFAULT_Y_DIMENSION);
    }

    //EFFECTS: constructs a brand new maze with specified dimensions
    public Maze(int x, int y) {
        this.dimensionX = x;
        this.dimensionY = y;
        getEmptyGrid(true);
    }

    //EFFECTS: constructs a brand new maze with set tile types
    public Maze(ArrayList<TileType> tileTypes, int x, int y) {
        this.dimensionX = x;
        this.dimensionY = y;

        getEmptyGrid(false);
        int counter = 0;

        for (TileType type : tileTypes) {
            switch (type) {
                case START:
                    this.changeStart(counter % this.dimensionX, counter / this.dimensionX);
                    break;
                case TARGET:
                    this.changeTarget(counter % this.dimensionX, counter / this.dimensionX);
                    break;
                case OBSTACLE:
                    this.addObstacle(counter % this.dimensionX, counter / this.dimensionX);
                    break;
                default: break;
            }
            counter++;
        }
    }

    //EFFECTS: returns the maze's starting x-position
    public int getStartX() {
        return this.startX;
    }

    //EFFECTS: returns the maze's starting y-position
    public int getStartY() {
        return this.startY;
    }

    //EFFECTS: returns the maze's target x-position
    public int getTargetX() {
        return this.targetX;
    }

    //EFFECTS: returns the maze's target y-position
    public int getTargetY() {
        return this.targetY;
    }

    //MODIFIES: this
    //EFFECTS: changes the start position of the maze
    public void changeStart(int x, int y) {
        this.startTile = this.setStartTile(x, y);
    }

    //MODIFIES: this
    //EFFECTS: changes the target position of the maze
    public void changeTarget(int x, int y) {
        this.setTargetTile(x, y);
    }

    //MODIFIES: this
    //EFFECTS: constructs a grid with a start and end tile, and the rest empty
    public void getEmptyGrid(boolean initializeStartAndTarget) {
        Tile topLeftTile = new Tile(1);
        addEmptyNeighbours(topLeftTile);

        if (initializeStartAndTarget) {
            this.startX = 0;
            this.startY = 0;
            this.targetX = this.dimensionX - 1;
            this.targetY = this.dimensionY - 1;

            startTile = initializeStartTile(topLeftTile, startX, startY);
            initializeTargetTile(targetX, targetY);
        } else {
            this.startTile = topLeftTile;
        }
    }

    //REQUIRES: consumed tile is top-left tile of graph
    //MODIFIES: this
    //EFFECTS: populates graph with empty tiles to initialise empty grid
    private void addEmptyNeighbours(Tile tile) {
        // we treat inputted tile as top-left tile
        // we will populate the neighbours one column at a time
        ArrayList<Tile> leftNeighbours = new ArrayList<>(this.dimensionY);
        Tile referenceTile = tile;
        //generate initial (left-most) row of tiles
        for (int i = 1; i < this.dimensionY; i++) {
            referenceTile.bottomNeighbour = new Tile(i + 2);
            leftNeighbours.add(referenceTile);
            referenceTile = referenceTile.bottomNeighbour;
            referenceTile.topNeighbour = leftNeighbours.get(i - 1);
        }

        leftNeighbours.add(referenceTile);

        //iteratively generate each subsequent row of tiles
        for (int j = 0; j < this.dimensionX - 1; j++) {
            for (int i = 0; i < this.dimensionY; i++) {
                Tile currentTile = leftNeighbours.get(i);
                currentTile.rightNeighbour = new Tile((j + 1) * this.dimensionX + i);
                leftNeighbours.set(i, currentTile.rightNeighbour);
                leftNeighbours.get(i).leftNeighbour = currentTile;
                if (i > 0) {
                    leftNeighbours.get(i).topNeighbour = leftNeighbours.get(i - 1);
                    leftNeighbours.get(i).topNeighbour.bottomNeighbour = leftNeighbours.get(i);
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the starting tile in maze
    private Tile initializeStartTile(Tile topLeftTile, int x, int y) {
        return setStartTile(topLeftTile, 0, 0, x, y);
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the starting tile in maze
    private Tile setStartTile(int x, int y) {
        if (x == this.targetX && y == this.targetY) {
            return this.startTile;
        }

        if (validCoordinates(x, y) && startTile.type.equals(TileType.START)) {
            //the last expression in the if is neccesary for loading; if "start tile" is
            //obstacle, it should remain an obstacle instead of reverting to empty.
            //TODO: it's still broken
            startTile.setType(TileType.EMPTY);
        }

        int oldStartX = this.startX;
        int oldStartY = this.startY;
        this.startX = x;
        this.startY = y;

        return setStartTile(this.startTile, oldStartX, oldStartY, x, y);
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the starting tile in maze
    private Tile setStartTile(Tile tile, int currentX, int currentY, int destinationX, int destinationY) {
        if (!validCoordinates(currentX, currentY)) {
            return this.startTile;
        }

        if (currentX == destinationX && currentY == destinationY) {
            tile.setType(Tile.TileType.START);
            this.startX = destinationX;
            this.startY = destinationY;
            return tile;
        } else if (currentX < destinationX) {
            return setStartTile(tile.rightNeighbour, currentX + 1, currentY, destinationX, destinationY);
        } else if (currentX > destinationX) {
            return setStartTile(tile.leftNeighbour, currentX - 1, currentY, destinationX, destinationY);
        } else if (currentY < destinationY) {
            return setStartTile(tile.bottomNeighbour, currentX, currentY + 1, destinationX, destinationY);
        } else {
            return setStartTile(tile.topNeighbour, currentX, currentY - 1, destinationX, destinationY);
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the target tile in maze
    private void initializeTargetTile(int x, int y) {
        setTargetTile(this.findTopLeftTile(startTile), 0, 0, x, y);
    }

    private boolean validCoordinates(int x, int y) {
        boolean validX = (x < Maze.this.dimensionX && x >= 0);
        boolean validY = (y < Maze.this.dimensionY && y >= 0);
        return validX && validY;
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the target tile in maze
    private void setTargetTile(int x, int y) {
        if (x == this.startX && y == this.startY) {
            return;
        }
       
        Tile targetTile = this.findTile(this.targetX, this.targetY);

        if (validCoordinates(x, y)) {
            targetTile.setType(TileType.EMPTY);
        }

        setTargetTile(targetTile, this.targetX, this.targetY, x, y);
    }

    //MODIFIES: this
    //EFFECTS: sets the initial value of the target tile in maze
    private void setTargetTile(Tile tile, int currentX, int currentY, int destinationX, int destinationY) {
        if (!validCoordinates(currentX, currentY)) {
            return;
        }

        if (currentX == destinationX && currentY == destinationY) {
            tile.setType(TileType.TARGET);
            this.targetY = destinationY;
            this.targetX = destinationX;
        } else if (currentX < destinationX) {
            setTargetTile(tile.rightNeighbour, currentX + 1, currentY, destinationX, destinationY);
        } else if (currentX > destinationX) {
            setTargetTile(tile.leftNeighbour, currentX - 1, currentY, destinationX, destinationY);
        } else if (currentY < destinationY) {
            setTargetTile(tile.bottomNeighbour, currentX, currentY + 1, destinationX, destinationY);
        } else {
            setTargetTile(tile.topNeighbour, currentX, currentY - 1, destinationX, destinationY);
        }
    }

    //EFFECTS: converts maze's data to a list of tileTypes
    public ArrayList<TileType> toTileTypesArrayList() {
        ArrayList<TileType> tileTypes = new ArrayList<>(this.dimensionX * this.dimensionY);

        Tile referenceTile = this.findTopLeftTile(startTile);
        Tile nextLeftmostTile = referenceTile.bottomNeighbour;
        tileTypes.add(referenceTile.type);

        //break condition is at bottom-right node, signaling end of graph
        while (!(referenceTile.bottomNeighbour == null && referenceTile.rightNeighbour == null)) {
            if (referenceTile.rightNeighbour != null) {
                referenceTile = referenceTile.rightNeighbour;
            } else {
                referenceTile = nextLeftmostTile;
                nextLeftmostTile = referenceTile.bottomNeighbour;
            }
            Tile nextTile = new Tile(referenceTile);
            tileTypes.add(nextTile.type);
        }
        return tileTypes;
    }

    //EFFECTS: converts maze's data to a list of animationStates
    public ArrayList<AnimationState> toAnimationStatesArrayList() {
        ArrayList<AnimationState> states = new ArrayList<>(this.dimensionX * this.dimensionY);

        Tile referenceTile = this.findTopLeftTile(startTile);
        Tile nextLeftmostTile = referenceTile.bottomNeighbour;
        states.add(referenceTile.animationState);

        //break condition is at bottom-right node, signaling end of graph
        while (!(referenceTile.bottomNeighbour == null && referenceTile.rightNeighbour == null)) {
            if (referenceTile.rightNeighbour != null) {
                referenceTile = referenceTile.rightNeighbour;
            } else {
                referenceTile = nextLeftmostTile;
                nextLeftmostTile = referenceTile.bottomNeighbour;
            }
            Tile nextTile = new Tile(referenceTile);
            states.add(nextTile.animationState);
        }
        return states;
    }

    //REQUIRES: 0 <= x < this.dimensionX and 0 <= y < this.dimensionY
    //EFFECTS: returns the tile at position (x,y) in maze
    public Tile findTile(int x, int y) {
        Tile referenceTile = this.findTopLeftTile(startTile);

        for (int i = 0; i < y; i++) {
            referenceTile = referenceTile.bottomNeighbour;
        }

        for (int j = 0; j < x; j++) {
            referenceTile = referenceTile.rightNeighbour;
        }

        return referenceTile;
    }

    //EFFECTS: returns the top left-most node in the graph of the inputted tile
    public Tile findTopLeftTile(Tile tile) {
        Tile referenceTile = tile;
        while (referenceTile.topNeighbour != null) {
            referenceTile = referenceTile.topNeighbour;
        }

        while (referenceTile.leftNeighbour != null) {
            referenceTile = referenceTile.leftNeighbour;
        }

        return referenceTile;
    }

    //EFFECTS: adds an obstacle to the maze at specified position
    //         if specified position is a start/target node, don't do anything
    public void addObstacle(int x, int y) {
        if (x >= this.dimensionX || y >= this.dimensionY) {
            return;
        }

        Tile referenceTile = this.findTopLeftTile(startTile);

        for (int i = 0; i < x; i++) {
            referenceTile = referenceTile.rightNeighbour;
        }

        for (int i = 0; i < y; i++) {
            referenceTile = referenceTile.bottomNeighbour;
        }

        if (referenceTile.type != TileType.START && referenceTile.type != TileType.TARGET) {
            referenceTile.setType(TileType.OBSTACLE);
        }
    }

    //EFFECTS: removes an obstacle to the maze at specified position
    //         if specified position is a start/target node, don't do anything
    public void removeObstacle(int x, int y) {
        setEmpty(x, y);
    }

    //EFFECTS: set tile at position x,y to be an empty tile type
    private void setEmpty(int x, int y) {

        Tile referenceTile = this.findTopLeftTile(startTile);

        for (int i = 0; i < x; i++) {
            referenceTile = referenceTile.rightNeighbour;
        }

        for (int i = 0; i < y; i++) {
            referenceTile = referenceTile.bottomNeighbour;
        }

        if (!referenceTile.type.equals(TileType.START) && !referenceTile.type.equals(TileType.TARGET)) {
            referenceTile.setType(TileType.EMPTY);
        }
    }

    //EFFECTS: converts maze's data to a list of tiles
    public ArrayList<Tile> toTilesArrayList() {
        ArrayList<Tile> tiles = new ArrayList<>(this.dimensionX * this.dimensionY);

        Tile referenceTile = this.findTopLeftTile(startTile);
        Tile nextLeftmostTile = referenceTile.bottomNeighbour;
        tiles.add(referenceTile);

        //break condition is at bottom-right node, signaling end of graph
        while (!(referenceTile.bottomNeighbour == null && referenceTile.rightNeighbour == null)) {
            if (referenceTile.rightNeighbour != null) {
                referenceTile = referenceTile.rightNeighbour;
            } else {
                referenceTile = nextLeftmostTile;
                nextLeftmostTile = referenceTile.bottomNeighbour;
            }
            Tile nextTile = new Tile(referenceTile);
            tiles.add(nextTile);
        }
        return tiles;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print(this.dimensionX);
        printWriter.print(Reader.DELIMITER);
        printWriter.print(this.dimensionY);
        printWriter.print(Reader.DELIMITER);
        ArrayList<TileType> tileTypes = this.toTileTypesArrayList();
        for (int i = 0; i < tileTypes.size(); i++) {
            printWriter.print(tileTypes.get(i).name());
            printWriter.print(Reader.DELIMITER);
        }
        printWriter.close();
    }
}
