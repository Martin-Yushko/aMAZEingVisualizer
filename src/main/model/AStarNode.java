package model;

//represents a node in the maze used for the A* algorithm
public class AStarNode {
    public AStarNode parent;
    public int cumulativeWeight;
    public int movementWeight;
    public int heuristic;
    public int posX;
    public int posY;

    //EFFECTS: constructs a new AStarNode with the given parent
    public AStarNode(AStarNode parent) {
        this.parent = parent;
    }

    //EFFECTS: calculates and returns the net cost of this node
    public int getCost() {
        return this.cumulativeWeight + this.heuristic;
    }

    @Override
    public boolean equals(Object o) {
        //modeled after: https://stackoverflow.com/questions/30855198/setting-objects-equal-to-eachother-java
        if (! (o instanceof AStarNode)) {
            return false; //AStar Node cannot be equal to an object which is not an AStarNode
        }

        AStarNode otherNode = (AStarNode) o;
        return this.posX == otherNode.posX && this.posY == otherNode.posY;
    }
}
