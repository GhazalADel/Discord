package discord.discord1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class represents users in program!
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */


public class User implements Serializable {

    //fields
    private String username;
    private String password;
    private String email;
    private String tel;

//    private ArrayList<RequestFriend> requests;
//
//    private ArrayList<User> friends;
//
//    private ArrayList<User> blockedFriends;
//
    private Status userStatus;
    private Status selectedUserStatus;
//    private ArrayList<DiscordServer> servers;
//    private ArrayList<Notification> notifications;
//    private ArrayList<PrivateChat> chats;
//    private PrivateChat currentChat;
//
//    private DiscordServer currentServer;
//    private Channel currentChannel;
//
//    private ArrayList<File> files;
//    private ArrayList<File> requestFiles;

    //constructor
    public User(String username, String password, String email, String tel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = tel;
//        requests = new ArrayList<>();
//        friends=new ArrayList<>();
//        blockedFriends=new ArrayList<>();
//        servers=new ArrayList<>();
//        notifications=new ArrayList<>();
//        chats=new ArrayList<>();
//        files = new ArrayList<>();
//        requestFiles = new ArrayList<>();
    }

    //getter methods

//    /**
//     * This method is used to access notifications ArrayList.
//     *@param -
//     *@return ArrayList<Notification> This returns notifications
//     */
//
//    public ArrayList<Notification> getNotifications() {
//        return notifications;
//    }
//    /**
//     * This method is used to access user's servers.
//     *@param -
//     *@return  ArrayList<DiscordServer>  This returns servers
//     */
//
//    public ArrayList<DiscordServer> getServers() {
//        return servers;
//    }
    /**
     * This method is used to access user's username.
     *@param -
     *@return  String This returns username
     */

    public String getUsername() {
        return username;
    }
    /**
     * This method is used to access user's password.
     *@param -
     *@return String This returns password.
     */

    public String getPassword() {
        return password;
    }
    /**
     * This method is used to access user's email.
     *@param -
     *@return String This returns email.
     */

    public String getEmail() {
        return email;
    }

    /**
     * This method is used to access user's tel.
     *@param -
     *@return String This returns tel.
     */
    public String getTel() {
        return tel;
    }

    /**
     * This method is used to access user's status.
     *@param -
     *@return Status This returns userStatus.
     */

    public Status getUserStatus() {
        return userStatus;
    }

    /**
     * This method is used to access status that user selected before.
     *@param -
     *@return Status This returns selectedUserStatus.
     */

    public Status getSelectedUserStatus() {
        return selectedUserStatus;
    }
//
//    /**
//     * This method is used to access user's Requests that this user has received.
//     *@param -
//     *@return ArrayList<RequestFriend> This returns requests.
//     */
//
//    public ArrayList<RequestFriend> getRequests() {
//        return requests;
//    }
//
//    /**
//     * This method is used to access user's friends.
//     *@param -
//     *@return ArrayList<User> This returns friends.
//     */
//    public ArrayList<User> getFriends() {
//        return friends;
//    }
//
//    /**
//     * This method is used to access user's privateChats.
//     *@param -
//     *@return ArrayList<PrivateChat> This returns chats.
//     */
//    public ArrayList<PrivateChat> getPrivateChats() {
//        return chats;
//    }
//
//    /**
//     * This method is used to access a specific privateChat by receiving an index.
//     *@param index
//     *@return PrivateChat
//     */
//    public PrivateChat getPrivateChat(int index) {
//        return chats.get(index);
//    }
//
//    /**
//     * This method is used to access a privateChat which user working on now.
//     *@param -
//     *@return PrivateChat This returns currentChat
//     */
//    public PrivateChat getCurrentChat() {
//        return currentChat;
//    }
//    /**
//     * This method is used to access a list of blocked friends.
//     *@param -
//     *@return ArrayList<User> This returns blockedFriends
//     */
//
//    public ArrayList<User> getBlockedFriends() {
//        return blockedFriends;
//    }
//    /**
//     * This method is used to access current channel
//     *@param -
//     *@return Channel This returns currentChannel
//     */
//    public Channel getCurrentChannel() {
//        return currentChannel;
//    }
//    /**
//     * This method is used to access current discord server
//     *@param -
//     *@return Channel This returns currentChannel
//     */
//
//    public DiscordServer getCurrentServer() {
//        return currentServer;
//    }
//
//
//    //setter methods
//
    /**
     * This method is used to set user's password.
     *@param password
     *@return Nothing
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method is used to set user's status.
     *@param userStatus
     *@return Nothing
     */

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * This method is used to set user's status.
     *@param selectedUserStatus
     *@return Nothing
     */
    public void setSelectedUserStatus(Status selectedUserStatus) {
        this.selectedUserStatus = selectedUserStatus;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    //
//    /**
//     * This method is used to set user's current chat.
//     *@param currentChat
//     *@return Nothing
//     */
//    public void setCurrentChat(PrivateChat currentChat) {
//        this.currentChat = currentChat;
//    }
//    /**
//     * This method is used to set user's current channel.
//     *@param currentChannel
//     *@return Nothing
//     */
//
//    public void setCurrentChannel(Channel currentChannel) {
//        this.currentChannel = currentChannel;
//    }
//    /**
//     * This method is used to set user's current server.
//     *@param currentServer
//     *@return Nothing
//     */
//
//    public void setCurrentServer(DiscordServer currentServer) {
//        this.currentServer = currentServer;
//    }
//
//
//    /**
//     * This method is used to get a specific private chat by receiving chatName.
//     *@param chatName
//     *@return PrivateChat
//     */
//
//    public PrivateChat gePrivateChat(String chatName) {
//        PrivateChat chat = null;
//        for(PrivateChat c : chats) {
//            if(c.getChatName().equals(chatName)) {
//                chat = c;
//            }
//        }
//        return chat;
//    }
//
//
//    /**
//     * This method is used to add a message to specific privateChat.
//     *@param privateChat
//     *@param message
//     *@return Nothing
//     */
//
//    public void addMessage(PrivateChat privateChat, PrivateMessage message) {
//        for(PrivateChat pv : chats) {
//            if(pv.equals(privateChat)) {
//                pv.addMessage(message);
//            }
//        }
//    }
//
//    /**
//     * This method is used to check that user is in private chat or not by receiving an username
//     *@param username
//     *@return boolean This returns true or false
//     */
//    public boolean isInThisPrivateChat(String username){
//        PrivateChat privateChat = doesPrivateChatExist(username);
//        if(privateChat != null && privateChat == currentChat) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * This method is used to check that a specific private chat exists or not by receiving an username .
//     * is specific private chat exists return it.
//     * if user doesn't have this private chat,  returns null.
//     *@param username
//     *@return PrivateChat
//     */
//    public PrivateChat doesPrivateChatExist(String username){
//        for (PrivateChat privateChat : chats) {
//            if (privateChat.getUser2().getUsername().equals(username))
//                return privateChat;
//        }
//        return null;
//    }
//
//    /**
//     * This method is used to a message to channel messages
//     *@param discordServer
//     *@param channel
//     *@param message
//     *@return Nothing
//     */
//
//    public void addMessage(DiscordServer discordServer, Channel channel, ChannelMessage message) {
//        for(DiscordServer d : servers) {
//            if(d == discordServer) {
//                for(Channel c : d.getChannels()) {
//                    if(c == channel) {
//                        c.getAllMessages().add(message);
//                    }
//                }
//            }
//        }
//    }
//
//    public void addFiles(File file) {
//        files.add(file);
//    }
//
//    public ArrayList<File> getFiles() {
//        return files;
//    }
//
//    public void addRequestFile(File file) {
//        requestFiles.add(file);
//    }
//
//    public void removeRequestFile(File file) {
//        requestFiles.remove(file);
//    }
//
//    public ArrayList<File> getRequestFiles() {
//        return requestFiles;
//    }
}
