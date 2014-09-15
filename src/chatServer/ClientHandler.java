package chatServer;

import Interfaces.Forum;
import Interfaces.Subscriber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Awesomeness
 */
public class ClientHandler implements Runnable, Subscriber {

    private final Socket client;
    private String userName;
    private BufferedReader in;
    private PrintWriter out;
    private boolean hasConnected;
    private String currentMessage;

    private BlockingQueue<Subscriber> connectionRequests;
    private BlockingQueue<Subscriber> messages;

    public ClientHandler(Socket clientSocket, BlockingQueue<Subscriber> connectionRequests, BlockingQueue<Subscriber> messages) throws IOException {
        this.messages = messages;
        this.connectionRequests = connectionRequests;
        this.hasConnected = false;
        this.client = clientSocket;
        openResources();
    }

    private void openResources() throws IOException {
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream());
    }

    public void closeResources() throws IOException {
        in.close();
        out.close();
    }

    private void connectAttempt(String msg) {
        if (msg.startsWith("CONNECT#")) {
            // Set the userName
            String[] token = msg.split("#");
            setUsername(token[1]);
        }
        hasConnected = true;
    }

    // Overriden methods //
    @Override
    public void closeConnection() throws IOException {
        send("CLOSE#");
        closeResources();
        client.close();
    }

    @Override
    public void listen() {
        new Thread(this).start();
    }

    @Override
    public void setMessage(String msg) {
        currentMessage = msg;
    }

    @Override
    public String getMessage() {
        return currentMessage;
    }

    @Override
    public void send(String msg) {
        out.println(msg);
    }

    @Override
    public void setUsername(String name) {
        userName = name;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    // Listens for input messages from client
    @Override
    public void run() {
        String response;

        try {
            while (true) {
                response = in.readLine();
                if (!hasConnected) {
                    connectAttempt(response);
                    connectionRequests.put(this);
                }else{
                    setMessage(response);
                    messages.put(this);
                }
            }
        } catch (IOException | InterruptedException e) {
            
        }
    }

}
