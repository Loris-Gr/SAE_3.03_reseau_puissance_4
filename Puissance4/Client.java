import java.io.*;
import java.net.*;

public class Client extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private String host;

    public Client(int port, String host) throws UnknownHostException, IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(this.host, this.port);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;

            // Lire les messages du serveur
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("Serveur: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            // Envoyer des messages depuis le terminal
            while (true) {
                message = userInput.readLine(); // Lire ce que l'utilisateur tape
                this.out.println(message);  // Envoyer le message au serveur
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
