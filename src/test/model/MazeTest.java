package model;

import org.junit.jupiter.api.*;
import util.Pair;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static model.Tile.TileType.*;
import static model.Tile.AnimationState.*;


class MazeTest {
    Maze maze;
    int X_DIM = Maze.DEFAULT_X_DIMENSION;
    int Y_DIM = Maze.DEFAULT_Y_DIMENSION;

    @BeforeEach
    void beforeEach() {
        maze = new Maze();
    }

    @Test
    void testConstructorEmptyGrid() {
        maze = new Maze();

        ArrayList<Tile> tiles = maze.toTilesArrayList();

        for (int x = 0; x < X_DIM; x++) {
            for (int y = 0; y < Y_DIM; y++) {
                int index = x + y * X_DIM;
                if (y == 0) {
                    assertNull(tiles.get(index).topNeighbour);
                } else {
                    assertNotNull(tiles.get(index).topNeighbour);
                }
                if (y == Y_DIM - 1) {
                    assertNull(tiles.get(index).bottomNeighbour);
                } else {
                    assertNotNull(tiles.get(index).bottomNeighbour);
                }
                if (x == 0) {
                    assertNull(tiles.get(index).leftNeighbour);
                } else {
                    assertNotNull(tiles.get(index).leftNeighbour);
                }
                if (x == X_DIM - 1) {
                    assertNull(tiles.get(index).rightNeighbour);
                } else {
                    assertNotNull(tiles.get(index).rightNeighbour);
                }
            }
        }
    }

    @Test
    void testToTileTypesArrayList() {
        ArrayList<Tile.TileType> tileTypes = maze.toTileTypesArrayList();

        assertEquals(X_DIM * Y_DIM, tileTypes.size());

        int numStarts = 0;
        int numTargets = 0;
        int numEmpty = 0;

        for (int i = 0; i < X_DIM * Y_DIM; i++) {
            if (tileTypes.get(i) == START) {
                numStarts += 1;
                assertEquals(maze.getStartX() + maze.getStartY()*X_DIM, i);
            } else if (tileTypes.get(i) == TARGET) {
                numTargets += 1;
                assertEquals(maze.getTargetX() + maze.getTargetY() * X_DIM, i);
            } else {
                numEmpty += 1;
            }
        }

        assertEquals(1, numStarts);
        assertEquals(1, numTargets);
        assertEquals(X_DIM * Y_DIM - 2, numEmpty);
    }

    @Test
    void testToAnimationStatesArrayList() {
        ArrayList<Tile.AnimationState> states = maze.toAnimationStatesArrayList();

        assertEquals(X_DIM * Y_DIM, states.size());

        for (int i = 0; i < X_DIM * Y_DIM; i++) {
            assertEquals(DEFAULT, states.get(i));
        }
    }

    @Test
    void testAddObstacles() {
        ArrayList<Pair<Integer, Integer>> obstacleCoords = new ArrayList<>();
        obstacleCoords.add(new Pair(2,0));
        obstacleCoords.add(new Pair(2,1));
        obstacleCoords.add(new Pair(2,2));
        obstacleCoords.add(new Pair(2,3));;
        obstacleCoords.add(new Pair(4,4));
        //should not overwrite start and end tiles
        obstacleCoords.add(new Pair(maze.getStartX(), maze.getStartY()));
        obstacleCoords.add(new Pair(maze.getTargetX(), maze.getTargetY()));
        //should not add obstaclesd at x,y coordinates which are too big or too small
        obstacleCoords.add(new Pair(-1,-1));
        obstacleCoords.add(new Pair(-1,0));
        obstacleCoords.add(new Pair(0,-1));
        obstacleCoords.add(new Pair(X_DIM * Y_DIM + 1,X_DIM * Y_DIM + 1));
        obstacleCoords.add(new Pair(X_DIM * Y_DIM + 1,0));
        obstacleCoords.add(new Pair(0,X_DIM * Y_DIM + 1));

        for (Pair<Integer, Integer> coord : obstacleCoords) {
            maze.addObstacle(coord.first, coord.second);
        }

        ArrayList<Tile> tiles = maze.toTilesArrayList();

        for (int x = 0; x < X_DIM; x++) {
            for (int y = 0; y < Y_DIM; y++) {
                int index = x + y * X_DIM;
                if (y == maze.getStartY() && x == maze.getStartX()) {
                    assertEquals(START, tiles.get(index).type);
                } else if (y == maze.getTargetY() && x == maze.getTargetX()) {
                    assertEquals(TARGET, tiles.get(index).type);
                } else if (obstacleCoords.contains(new Pair(x, y))) {
                    assertEquals(OBSTACLE, tiles.get(index).type);
                } else {
                    assertEquals(EMPTY, tiles.get(index).type);
                }
            }
        }
    }

    @Test
    void testSetStartTileInvalid() {
        maze.changeStart(X_DIM + 1, Y_DIM + 1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles1 = maze.toTilesArrayList();
        for (int i = 0; i < tiles1.size(); i++) {
            if (i == 0) {
                assertEquals(START, tiles1.get(i).type);
            } else {
                assertNotSame(tiles1.get(i).type, START);
            }
        }

        maze.changeStart(-1, -1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles2 = maze.toTilesArrayList();
        for (int i = 0; i < tiles2.size(); i++) {
            if (i == 0) {
                assertEquals(START, tiles2.get(i).type);
            } else {
                assertNotSame(tiles2.get(i).type, START);
            }
        }

        maze.changeStart(-1, Y_DIM + 1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles3 = maze.toTilesArrayList();
        for (int i = 0; i < tiles3.size(); i++) {
            if (i == 0) {
                assertEquals(START, tiles3.get(i).type);
            } else {
                assertNotSame(tiles3.get(i).type, START);
            }
        }

        maze.changeStart(maze.getTargetX(), maze.getTargetY());
        maze.changeStart(X_DIM + 1, -1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles4 = maze.toTilesArrayList();
        for (int i = 0; i < tiles4.size(); i++) {
            if (i == 0) {
                assertEquals(START, tiles4.get(i).type);
            } else {
                assertNotSame(tiles4.get(i).type, START);
            }
        }
    }

    @Test
    void testSetStartTile() {
        maze.changeStart(maze.getTargetX(), maze.getTargetY());
        //shouldn't have moved since destination is target tile; check is still top left corner
        ArrayList<Tile> tiles1 = maze.toTilesArrayList();
        for (int i = 0; i < tiles1.size(); i++) {
            if (i == 0) {
                assertEquals(START, tiles1.get(i).type);
            } else {
                assertNotSame(tiles1.get(i).type, START);
            }
        }

        maze.changeStart(3, 2);
        //should move normally; this tests checks right + bottom movement
        ArrayList<Tile> tiles2 = maze.toTilesArrayList();
        for (int i = 0; i < tiles2.size(); i++) {
            if (i == 3 + 2 * X_DIM) {
                assertEquals(START, tiles2.get(i).type);
            } else {
                assertNotSame(tiles2.get(i).type, START);
            }
        }

        maze.changeStart(1, 0);

        //should move normally; this tests checks left + up movement
        ArrayList<Tile> tiles3 = maze.toTilesArrayList();
        for (int i = 0; i < tiles3.size(); i++) {
            if (i == 1 + 0 * X_DIM) {
                assertEquals(START, tiles3.get(i).type);
            } else {
                assertNotSame(tiles3.get(i).type, START);
            }
        }
    }

    @Test
    void testSetTargetTileInvalid() {
        maze.changeTarget(X_DIM + 1, Y_DIM + 1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles1 = maze.toTilesArrayList();
        for (int i = 0; i < tiles1.size(); i++) {
            if (i == X_DIM * Y_DIM - 1) {
                assertEquals(TARGET, tiles1.get(i).type);
            } else {
                assertNotSame(tiles1.get(i).type, TARGET);
            }
        }

        maze.changeTarget(-1, -1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles2 = maze.toTilesArrayList();
        for (int i = 0; i < tiles2.size(); i++) {
            if (i == X_DIM * Y_DIM - 1) {
                assertEquals(TARGET, tiles2.get(i).type);
            } else {
                assertNotSame(tiles2.get(i).type, TARGET);
            }
        }

        maze.changeTarget(X_DIM + 1, -1);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles3 = maze.toTilesArrayList();
        for (int i = 0; i < tiles3.size(); i++) {
            if (i == X_DIM * Y_DIM - 1) {
                assertEquals(TARGET, tiles3.get(i).type);
            } else {
                assertNotSame(tiles3.get(i).type, TARGET);
            }
        }

        maze.changeTarget(maze.getStartX(), maze.getStartY());
        maze.changeTarget(-1, Y_DIM);
        //shouldn't have moved since coordinates are invalid
        ArrayList<Tile> tiles4 = maze.toTilesArrayList();
        for (int i = 0; i < tiles4.size(); i++) {
            if (i == X_DIM * Y_DIM - 1) {
                assertEquals(TARGET, tiles4.get(i).type);
            } else {
                assertNotSame(tiles4.get(i).type, TARGET);
            }
        }
    }

    @Test
    void testSetTargetTile() {
        maze.changeTarget(maze.getStartX(), maze.getStartY());
        //shouldn't have moved since destination is start tile; check is still bottom right corner
        ArrayList<Tile> tiles1 = maze.toTilesArrayList();
        for (int i = 0; i < tiles1.size(); i++) {
            if (i == X_DIM * Y_DIM - 1) {
                assertEquals(TARGET, tiles1.get(i).type);
            } else {
                assertNotSame(tiles1.get(i).type, TARGET);
            }
        }

        maze.changeTarget(2, 1);
        //should move normally; this tests checks left + top movement
        ArrayList<Tile> tiles2 = maze.toTilesArrayList();
        for (int i = 0; i < tiles2.size(); i++) {
            if (i == 2 + 1 * X_DIM) {
                assertEquals(TARGET, tiles2.get(i).type);
            } else {
                if (tiles2.get(i).type == TARGET) {
                    System.out.print(i);
                }
                assertNotSame(tiles2.get(i).type, TARGET);
            }
        }

        maze.changeTarget(4, 3);
        //should move normally; this tests checks right + bottom movement
        ArrayList<Tile> tiles3 = maze.toTilesArrayList();
        for (int i = 0; i < tiles3.size(); i++) {
            if (i == 4 + 3 * X_DIM) {
                assertEquals(TARGET, tiles3.get(i).type);
            } else {
                assertNotSame(tiles3.get(i).type, TARGET);
            }
        }
    }

    @Test
    void testRemoveObstacle() {
        maze.addObstacle(3, 3);
        maze.removeObstacle(3, 3);
        assertEquals(EMPTY, maze.findTile(3,3).type);
    }

    @Test
    void testRemoveObstacleInvalid() {
        //remove start and target tiles
        maze.removeObstacle(maze.getStartX(), maze.getStartY());
        maze.removeObstacle(maze.getTargetX(), maze.getTargetY());

        assertEquals(START, maze.findTile(maze.getStartX(), maze.getStartY()).type);
        assertEquals(TARGET, maze.findTile(maze.getTargetX(), maze.getTargetY()).type);
    }
}