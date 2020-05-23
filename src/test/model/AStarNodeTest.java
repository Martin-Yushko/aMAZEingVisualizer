package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AStarNodeTest {
    AStarNode node;

    @BeforeEach
    void beforeEach() {
        node = new AStarNode(null);
    }

    @Test
    void testEqualsWithNonAStarNode() {
        Tile someTile = new Tile(1);
        assertFalse(node.equals(someTile));
    }
}
