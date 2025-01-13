package reseau;

public class JoueurServeur {
    private String pseudo;
    private String ipJoueur;

    public JoueurServeur(String pseudo, String ipJoueur) {
        this.pseudo = pseudo;
        this.ipJoueur = ipJoueur;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getIpJoueur() {
        return this.ipJoueur;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JoueurServeur)) {
            return false;
        }
        JoueurServeur j = (JoueurServeur) o;
        return j.pseudo.equals(this.pseudo) && j.ipJoueur.equals(this.ipJoueur);
    }
}
