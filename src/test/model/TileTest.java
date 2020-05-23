package model;

import org.junit.jupiter.api.*;
import util.Pair;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static model.Tile.TileType.*;
import static model.Tile.AnimationState.*;
import static model.Tile.Direction.*;

public class TileTest {
    Tile tile;

    @BeforeEach
    void beforeEach() {
        tile = new Tile(1);
    }

    @Test
    void testNoParameterConstructor() {
        assertNull(tile.rightNeighbour);
        assertNull(tile.bottomNeighbour);
        assertNull(tile.leftNeighbour);
        assertNull(tile.topNeighbour);
        assertEquals(1, tile.weight);
        assertEquals(EMPTY, tile.type);
        assertEquals(DEFAULT, tile.animationState);
    }

    @Test
    void testTileCopyConstructor() {
        tile.weight = 2;
        tile.setType(START);
        tile.setState(VISITED);
        tile.rightNeighbour = new Tile(1);

        Tile newTile = new Tile(tile);

        assertEquals(tile.weight, newTile.weight);
        assertEquals(tile.type, newTile.type);
        assertEquals(tile.animationState, newTile.animationState);
        assertEquals(tile.rightNeighbour, newTile.rightNeighbour);
        assertEquals(tile.leftNeighbour, newTile.leftNeighbour);
        assertEquals(tile.topNeighbour, newTile.topNeighbour);
        assertEquals(tile.bottomNeighbour, newTile.bottomNeighbour);
    }

    @Test
    void testSetType() {
        tile.setType(TARGET);
        assertEquals(TARGET, tile.type);

        tile.setType(TARGET);
        assertEquals(TARGET, tile.type);

        tile.setType(START);
        assertEquals(START, tile.type);

        tile.setType(OBSTACLE);
        assertEquals(OBSTACLE, tile.type);
    }

    @Test
    void testSetState() {
        tile.setState(VISITED);
        assertEquals(VISITED, tile.animationState);

        tile.setState(VISITED);
        assertEquals(VISITED, tile.animationState);

        tile.setState(PATH);
        assertEquals(PATH, tile.animationState);

        tile.setState(DEFAULT);
        assertEquals(DEFAULT, tile.animationState);
    }

    @Test
    void testGetNeighbours() {
        assertEquals(0, tile.getNeighbours().size());

        Tile rightNeighbour = new Tile(2);
        rightNeighbour.weight = 2;
        Tile bottomNeighbour = new Tile(3);
        bottomNeighbour.weight = 3;
        Tile leftNeighbour = new Tile(4);
        leftNeighbour.weight = 4;
        Tile topNeighbour = new Tile(5);
        topNeighbour.weight = 5;

        tile.rightNeighbour = rightNeighbour;
        tile.bottomNeighbour = bottomNeighbour;
        tile.leftNeighbour = leftNeighbour;
        tile.topNeighbour = topNeighbour;

        ArrayList<Pair<Tile, Tile.Direction>> neighbours = tile.getNeighbours();
        assertEquals(4, neighbours.size());
        assertEquals(2, neighbours.get(0).first.weight);
        assertEquals(RIGHT, neighbours.get(0).second);
        assertEquals(3, neighbours.get(1).first.weight);
        assertEquals(BOTTOM, neighbours.get(1).second);
        assertEquals(4, neighbours.get(2).first.weight);
        assertEquals(LEFT, neighbours.get(2).second);
        assertEquals(5, neighbours.get(3).first.weight);
        assertEquals(TOP, neighbours.get(3).second);
    }

    @Test
    void testTileEqual() {
        Tile similarTile = new Tile(1);
        Tile differentTile = new Tile(31415926);
        assertTrue(tile.equals(similarTile));
        assertFalse(tile.equals(differentTile));
    }
    
    @Test
    void testEqualsWithDifferentObject() {
        Pair<Integer, Integer> pair = new Pair(1,2);
        assertFalse(tile.equals(pair));
    }


}
