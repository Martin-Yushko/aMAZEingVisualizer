package persistence;

import javafx.scene.Scene;
import model.Maze;
import model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Main;
import ui.SceneAssembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static model.Tile.TileType.*;
import static model.Tile.TileType.EMPTY;
import static org.junit.jupiter.api.Assertions.*;

//outline of tests inspired from Teller App
public class WriterTest {
    private int X_DIM = Maze.DEFAULT_X_DIMENSION;
    private int Y_DIM = Maze.DEFAULT_Y_DIMENSION;
    private static final String TEST_FILE = "./data/testMaze.txt";
    private Writer testWriter;
    private Maze maze;

    @BeforeEach
    void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter = new Writer(new File(TEST_FILE));
        maze = SceneAssembler.generateMaze();
    }

    @Test
    void testWriteAccounts() {
        // save maze to file
        testWriter.write(maze);
        testWriter.close();

        // now read them back in and verify that the accounts have the expected values
        try {
            Maze maze = Reader.readMaze(new File(TEST_FILE));
            assertEquals(maze.dimensionX, X_DIM);
            assertEquals(maze.dimensionY, Y_DIM);

            ArrayList<Tile.TileType> tilesTypes = maze.toTileTypesArrayList();
            for (int x = 0; x < X_DIM; x++) {
                for (int y = 0; y < Y_DIM; y++) {
                    int index = x + y * X_DIM;
                    if (x == 1 && y == 1) {
                        assertEquals(START, tilesTypes.get(index));
                    } else if (x == 5 && y == 5) {
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
            fail("IOException should not have been thrown");
        }
    }
}
