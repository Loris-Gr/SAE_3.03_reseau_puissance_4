import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Serveur extends Thread {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlers;
    private List<PartieEnCours> partieEnCours;

    public Serveur(int port) {
        this.port = port;
        this.clientHandlers = new ArrayList<>();
        this.partieEnCours = new ArrayList<>();
    }

    public ClientHandler trouverJoueur(String pseudo) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getPseudo() != null && clientHandler.getPseudo().equals(pseudo)) {
                return clientHandler;
            }
        }
        return null;
    }

    public void challegerJoueur(ClientHandler joueur1, String joueur2){
        ClientHandler clientHandler = this.trouverJoueur(joueur2);
        if (clientHandler == null) {
            joueur1.getOut().println("Joueur " + joueur2 + " non trouvé");;
        }
        else {
            joueur1.getOut().println("En attente de réponse de " + joueur2 + " ...");
            joueur1.setAdversaire(clientHandler);

            clientHandler.getOut().println("Le joueur " + joueur1.getPseudo() + " veut vous affronter");
            clientHandler.getOut().println("Acceptez-vous ? (o/n)");
            
            clientHandler.setAdversaire(joueur1);
            clientHandler.setDuelPropose(true);
        }
    }

    public void creerPartie(ClientHandler joueur1, ClientHandler joueur2) {
        joueur1.getOut().println("Partie Créée");
        joueur2.getOut().println("Partie Créée");
    }

    public void refuserPartie(ClientHandler joueur1, ClientHandler joueur2) {
        joueur1.getOut().println("Duel Créée");
        joueur2.getOut().println("Duel Refusée");
    }

    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Serveur en attente de connexions sur le port " + this.port);

            while (true) {
                Socket clientSocket = this.serverSocket.accept();  // Attente d'une connexion
                System.out.println("Un client est connecté !");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                this.clientHandlers.add(clientHandler);
                clientHandler.start(); // Lancer un thread pour chaque client connecté
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Vous pouvez également ajouter d'autres méthodes pour gérer les clients ou les messages
}
