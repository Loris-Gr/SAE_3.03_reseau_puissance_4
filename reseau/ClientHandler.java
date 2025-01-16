package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter writer;
    private InputStreamReader stream;
    private BufferedReader reader;
    private Serveur serveur;
    private String pseudo;

    public ClientHandler(Socket socket, Serveur serveur) throws IOException{
        this.socket = socket;
        this.serveur = serveur;
        this.writer = new PrintWriter(socket.getOutputStream());
        this.stream = new InputStreamReader(socket.getInputStream());
        this.reader = new BufferedReader(stream);
    }

    public void envoyerMessage(String message) {
        this.writer.println(message);
        this.writer.flush();
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public Partie trouverMaPartie(ClientHandler joueur) {
        for (Partie partie : serveur.getParties()) {
            if (partie.getJoueur1().equals(joueur) || partie.getJoueur2().equals(joueur)) {
                return partie;
            }
        }
        return null;
    }

    public void run(){
        String message;
        try {
            while (true) {
                if (socket.isClosed()) {
                    break;
                }
                message = reader.readLine();
                String[] messages = message.split(" "); // Avec split de sépare le message en deux grâce aux espaces
                if (messages[0].equals("connect")) {
                    Serveur.TupleRetour tupleRetour = serveur.connecterClient(messages[1], messages[2]);
                    boolean etat = tupleRetour.estValide();
                    String retour = tupleRetour.getMessage(); 
                    if (etat) {
                        this.pseudo = messages[1];
                        this.envoyerMessage("OK");
                    }
                    else {
                        this.envoyerMessage("ERR, " + retour);
                    }
                }

                else if (messages[0].equals("players")) {
                    message = this.serveur.getStringClientConnectes();
                    this.envoyerMessage(message);
                }

                else if (messages[0].equals("ask")) {
                    ClientHandler adversaireHandler = serveur.getHandler(messages[1]);
                    adversaireHandler.envoyerMessage("duel "+this.pseudo);
                    
                }

                else if (messages[0].equals("duel")) {
                    ClientHandler adversaire = serveur.getHandler(messages[2]);
                    if (messages[1].equals("y")) {
                        adversaire.envoyerMessage("accepte");
                        serveur.lancerPartie(adversaire, this);
                    } else if (messages[1].equals("n")) {
                        adversaire.envoyerMessage("refuse");
                    }
                }

                else if (messages[0].equals("play")) {
                    Partie maPartie = trouverMaPartie(this);
                        if (maPartie != null) {
                            int colonne = Integer.parseInt(messages[1]);
                            maPartie.jouerColonne(this, colonne);
                        }
                    
                }
                else if (messages[0].equals("quit")) {
                    boolean etat = serveur.deconnecterClient(messages[1]);
                    if (etat) {
                        this.envoyerMessage("Client " + messages[1] + " déconnecté");
                        System.out.println("Client " + messages[1] + " déconnecté");
                    }
                    else {
                        this.envoyerMessage("Client non connecté");
                    }
                }
                else {
                    this.envoyerMessage("Commande inconnue");
                }
            }
            reader.close();
            writer.close();
            socket.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Pseudo de l'handler : " + this.pseudo;
    }

}
