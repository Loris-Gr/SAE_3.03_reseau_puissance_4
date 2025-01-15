package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private InputStreamReader stream;
    private BufferedReader reader;
    private String derniereReponse;
    public Client client;

    public SocketClient(Client client, int port, String ip) throws IOException{
        this.socket = new Socket(ip, port);
        this.writer = new PrintWriter(socket.getOutputStream());
        this.stream = new InputStreamReader(socket.getInputStream());
        this.reader = new BufferedReader(stream);
        this.client = client;
    }

    public void envoyerCommande(String commande) {
        writer.println(commande);
        writer.flush();
    }

    public void fermetureSocket() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
            this.interrupt();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                String message = "";
                if (reader.ready()) {
                    while (reader.ready()) {
                        message += reader.readLine() + "\n";
                    }
                this.client.setMessage(message);
                this.client.nouveauMessage();
                }
                   
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
