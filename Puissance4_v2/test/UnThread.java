package test;

public class UnThread extends Thread {

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                System.out.println("je suis aze");
                sleep(1000);
                interrupt();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        try {
            sleep(2000);    
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        while (!isInterrupted()) {
            try {
                System.out.println("je suis aze");
                sleep(1000);
                interrupt();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}