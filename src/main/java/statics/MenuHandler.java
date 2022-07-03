package statics;

/**
 * MenuHandler class contains six static methods for print static menus in program.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class MenuHandler {

    /**
     * This method is used to print first menu in program.
     */
    public static void firstMenu() {
        System.out.println("1.Sign up");
        System.out.println("2.Log in");
        System.out.println("3.Exit");
    }


    /**
     * This method is used to print menu for logged users.
     */
    public static void loginMenu() {
        System.out.println("1.Chats");
        System.out.println("2.Received Requests");
        System.out.println("3.Send Request");
        System.out.println("4.List Of Friends");
        System.out.println("5.Create a New Server");
        System.out.println("6.Change Status");
        System.out.println("7.Block a friend");
        System.out.println("8.See blocked friends");
        System.out.println("9.change password");
        System.out.println("10.servers settings");
        System.out.println("11.Select a picture");
        System.out.println("12.See Received Files");
        System.out.println("13.See Saved Files");
        System.out.println("0.Log out");
    }


    /**
     * This method is used to print menu for logged users.
     */
    public static void chats() {
        System.out.println("1.PVs");
        System.out.println("2.Servers");
        System.out.println("3.Send File In Chat");
        System.out.println("0.Back");
    }


    /**
     * This method is used to print menu for admin(owner) of server in discord servers.
     */
    public static void adminMenu() {
        System.out.println("1.Change server Name");
        System.out.println("2.Add a member/role");
        System.out.println("3.remove a member");
        System.out.println("4.Limit a Member To Send Message");
        System.out.println("5.Ban a member");
        System.out.println("6.List of Members");
        System.out.println("7.Create a Channel");
        System.out.println("8.remove a channel");
        System.out.println("9.Change role permission");
        System.out.println("10.Remove server");
        System.out.println("11.show chat history");
        System.out.println("12.show limited members list");
        System.out.println("13.show banned members list");
        System.out.println("14.show channels list");
        System.out.println("0.Back");
    }


    /**
     * This method is used to print list of permissions when admin want to add a new role in server.
     */
    public static void permissionList() {
        System.out.println("1.Create Channel");
        System.out.println("2.Remove Channel");
        System.out.println("3.Remove a Member From Server");
        System.out.println("4.Ban a Member");
        System.out.println("5.Limit A Member To Send Message");
        System.out.println("6.Change  server  Name");
        System.out.println("7.Chat History");
        System.out.println("8.Pin Message");
        System.out.println("0.Back");
    }


    /**
     * This method is used to print a menu for server's users when they select a channel in server.
     */
    public static void channelMenu(){
        System.out.println("1.enter channel");
        System.out.println("2.show pinned messages");
        System.out.println("3.pin a message");
        System.out.println("4.show reactions");
        System.out.println("5.send reaction to a message");
        System.out.println("6.send a file");
    }


}