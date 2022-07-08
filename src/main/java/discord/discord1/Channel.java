package discord.discord1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Channel class represents a channel in server
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class Channel implements Serializable {

    //fields
    private String channelName;
    private ArrayList<User> limitedMembers;
    private ArrayList<ChannelMessage> messages;
    private ArrayList<ChannelMessage> pinedMessages;
    private ArrayList<ChannelMessage> allMessages;

    //constructor
    public Channel(String channelName) {
        this.channelName = channelName;
        limitedMembers = new ArrayList<>();
        messages = new ArrayList<>();
        pinedMessages=new ArrayList<>();
        allMessages=new ArrayList<>();
    }

    //getter methods

    /**
     * This method is used to access channel's name
     *@param -
     *@return String This returns channelName
     */
    public String getName() {
        return channelName;
    }

    /**
     * This method is used to access channel's name
     *@param -
     *@return String This returns channelName
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * This method is used to access list of limited members in channel
     *@param -
     *@return ArrayList<User> This returns limitedMembers
     */
    public ArrayList<User> getLimitedMembers() {
        return limitedMembers;
    }

    /**
     * This method is used to access list of unpinned messages in channel
     *@param -
     *@return ArrayList<ChannelMessage> This returns messages
     */
    public ArrayList<ChannelMessage> getMessages() {
        return messages;
    }

    /**
     * This method is used to access list of pinned messages in channel
     *@param -
     *@return ArrayList<ChannelMessage> This returns pinedMessages
     */
    public ArrayList<ChannelMessage> getPinedMessages() {
        return pinedMessages;
    }

    /**
     * This method is used to access list of pinned and unpinned messages in channel
     *@param -
     *@return ArrayList<ChannelMessage> This returns allMessages
     */
    public ArrayList<ChannelMessage> getAllMessages() {
        return allMessages;
    }

    /**
     * This method is used to adding all messages in channel into a string
     *@param -
     *@return  String channelMessages
     */
    public String channelMessages() {
        String channelMessages = "";
        for(ChannelMessage m : allMessages) {
            channelMessages = channelMessages.concat( m.toString() + "\n");
        }
        return channelMessages;
    }

    /**
     * This method is used to add a new message in channel
     *@param channelMessage
     *@return Nothing
     */

    public void addMessage(ChannelMessage channelMessage) {
        allMessages.add(channelMessage);
    }

}
