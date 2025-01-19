public enum Equipe {
    AUCUNE(0, " "),
    JAUNE(1, "X"),
    ROUGE(2, "O");

    private final int id; // Identifiant de l'équipe
    private final String symbole; // Symbole de l'équipe
    
    /**
     * Constructeur de la classe Equipe
     * @param id l'identifiant de l'équipe
     * @param symbole le symbole de l'équipe
     */
    Equipe(int id, String symbole) {
        this.id = id;
        this.symbole = symbole;
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
