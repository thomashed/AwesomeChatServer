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

/**
 *
 * @author Awesomeness
 */
public class ClientHandler implements Runnable, Subscriber /*Will implement the Subscriber interface*/ {

    private final Socket client;
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private List<Forum> observers;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        this.observers = new ArrayList();
        openResources();
    }

    private void openResources() throws IOException {
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream());
    }

    public void sendMessage(String msg) { 
        out.println(msg);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Overriden methods
    @Override
    public void registerObserver(Forum f) {
        observers.add(f);
    }

    @Override
    public void unRegisterObserver(Forum f) {
        observers.remove(f);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Forum current : observers) {
            current.messageArrived(msg);
        }
    }

    // Listens for input messages from client
    @Override
    public void run() {
        String response;

        try {
            while (true) {
                response = in.readLine();
                notifyObservers(response);
            }
        } catch (IOException é) {

        }
    }

}
