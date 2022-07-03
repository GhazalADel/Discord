package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class has a serverSocket and a method for listening and create connection with clients.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class Server {
    private ServerSocket serverSocket;

    //constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * This method is used to creating sockets between client and server by creating a new ClientHandler object in
     * a new thread.
     *@param -
     *@return -
     */
    public void startServer(){
        //for read user's properties form file when program starts.
        ClientHandler.setUsers(ClientHandler.readUsersFile());

        try{
            while (!serverSocket.isClosed()){
                Socket socket=serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(socket);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e){
            System.out.println("Exception!");
        }

    }
}
