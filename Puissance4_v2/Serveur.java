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
        joueur1.getOut().println("Le joueur " + joueur2.getPseudo() + " à refusé le duel");
        joueur2.getOut().println("Vous avez refusé le duel");
    }

    public void finirPartie(PartieEnCours partieEnCours, ClientHandler joueur1, ClientHandler joueur2, ClientHandler gagnant) throws ClassNotFoundException {
        partieEnCours.getJoueur1().setEnDuel(false);
        partieEnCours.getJoueur2().setEnDuel(false);

        partieEnCours.getJoueur1().setAdversaire(null);
        partieEnCours.getJoueur2().setAdversaire(null);

        try {
            // Enregistrement de la partie
            ConnexionMySQL connexion = null;
            connexion = new ConnexionMySQL();
            connexion.connecter("localhost", "puissance4", "hun", "");

            PartieBD partieBD = new PartieBD(connexion);
            partieBD.creerJoueur(joueur1.getPseudo());
            partieBD.creerJoueur(joueur2.getPseudo());

            if (gagnant != null) {
                System.out.println("Enregistrement de la partie : " + joueur1.getPseudo() + " contre " + joueur2.getPseudo());
                System.out.println("Vainqueur : " + gagnant.getPseudo());
                partieBD.enregistrerPartie(joueur1.getPseudo(), joueur2.getPseudo(), gagnant.getPseudo(), new java.sql.Date(System.currentTimeMillis()));
                int score = partieBD.getScore(gagnant.getPseudo()) + 1;
                partieBD.setScore(gagnant.getPseudo(), score);
                System.out.println();
            }
            else {
                System.out.println("Enregistrement de la partie : " + joueur1.getPseudo() + " contre " + joueur2.getPseudo());
                partieBD.enregistrerPartie(joueur1.getPseudo(), joueur2.getPseudo(), "null", new java.sql.Date(System.currentTimeMillis()));
                System.out.println("Égalité");
            }            

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
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
}
