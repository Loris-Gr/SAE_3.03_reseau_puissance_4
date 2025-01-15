package Puissance4Terminal.model;

public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1);

    private final int offsetL; //ligne
    private final int offsetC; //colonne

    /**
     * Constructeur de la classe Direction
     * @param offsetL la ligne
     * @param offsetC la colonne
     */
    Direction(int offsetL, int offsetC) {
        this.offsetL = offsetL;
        this.offsetC = offsetC;
    }

    /**
     * Retourne la ligne
     * @return la ligne
     */
    public int getOffsetL() {
        return offsetL;
    }

    /**
     * Retourne la colonne
     * @return la colonne
     */
    public int getOffsetC() {
        return offsetC;
    }
}
