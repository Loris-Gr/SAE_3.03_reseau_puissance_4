package reseau;

import java.io.*;
import java.net.InetAddress;
import java.util.Scanner;
public class Client {
    private String IPServeur;
    private int portServeur;
    private String IPClient;
    private SocketClient socketClient;
    private String pseudo;
    private String message;

    public Client(String IPServeur, int portServeur) throws IOException {
        this.IPServeur = IPServeur;
        this.portServeur = portServeur;
        this.IPClient = InetAddress.getLocalHost().getHostAddress();
        this.socketClient = new SocketClient(this.portServeur, this.IPServeur);
    }

    public String getIPServeur() {
        return IPServeur;
    }

    public int getPortServeur() {
        return portServeur;
    }

    public String getIPClient() {
        return IPClient;
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

    public static void main(String[] args) {
        boolean continuer = true;
        Scanner scanner = new Scanner(System.in);
        String s;
            while (continuer) {
                System.out.println("Quel message ? ");
                s = scanner.nextLine();
                String[] messages = s.split(" "); // Avec split de sépare le message en deux grâce aux espaces
                if (messages[0].equals("connect")) {
                    
                }
                else if (messages[0].equals("ask")) {
                    
                }
                else if (messages[0].equals("play")) {
                    
                }

                else if (s.equals("quit")) {
                    continuer = false; 
                }
                else {
                    System.out.println("Erreur, commande inconnue");
                }
            }
            scanner.close();
    }        
}