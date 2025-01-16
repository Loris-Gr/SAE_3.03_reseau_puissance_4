import java.io.IOException;
import java.net.UnknownHostException;

public class ExecClient {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client client = new Client("val", 4444, "localhost");
        client.start();
    }
}
