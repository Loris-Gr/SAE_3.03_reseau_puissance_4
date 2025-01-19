import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import bd.ConnexionMySQL;
import bd.PartieBD;

public class Serveur extends Thread {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlers;
    private List<PartieEnCours> lesPartiesEnCours;

    public Serveur(int port) {
        this.port = port;
        this.clientHandlers = new ArrayList<>();
        this.lesPartiesEnCours = new ArrayList<>();
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
        PartieEnCours partie = new PartieEnCours(joueur1, joueur2, this);
        joueur1.getOut().println("Partie Créée");
        joueur2.getOut().println("Partie Créée");

        this.lesPartiesEnCours.add(partie);

        joueur1.setEnDuel(true);
        joueur2.setEnDuel(true);

        partie.start();
    }

    public void refuserPartie(ClientHandler joueur1, ClientHandler joueur2) {
        joueur1.getOut().println("Duel Refusée");
        joueur2.getOut().println("Duel Refusée");
    }

    public void finirPartie(PartieEnCours partieEnCours) throws ClassNotFoundException {
        partieEnCours.getJoueur1().setEnDuel(false);
        partieEnCours.getJoueur2().setEnDuel(false);

        ModeleJeu modele = partieEnCours.getModele();

        try {
            ConnexionMySQL connexion = null;
            connexion = new ConnexionMySQL();
            connexion.connecter("localhost", "puissance4", "hun", "");
            PartieBD partieBD = new PartieBD(connexion);
            System.out.println("Enregistrement de la partie...");
            
            partieBD.enregistrerPartie(modele.getJoueur().getId(), new java.sql.Date(System.currentTimeMillis()));
            int score = partieBD.getScore(modele.getJoueur().getSymbole()) + 1;
            partieBD.setScore(modele.getJoueur().getSymbole(), score);

            int scoreJoueur1 = partieBD.getScore(modele.getJoueur().getSymbole());    
            int scoreJoueur2 = partieBD.getScore(modele.getJoueur().getSymbole());
            System.out.println("Score du joueur " +  modele.getJoueur().getSymbole()+ " : " + scoreJoueur1);
            System.out.println("Score du joueur " + modele.getJoueur().getSymbole()  + " : " + scoreJoueur2);
            
            System.out.println("Partie enregistrée");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        partieEnCours.interrupt();
        this.lesPartiesEnCours.remove(partieEnCours);
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
