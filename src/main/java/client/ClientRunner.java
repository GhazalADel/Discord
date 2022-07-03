package client;



import java.io.IOException;
import java.net.Socket;


/**
 * ClientRunner class connect to server by port and ip and creates a socket
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class ClientRunner {
    public static void main(String[] args) throws InterruptedException {
        ShutDownTask shutDownTask = new ShutDownTask();
        Runtime.getRuntime().addShutdownHook(shutDownTask);

        while (true) {
            Socket socket;
            try {
                socket=new Socket("localhost",11000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //creating client object and invoking process method!
            Client client=new Client(socket);
            try {
                client.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
