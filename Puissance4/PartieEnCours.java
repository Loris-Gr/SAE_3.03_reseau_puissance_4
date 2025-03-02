/**
 * PartieEnCours représente une partie de jeu en cours entre deux joueurs.
 * Elle est exécutée dans un thread distinct pour permettre la gestion des interactions en temps réel.
 */
public class PartieEnCours extends Thread {
    
    private ModeleJeu modele;
    private Serveur serveur;
    private ClientHandler joueur1;
    private ClientHandler joueur2;
    private ClientHandler joueurActuel;
    
    /**
     * Constructeur de la classe PartieEnCours.
     *
     * @param joueur1 Le premier joueur de la partie.
     * @param joueur2 Le second joueur de la partie.
     * @param serveur Le serveur gérant la partie.
     */
    public PartieEnCours(ClientHandler joueur1, ClientHandler joueur2, Serveur serveur) {
        this.modele = new ModeleJeu(Equipe.JAUNE);
        this.serveur = serveur;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    /**
     * Récupère le premier joueur de la partie.
     *
     * @return Le premier joueur.
     */
    public ClientHandler getJoueur1() {
        return joueur1;
    }
    
    /**
     * Récupère le second joueur de la partie.
     *
     * @return Le second joueur.
     */
    public ClientHandler getJoueur2() {
        return joueur2;
    }

    /**
     * Récupère le modèle du jeu associé à cette partie.
     *
     * @return Le modèle du jeu.
     */
    public ModeleJeu getModele() {
        return modele;
    }

    /**
     * Point d'entrée du thread, gérant la logique de la partie.
     * Les joueurs jouent tour à tour jusqu'à ce qu'il y ait un gagnant ou un match nul.
     */
    @Override
    public void run() {
        System.out.println();
        System.out.println("Début de la partie " + joueur1.getPseudo() + " contre " + joueur2.getPseudo());
        System.out.println();
        while (true) {

            try {
                // Affichage de la grille et instructions pour le joueur
                String affichage = modele.getGrille().afficher2();
                joueur1.getOut().println(affichage); joueur2.getOut().println(affichage);

                // On regarde le joueur dont la couleur correspond au client
                Equipe couleur = modele.getJoueur();
                if (couleur == Equipe.JAUNE) {
                    joueurActuel = joueur1;
                }
                else {
                    joueurActuel = joueur2;
                }
                joueur1.getOut().println("Joueur " + joueurActuel.getPseudo() + ", choisissez une colonne (0-6) :");
                joueur2.getOut().println("Joueur " + joueurActuel.getPseudo() + ", choisissez une colonne (0-6) :");

                // Lecture de la colonne choisie par le joueur actuel
                int colonne;
                colonne = Integer.parseInt(joueurActuel.getIn().readLine());

                // Validation de l'entrée utilisateur
                if (colonne < 0 || colonne >= ModeleJeu.COLONNES) {
                    joueurActuel.getOut().println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                    continue;
                }

                // Tentative de déposer le pion
                if (!modele.getGrille().poserPion(colonne, modele.getJoueur())) {
                    joueurActuel.getOut().println("Colonne pleine, choisissez une autre.");
                    continue;
                }

                // Vérification de victoire
                if (modele.estGagnee()) {
                    joueur1.getOut().println(modele.getGrille().afficher2()); joueur2.getOut().println(modele.getGrille().afficher2());
                    joueur1.getOut().println("Le joueur " + joueurActuel.getPseudo() + " a gagné !");
                    joueur2.getOut().println("Le joueur " + joueurActuel.getPseudo() + " a gagné !");
                    this.serveur.finirPartie(this, joueur1, joueur2, joueurActuel);
                    break;
                }

                // Vérification de match nul
                if (modele.estPerdu()) {
                    joueur1.getOut().println(modele.getGrille().afficher2()); joueur2.getOut().println(modele.getGrille().afficher2());
                    joueur1.getOut().println("Match nul !");
                    joueur2.getOut().println("Match nul !");
                    this.serveur.finirPartie(this, joueur1, joueur2, null);
                    break;
                }

                // Changement de joueur
                modele.changerJoueur();
                
                } catch (Exception e) {
                    e.getMessage();
            }
        }
    }
}
