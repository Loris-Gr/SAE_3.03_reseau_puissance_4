import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private String pseudo;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Serveur serveur;
    private boolean enAttenteReponse; // Indique si ce client attend une réponse pour un défi
    private ClientHandler adversaire;  // Référence à l'adversaire qui reçoit la demande
    private ModeleJeu modele;

    public ClientHandler(Socket socket, Serveur serveur) throws IOException {
        this.socket = socket;
        this.serveur = serveur;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.enAttenteReponse = false;
        this.adversaire = null;
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

    @Override
    public void run() {
        String commande;
        try {
            while (true) {
                commande = this.in.readLine();  // Lire la commande du client
                if (commande == null || socket.isClosed()) {
                    break;
                }

                System.out.println("Commande reçue: " + commande);
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
                        ClientHandler adversaire = this.serveur.trouverJoueur(adversairePseudo);
                        if (adversaire != null) {
                            this.adversaire = adversaire;  // Enregistrer l'adversaire
                            this.enAttenteReponse = true;  // Le joueur attend la réponse de l'adversaire
                            this.adversaire.setAdversaire(this);
                            this.adversaire.enAttenteReponse = true;
                            this.out.println("En attente de réponse de " + adversairePseudo + "...");
                            adversaire.out.println("Le joueur " + this.getPseudo() + " veut vous affronter. Acceptez-vous ? (o/n)");

                            // // Permettre à ce joueur d'envoyer d'autres commandes pendant qu'il attend la réponse
                            // while (this.enAttenteReponse) {
                            //     // Cette boucle permet de vérifier régulièrement si l'adversaire a répondu
                            //     Thread.sleep(1000);
                            // }

                        } else {
                            this.out.println("Joueur " + adversairePseudo + " non trouvé.");
                        }
                    }
                }
                // Gérer la réponse de l'adversaire (oui/non)
                else if (messages[0].equals("o") || messages[0].equals("n")) {
                    while (this.adversaire != null && this.enAttenteReponse) {
                        // Répondre au défi de l'adversaire
                        if (messages[0].equals("o")) {
                            this.adversaire.attendreReponse(true);  // Accepter le défi
                            this.out.println("Le défi a été accepté !");
                            ModeleJeu modeleJeu = new ModeleJeu(Equipe.JAUNE);
                            this.modele = modeleJeu;
                            this.adversaire.modele = modeleJeu;
                            Scanner scanner = new Scanner(this.in);

                            while (true) {
                                // Affichage de la grille et instructions pour le joueur
                                this.out.println(this.modele.getGrille().afficher());
                                this.adversaire.out.println(this.modele.getGrille().afficher());

                                this.out.println("Joueur " + modele.getJoueur() + ", choisissez une colonne (0-6) :");
                                this.adversaire.out.println("Joueur " + modele.getJoueur() + ", choisissez une colonne (0-6) :");
                    
                                // Lecture de la colonne choisie par le joueur
                                int colonne = scanner.nextInt();
                    
                                // Validation de l'entrée utilisateur
                                if (colonne < 0 || colonne >= ModeleJeu.COLONNES) {
                                    this.out.println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                                    this.adversaire.out.println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                                    continue;
                                }
                    
                                // Tentative de déposer le pion
                                if (!modele.getGrille().poserPion(colonne, modele.getJoueur())) {
                                    this.out.println("Colonne pleine, choisissez une autre.");
                                    this.adversaire.out.println("Colonne pleine, choisissez une autre.");
                                    continue;
                                }
                    
                                // Vérification de victoire
                                if (modele.estGagnee()) {
                                    modele.getGrille().afficher();
                                    this.out.println("Le joueur " + modele.getJoueur() + " a gagné !");
                                    this.adversaire.out.println("Le joueur " + modele.getJoueur() + " a gagné !");

                                    this.enAttenteReponse = false;
                                    this.adversaire.enAttenteReponse = false;

                                    this.modele = null;
                                    this.adversaire.modele = null;
                                    break;
                                }
                    
                                // Vérification de match nul
                                if (modele.estPerdu()) {
                                    modele.getGrille().afficher();
                                    this.out.println("Match nul !");
                                    this.adversaire.out.println("Match nul !");

                                    this.enAttenteReponse = false;
                                    this.adversaire.enAttenteReponse = false;

                                    this.modele = null;
                                    this.adversaire.modele = null;
                                    break;
                                }
                    
                                // Changement de joueur
                                modele.changerJoueur();
                                this.adversaire.modele.changerJoueur();
                            }

                        } else {
                            this.adversaire.attendreReponse(true);  // Refuser le défi
                            this.out.println("Le défi a été refusé.");
                        }
                    }
                } else {
                    this.out.println("Commande inconnue");
                }
            }
        } catch (IOException e) {
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

    // Méthode pour informer le joueur que la réponse a été donnée
    public void attendreReponse(boolean reponse) {
        this.enAttenteReponse = true;
        if (reponse) {
            this.out.println("Le défi a été accepté !");
            // Démarrez la partie ici, si nécessaire
        } else {
            this.out.println("Le défi a été refusé.");
        }
    }
}
