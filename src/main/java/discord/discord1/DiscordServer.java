package discord.discord1;



import java.io.Serializable;
import java.util.ArrayList;


/**
 * DiscordServer class represents a server in program
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class DiscordServer implements Serializable {

    //fields
    private String name;
    private User serverAdmin;
    private ArrayList<Role> serverRoles;
    ArrayList<User> bannedUsers;
    private ArrayList<Channel> channels;

    //a static array of permission
    //it uses for print a specific role's permissions
    public static final Permission[] allPermissions= {Permission.CREATE_CHANNEL,Permission.REMOVE_CHANNEL,
            Permission.REMOVE_MEMBER_FROM_SERVER,Permission.BAN_A_MEMBER,Permission.LIMIT_MEMBER_MESSAGE,Permission.CHANGING_SERVER_NAME,
            Permission.SEE_CHAT_HISTORY,Permission.PIN_MESSAGE};


    //constructor
    public DiscordServer(String name) {
        this.name=name;
        serverRoles =new ArrayList<>();
        channels=new ArrayList<>();
        bannedUsers=new ArrayList<>();
    }

    //getter methods

    /**
     * This method is used to access list of banned users in server
     *@param -
     *@return ArrayList<User> This returns bannedUsers
     */

    public ArrayList<User> getBannedUsers() {
        return bannedUsers;
    }

    /**
     * This method is used to access server's name
     *@param -
     *@return String This returns name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to access server's admin
     *@param -
     *@return User This returns serverAdmin
     */
    public User getServerAdmin() {
        return serverAdmin;
    }

    /**
     * This method is used to access a list of roles in server
     *@param -
     *@return ArrayList<Role> This returns serverRoles
     */
    public ArrayList<Role> getServerRoles() {
        return serverRoles;
    }

    /**
     * This method is used to access a list of channels in server
     *@param -
     *@return ArrayList<Channel> This returns channels
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }

    //setter methods

    /**
     * This method is used to set server's name
     *@param name
     *@return Nothing
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is used to set server's admin
     *@param serverAdmin
     *@return Nothing
     */
    public void setServerAdmin(User serverAdmin) {
        this.serverAdmin = serverAdmin;
    }

}
