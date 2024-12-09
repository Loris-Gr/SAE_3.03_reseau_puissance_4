package test.java.com.cdal;

import main.java.com.cdal.Equipe;
import main.java.com.cdal.Pion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;

public class PionTest {

    @Test
    public void testPionDefaultConstructor() {
        Pion pion = new Pion();
        assertEquals(Equipe.AUCUNE, pion.getEquipe());
        assertEquals(Pion.SIZE, pion.getRadius());
        assertEquals(Color.TRANSPARENT, pion.getFill());
        assertTrue(pion.estVide());
    }

    @Test
    public void testPionConstructorWithEquipe() {
        Pion pion = new Pion(Equipe.ROUGE);
        assertEquals(Equipe.ROUGE, pion.getEquipe());
        assertEquals(Pion.SIZE, pion.getRadius());
        assertEquals(Equipe.ROUGE.getColor(), pion.getFill());
        assertFalse(pion.estVide());
    }

    @Test
    public void testSetEquipe() {
        Pion pion = new Pion();
        pion.setEquipe(Equipe.BLEU);
        assertEquals(Equipe.BLEU, pion.getEquipe());
        assertEquals(Equipe.BLEU.getColor(), pion.getFill());
    }

    @Test
    public void testReset() {
        Pion pion = new Pion(Equipe.ROUGE);
        pion.reset();
        assertEquals(Equipe.AUCUNE, pion.getEquipe());
        assertEquals(Color.TRANSPARENT, pion.getFill());
        assertTrue(pion.estVide());
    }

    @Test
    public void testEstVide() {
        Pion pion = new Pion();
        assertTrue(pion.estVide());
        pion.setEquipe(Equipe.ROUGE);
        assertFalse(pion.estVide());
    }
}
