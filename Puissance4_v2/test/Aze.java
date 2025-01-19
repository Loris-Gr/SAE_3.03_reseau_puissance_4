package test;

public class Aze {
    public static void main(String[] args) {
        UnThread untThread = new UnThread();
        try {
            untThread.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.getMessage();
        }
        
        
    }
}