import java.io.*;
import java.net.*;
import java.util.Scanner;

import bd.ConnexionMySQL;
import bd.PartieBD;

public class ClientHandler extends Thread {
    private String pseudo;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Serveur serveur;
    private ClientHandler adversaire;  // Référence à l'adversaire qui reçoit la demande
    private boolean duelPropose;
    private boolean enDuel;

    public ClientHandler(Socket socket, Serveur serveur) throws IOException {
        this.socket = socket;
        this.serveur = serveur;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.adversaire = null;
        this.duelPropose = false;
        this.enDuel = false;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Serveur getServeur() {
        return serveur;
    }

    public ClientHandler getAdversaire() {
        return adversaire;
    }
    public void setAdversaire(ClientHandler adversaire) {
        this.adversaire = adversaire;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setDuelPropose(boolean duelPropose) {
        this.duelPropose = duelPropose;
    }

    public BufferedReader getIn() {
        return in;
    }

    public boolean getEnDuel() {
        return enDuel;
    }

    public void setEnDuel(boolean enDuel) {
        this.enDuel = enDuel;
    }
    

    @Override
    public void run() {
        String commande;
        try {
            while (true) {
                if (!enDuel) {

                    commande = this.in.readLine();  // Lire la commande du client
                    if (commande == null || socket.isClosed()) {
                        break;
                    }
                    
                    System.out.println("Commande reçue de " + this.pseudo + ": " + commande);
                    String[] messages = commande.split(" ");

                    // Gérer la commande "bonjour"
                    if (messages[0].equals("bonjour")) {
                        this.out.println("Bonjour, " + this.getPseudo() + " !");
                    }
                    // Gérer la commande "connect"
                    else if (commande.contains("co")) {
                        try {
                            this.setPseudo(messages[1]);
                            this.out.println("Bienvenue, " + this.getPseudo() + " !");
                        } catch (Exception e) {
                            this.out.println("Pseudo incorrect");
                        }
                    }
                    // Gérer la commande "ask"
                    else if (messages[0].equals("ask") && messages.length == 2) {
                        String adversairePseudo = messages[1];
                        if (adversairePseudo.equals(this.getPseudo())) {
                            this.out.println("Vous ne pouvez pas vous affronter vous-même.");
                        } else {
                            // Chercher l'adversaire parmi les clients connectés
                            this.serveur.challegerJoueur(this, adversairePseudo);
                        }
                    }
                    // Gérer la réponse à l'adversaire
                    else if (messages[0].equals("o") || messages[0].equals("n")) {
                        if (this.duelPropose) {
                            if (messages[0].equals("o")) {
                                this.serveur.creerPartie(adversaire, this);
                            }
                            else {
                                this.serveur.refuserPartie(adversaire, this);
                                this.duelPropose = false;
                            }
                        }
                    }
                    // Voir les joueurs pas en duel
                    else if (messages[0].equals("players")) {
                        this.out.println(this.serveur.joueurDisponibles());
                    }
                    else {
                        this.out.println("Commande inconnue");
                    }
                }
                else {
                    sleep(2);
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
