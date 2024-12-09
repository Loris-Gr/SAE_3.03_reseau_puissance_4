package test.java.com.cdal;

import main.java.com.cdal.Coords;
import main.java.com.cdal.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testGetOffsetL() {
        assertEquals(-1, Direction.HAUT_GAUCHE.getOffsetL());
        assertEquals(-1, Direction.HAUT.getOffsetL());
        assertEquals(-1, Direction.HAUT_DROITE.getOffsetL());
        assertEquals(0, Direction.GAUCHE.getOffsetL());
        assertEquals(0, Direction.DROITE.getOffsetL());
        assertEquals(1, Direction.BAS_GAUCHE.getOffsetL());
        assertEquals(1, Direction.BAS.getOffsetL());
        assertEquals(1, Direction.BAS_DROITE.getOffsetL());
    }

    @Test
    public void testGetOffsetC() {
        assertEquals(-1, Direction.HAUT_GAUCHE.getOffsetC());
        assertEquals(0, Direction.HAUT.getOffsetC());
        assertEquals(1, Direction.HAUT_DROITE.getOffsetC());
        assertEquals(-1, Direction.GAUCHE.getOffsetC());
        assertEquals(1, Direction.DROITE.getOffsetC());
        assertEquals(-1, Direction.BAS_GAUCHE.getOffsetC());
        assertEquals(0, Direction.BAS.getOffsetC());
        assertEquals(1, Direction.BAS_DROITE.getOffsetC());
    }

    @Test
    public void testGetVoisin() {
        Coords coord = Direction.HAUT_GAUCHE.getVoisin(2, 2);
        assertEquals(1, coord.getL());
        assertEquals(1, coord.getC());

        coord = Direction.BAS_DROITE.getVoisin(2, 2);
        assertEquals(3, coord.getL());
        assertEquals(3, coord.getC());
    }

    @Test
    public void testVoisinsWithCoordinates() {
        Coords[] voisins = Direction.voisins(2, 2);
        assertEquals(8, voisins.length);
        assertTrue(containsCoord(voisins, new Coords(1, 1)));
        assertTrue(containsCoord(voisins, new Coords(1, 2)));
        assertTrue(containsCoord(voisins, new Coords(1, 3)));
        assertTrue(containsCoord(voisins, new Coords(2, 1)));
        assertTrue(containsCoord(voisins, new Coords(2, 3)));
        assertTrue(containsCoord(voisins, new Coords(3, 1)));
        assertTrue(containsCoord(voisins, new Coords(3, 2)));
        assertTrue(containsCoord(voisins, new Coords(3, 3)));
    }

    @Test
    public void testVoisins() {
        Coords[] voisins = Direction.voisins();
        assertEquals(8, voisins.length);
        assertTrue(containsCoord(voisins, new Coords(-1, -1)));
        assertTrue(containsCoord(voisins, new Coords(-1, 0)));
        assertTrue(containsCoord(voisins, new Coords(-1, 1)));
        assertTrue(containsCoord(voisins, new Coords(0, -1)));
        assertTrue(containsCoord(voisins, new Coords(0, 1)));
        assertTrue(containsCoord(voisins, new Coords(1, -1)));
        assertTrue(containsCoord(voisins, new Coords(1, 0)));
        assertTrue(containsCoord(voisins, new Coords(1, 1)));
    }

    @Test
    public void testToString() {
        assertEquals("HG", Direction.HAUT_GAUCHE.toString());
        assertEquals("H", Direction.HAUT.toString());
        assertEquals("HD", Direction.HAUT_DROITE.toString());
        assertEquals("G", Direction.GAUCHE.toString());
        assertEquals("D", Direction.DROITE.toString());
        assertEquals("BG", Direction.BAS_GAUCHE.toString());
        assertEquals("B", Direction.BAS.toString());
        assertEquals("BD", Direction.BAS_DROITE.toString());
    }

    private boolean containsCoord(Coords[] coordsArray, Coords coord) {
        for (Coords c : coordsArray) {
            if (c.equals(coord)) {
                return true;
            }
        }
        return false;
    }
}
