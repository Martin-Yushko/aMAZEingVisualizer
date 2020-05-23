package model;

import util.Pair;

import java.util.ArrayList;

// Represents a single tile in the maze
// A single tile represents a node in a graph whose neighbours are adjacent in the maze, or which are null,
// expressing that they are an edge or corner of the maze
public class Tile {
    public Tile rightNeighbour;
    public Tile bottomNeighbour;
    public Tile leftNeighbour;
    public Tile topNeighbour;
    public Tile portalDestination;
    private int id;
    public int weight;
    public TileType type;
    public AnimationState animationState;

    //EFFECTS: constructs a default tile with a certain id
    public Tile(int id) {
        this.rightNeighbour = null;
        this.bottomNeighbour = null;
        this.leftNeighbour = null;
        this.topNeighbour = null;
        this.weight = 1;
        this.type = TileType.EMPTY;
        this.animationState = AnimationState.DEFAULT;
        this.id = id;
    }

    //EFFECTS: constructs a copy of the passed-in tile.
    public Tile(Tile tileToCopy) {
        this.rightNeighbour = tileToCopy.rightNeighbour;
        this.bottomNeighbour = tileToCopy.bottomNeighbour;
        this.leftNeighbour = tileToCopy.leftNeighbour;
        this.topNeighbour = tileToCopy.topNeighbour;
//        this.colour = tileToCopy.colour;
        this.weight = tileToCopy.weight;
        this.type = tileToCopy.type;
        this.animationState = tileToCopy.animationState;
        this.id = tileToCopy.id;
    }

    //MODIFIES: this
    //EFFECTS: changes this tile's type to the specified one
    public void setType(TileType newType) {
        //if (newType != TileType.PORTAL) {
        this.type = newType;
        //}
    }

//    public void setPortal(Tile destination) {
//        this.type = TileType.PORTAL;
//        this.portalDestination = destination;
//    }

    //MODIFIES: this
    //EFFECTS: changes this tile's animation state to the specified one
    public void setState(AnimationState state) {
        this.animationState = state;
    }

    //EFFECTS: returns all neighbours of this tile
    public ArrayList<Pair<Tile, Direction>> getNeighbours() {
        ArrayList<Pair<Tile, Direction>> neighbours = new ArrayList<>();

//        if (this.type == TileType.PORTAL) {
//            neighbours.add(portalDestination);
//            return neighbours
//        }
        if (this.rightNeighbour != null) {
            Pair<Tile, Direction> entry = new Pair<>(this.rightNeighbour, Direction.RIGHT);
            neighbours.add(entry);
        }
        if (this.bottomNeighbour != null) {
            Pair<Tile, Direction> entry = new Pair<>(this.bottomNeighbour, Direction.BOTTOM);
            neighbours.add(entry);
        }
        if (this.leftNeighbour != null) {
            Pair<Tile, Direction> entry = new Pair<>(this.leftNeighbour, Direction.LEFT);
            neighbours.add(entry);
        }
        if (this.topNeighbour != null) {
            Pair<Tile, Direction> entry = new Pair<>(this.topNeighbour, Direction.TOP);
            neighbours.add(entry);
        }

        return neighbours;
    }

    //enumeration representing the different tile types that there can exist
    public enum TileType {
        EMPTY,
        OBSTACLE,
        START,
        TARGET,
        CHECKPOINT,
        PORTAL
    }

    //enumeration representing the different directions a tile can be with respect to another
    enum Direction {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    //enumeration representing the different animation states that there can exist
    public enum AnimationState {
        CURRENT,
        VISITED,
        PATH,
        DEFAULT,
        FAILURE
    }

    @Override
    public boolean equals(Object o) {
        //modeled after: https://stackoverflow.com/questions/30855198/setting-objects-equal-to-eachother-java
        if (! (o instanceof Tile)) {
            return false; //Tile cannot be equal to an object which is not a tile
        }

        Tile otherTile = (Tile) o;
        return this.id == otherTile.id;
    }
}


