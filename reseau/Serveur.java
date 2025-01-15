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

    public static class TupleRetour {
        private boolean estValide;
        private String message;

        public TupleRetour(boolean estValide, String message) {
            this.estValide = estValide;
            this.message = message;
        }

        public boolean estValide() {
            return estValide;
        }

        public String getMessage() {
            return message;
        }
    }

    public int getPortServeur() {
        return portServeur;
    }

    public String getStringClientConnectes() {
        String pseudos = "";
        for (JoueurServeur joueur : this.joueursConnectes) {
            pseudos += joueur.getPseudo() + "\n";
        }
        return pseudos;
    }

    public boolean clientConnecte(String pseudo, String ipClient) {
        return this.joueursConnectes.contains(new JoueurServeur(pseudo, ipClient));
    }

    public TupleRetour connecterClient(String pseudo, String ipClient) {
        String message = "";
        boolean erreurPseudo = false;
        if (pseudo.length() < 3 ) {
            message+="Pseudo trop court";
            erreurPseudo = true;
        }
        if (pseudo.length() > 10 ) {
            message+="Pseudo trop long";
            erreurPseudo = true;
        }
        if (pseudo.contains(" ")) {
            if (message.equals("")) {
                message+="Il ne faut pas d'espace dans le pseudo";
                erreurPseudo = true;
            }
            else {
                message+= " et il ne faut pas d'espace dans le pseudo";
            }
        }
        if (erreurPseudo) {
            return new TupleRetour(false, message);
        }

        if (this.clientConnecte(pseudo, ipClient)) {
            return new TupleRetour(false, "Client déjà connecté");
        }
        for (JoueurServeur joueur : this.joueursConnectes) {
            if (joueur.getPseudo().equals(pseudo)) {
                return new TupleRetour(false, "Pseudo déjà utilisé");
            }
        }
        this.joueursConnectes.add(new JoueurServeur(pseudo, ipClient));
        return new TupleRetour(true, "OK");
    }

    public boolean deconnecterClient(String ipClient) {
        for (JoueurServeur joueur : this.joueursConnectes) {
            if (joueur.getIpJoueur().equals(ipClient)) {
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