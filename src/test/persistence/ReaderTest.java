package persistence;

import model.Maze;
import model.Tile.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static model.Tile.TileType.*;
import static model.Tile.TileType.EMPTY;
import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    Reader reader;
    private int X_DIM = Maze.DEFAULT_X_DIMENSION;
    private int Y_DIM = Maze.DEFAULT_Y_DIMENSION;

    @BeforeEach
    void beforeEach() {
        reader = new Reader();
    }

    @Test
    void testReadMaze() {
        File testFile1 = new File("./data/mazeTest1.txt");
        // EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,
        // OBSTACLE,START,EMPTY,EMPTY,OBSTACLE,EMPTY,
        // EMPTY,EMPTY,EMPTY,EMPTY,OBSTACLE,EMPTY,
        // EMPTY,EMPTY,EMPTY,OBSTACLE,EMPTY,EMPTY,
        // EMPTY,EMPTY,OBSTACLE,EMPTY,EMPTY,TARGET,
        // EMPTY,OBSTACLE,EMPTY,EMPTY,EMPTY,EMPTY
        try {
            Maze maze1 = reader.readMaze(testFile1);
            ArrayList<TileType> tilesTypes = maze1.toTileTypesArrayList();
            for (int x = 0; x < X_DIM; x++) {
                for (int y = 0; y < Y_DIM; y++) {
                    int index = x + y * X_DIM;
                    if (x == 1 && y == 1) {
                        assertEquals(START, tilesTypes.get(index));
                    } else if (x == 5 && y == 4) {
                        assertEquals(TARGET, tilesTypes.get(index));
                    } else if (index == 6 || index == 10 || index == 16 || index == 21
                            || index == 26 || index == 31) {
                        assertEquals(OBSTACLE, tilesTypes.get(index));
                    } else {
                        assertEquals(EMPTY, tilesTypes.get(index));
                    }
                }
            }
        } catch (IOException e) {
            fail();
        }
    }
}
