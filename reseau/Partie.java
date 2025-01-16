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
        String grille = puissance4.getGrille().getStringGrille();
        joueur1.envoyerMessage("Grille :\n" + grille);
        joueur2.envoyerMessage("Grille :\n" + grille);
    }

    public void jouerColonne(ClientHandler joueur, int numColonne) {
        if (!puissance4.getGrille().poserPion(numColonne, puissance4.getJoueur())) {
            joueur.envoyerMessage("Colonne pleine, choisissez une autre.");
            return;
        }

        envoyerGrille();

        if (puissance4.estGagnee()) {
            joueur.envoyerMessage("win");
            (joueur == joueur1 ? joueur2 : joueur1).envoyerMessage("lose");
            return;
        }

        if (puissance4.estPerdu()) {
            joueur1.envoyerMessage("nul");
            joueur2.envoyerMessage("nul");
            return;
        }

        Equipe equipeSuivante = puissance4.getJoueur();
        if (equipeSuivante == Equipe.JAUNE) {
            joueur1.envoyerMessage("play"); // Le joueur 1 joue
            joueur2.envoyerMessage("wait"); // Le joueur 2 attend
        } else {
            joueur1.envoyerMessage("wait"); // Le joueur 1 attend
            joueur2.envoyerMessage("play"); // Le joueur 2 joue
        }
    }
}
