package reseau.Executable;

import java.io.IOException;

import reseau.Serveur;

public class ServeurExecutable {
    public static void main(String[] args) throws IOException {
        int port = 4444;
        Serveur serveur = new Serveur(port);
        System.out.println("Lancement du serveur");
    }
}
