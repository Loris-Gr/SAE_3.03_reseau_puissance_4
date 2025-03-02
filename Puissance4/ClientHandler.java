import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class ClientHandler extends Thread {
    private String pseudo;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Serveur serveur;
    private ClientHandler adversaire;
    private boolean duelPropose;
    private boolean enDuel;

    /**
     * Constructeur de la classe ClientHandler.
     *
     * @param socket  Le socket utilisé pour communiquer avec le client.
     * @param serveur Référence au serveur principal.
     * @throws IOException En cas d'erreur lors de la création des flux.
     */
    public ClientHandler(Socket socket, Serveur serveur) throws IOException {
        this.socket = socket;
        this.serveur = serveur;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.adversaire = null;
        this.duelPropose = false;
        this.enDuel = false;
    }

    /**
     * Récupère le pseudo du joueur.
     *
     * @return Le pseudo du joueur.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Définit le pseudo du joueur.
     *
     * @param pseudo Le pseudo à définir.
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Récupère la référence au serveur principal.
     *
     * @return La référence au serveur principal.
     */
    public Serveur getServeur() {
        return serveur;
    }

    /**
     * Récupère l'adversaire actuel du joueur.
     *
     * @return L'adversaire actuel.
     */
    public ClientHandler getAdversaire() {
        return adversaire;
    }

    /**
     * Définit l'adversaire actuel du joueur.
     *
     * @param adversaire L'adversaire à définir.
     */
    public void setAdversaire(ClientHandler adversaire) {
        this.adversaire = adversaire;
    }

    /**
     * Récupère le flux de sortie utilisé pour communiquer avec le client.
     *
     * @return Le flux de sortie.
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * Définit si un duel a été proposé au joueur.
     *
     * @param duelPropose true si un duel a été proposé, sinon false.
     */
    public void setDuelPropose(boolean duelPropose) {
        this.duelPropose = duelPropose;
    }

    /**
     * Récupère le flux d'entrée utilisé pour lire les messages du client.
     *
     * @return Le flux d'entrée.
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * Indique si le joueur est actuellement en duel.
     *
     * @return true si le joueur est en duel, sinon false.
     */
    public boolean getEnDuel() {
        return enDuel;
    }

    /**
     * Définit si le joueur est actuellement en duel.
     *
     * @param enDuel true si le joueur est en duel, sinon false.
     */
    public void setEnDuel(boolean enDuel) {
        this.enDuel = enDuel;
    }
    
    /**
     * Méthode exécutée par le thread, gère les commandes reçues du client.
     */
    @Override
    public void run() {
        String commande;
        try {
            while (true) {
                // Tant que le joueur n'est pas en duel
                // on regarde ce qu'il écrit
                if (!enDuel) {
                    // Lire la commande du client
                    commande = this.in.readLine();
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
                    else if (messages[0].equals("connect")) {
                        this.serveur.connecter(this, messages[1]);
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
                    // Gérer la commande "players"
                    else if (messages[0].equals("players")) {
                        this.out.println(this.serveur.joueurDisponibles());
                    }
                    // Gérer la commande "historique"
                    else if (messages[0].equals("historique")) {
                        this.out.println(this.serveur.historique(this));
                    }
                    // Gérer la commande "score"
                    else if (messages[0].equals("score")) {
                        this.out.println("Votre score est de : " + this.serveur.score(this));
                    }
                    else {
                        this.out.println("Commande inconnue");
                    }
                } else {
                    sleep(2);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
