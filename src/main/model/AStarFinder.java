package model;

import util.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import model.Tile.*;
import util.Triplet;

//responsible for all the logic concerning the A* Algorithm
public class AStarFinder extends Pathfinder {

    private ArrayList<Triplet<AStarNode, Direction, Tile>> todo;
    private ArrayList<AStarNode> visited;


    //EFFECTS: initializes the values necessary for the logic of the algorithm of A* given an input maze
    @Override
    protected void initializeValues(Maze maze) {
        super.initializeValues(maze);
        this.todo = new ArrayList<>();
        this.visited = new ArrayList<>();
    }

    //EFFECTS: calculates heuristic of given node
    private int calculateHeuristic(AStarNode node) {
        return Math.abs(node.posX - this.targetX) + Math.abs(node.posY - this.targetY);
    }

    //EFFECTS: calculates weight (distance traveled) of given node
    private int calculateWeight(AStarNode node) {
        return node.parent.cumulativeWeight + node.movementWeight;
    }

    //EFFECTS: converts consumed maze's start tile to an AStarNode
    private AStarNode getStartingNode(Maze maze) {
        AStarNode startNode = new AStarNode(null);
        startNode.movementWeight = 0;
        startNode.cumulativeWeight = 0;
        startNode.posX = maze.getStartX();
        startNode.posY = maze.getStartY();
        startNode.heuristic = this.calculateHeuristic(startNode);
        return startNode;
    }

    //MODIFIES: this
    //EFFECTS: find (the shortest) path in the given maze, if one exists
    public PathInformation find(Maze maze) {
        // A* Algorithm inspired from https://www.growingwiththeweb.com/2012/06/a-pathfinding-algorithm.html
        this.initializeValues(maze);
        AStarNode startNode = this.getStartingNode(maze);
        Tile referenceTile = maze.startTile;
        todo.add(new Triplet<>(startNode, null, referenceTile));

        Animation currentAnimation = new Animation(maze);
        Pair<Integer, Integer> parentPos = new Pair<>(startNode.posX, startNode.posY);

        while (todo.size() > 0) {
            nodesVisited += 1;
            Triplet<AStarNode, Direction, Tile> current = this.getLowest(todo);
            Animation nextAnimation = currentAnimation.nextAnimation(current.first.posX, current.first.posY,
                                                                        parentPos.first, parentPos.second);
            animations.add(nextAnimation);
            currentAnimation = nextAnimation;

            referenceTile = updateReferenceTile(current.third, current.second);

            if (this.isTarget(current.first)) {
                this.addPathAnimations(current.first);
                return new PathInformation(animations, getTimeTaken(), nodesVisited);
            }

            removeFromTodo(current);
            this.checkNeighbours(referenceTile, current.first);

            parentPos = new Pair<>(current.first.posX, current.first.posY);
        }

        return new PathInformation(animations, getTimeTaken(), nodesVisited, false);
    }

    //MODIFIES: this
    //EFFECTS: removes given entry from list of entries which still need to be checked and adds to visited
    private void removeFromTodo(Triplet<AStarNode, Direction, Tile> current) {
        todo.remove(current);
        visited.add(current.first);
    }

    //REQUIRES: the direction given yields a valid tile
    //EFFECTS: given the previous tile and a direction, finds the tile to that direction of the previous
    private Tile updateReferenceTile(Tile tile, Direction direction) {
        if (direction == Direction.RIGHT) {
            return tile.rightNeighbour;
        } else if (direction == Direction.BOTTOM) {
            return tile.bottomNeighbour;
        } else if (direction == Direction.LEFT) {
            return tile.leftNeighbour;
        } else if (direction == Direction.TOP) {
            return tile.topNeighbour;
        } else {
            return tile;
        }
    }

    private ArrayList<AStarNode> todoToNodeList(ArrayList<Triplet<AStarNode, Direction, Tile>> source) {
        ArrayList<AStarNode> result = new ArrayList<>();
        for (Triplet<AStarNode, Direction, Tile> data : source) {
            result.add(data.first);
        }
        return result;
    }

    //REQUIRES: the node is contained in the source arrayList
    //EFFECTS: returns the entry matching inputted data from list of entries that still need to be checked
    public AStarNode findNode(AStarNode node, ArrayList<AStarNode> source) {
        AStarNode foundNode = null;
        for (AStarNode candidateNode : source) {
            if (node.equals(candidateNode)) {
                foundNode = candidateNode;
                break;
            }
        }
        return foundNode;
    }

    //MODIFIES: this
    //EFFECTS: adds valid neighbours of given node as entries that need to be checked
    //         if nodes are already entries, replaces old entry if it has a lower cumulativeWeight
    private void checkNeighbours(Tile referenceTile, AStarNode current) {
        ArrayList<Pair<Tile, Direction>> neighbours = referenceTile.getNeighbours();
        ArrayList<Pair<Tile, Direction>> validNeighbours = this.validNeighbours(neighbours);

        for (Pair<Tile, Direction> tileData: validNeighbours) {
            AStarNode nextNode = new AStarNode(current);
            nextNode.posX = this.assignXPosition(nextNode, tileData.second);
            nextNode.posY = this.assignYPosition(nextNode, tileData.second);
            nextNode.parent = current;
            if (!visited.contains(nextNode)) {
                nextNode.movementWeight = tileData.first.weight;
                nextNode.cumulativeWeight = this.calculateWeight(nextNode);
                nextNode.heuristic = this.calculateHeuristic(nextNode);
                if (!inTodo(nextNode)) {
                    todo.add(0, new Triplet<>(nextNode, tileData.second, referenceTile));
                } else {
                    AStarNode nodeFromTodo = this.findNode(nextNode, todoToNodeList(todo));

                    if (nodeFromTodo.cumulativeWeight > nextNode.cumulativeWeight) {
                        nodeFromTodo.cumulativeWeight = nextNode.cumulativeWeight;
                        nodeFromTodo.parent = nextNode.parent;
                    }
                }
            }
        }
    }

    //EFFECTS: return true if node is in list of entries that must be checked; otherwise returns false
    private boolean inTodo(AStarNode node) {
        for (int i = 0; i < todo.size(); i++) {
            if (todo.get(i).first.equals(node)) {
                return true;
            }
        }

        return false;
    }

    //EFFECTS: returns filtered list of neighbours with the criteria that they mustn't be an obstacle tile
    private ArrayList<Pair<Tile, Direction>> validNeighbours(ArrayList<Pair<Tile, Direction>> neighbours) {
        ArrayList<Pair<Tile, Direction>> validNeighbours = new ArrayList<>();
        for (Pair<Tile, Direction> neighbour : neighbours) {
            // filter out obstacles from possible paths
            if (neighbour.first.type != TileType.OBSTACLE) {
                validNeighbours.add(neighbour);
            }
        }
        return validNeighbours;
    }

    //REQUIRES: given node has a parent
    //MODIFIES: node
    //EFFECTS: initialize the y value of given node given its relative direction to parent
    private int assignYPosition(AStarNode node, Direction direction) {
        if (direction == Direction.TOP) {
            return node.parent.posY - 1;
        } else if (direction == Direction.BOTTOM) {
            return node.parent.posY + 1;
        } else {
            return node.parent.posY;
        }
    }

    //REQUIRES: given node has a parent
    //MODIFIES: node
    //EFFECTS: initialize the y value of given node given its relative direction to parent
    private int assignXPosition(AStarNode node, Direction direction) {
        if (direction == Direction.LEFT) {
            return node.parent.posX - 1;
        } else if (direction == Direction.RIGHT) {
            return node.parent.posX + 1;
        } else {
            return node.parent.posX;
        }
    }

    //REQUIRES: candidate list is non-empty
    //EFFECTS: outputs the candidate with the least cost and consequently most potential for shortest path
    private Triplet<AStarNode,Direction, Tile> getLowest(ArrayList<Triplet<AStarNode, Direction, Tile>> candidates) {
        Triplet<AStarNode,Direction, Tile> currentLowest = candidates.get(0);
        for (Triplet<AStarNode, Direction, Tile> nodeData : candidates) {
            if (nodeData.first.getCost() < currentLowest.first.getCost()) {
                currentLowest = nodeData;
            }
        }

        return currentLowest;
    }

    //EFFECTS: returns true if given node is the target node; otherwise returns false
    private Boolean isTarget(AStarNode node) {
        return node.posX == this.targetX && node.posY == this.targetY;
    }

    //REQUIRES: a shortest path exists in the maze
    //EFFECTS: adds a series of animations representing the shortest path of maze
    private void addPathAnimations(AStarNode endNode) {
        LinkedList<Pair<Integer, Integer>> pathCoordinates = new LinkedList<>();
        AStarNode referenceNode = endNode;
        pathCoordinates.add(new Pair<>(referenceNode.posX, referenceNode.posY));
        while (referenceNode.parent != null) {
            referenceNode = referenceNode.parent;
            pathCoordinates.add(new Pair<>(referenceNode.posX, referenceNode.posY));
        }

        animations.addAll(Animation.getPathAnimations(pathCoordinates, animations.getLast(), this.mazeX));
    }
}
