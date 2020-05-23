package model;

import java.util.LinkedList;

// Represents a data model for all the aspects of the visualisation which the user may be interested in,
// including run-time statistics, and the raw animation data itself
public class PathInformation {
    public LinkedList<Animation> animations;
    public long timeTaken;
    public int numberNodesVisited;
    public boolean possible;

    //REQUIRES: the path asked to be depicted by the constructed object must be possible
    //EFFECTS: constructs a PathInformation object which implicitly represents a path which is possible
    public PathInformation(LinkedList<Animation> animations, long timeTaken, int nodesVisited) {
        this.animations = animations;
        this.timeTaken = timeTaken;
        this.numberNodesVisited = nodesVisited;
        this.possible = true;
    }

    //EFFECTS: constructs a PathInformation object which may or may not be possible
    public PathInformation(LinkedList<Animation> animations, long timeTaken, int nodesVisited, boolean possible) {
        this.animations = animations;
        this.timeTaken = timeTaken;
        this.numberNodesVisited = nodesVisited;
        this.possible = possible;
    }
}
