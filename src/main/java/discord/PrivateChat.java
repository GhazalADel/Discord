package discord;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * PrivateChat class represents a chat between two users
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class PrivateChat implements Serializable {

    //fields
    private ArrayList<PrivateMessage> messages;
    private User user1;
    private User user2;

    //constructor
    public PrivateChat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        messages = new ArrayList<>();
    }

    //getter methods

    /**
     * This method is used to access chat's name!
     *@param -
     *@return String This returns username
     */
    public String getChatName() {
        return user2.getUsername();
    }

    /**
     * This method is used to access a list of private messages in private chat
     *@param -
     *@return  ArrayList<PrivateMessage> This returns messages
     */
    public ArrayList<PrivateMessage> getMessages() {
        return messages;
    }

    /**
     * This method is used to access user 1
     *@param -
     *@return  User
     */
    public User getUser1() {
        return user1;
    }

    /**
     * This method is used to access user 2
     *@param -
     *@return  User
     */
    public User getUser2() {
        return user2;
    }

    //setter methods

    /**
     * This method is used to set list of messages to private chat
     *@param messages
     *@return  Nothing
     */
    public void setMessages(ArrayList<PrivateMessage> messages) {
        this.messages = messages;
    }

    /**
     * This method is used to set user1 to private chat
     *@param user1
     *@return  Nothing
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * This method is used to set user2 to private chat
     *@param user2
     *@return  Nothing
     */

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    /**
     * This method is used to adding all messages in private chat into a string
     *@param -
     *@return  String chatMessages
     */

    public String chatMessages() {
        String chatMessages = "";
        for(PrivateMessage m : messages) {
            chatMessages = chatMessages.concat( m.toString() + "\n");
        }
        return chatMessages;
    }

    /**
     * This method is used to add a message into list of messages
     *@param message
     *@return  -
     */

    public void addMessage(PrivateMessage message) {
        messages.add(message);
    }
}
