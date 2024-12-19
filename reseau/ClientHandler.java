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
                    boolean etat = serveur.connecterClient(messages[1], messages[2]);
                    if (etat) {
                        this.envoyerMessage("Client " + messages[1] + " connecté");
                    }
                    else {
                        this.envoyerMessage("Client déjà connecté");
                    }
                }
                else if (messages[0].equals("ask")) {
                    this.envoyerMessage("Recherche du joueur " + messages[1]);
                    
                }
                else if (messages[0].equals("play")) {
                    this.envoyerMessage("Bientôt");
                    
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

}
