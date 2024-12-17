package reseau;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        boolean continuer = true;
        Scanner scanner = new Scanner(System.in);
        String s;
        try {
            Socket socket = new Socket("127.0.0.1",4444);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            
            while (continuer) {
                String message = reader.readLine();
                System.out.println(message);
                System.out.println("Quel message ? ");
                s = scanner.nextLine();
                if (s.equals("quit")) {
                    continuer = false; 
                }
                writer.println(s);
                writer.flush();  
            }
            socket.close();
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }        
}