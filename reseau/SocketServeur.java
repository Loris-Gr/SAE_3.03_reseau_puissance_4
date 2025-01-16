package reseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServeur extends Thread{
    private ServerSocket socketServeur;
    private Serveur serveur;

    public SocketServeur(int port, Serveur serveur) throws IOException {
        this.socketServeur = new ServerSocket(port);
        this.serveur = serveur;
    }

    public void run() {
        try {
            while (true) {
                Socket socketClient = socketServeur.accept();
                ClientHandler handler = new ClientHandler(socketClient, this.serveur);
                serveur.ajoutHandler(handler);
                Thread t = new Thread(handler);
                t.start();
                System.out.println("connexion d'un client : \n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
