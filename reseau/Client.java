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

    public Client(String ipServeur, int portServeur) throws IOException {
        this.ipServeur = ipServeur;
        this.portServeur = portServeur;
        this.ipClient = InetAddress.getLocalHost().getHostAddress();
        this.socketClient = new SocketClient(this, this.portServeur, this.ipServeur);
        this.nouveauMessage = false;
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

    public void lancement() {
        boolean continuer = true;
        Scanner scanner = new Scanner(System.in);
        String s;
        boolean connecte = false;
        this.socketClient.start();
            while (continuer) {
                System.out.println("Quel message ? ");
                s = scanner.nextLine();
                String[] messages = s.split(" "); // Avec split de sépare le message en deux grâce aux espaces
                if (messages[0].equals("connect")) {
                    synchronized (lock) {
                    this.socketClient.envoyerCommande("connect "+ messages[1] + " " + this.ipClient);
                    while (!nouveauMessage) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                    if (this.message.split("\n")[0].equals("OK")) {
                        System.out.println("Connexion réussie !");
                        connecte = true;
                    }
                    else {
                        System.out.println("Erreur : " + this.message);
                    }
                    
                }
                else if (messages[0].equals("players")) {
                    synchronized (lock) {   
                    this.socketClient.envoyerCommande("players");
                    while (!nouveauMessage) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Liste des autres joueurs : " +this.message);
                }
            }

                else if (messages[0].equals("ask")) {
                    this.socketClient.envoyerCommande("ask "+ messages[1]);
                }
                else if (messages[0].equals("play")) {
                    this.socketClient.envoyerCommande("play "+ messages[1]);
                }
                else if (s.equals("quit")) {
                    this.socketClient.envoyerCommande("quit " + this.ipClient);
                    continuer = false; 
                }
                else {
                    System.out.println("Erreur, commande inconnue");
                }
                nouveauMessage = false;
                this.message = "";
            }
            scanner.close();
    }        
}