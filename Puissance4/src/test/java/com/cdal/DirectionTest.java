package test.java.com.cdal;

import main.java.com.cdal.Coords;
import main.java.com.cdal.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testGetOffsetL() {
        assertEquals(-1, GrilleBD.HAUT_GAUCHE.getOffsetL());
        assertEquals(-1, GrilleBD.HAUT.getOffsetL());
        assertEquals(-1, GrilleBD.HAUT_DROITE.getOffsetL());
        assertEquals(0, GrilleBD.GAUCHE.getOffsetL());
        assertEquals(0, GrilleBD.DROITE.getOffsetL());
        assertEquals(1, GrilleBD.BAS_GAUCHE.getOffsetL());
        assertEquals(1, GrilleBD.BAS.getOffsetL());
        assertEquals(1, GrilleBD.BAS_DROITE.getOffsetL());
    }

    @Test
    public void testGetOffsetC() {
        assertEquals(-1, GrilleBD.HAUT_GAUCHE.getOffsetC());
        assertEquals(0, GrilleBD.HAUT.getOffsetC());
        assertEquals(1, GrilleBD.HAUT_DROITE.getOffsetC());
        assertEquals(-1, GrilleBD.GAUCHE.getOffsetC());
        assertEquals(1, GrilleBD.DROITE.getOffsetC());
        assertEquals(-1, GrilleBD.BAS_GAUCHE.getOffsetC());
        assertEquals(0, GrilleBD.BAS.getOffsetC());
        assertEquals(1, GrilleBD.BAS_DROITE.getOffsetC());
    }

    @Test
    public void testGetVoisin() {
        Coords coord = GrilleBD.HAUT_GAUCHE.getVoisin(2, 2);
        assertEquals(1, coord.getL());
        assertEquals(1, coord.getC());

        coord = GrilleBD.BAS_DROITE.getVoisin(2, 2);
        assertEquals(3, coord.getL());
        assertEquals(3, coord.getC());
    }

    @Test
    public void testVoisinsWithCoordinates() {
        Coords[] voisins = GrilleBD.voisins(2, 2);
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
        Coords[] voisins = GrilleBD.voisins();
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
        assertEquals("HG", GrilleBD.HAUT_GAUCHE.toString());
        assertEquals("H", GrilleBD.HAUT.toString());
        assertEquals("HD", GrilleBD.HAUT_DROITE.toString());
        assertEquals("G", GrilleBD.GAUCHE.toString());
        assertEquals("D", GrilleBD.DROITE.toString());
        assertEquals("BG", GrilleBD.BAS_GAUCHE.toString());
        assertEquals("B", GrilleBD.BAS.toString());
        assertEquals("BD", GrilleBD.BAS_DROITE.toString());
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
