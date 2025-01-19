import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bd.ConnexionMySQL;
import bd.PartieBD;

/**
 * Serveur est une classe qui représente le serveur d'un système de jeu multijoueur. 
 * Elle gère les connexions des clients, les interactions entre joueurs et l'enregistrement des parties.
 */
public class Serveur extends Thread {
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlers;
    private List<PartieEnCours> lesPartiesEnCours;
    private ConnexionMySQL connexion = null;

    /**
     * Constructeur de la classe Serveur.
     *
     * @param port      Le port sur lequel le serveur écoute.
     * @param connexion La connexion à la base de données.
     * @throws ClassNotFoundException Si une classe requise est introuvable.
     * @throws SQLException           Si une erreur SQL se produit.
     */
    public Serveur(int port, ConnexionMySQL connexion) throws ClassNotFoundException, SQLException {
        this.port = port;
        this.clientHandlers = new ArrayList<>();
        this.lesPartiesEnCours = new ArrayList<>();
        this.connexion = connexion;
    }

    /**
     * Connecte un joueur au serveur.
     *
     * @param client     Le client à connecter.
     * @param nomJoueur  Le pseudo du joueur.
     */
    public void connecter(ClientHandler client, String nomJoueur) {
        ClientHandler clientHandler = this.trouverJoueur(nomJoueur);
        if (clientHandler == null) {
            client.setPseudo(nomJoueur);
            client.getOut().println("Bienvenue, " + nomJoueur + " !");
        }
        else {
            client.getOut().println("Le pseudo " + nomJoueur + " est déjà utilisé");
        }
        
    }

    /**
     * Recherche un joueur par son pseudo.
     *
     * @param pseudo Le pseudo du joueur.
     * @return Le ClientHandler correspondant, ou null si aucun joueur n'est trouvé.
     */
    public ClientHandler trouverJoueur(String pseudo) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getPseudo() != null && clientHandler.getPseudo().equals(pseudo)) {
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Envoie une demande de duel à un autre joueur.
     *
     * @param joueur1 Le joueur qui initie le défi.
     * @param joueur2 Le pseudo du joueur défié.
     */
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

    /**
     * Crée une partie entre deux joueurs.
     *
     * @param joueur1 Le premier joueur.
     * @param joueur2 Le second joueur.
     */
    public void creerPartie(ClientHandler joueur1, ClientHandler joueur2) {
        PartieEnCours partie = new PartieEnCours(joueur1, joueur2, this);
        joueur1.getOut().println("Partie Créée");
        joueur2.getOut().println("Partie Créée");

        this.lesPartiesEnCours.add(partie);

        joueur1.setEnDuel(true);
        joueur2.setEnDuel(true);

        partie.start();
    }

    /**
     * Gère le refus d'une partie par un joueur.
     *
     * @param joueur1 Le joueur ayant proposé le duel.
     * @param joueur2 Le joueur ayant refusé.
     */
    public void refuserPartie(ClientHandler joueur1, ClientHandler joueur2) {
        joueur1.getOut().println("Le joueur " + joueur2.getPseudo() + " à refusé le duel");
        joueur2.getOut().println("Vous avez refusé le duel");
    }

    /**
     * Termine une partie, enregistre les résultats et met à jour l'état des joueurs.
     *
     * @param partieEnCours La partie en cours.
     * @param joueur1       Le premier joueur.
     * @param joueur2       Le second joueur.
     * @param gagnant       Le gagnant de la partie, ou null en cas d'égalité.
     * @throws ClassNotFoundException Si une classe requise est introuvable.
     */
    public void finirPartie(PartieEnCours partieEnCours, ClientHandler joueur1, ClientHandler joueur2, ClientHandler gagnant) throws ClassNotFoundException {
        partieEnCours.getJoueur1().setEnDuel(false);
        partieEnCours.getJoueur2().setEnDuel(false);

        partieEnCours.getJoueur1().setAdversaire(null);
        partieEnCours.getJoueur2().setAdversaire(null);

        try {
            // Enregistrement de la partie
            PartieBD partieBD = new PartieBD(this.connexion);
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

    /**
     * Vérifie si un joueur est disponible pour un duel.
     *
     * @param clientHandler Le joueur à vérifier.
     * @return true si le joueur est disponible, false sinon.
     */
    public boolean estDisponible(ClientHandler clientHandler) {
        return !clientHandler.getEnDuel();
    }

    /**
     * Renvoie une liste des joueurs disponibles pour un duel.
     *
     * @return Une chaîne contenant les pseudos des joueurs disponibles.
     */
    public String joueurDisponibles() {
        String res = "";

        for (ClientHandler clientHandler : clientHandlers) {
            if (estDisponible(clientHandler)) {
                res += clientHandler.getPseudo() + " ";
            }
        }
        return res;
    }

    /**
     * Récupère l'historique des parties d'un joueur.
     *
     * @param clientHandler Le joueur pour lequel récupérer l'historique.
     * @return Une chaîne contenant l'historique des parties.
     * @throws SQLException           Si une erreur SQL se produit.
     * @throws ClassNotFoundException Si une classe requise est introuvable.
     */
    public String historique(ClientHandler clientHandler) throws SQLException, ClassNotFoundException {
        PartieBD partieBD = new PartieBD(connexion);
        String histo = partieBD.historique(clientHandler.getPseudo());
        return histo;
    }

    /**
     * Récupère le score d'un joueur.
     *
     * @param clientHandler Le joueur pour lequel récupérer le score.
     * @return Une chaîne contenant le score du joueur.
     * @throws SQLException           Si une erreur SQL se produit.
     * @throws ClassNotFoundException Si une classe requise est introuvable.
     */
    public String score(ClientHandler clientHandler) throws SQLException, ClassNotFoundException {
        PartieBD partieBD = new PartieBD(connexion);
        int score = partieBD.getScore(clientHandler.getPseudo());
        return score + "";
    }

    /**
     * Point d'entrée du thread. Lance le serveur et gère les connexions clients.
     */
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
