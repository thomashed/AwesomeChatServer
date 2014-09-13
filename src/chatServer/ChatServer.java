package chatServer;

import Interfaces.Forum;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 *
 * @author Awesomeness
 */
public class ChatServer {

    private ServerSocket server;
    private Forum forum;
    private final int port;
    private final int backlog;

    public ChatServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    // Starts the server -> then the listening thread
    public boolean startServer() throws IOException {
        server = new ServerSocket(port, backlog, InetAddress.getLocalHost());
        forum = new MessageHandler(server);
        forum.startListener();
        return server != null;
    }

}
