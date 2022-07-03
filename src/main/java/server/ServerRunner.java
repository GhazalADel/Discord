package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * ServerRunner class used to create a serverSocket by giving a port number.
 * also we create a Server object and turn on our server by invoking startServer.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class ServerRunner {
    public static void main(String[] args) {

        ServerSocket serverSocket= null;

        try {
            serverSocket = new ServerSocket(11000);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Server server=new Server(serverSocket);
        server.startServer();
    }
}
