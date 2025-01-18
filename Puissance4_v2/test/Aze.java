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
    private boolean duelAccepte;
    private ModeleJeu modele;

    public ClientHandler(Socket socket, Serveur serveur) throws IOException {
        this.socket = socket;
        this.serveur = serveur;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.adversaire = null;
        this.duelAccepte = false;
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


    

    @Override
    public void run() {
        // ConnexionMySQL connexion = null;
        // try {
        //     connexion = new ConnexionMySQL();
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        //     System.exit(1);
        // }
        // try {
        //     connexion.connecter("localhost", "puissance4", "hun", "");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // PartieBD partieBD = new PartieBD(connexion);

            String commande;
        try {
            while (true) {
                try {
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
                            this.serveur.challegerJoueur(this, adversairePseudo);
                            // if (adversaire != null) {

                            //     this.adversaire = adversaire;  // Enregistrer l'adversaire
                            //     this.adversaire.setAdversaire(this);

                            //     this.out.println("En attente de réponse de " + adversairePseudo + "...");
                            //     adversaire.out.println("Le joueur " + this.getPseudo() + " veut vous affronter. Acceptez-vous ? (o/n)");

                            // } else {
                            //     this.out.println("Joueur " + adversairePseudo + " non trouvé.");
                            // }
                        }
                    }

                    // // Gérer la réponse de l'adversaire (oui/non)
                    // else if (messages[0].equals("o") || messages[0].equals("n")) {
                    //     if (this.adversaire != null) {
                    //         // Répondre au défi de l'adversaire
                    //         if (messages[0].equals("o")) {

                    //             this.out.println("Vous avez accepté le défi !");
                    //             this.adversaire.out.println("Le défi a été accepté");

                    //             ModeleJeu modeleJeu = new ModeleJeu(Equipe.JAUNE);

                    //             this.modele = modeleJeu;
                    //             this.adversaire.modele = modeleJeu;

                    //             this.adversaire.duelAccepte = true;
                    //             this.duelAccepte = true;

                    //         } else {
                    //             this.out.println("Vous avez refusé le défi.");
                    //             this.adversaire.out.println("Le défi a été refusé.");

                    //             this.adversaire.setAdversaire(null);
                    //             this.adversaire = null;
                    //         }
                    //     }
                    // }
                    else {
                        this.out.println("Commande inconnue");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                
                

                // while (this.adversaire != null && this.duelAccepte) {
                //     Scanner scanner = new Scanner(this.in);
                //     // Affichage de la grille et instructions pour le joueur
                //     this.out.println(this.modele.getGrille().afficher2());
                //     this.adversaire.out.println(this.modele.getGrille().afficher2());

                //     this.out.println("Joueur " + modele.getJoueur() + ", choisissez une colonne (0-6) :");
                //     this.adversaire.out.println("Joueur " + modele.getJoueur() + ", choisissez une colonne (0-6) :");
        
                //     // Lecture de la colonne choisie par le joueur
                //     int colonne = scanner.nextInt();
        
                //     // Validation de l'entrée utilisateur
                //     if (colonne < 0 || colonne >= ModeleJeu.COLONNES) {
                //         this.out.println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                //         this.adversaire.out.println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                //         continue;
                //     }
        
                //     // Tentative de déposer le pion
                //     if (!modele.getGrille().poserPion(colonne, modele.getJoueur())) {
                //         this.out.println("Colonne pleine, choisissez une autre.");
                //         this.adversaire.out.println("Colonne pleine, choisissez une autre.");
                //         continue;
                //     }
        
                //     // Vérification de victoire
                //     if (modele.estGagnee()) {
                //         modele.getGrille().afficher();
                //         this.out.println("Le joueur " + modele.getJoueur() + " a gagné !");
                //         this.adversaire.out.println("Le joueur " + modele.getJoueur() + " a gagné !");

                //         try {
                //             this.out.println("Enregistrement de la partie...");
                //             partieBD.enregistrerPartie(modele.getJoueur().getId(), new java.sql.Date(System.currentTimeMillis()));
                //             int score = partieBD.getScore(modele.getJoueur().getSymbole()) + 1;
                //             partieBD.setScore(modele.getJoueur().getSymbole(), score);
                
                //             int scoreJoueur1 = partieBD.getScore(modele.getJoueur().getSymbole());    
                //             int scoreJoueur2 = partieBD.getScore(modele.getJoueur().getSymbole());
                //             System.out.println("Score du joueur " +  modele.getJoueur().getSymbole()+ " : " + scoreJoueur1);
                //             System.out.println("Score du joueur " + modele.getJoueur().getSymbole()  + " : " + scoreJoueur2);
                            
                //         } catch (java.sql.SQLException e) {
                //             e.printStackTrace();
                //         }
                

                //         this.adversaire.modele = null;
                //         this.modele = null;

                //         this.adversaire.duelAccepte = false;
                //         this.duelAccepte = false;
                //         break;
                //     }
        
                //     // Vérification de match nul
                //     if (modele.estPerdu()) {
                //         modele.getGrille().afficher();
                //         this.out.println("Match nul !");
                //         this.adversaire.out.println("Match nul !");

                //         this.adversaire.modele = null;
                //         this.modele = null;

                //         this.adversaire.duelAccepte = false;
                //         this.duelAccepte = false;
                //         break;
                //     }
        
                //     // Changement de joueur
                //     modele.changerJoueur();
                //     // this.adversaire.modele.changerJoueur();
                //     // scanner.close();
                // }
                // // scanner.close();
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
}
