package reseau;

import Puissance4Terminal.Equipe;
import Puissance4Terminal.ModeleJeu;

public class Partie {
    private final ClientHandler joueur1;
    private final ClientHandler joueur2;
    private final ModeleJeu puissance4;
    private boolean partieEnCours;

    public Partie(ClientHandler joueur1, ClientHandler joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.puissance4 = new ModeleJeu(Equipe.JAUNE);
        this.partieEnCours = true;

        // Envoyer les messages initiaux
        demarrerPartie();
    }

    private void demarrerPartie() {
        // Informer les joueurs de leurs couleurs
        joueur1.envoyerMessage("La partie commence ! Vous jouez avec les pions JAUNES.");
        joueur2.envoyerMessage("La partie commence ! Vous jouez avec les pions ROUGES.");

        // Envoyer la grille initiale
        envoyerGrille();

        // Configurer le premier tour
        joueur1.envoyerMessage("play");
        joueur2.envoyerMessage("wait");
    }

    public void jouerColonne(ClientHandler joueur, int numColonne) {
        if (!partieEnCours) return;

        boolean estJoueur1 = joueur.equals(joueur1);
        
        // Vérifier si c'est le bon tour
        if ((puissance4.getJoueur() == Equipe.JAUNE && !estJoueur1) ||
            (puissance4.getJoueur() == Equipe.ROUGE && estJoueur1)) {
            joueur.envoyerMessage("Ce n'est pas votre tour");
            return;
        }

        // Vérifier si le coup est valide
        if (!puissance4.getGrille().poserPion(numColonne-1, puissance4.getJoueur())) {
            joueur.envoyerMessage("Colonne pleine ou invalide, choisissez une autre.");
            return;
        }

        // Envoyer la nouvelle grille aux deux joueurs
        envoyerGrille();

        // Vérifier les conditions de fin de partie
        if (puissance4.estGagnee()) {
            joueur.envoyerMessage("win");
            (estJoueur1 ? joueur2 : joueur1).envoyerMessage("lose");
            partieEnCours = false;
            return;
        }

        if (puissance4.estPerdu()) {
            joueur1.envoyerMessage("nul");
            joueur2.envoyerMessage("nul");
            partieEnCours = false;
            return;
        }

        // Changer de joueur et envoyer les messages appropriés
        puissance4.changerJoueur();
        if (puissance4.getJoueur() == Equipe.JAUNE) {
            joueur1.envoyerMessage("play");
            joueur2.envoyerMessage("wait");
        } else {
            joueur1.envoyerMessage("wait");
            joueur2.envoyerMessage("play");
        }
    }

    public void envoyerGrille() {
        String grille = puissance4.getGrille().getStringGrille();
        String message = "Grille :\n" + grille;
        joueur1.envoyerMessage(message);
        joueur2.envoyerMessage(message);
    }

    public boolean estTerminee() {
        return !partieEnCours;
    }

    public ClientHandler getJoueur1() {
        return joueur1;
    }

    public ClientHandler getJoueur2() {
        return joueur2;
    }
}