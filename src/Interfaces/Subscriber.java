package Interfaces;

import java.io.IOException;

/**
 *
 * @author Awesomeness
 */
public interface Subscriber {

    public void send(String msg);
    
    public void setMessage(String msg);
    
    public String getMessage();

    public void setUsername(String name);

    public String getUsername();

    /**
     * Starts the thread in subsciber that listen for input from the client
     *
     */
    public void listen();

    public void closeConnection() throws IOException;

}
