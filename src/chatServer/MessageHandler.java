package chatServer;

import Interfaces.Forum;
import Interfaces.Subscriber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    private List<Subscriber> subscribers;
    private BlockingQueue<Subscriber> messages;
    private BlockingQueue<Subscriber> connectionRequests;

    public MessageHandler(ServerSocket server) {
        this.server = server;
        this.messages = new ArrayBlockingQueue(20);
        this.subscribers = new ArrayList();
        this.connectionRequests = new ArrayBlockingQueue(10);
    }

    public void analyzeMessage(Subscriber msg) {

//        if (msg.startsWith("CONNECT#")) {
//            String userName = msg.substring(8);
//            // now we need to check if username already exists
//
//        } else if (msg.startsWith("SEND#")) {
//            String recieversAndMessage = msg.substring(5);
//            String[] tokens = recieversAndMessage.split("#");       // tokens[0] == recievers ----- tokens[1] == message
//            String[] receivers = tokens[0].split(",");              // If recievers == * --> Then do something else!
//            String message = tokens[1];
//            // Loop through all subs with the corresponding names
//        } else if (msg.startsWith("CLOSE#")) {
//            // Expected: CLOSE#username
//            String user = msg.substring(6);
//            // Loop through and close connection on client with that name!
//        }

    }

    // Overriden methods
    /**
     * Starts the thread that listens for new connections to clients. Also
     * starts the thread that calls .take from message queue.
     *
     */
    @Override
    public void startListener() {
        new Thread(this).start();
        new Thread(new peek()).start();
        new Thread(new joinChat()).start();
    }

    @Override
    public boolean registerSubscriber(Subscriber client) {
        return subscribers.add(client);
    }

    /**
     *
     * @param client
     * @return true if specified subscriber got removed
     */
    @Override
    public boolean unRegisterSubscriber(Subscriber client) {
        return subscribers.remove(client);
    }

    /**
     * Notifies ALL subscribers of the new message. Will only be called if the
     * message contains "*"
     *
     * @param msg
     */
    @Override
    public void notifySubscribers(String msg) {
        for (Subscriber current : subscribers) {
            current.send(msg);
        }
    }

    /**
     * Will only notify the subscribers that the sender intended to send this
     * message.
     *
     */
    public void notifySubscriber() {
    }

    // Listens for new clients on the server
    @Override
    public void run() {

        try {
            while (true) {
                Socket clientSocket = server.accept();
                Subscriber client = new ClientHandler(clientSocket, connectionRequests, messages);
                client.listen();
            }
        } catch (IOException e) {

        }
    }

    // This peeks on the mesgQeue  -->  Is an INNER-CLASS because it isn't static
    class peek implements Runnable {

        @Override
        public void run() {
            String msg;

            while (true) {
                try {
                    // Analyze the new message 
                    Subscriber client = messages.take();
                    analyzeMessage(client);
                } catch (InterruptedException ex) {
                }
            }

        }

    }

    class joinChat implements Runnable {

        public boolean canJoin(String name) {
            boolean validName = true;

            for (Subscriber client : subscribers) {
                if (client.getUsername().equals(name)) {
                    validName = false;
                }
            }

            return validName;
        }

        @Override
        public void run() {

            try {
                while (true) {
                    Subscriber client = connectionRequests.take();
                    if (canJoin(client.getUsername())) { // Then the client picked a valid userName
                        registerSubscriber(client);
                    }
                }
            } catch (InterruptedException e) {

            }

        }

    }

}
