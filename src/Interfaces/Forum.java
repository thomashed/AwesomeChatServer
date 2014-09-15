package Interfaces;

/**
 *
 * @author Awesomeness
 */
public interface Forum {

    public void startListeners();
    
    public boolean registerSubscriber(Subscriber client);
    
    public boolean unRegisterSubscriber(Subscriber client);
    
    public void notifySubscribers(String msg);
    
}
