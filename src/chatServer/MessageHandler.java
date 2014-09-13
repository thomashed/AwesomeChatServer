package chatServer;

import Interfaces.Forum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Awesomeness
 */
public class MessageHandler implements Runnable, Forum {

    private final ServerSocket server;
    private BufferedReader in;
    private PrintWriter out;

    //
    private BlockingQueue<String> messages;
    // Here a list or map of users should be
    //

    public MessageHandler(ServerSocket server) {
        this.server = server;
        this.messages = new ArrayBlockingQueue(20);
    }

    @Override
    public void startListener() {
        new Thread(this).start();
    }
    
    @Override
    public void messageArrived(String msg){
        // When a message arrives, we must analyse it!!!
    }

    // Listens for new clients on the server
    @Override
    public void run() {

        try {
            while (true) {
                Socket clientSocket = server.accept();
                ClientHandler client = new ClientHandler(clientSocket);
                
            }
        } catch (IOException e) {

        }
    }

}
