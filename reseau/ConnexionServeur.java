package reseau;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnexionServeur implements Runnable{
    private ServerSocket socketServeur;
    private Socket socketClient;

    public ConnexionServeur(ServerSocket socket){
        try {
            this.socketServeur = socket;
            this.socketClient = this.socketServeur.accept();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    public void run(){
    // Interaction avec le client
    }
}
