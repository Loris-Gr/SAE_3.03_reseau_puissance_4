import java.io.*;
import java.net.*;

public class Client extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private String host;

    /**
     * Constructeur de la classe Client.
     *
     * @param port Le port sur lequel le serveur écoute.
     * @param host L'adresse du serveur.
     * @throws UnknownHostException Si l'adresse du serveur est introuvable.
     * @throws IOException          Si une erreur survient lors de la création des flux.
     */
    public Client(int port, String host) throws UnknownHostException, IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(this.host, this.port);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Méthode exécutée par le thread, gérant la communication entre le client et le serveur.
     * Elle lit les messages de l'utilisateur pour les envoyer au serveur
     * et démarre un thread séparé pour lire les messages reçus du serveur.
     */
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
