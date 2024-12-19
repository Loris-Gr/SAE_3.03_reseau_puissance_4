package reseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
public class Serveur {
    private int portServeur;
    private SocketServeur socketServer;
    private List<JoueurServeur> joueursConnectes;

    public Serveur(int portServeur) throws IOException {
        this.portServeur = portServeur;
        this.joueursConnectes = new ArrayList<>();
        this.socketServer = new SocketServeur(portServeur, this);
        this.socketServer.start();
    }

    public int getPortServeur() {
        return portServeur;
    }

    public boolean clientConnecte(String pseudo, String ipClient) {
        return this.joueursConnectes.contains(new JoueurServeur(pseudo, ipClient));
    }

    public boolean connecterClient(String pseudo, String ipClient) {
        if (this.clientConnecte(pseudo, ipClient)) {
            return false;
        }
        for (JoueurServeur joueur : this.joueursConnectes) {
            if (joueur.getPseudo().equals(pseudo)) {
                return false;
            }
        }
        this.joueursConnectes.add(new JoueurServeur(pseudo, ipClient));
        return true;
    }

    public boolean deconnecterClient(String pseudo, String ipClient) {
        for (JoueurServeur joueur : this.joueursConnectes) {
            if (joueur.getPseudo().equals(pseudo) && joueur.getIpJoueur().equals(ipClient)) {
                this.joueursConnectes.remove(joueur);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        while (true) {
            try {
                ServerSocket socketServer = new ServerSocket(4444);
                //Socket socketClient = socketServer.accept();
                Thread t = new Thread(new ConnexionServeur(socketServer));
                t.start();
                System.out.println("connexion d'un client");
                //socketClient.close();
                socketServer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}