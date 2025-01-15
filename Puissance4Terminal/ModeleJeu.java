package Puissance4Terminal;

public class ModeleJeu {
    public static final int LIGNES = 6; // Nombre de lignes
    public static final int COLONNES = 7; // Nombre de colonnes

    private final Grille grille; // Grille de jeu
    private Equipe joueurActuel; // Joueur actuel
    private int selectIndex; // Index de la colonne sélectionnée

    /**
     * Constructeur de la classe ModeleJeu
     * @param joueur l'équipe du joueur
     */
    public ModeleJeu(Equipe joueur) {
        this.grille = new Grille(LIGNES, COLONNES);
        this.joueurActuel = joueur;
        this.selectIndex = COLONNES / 2;
    }

    /**
     * Sélectionne la colonne à gauche
     */
    public void selectionGauche() {
        if (selectIndex > 0) selectIndex--;
    }

    /**
     * Sélectionne la colonne à droite
     */
    public void selectionDroite() {
        if (selectIndex < COLONNES - 1) selectIndex++;
    }

    /**
     * Dépose le pion dans la colonne sélectionnée
     * @return true si le pion a été déposé, false sinon
     */
    public boolean drop() {
        return grille.poserPion(selectIndex, joueurActuel);
    }

    /**
     * Vérifie si la partie est gagnée
     * @return true si la partie est gagnée, false sinon
     */
    public boolean estGagnee() {
        return verifierVictoire(joueurActuel);
    }

    /**
     * Vérifie si la partie est perdue
     * @return true si la partie est perdue, false sinon
     */
    public boolean estPerdu() {
        return grille.estRemplie();
    }

    /**
     * Retourne l'équipe gagnante
     * @return l'équipe gagnante
     */
    public Equipe getGagnant() {
        return joueurActuel;
    }

    /**
     * Retourne la grille de jeu
     * @return la grille de jeu
     */
    public Grille getGrille() {
        return grille;
    }

    /**
     * Retourne l'index de la colonne sélectionnée
     * @return l'index de la colonne sélectionnée
     */
    public int getSelectIndex() {
        return selectIndex;
    }

    /**
     * Retourne l'équipe du joueur actuel
     * @return l'équipe du joueur actuel
     */
    public Equipe getJoueur() {
        return joueurActuel;
    }

    /**
     * Vérifie si l'équipe a gagné
     * @param equipe l'équipe à vérifier
     * @return true si l'équipe a gagné, false sinon
     */
    private boolean verifierVictoire(Equipe equipe) {
        return verifierLignes(equipe) || verifierColonnes(equipe) || verifierDiagonales(equipe);
    }

    /**
     * Vérifie si l'équipe a gagné en ligne
     * @param equipe l'équipe à vérifier
     * @return true si l'équipe a gagné en ligne, false sinon
     */
    private boolean verifierLignes(Equipe equipe) {
        for (int i = 0; i < LIGNES; i++) {
            int count = 0;
            for (int j = 0; j < COLONNES; j++) {
                count = (grille.getCase(i, j) == equipe) ? count + 1 : 0;
                if (count == 4) return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si l'équipe a gagné en colonne
     * @param equipe l'équipe à vérifier
     * @return true si l'équipe a gagné en colonne, false sinon
     */
    private boolean verifierColonnes(Equipe equipe) {
        for (int j = 0; j < COLONNES; j++) {
            int count = 0;
            for (int i = 0; i < LIGNES; i++) {
                count = (grille.getCase(i, j) == equipe) ? count + 1 : 0;
                if (count == 4) return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si l'équipe a gagné en diagonale
     * @param equipe l'équipe à vérifier
     * @return true si l'équipe a gagné en diagonale, false sinon
     */
    private boolean verifierDiagonales(Equipe equipe) {
        for (int i = 3; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES - 3; j++) {
                if (grille.getCase(i, j) == equipe &&
                    grille.getCase(i - 1, j + 1) == equipe &&
                    grille.getCase(i - 2, j + 2) == equipe &&
                    grille.getCase(i - 3, j + 3) == equipe) return true;
            }
        }
        for (int i = 0; i < LIGNES - 3; i++) {
            for (int j = 0; j < COLONNES - 3; j++) {
                if (grille.getCase(i, j) == equipe &&
                    grille.getCase(i + 1, j + 1) == equipe &&
                    grille.getCase(i + 2, j + 2) == equipe &&
                    grille.getCase(i + 3, j + 3) == equipe) return true;
            }
        }
        return false;
    }

    /**
     * Change le joueur actuel
     */
    public void changerJoueur() {
        if (joueurActuel == Equipe.JAUNE) {
            joueurActuel = Equipe.ROUGE;
        } else {
            joueurActuel = Equipe.JAUNE;
        }
    }
}
