package test.java.com.cdal;

import main.java.com.cdal.Coords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoordsTest {

    private Coords c1;
    private Coords c2;

    @BeforeEach
    public void setUp() {
        c1 = new Coords(2, 3);
        c2 = new Coords(3, 4);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(2, c1.getL());
        assertEquals(3, c1.getC());
    }

    @Test
    public void testPlusMethodWithTwoCoords() {
        Coords result = c1.plus(c1, c2);
        assertEquals(5, result.getL());
        assertEquals(7, result.getC());
    }

    @Test
    public void testMoinsMethodWithTwoCoords() {
        Coords c3 = new Coords(5, 7);
        Coords result = c1.moins(c3, c2);
        assertEquals(2, result.getL());
        assertEquals(3, result.getC());
    }

    @Test
    public void testPlusMethodWithCoordsAndInts() {
        Coords result = c1.plus(c1, 3, 4);
        assertEquals(5, result.getL());
        assertEquals(7, result.getC());
    }

    @Test
    public void testMoinsMethodWithCoordsAndInts() {
        Coords c3 = new Coords(5, 7);
        Coords result = c1.moins(c3, 2, 3);
        assertEquals(3, result.getL());
        assertEquals(4, result.getC());
    }

    @Test
    public void testToString() {
        Coords coords = new Coords(6, 8);
        assertEquals("6;8", coords.toString());
    }
}
