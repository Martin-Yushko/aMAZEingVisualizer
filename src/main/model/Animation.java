package model;

import util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

import model.Tile.*;

// Represents one frame of a complete animation
public class Animation {
    public ArrayList<TileType> tileStates;
    public ArrayList<Tile.AnimationState> animationStates;
    private int mazeX;
    private int mazeY;
    private double durationInMillis = -1;

    //EFFECTS: constructs a frame from the given maze
    public Animation(Maze maze) {
        this.tileStates = maze.toTileTypesArrayList();
        this.animationStates = maze.toAnimationStatesArrayList();
        this.mazeX = maze.dimensionX;
        this.mazeY = maze.dimensionY;
    }

    public Animation(double durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    //EFFECTS: constructs a copy of the passed-in animation
    public Animation(Animation animationToCopy) {
        this.tileStates = (ArrayList<TileType>) animationToCopy.tileStates.clone();
        this.animationStates = (ArrayList<AnimationState>) animationToCopy.animationStates.clone();
        this.durationInMillis = animationToCopy.durationInMillis;
        this.mazeX = animationToCopy.getMazeX();
        this.mazeY = animationToCopy.getMazeY();
    }

    //REQUIRES: all the x and y values passed in are between 0 and Maze.X_DIMENSION or Maze.Y_DIMENSION, respectively
    //EFFECTS: creates an animation of the frame after which the current node goes from current
    //         to visited, and the new node becomes the new current one; 1 step in the algorithm
    public Animation nextAnimation(int currentX, int currentY, int parentX, int parentY) {
        int parentIndex = parentX + parentY * mazeX;
        int currentIndex = currentX + currentY * mazeX;
        Animation nextAnimation = new Animation(this);

        //Change current node to Current status
        nextAnimation.animationStates.set(currentIndex, AnimationState.CURRENT);

        //Change previously visited node to Visited status
        nextAnimation.animationStates.set(parentIndex, AnimationState.VISITED);

        return nextAnimation;
    }

    //REQUIRES: all coordinates are valid in the context of the maze; between 0 and Maze.X_DIMENSION or Maze.Y_DIMENSION
    //EFFECTS: with the latest animation as reference, produces the sequence of animations which
    //         display the found path
    public static LinkedList<Animation> getPathAnimations(LinkedList<Pair<Integer, Integer>> pathCoordinates,
                                                           Animation lastAnimation, int mazeX) {
        LinkedList<Animation> pathAnimations = new LinkedList<>();
        Animation latestAnimation = lastAnimation;

        for (int i = 0; i < pathCoordinates.size(); i++) {
            //create path in backwards order since inputted coordinates begin at target and end at start
            latestAnimation = createPathAnimation(latestAnimation,
                    pathCoordinates.get(pathCoordinates.size() - 1 - i), mazeX);
            pathAnimations.add(latestAnimation);
        }

        return pathAnimations;
    }

    //REQUIRES: the coordinate is valid in the context of the maze
    //EFFECTS: produces a new animation which is identical to the given animation,
    //         except the tile at (x,y) is highlighted as a path tile
    private static Animation createPathAnimation(Animation latestAnimation,
                                                 Pair<Integer, Integer> coordinate, int mazeX) {
        int coordX = coordinate.first;
        int coordY = coordinate.second;
        int index = coordX + coordY * mazeX;

        Animation newAnimation = new Animation(latestAnimation);
        newAnimation.animationStates.set(index, AnimationState.PATH);

        return newAnimation;
    }

    public int getMazeX() {
        return this.mazeX;
    }

    public int getMazeY() {
        return this.mazeY;
    }

    public double getDuration() {
        return this.durationInMillis;
    }
}
