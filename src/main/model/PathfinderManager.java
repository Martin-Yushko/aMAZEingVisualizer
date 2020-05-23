package model;

import ui.ActionManager;
import ui.AnimationPlayer;

import java.util.List;

// Responsible for generating the animation frames and finding the path in the maze
public class PathfinderManager {
    public Maze source;

    //EFFECTS: constructs a new PathfinderManager whose source is the given maze
    public PathfinderManager(Maze maze) {
        this.source = maze;
    }

    //EFFECTS: Finds path according to the A* algorithm
    public PathInformation runAStar() {
        AStarFinder pathFinder = new AStarFinder();

        PathInformation output = pathFinder.find(source);

        if (output.possible) {
            return output;
        } else {
            return handleImpossiblePath(output);
        }
    }

    //REQUIRES: Path is not possible for the given maze
    //EFFECTS: If no valid path exists, notify user
    public PathInformation handleImpossiblePath(PathInformation pathInfo) {
        //stub because this becomes relevant when beginning to implement UI
        Animation lastAnimation = pathInfo.animations.getLast();
        pathInfo.animations.add(new Animation(AnimationPlayer.VISUALIZATION_SPEED * 1000 * 3));
        int counter = 0;
        while (counter < lastAnimation.animationStates.size()) {
            Tile.AnimationState state = lastAnimation.animationStates.get(counter);
            if (state.equals(Tile.AnimationState.VISITED) || state.equals(Tile.AnimationState.CURRENT)) {
                Animation newAnimation = new Animation(lastAnimation);
                newAnimation.animationStates.set(counter, Tile.AnimationState.FAILURE);
                pathInfo.animations.add(newAnimation);
                lastAnimation = newAnimation;
            }
            counter++;
        }

        return pathInfo;
    }
}
