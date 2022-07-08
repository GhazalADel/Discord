package discord.discord1;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Message class is superclass of  every message in program.subclasses:ChannelMessage and PrivateMessage
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public abstract class Message implements Serializable {

    //fields
    protected String text;
    protected LocalDateTime time;

    protected User sender;

    //constructor
    public Message(String text, User sender) {
        this.text = text;
        this.sender = sender;
        this.time = time.now();
    }

    //getter methods

    /**
     * This method is used to access message's text
     *@param -
     *@return String This returns text
     */
    public String getText() {
        return text;
    }

    //setter methods

    /**
     * This method is used to set message's text
     *@param text
     *@return -
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method is used to set message's time
     *@param time
     *@return -
     */

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * This method is used to access message's time in special format
     *@param -
     *@return String This returns formattedTime
     */

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        String formattedTime = time.format(formatter);
        return formattedTime;
    }

    /**
     * toString method overrided for creating specific format
     *@param -
     *@return String
     */
    @Override
    public String toString() {
        return sender.getUsername() + " : " + text + " " + getTime();
    }
}
