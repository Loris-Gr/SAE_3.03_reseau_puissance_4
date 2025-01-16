package reseau;

import java.io.*;
import java.net.InetAddress;
import java.util.Scanner;
public class Client {
    private String ipServeur;
    private int portServeur;
    private String ipClient;
    private SocketClient socketClient;
    private String pseudo;
    private String message;
    private boolean nouveauMessage;
    private final Object lock = new Object();
    private boolean partieEnCours;

    public Client(String ipServeur, int portServeur) throws IOException {
        this.ipServeur = ipServeur;
        this.portServeur = portServeur;
        this.ipClient = InetAddress.getLocalHost().getHostAddress();
        this.socketClient = new SocketClient(this, this.portServeur, this.ipServeur);
        this.nouveauMessage = false;
        this.partieEnCours = false;
    }

    public String getIpServeur() {
        return ipServeur;
    }

    public void nouveauMessage() {
        synchronized(lock) {
            this.nouveauMessage = true;
            lock.notify();
        }
    }

    public int getPortServeur() {
        return portServeur;
    }

    public String getIpClient() {
        return ipClient;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public String getMessage() {
        return message;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setMessage(String reponse) {
        synchronized(lock) {
        this.message = reponse;
        }
    }

    public void setPartieEnCours(boolean partieEnCours) {
        this.partieEnCours = partieEnCours;
    }

    private void attendreMessage() {
        synchronized (lock) {
            while (!nouveauMessage) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nouveauMessage = false;
        }
    }

    private void lancerPartie() {
        Scanner scanner = new Scanner(System.in);
        String s;
        this.partieEnCours = true;
            while (this.partieEnCours) {
                    attendreMessage();
                    System.out.println(this.message);
                    if (this.message.split("\n")[0].equals("win") || this.message.split("\n")[0].equals("lose") || this.message.split("\n")[0].equals("nul")) {
                        this.partieEnCours = false;
                        System.out.println("Fin de la partie : " + this.message);
                    }
                    else if (this.message.split("\n")[0].equals("play")) {
                        System.out.println("Entrez le numéro de colonne 1;2;3;4;5;6");
                        s = scanner.nextLine();
                        while (!s.matches("[1-6]")) {
                            System.out.println("Numéro invalide. Entrez le numéro de colonne 1;2;3;4;5;6");
                            s = scanner.nextLine();
                        }
                        this.socketClient.envoyerCommande("play " + s);
                    }
                    else if (this.message.equals("wait")) {
                        System.out.println("En attente de l'action de l'autre joueur...");
                    }
            }
        }

    public void lancement() {
        boolean continuer = true;
        Scanner scanner = new Scanner(System.in);
        String s;
        boolean connecte = false;
        this.socketClient.start();
            while (continuer) {
                if (connecte && nouveauMessage) {
                        String[] messages = this.message.split(" ");
                        if (messages[0].split("\n")[0].equals("duel")) {
                            System.out.println(messages[1].split("\n")[0] + " vous défi en duel, acceptez-vous ? (y/n)");
                            s = scanner.nextLine();
                            if (s.equals("n")) {
                                    this.socketClient.envoyerCommande("duel "+s + " " + this.pseudo);
                                }
                            else if (s.equals("y")) {
                                this.socketClient.envoyerCommande("duel "+s+ " " + this.pseudo);
                                lancerPartie();
                            }
                            nouveauMessage = false;
                            continue;
                            }
                    }
                System.out.println("Quel message ? ");
                s = scanner.nextLine();
                String[] messages = s.split(" "); // Avec split de sépare le message en deux grâce aux espaces
                if (messages[0].equals("connect")) {
                    this.socketClient.envoyerCommande("connect "+ messages[1] + " " + this.ipClient);
                    attendreMessage();
                    if (this.message.split("\n")[0].equals("OK")) {
                        System.out.println("Connexion réussie !");
                        connecte = true;
                        this.pseudo = messages[1];
                    }
                    else {
                        System.out.println("Erreur : " + this.message);
                    }
                }
                else if (messages[0].equals("players")) {
                    this.socketClient.envoyerCommande("players");
                    attendreMessage();
                    System.out.println("Liste des autres joueurs : " +this.message);
                }

                else if (messages[0].equals("ask")) {
                        this.socketClient.envoyerCommande("ask "+ messages[1]);
                        attendreMessage();
                        if (this.message.split("\n")[0].equals("accepte")) {
                            System.out.println("Le joueur a accepté le duel !");
                            lancerPartie();
                        }
                        else if (this.message.split("\n")[0].equals("refuse")) {
                            System.out.println("Le joueur n'a pas accepté le duel :(");
                        }
                else if (s.equals("quit")) {
                    this.socketClient.envoyerCommande("quit " + this.ipClient);
                    continuer = false; 
                }
                else {
                    System.out.println("Erreur, commande inconnue");
                }
            }    
        }        
    scanner.close();
}
}