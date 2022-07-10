package discord.discord1;

import java.io.Serializable;

/**
 * RequestFriend class represents a friend request
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class RequestFriend implements Serializable {

    //fields
    private User sender;
    private User receiver;

    //constructor
    public RequestFriend(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    //getter methods

    /**
     * This method is used to access user who sent friend request.
     *@param -
     *@return User This returns sender
     */
    public User getSender() {
        return this.sender;
    }

    /**
     * This method is used to access user who receive friend request.
     *@param -
     *@return User This returns receiver
     */
    public User getReceiver() {
        return this.receiver;
    }

    /**
     * This method is used to access user'username who sent friend request.
     *@param -
     *@return String This returns username
     */
    public String getSenderName() {
        return sender.getUsername();
    }

    /**
     * This method is used to access user'username who receive friend request.
     *@param -
     *@return String This returns username
     */
    public String getReceiverName() {
        return receiver.getUsername();
    }

}
