package reseau.Executable;

public class ClientExecutable {
    public static void main(String[] args) {
        String serveur = "127.0.0.1";
        int port = 4444;
        try {
            reseau.Client client = new reseau.Client(serveur, port);
            client.lancement();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
