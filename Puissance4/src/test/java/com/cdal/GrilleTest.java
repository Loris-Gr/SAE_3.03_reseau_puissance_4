package test.java.com.cdal;

import main.java.com.cdal.Equipe;
import main.java.com.cdal.Grille;
import main.java.com.cdal.ModeleJeu;
import main.java.com.cdal.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class GrilleTest extends ApplicationTest {

    private Grille grille;

    @Override
    public void start(Stage stage) {
        grille = new Grille();
        stage.setScene(new javafx.scene.Scene(grille));
        stage.show();
    }

    @Test
    public void testConstructor() {
        assertNotNull(grille);
        assertEquals(ModeleJeu.COLONNES / 2, grille.getSelectIndex());
        assertEquals(Equipe.JAUNE, grille.getJoueur());
    }

    @Test
    public void testInitPions() {
        grille.initPions();
        GridPane grid = grille.getGrid();
        assertEquals(ModeleJeu.COLONNES, grid.getColumnCount());
        assertEquals(ModeleJeu.LIGNES + 1, grid.getRowCount());
    }

    @Test
    public void testMaj() {
        ModeleJeu modele = new ModeleJeu();
        Change change = Change.DROP;
        grille.maj(modele, change);

        // Verifiez que les pions sont correctement mis à jour
        for (int l = 0; l < ModeleJeu.LIGNES; l++) {
            for (int c = 0; c < ModeleJeu.COLONNES; c++) {
                assertEquals(modele.getCase(l, c), grille.getGrille().get(l).get(c).getEquipe());
            }
        }
    }

    @Test
    public void testSetJoueur() {
        grille.setJoueur(Equipe.ROUGE);
        assertEquals(Equipe.ROUGE, grille.getJoueur());
        assertEquals(Equipe.ROUGE, grille.getSelection().get(grille.getSelectIndex()).getEquipe());
    }

    @Test
    public void testReset() {
        grille.reset();
        assertEquals(ModeleJeu.COLONNES / 2, grille.getSelectIndex());
        assertEquals(Equipe.JAUNE, grille.getJoueur());
        for (int l = 0; l < ModeleJeu.LIGNES; l++) {
            for (int c = 0; c < ModeleJeu.COLONNES; c++) {
                assertEquals(Equipe.AUCUNE, grille.getGrille().get(l).get(c).getEquipe());
            }
        }
    }
}
