package Interfaces;

/**
 *
 * @author Awesomeness
 */
public interface Subscriber {

    public void registerObserver(Forum f);

    public void unRegisterObserver(Forum f);

    public void notifyObservers(String msg);

}
