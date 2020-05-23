package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pair;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PathfinderManagerTest {
    PathfinderManager manager;

    @BeforeEach
    void beforeEach() {
        manager = new PathfinderManager(new Maze());
    }

    @Test
    void testRunAStar() {
        PathInformation output = manager.runAStar();

        assertTrue(output.possible);
        assertEquals(11, output.numberNodesVisited);
        assertEquals(22, output.animations.size());
    }

    @Test
    void testRunAStarImpossible() {
        ArrayList<Pair<Integer, Integer>> obstacleCoords = new ArrayList<>();
        obstacleCoords.add(new Pair(4,5));
        obstacleCoords.add(new Pair(4,4));
        obstacleCoords.add(new Pair(5,4));

        for (Pair<Integer, Integer> coord : obstacleCoords) {
            manager.source.addObstacle(coord.first, coord.second);
        }

        PathInformation output = manager.runAStar();

        assertFalse(output.possible);
        assertEquals(32, output.numberNodesVisited);
        assertEquals(65, output.animations.size());
    }
}
