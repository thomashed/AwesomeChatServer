package chatServer;

import java.io.IOException;

/**
 *
 * @author Awesomeness
 */
public class ServerExecutor {

    // Start 
    public static void main(String[] args) {
        try {
            new ChatServer(7070, 10).startServer();
        } catch (IOException e) {
        }

    }

}
