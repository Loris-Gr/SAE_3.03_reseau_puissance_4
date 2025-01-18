public class PartieEnCours extends Thread {
    
    private ModeleJeu modeleJeu;

    public PartieEnCours() {
        this.modeleJeu = new ModeleJeu(Equipe.AUCUNE);
    }
}
