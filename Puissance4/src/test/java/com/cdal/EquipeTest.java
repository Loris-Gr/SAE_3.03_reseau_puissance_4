package test.java.com.cdal;

import main.java.com.cdal.Equipe;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipeTest {

    @Test
    public void testGetId() {
        assertEquals(0, Equipe.AUCUNE.getId());
        assertEquals(1, Equipe.JAUNE.getId());
        assertEquals(2, Equipe.ROUGE.getId());
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.WHITE, Equipe.AUCUNE.getColor());
        assertEquals(Color.GOLD, Equipe.JAUNE.getColor());
        assertEquals(Color.RED, Equipe.ROUGE.getColor());
    }

    @Test
    public void testGetSymbole() {
        assertEquals("V", Equipe.AUCUNE.getSymbole());
        assertEquals("J", Equipe.JAUNE.getSymbole());
        assertEquals("R", Equipe.ROUGE.getSymbole());
    }

    @Test
    public void testGetNom() {
        assertEquals(" ", Equipe.AUCUNE.getNom());
        assertEquals("jaunes", Equipe.JAUNE.getNom());
        assertEquals("rouges", Equipe.ROUGE.getNom());
    }

    @Test
    public void testValueOfId() {
        assertEquals(Equipe.AUCUNE, Equipe.AUCUNE.valueOf(0));
        assertEquals(Equipe.JAUNE, Equipe.JAUNE.valueOf(1));
        assertEquals(Equipe.ROUGE, Equipe.ROUGE.valueOf(2));
        assertEquals(Equipe.AUCUNE, Equipe.AUCUNE.valueOf(999)); // Test avec un id invalide
    }

    @Test
    public void testValueOfColor() {
        assertEquals(Equipe.AUCUNE, Equipe.AUCUNE.valueOf(Color.WHITE));
        assertEquals(Equipe.JAUNE, Equipe.JAUNE.valueOf(Color.GOLD));
        assertEquals(Equipe.ROUGE, Equipe.ROUGE.valueOf(Color.RED));
        assertEquals(Equipe.AUCUNE, Equipe.AUCUNE.valueOf(Color.BLACK)); // Test avec une couleur invalide
    }

    @Test
    public void testToString() {
        assertEquals("V", Equipe.AUCUNE.toString());
        assertEquals("J", Equipe.JAUNE.toString());
        assertEquals("R", Equipe.ROUGE.toString());
    }
}
