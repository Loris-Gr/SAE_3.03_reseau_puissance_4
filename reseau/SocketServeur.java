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
                Thread t = new Thread(new ClientHandler(socketClient, this.serveur));
                t.start();
                System.out.println("connexion d'un client");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
