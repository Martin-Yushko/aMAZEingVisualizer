package model;

import java.util.LinkedList;

//Represents the skeleton of any pathfinder class in this project
public abstract class Pathfinder {
    protected long startTime;
    protected int targetX;
    protected int targetY;
    protected int mazeX;
    protected int nodesVisited;
    protected LinkedList<Animation> animations;

    //EFFECTS: finds a path through maze if one exists.
    public abstract PathInformation find(Maze maze);

    //EFFECTS: initializes the values necessary for the logic of an arbitrary algorithm given an input maze
    protected void initializeValues(Maze maze) {
        this.startTime = 0;
        this.targetX = maze.getTargetX();
        this.targetY = maze.getTargetY();
        this.startTime = System.nanoTime();
        this.nodesVisited = 0;
        this.animations = new LinkedList<>();
        this.mazeX = maze.dimensionX;
    }

    //EFFECTS: calculate the time taken for a function to run
    protected long getTimeTaken() {
        return (System.nanoTime() - startTime) / 1000000;
    }

}
