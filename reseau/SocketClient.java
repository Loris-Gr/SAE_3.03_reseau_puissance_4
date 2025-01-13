package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SocketClient extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private InputStreamReader stream;
    private BufferedReader reader;
    private String derniereReponse;
    private Lock lock;
    private Condition reponseRecue;

    public SocketClient(int port, String ip) throws IOException{
        this.socket = new Socket(ip, port);
        this.writer = new PrintWriter(socket.getOutputStream());
        this.stream = new InputStreamReader(socket.getInputStream());
        this.reader = new BufferedReader(stream);
        this.lock = new ReentrantLock();
        this.reponseRecue = this.lock.newCondition();

    }

    public void envoyerCommande(String commande) {
        writer.println(commande);
        writer.flush();
    }

    public String lireReponse() {
        lock.lock();
        try {
            this.reponseRecue.await(); // Attendre la réponse du serveur
            return this.derniereReponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
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
            String message;
            while ((message = reader.readLine()) != null) {
                lock.lock();
                try {
                    this.derniereReponse = message;
                    this.reponseRecue.signal(); // Réveiller le thread principal
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
