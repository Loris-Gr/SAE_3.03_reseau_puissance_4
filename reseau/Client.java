package reseau;

import java.io.*;
import java.net.Socket;
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",4444);
            InputStreamReader stream =
                new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            String message = reader.readLine();
            System.out.println(message);
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Socket socket = new Socket("127.0.0.1",4444);
            PrintWriter writer =
            new PrintWriter(socket.getOutputStream());
            writer.println("Hellow world!");
            writer.flush();
            socket.close();
            }
        catch (Exception e) {
            e.printStackTrace();
        }
    }        
}