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

    public Client(String ipServeur, int portServeur) throws IOException {
        this.ipServeur = ipServeur;
        this.portServeur = portServeur;
        this.ipClient = InetAddress.getLocalHost().getHostAddress();
        this.socketClient = new SocketClient(this.portServeur, this.ipServeur);
    }

    public String getIpServeur() {
        return ipServeur;
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
                    this.socketClient.envoyerCommande("connect "+ messages[1] + " " + this.ipClient);
                    String reponse = this.socketClient.lireReponse();
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion réussie !");
                        connecte = true;
                    }
                    else {
                        System.out.println("Erreur : " + reponse);
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
            }
            scanner.close();
    }        
}