package Puissance4Terminal.model;

public class Grille {
    private final Equipe[][] grille; // Grille de jeu

    /**
     * Constructeur de la classe Grille
     * @param lignes le nombre de lignes
     * @param colonnes le nombre de colonnes
     */
    public Grille(int lignes, int colonnes) {
        grille = new Equipe[lignes][colonnes];
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                grille[i][j] = Equipe.AUCUNE;
            }
        }
    }

    /**
     * Pose un pion dans la colonne choisie
     * @param colonne la colonne choisie
     * @param equipe l'équipe du joueur
     * @return true si le pion a été posé, false sinon
     */
    public boolean poserPion(int colonne, Equipe equipe) {
        for (int i = grille.length - 1; i >= 0; i--) {
            if (grille[i][colonne] == Equipe.AUCUNE) {
                grille[i][colonne] = equipe;
                return true;
            }
        }
        return false; // Colonne pleine
    }

    /**
     * Retourne l'équipe de la case choisie
     * @param ligne la ligne choisie
     * @param colonne la colonne choisie
     * @return l'équipe de la case choisie
     */
    public Equipe getCase(int ligne, int colonne) {
        return grille[ligne][colonne];
    }

    /**
     * Vérifie si la grille est remplie
     * @return true si la grille est remplie, false sinon
     */
    public boolean estRemplie() {
        for (int i = 0; i < grille[0].length; i++) {
            if (grille[0][i] == Equipe.AUCUNE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Affiche la grille de jeu
     */
    public void afficher() {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                System.out.print("| " + grille[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-".repeat(grille[0].length * 4 + 1));
        for (int i = 0; i < grille[0].length; i++) {
            System.out.print("  " + i + " ");
        }
        System.out.println();
    }
}
