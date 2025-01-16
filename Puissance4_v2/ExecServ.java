public class ExecServ {
    public static void main(String[] args) {
        Serveur serveur = new Serveur(4444);
        serveur.start();
    }
}
