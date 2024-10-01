package student;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

// You must only call the constructors that are declared in AbstractGrid.
// You must not call methods that are declared in your Grid implementation.
public class AbstractGridTest {
    private static final long SEED = 1234L;

    // You must not change this constructor.
    // Use only this to create test fixtures.
    public AbstractGrid makeGrid(BufferedImage bufferedImage) {
        return new Grid(bufferedImage, SEED, true);
    }
}