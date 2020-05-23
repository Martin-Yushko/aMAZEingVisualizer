package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PauseAnimationTest {
    Animation animation;

    @BeforeEach
    void beforeEach() {
        animation = new Animation(100);
    }

    @Test
    void testGetMillis() {
        assertEquals(100, animation.getDuration());
    }
}
