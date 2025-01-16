package Puissance4Terminal.model;

public enum Equipe {
    AUCUNE(0, " ",0),
    JAUNE(1, "J",0),
    ROUGE(2, "R",0);

    private final int id; // Identifiant de l'équipe
    private final String symbole; // Symbole de l'équipe
    private int score; // Score de l'équipe
    /**
     * Constructeur de la classe Equipe
     * @param id l'identifiant de l'équipe
     * @param symbole le symbole de l'équipe
     */
    Equipe(int id, String symbole, int score) {
        this.id = id;
        this.symbole = symbole;
        this.score = score;
    }

    /**
     * Retourne le score de l'équipe
     * @return le score de l'équipe
     */
    public int getScore() {
        return score;
    }

    /**
     * Définit le score de l'équipe
     * @param score le nouveau score de l'équipe
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retourne l'identifiant de l'équipe
     * @return l'identifiant de l'équipe
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le symbole de l'équipe
     * @return le symbole de l'équipe
     */
    public String getSymbole() {
        return symbole;
    }

    @Override
    public String toString() {
        return symbole;
    }
}
