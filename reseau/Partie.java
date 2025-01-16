package reseau;

import Puissance4Terminal.Equipe;
import Puissance4Terminal.ModeleJeu;

public class Partie {
    private ClientHandler joueur1;
    private ClientHandler joueur2;
    private ModeleJeu puissance4;

    public Partie(ClientHandler joueur1, ClientHandler joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.puissance4 = new ModeleJeu(Equipe.JAUNE);
    }

    public ClientHandler getJoueur1() {
        return joueur1;
    }

    public ClientHandler getJoueur2() {
        return joueur2;
    }

    public void envoyerGrille() {
        String grille = this.puissance4.getGrille().getStringGrille();
        this.joueur1.envoyerMessage(grille);
        this.joueur2.envoyerMessage(grille);
    }

    public void jouerColonne(ClientHandler joueur, int numColonne) {
        // Tentative de déposer le pion
        if (!this.puissance4.getGrille().poserPion(numColonne, this.puissance4.getJoueur())) {
            joueur.envoyerMessage("Colonne pleine, choisissez une autre.");
        }
        envoyerGrille();

        // Vérification de victoire
        if (this.puissance4.estGagnee()) {
            if (joueur == joueur1) {
                this.joueur2.envoyerMessage("lose");
            }
            else { // si joueur 2 a gagné
                this.joueur1.envoyerMessage("lose");
            }
            joueur.envoyerMessage("win");
            
        }

        // Vérification de match nul
        if (this.puissance4.estPerdu()) {
            this.joueur1.envoyerMessage("nul");
            this.joueur2.envoyerMessage("nul");
        }

        // Changement de joueur
        this.puissance4.changerJoueur();
    }
    


    
}
