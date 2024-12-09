package main.java.com.cdal;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Classe qui gère les événements clavier
 */
public class ControleurClavier implements EventHandler<KeyEvent>{
    private AppliJeu vueJeu;
    private ModeleJeu modeleJeu;
    public ControleurClavier(AppliJeu vueJeu, ModeleJeu modeleJeu){
        this.vueJeu = vueJeu;
        this.modeleJeu = modeleJeu;
    }

    /**
     * Méthode qui gère les événements clavier
     * @param e l'événement clavier
     */
    @Override
    public void handle(KeyEvent e) {
        if(this.modeleJeu.estGagnee() || AppliJeu.WAITING) return;
        KeyCode k = e.getCode();
        if(k == KeyCode.LEFT){
            this.modeleJeu.selectionGauche();
            this.vueJeu.maj(Change.LEFT);
        } else if(k == KeyCode.RIGHT){
            this.modeleJeu.selectionDroite();
            this.vueJeu.maj(Change.RIGHT);
        } else if(k == KeyCode.ENTER || k == KeyCode.SPACE){
            this.modeleJeu.drop();
            this.vueJeu.maj(Change.DROP);
        }
    }
}
