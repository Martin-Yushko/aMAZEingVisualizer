package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pair;
import util.Triplet;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class AStarFinderTest {
    private AStarFinder finder;

    @BeforeEach
    void beforeEach() {
        finder = new AStarFinder();
    }

    @Test
    void testPathFindingEmptyMaze() {
        Maze myMaze = new Maze();

        PathInformation pathInformation = finder.find(myMaze);

        assertTrue(pathInformation.possible);
        assertEquals(11, pathInformation.numberNodesVisited);
        assertEquals(22, pathInformation.animations.size());
    }

    @Test
    void testPathFindingMazeWithObstacles() {
        Maze myMaze = new Maze();

        myMaze.changeStart(1, 4);
        ArrayList<Pair<Integer, Integer>> obstacleCoords = new ArrayList<>();
        obstacleCoords.add(new Pair(2,0));
        obstacleCoords.add(new Pair(2,1));
        obstacleCoords.add(new Pair(2,2));
        obstacleCoords.add(new Pair(2,3));;
        obstacleCoords.add(new Pair(4,4));
        obstacleCoords.add(new Pair(4,5));

        for (Pair<Integer, Integer> coord : obstacleCoords) {
            myMaze.addObstacle(coord.first, coord.second);
        }

        PathInformation pathInformation = finder.find(myMaze);

        assertTrue(pathInformation.possible);
        assertEquals(11, pathInformation.numberNodesVisited);
        assertEquals(19, pathInformation.animations.size());
    }

    @Test
    void testPathFindingImpossibleMaze() {
        Maze myMaze = new Maze();

        ArrayList<Pair<Integer, Integer>> obstacleCoords = new ArrayList<>();
        obstacleCoords.add(new Pair(4,5));
        obstacleCoords.add(new Pair(4,4));
        obstacleCoords.add(new Pair(5,4));

        for (Pair<Integer, Integer> coord : obstacleCoords) {
            myMaze.addObstacle(coord.first, coord.second);
        }

        PathInformation pathInformation = finder.find(myMaze);

        assertFalse(pathInformation.possible);
        assertEquals(32, pathInformation.numberNodesVisited);
        assertEquals(32, pathInformation.animations.size());
    }

    @Test
    void testEqualsWithNonAStarNode() {
        ArrayList<AStarNode> source = new ArrayList<>();
        assertNull(finder.findNode(new AStarNode(null), source));
    }
}
