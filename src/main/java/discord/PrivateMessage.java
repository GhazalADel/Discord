package discord;

import java.io.Serializable;

/**
 * PrivateMessage class represents a message in private chat
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class PrivateMessage extends Message implements Serializable {


    //constructor
    public PrivateMessage(String text, User sender) {
        super(text, sender);
    }

    /**
     * this method used to access text of message
     *@param -
     *@return String
     */
    public String getText() {
        return super.getText();
    }

    /**
     * toString method overrided for creating specific format
     *@param -
     *@return String
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
