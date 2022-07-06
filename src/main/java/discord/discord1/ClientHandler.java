package discord.discord1;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ClientHandler class represents server in program
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class ClientHandler implements Runnable {

    //fields
    public static ArrayList<ClientHandler> clientHandlers=new ArrayList<>();
//    public static ArrayList<DiscordServer> discordServers=new ArrayList<>();
    public static ArrayList<User> users=new ArrayList<>();
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private static User user;
    private static File usersFile=new File("./Files/userFiles.txt");

    private int counter = 0;
    private int imageCount=0;

    //constructor
    public ClientHandler(Socket socket) {
        this.socket=socket;
        try {
            this.objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.objectInputStream=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientHandlers.add(this);
    }
    public ClientHandler(User user){
        this.user = user;
    }

    /**
     * This method used to get current user
     *
     * *@param users
     * *@return Nothing
     */

    public User getUser() {
        return user;
    }
    /**
     * This method used to set current user
     *
     * *@param users
     * *@return Nothing
     */

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * This method used to set list of users in program
     *
     * *@param users
     * *@return Nothing
     */
    public static void setUsers(ArrayList<User> users) {
        ClientHandler.users = users;
    }

    /**
     * This method used to read data form file when program starts
     *
     * *@param -
     * *@return ArrayList<User>
     */
//    public static ArrayList<User> readUsersFile(){
//        ArrayList<User> us=new ArrayList<>();
////        for (User uu:us){
////            uu.setUserStatus(Status.OFFLINE);
////        }
//        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(usersFile))){
//            us=(ArrayList<User>) ois.readObject();
//        }
//        catch (EOFException ignored){
//            //nothing
//        }
//        catch (IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//        clientHandlers = new ArrayList<>();
//        for (User user : us) {
//            clientHandlers.add(new ClientHandler(user));
//        }
//        return us;
//    }

    /**
     * run method for thread
     *
     * *@param -
     * *@return ArrayList<User>
     */

    @Override
    public void run() {
        Request command;
        while (socket.isConnected()) {
            try {
                command = (Request) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            //check entered username is duplicated or not
            if (command.getCode() == RequestCode.CHECK_USERNAME_DUPLICATION) {
                String username = (String) command.getData("username");
                Boolean isDuplicate = checkDuplication(username);
                Response response = new Response(ResponseCode.Is_USERNAME_DUPLICATED);
                response.addData("IsDuplicated", isDuplicate);
                try {
                    objectOutputStream.writeObject(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //add a new user in program
            else if (command.getCode() == RequestCode.ADD_USER) {
                User u = (User) command.getData("user");
                user = u;
//                user.setUserStatus(Status.ONLINE);
                users.add(user);
                writeUsersFile();
            }
            //find user based on username
            else if (command.getCode() == RequestCode.FIND_USER_BY_USERNAME) {
                String username = (String) command.getData("username");
                User wanted = findUser(username);
                Response response = new Response(ResponseCode.IS_EXISTS);
                response.addData("user", wanted);
                try {
                    objectOutputStream.writeObject(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //log out
            else if(command.getCode()==RequestCode.LOG_OUT){
                int userIndex=0;
                for (User uu:users){
                    if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
                        break;
                    }
                    userIndex++;
                }
                users.get(userIndex).setUserStatus(Status.OFFLINE);
                user=null;
                writeUsersFile();
            }
            //check is entered password true or not
            else if(command.getCode()==RequestCode.CHECK_PASSWORD){
                String password= (String) command.getData("password");
                boolean isValid=false;
                if(password.equalsIgnoreCase(user.getPassword())){
                    isValid=true;
                }
                Response response=new Response(ResponseCode.SUCCESSFUL);
                response.addData("valid",isValid);
                try {
                    objectOutputStream.writeObject(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //change user's password
            else if(command.getCode()==RequestCode.CHANGE_PASSWORD){
                String newPassword= (String) command.getData("password");
                user.setPassword(newPassword);
                int userIndex=0;
                for (User uu:users){
                    if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
                        break;
                    }
                    userIndex++;
                }
                users.get(userIndex).setPassword(newPassword);
            }
            else if(command.getCode()==RequestCode.GET_PHONE_NUMBER){
                int userIndex=0;
                for (User uu:users){
                    if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
                        break;
                    }
                    userIndex++;
                }
                String phoneNumber="";
                if(users.size()!=0){
                    phoneNumber=users.get(userIndex).getTel();
                }
                Response response=new Response(ResponseCode.SUCCESSFUL);
                response.addData("phone",phoneNumber);
                try {
                    objectOutputStream.writeObject(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            else if(command.getCode()==RequestCode.REMOVE_PHONE_NUMBER){
                int userIndex=0;
                for (User uu:users){
                    if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
                        break;
                    }
                    userIndex++;
                }
                users.get(userIndex).setTel("");
            }
            else if(command.getCode()==RequestCode.CHANGE_PHONE_NUMBER){
                int userIndex=0;
                for (User uu:users){
                    if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
                        break;
                    }
                    userIndex++;
                }
                String phone= (String) command.getData("phone");
                users.get(userIndex).setTel(phone);
            }



//            //add request friend
//            else if(command.getCode()==RequestCode.ADD_REQUEST){
//                RequestFriend requestFriend= (RequestFriend) command.getData("request");
//                String receiverUsername=requestFriend.getReceiverName();
//                for (User user1:users){
//                    if(user1.getUsername().equals(receiverUsername)){
//                        user1.getRequests().add(requestFriend);
//                        break;
//                    }
//                }
//            }
//            //add a friend
//            else if(command.getCode()==RequestCode.ADD_FRIEND){
//                String user1Username= (String) command.getData("username");
//                String newFriendUsername= (String) command.getData("newFriend");
//                User newFriend=findUser(newFriendUsername);
//                for (User user1:users){
//                    if(user1.getUsername().equalsIgnoreCase(user1Username)){
//                        user1.getFriends().add(newFriend);
//                        break;
//                    }
//                }
//            }
//            //see received friend requests
//            else if(command.getCode()==RequestCode.SEE_REQUEST){
//                String requests="";
//                if(user.getRequests().size()!=0) {
//                    for (RequestFriend request : user.getRequests()) {
//                        requests += request.getSenderName();
//                        requests += "@@@";
//                    }
//                    requests=requests.substring(0,requests.length()-3);
//                }
//                Response response=new Response(ResponseCode.SHOW_REQUEST);
//                response.addData("requests",requests);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //accept a received request friend
//            else if(command.getCode()==RequestCode.ACCEPT_REQUEST_FRIEND){
//                int requestIndex= (int) command.getData("index");
//                RequestFriend requestFriend=user.getRequests().get(requestIndex);
//                user.getRequests().remove(requestIndex);
//                User friend=requestFriend.getSender();
//                user.getFriends().add(friend);
//                friend.getFriends().add(user);
//
//            }
//            //send a request friend to someone
//            else if(command.getCode()==RequestCode.SEND_REQUEST) {
//                String username = (String) command.getData("username");
//                User u = findUser(username);
//                Response response = null;
//                if (username.equalsIgnoreCase(user.getUsername())) {
//                    response = new Response(ResponseCode.OWN_REQUEST);
//                }
//                else {
//                    if (u == null) {
//                        response = new Response(ResponseCode.FRIEND_NOT_FOUND);
//                    }
//                    else {
//                        boolean isFriend = false;
//                        for (User userss : user.getFriends()) {
//                            if (userss.getUsername().equalsIgnoreCase(u.getUsername())) {
//                                isFriend = true;
//                                break;
//                            }
//                        }
//                        if (isFriend) {
//                            response = new Response(ResponseCode.BEFORE_FRIEND);
//                        } else {
//                            boolean beforeRequest = false;
//                            for (RequestFriend r : u.getRequests()) {
//                                if ((r.getSender().getUsername().equalsIgnoreCase(user.getUsername()) && r.getReceiver().getUsername().equalsIgnoreCase(u.getUsername()))) {
//                                    beforeRequest = true;
//                                    break;
//                                }
//                            }
//                            if (beforeRequest) {
//                                response = new Response(ResponseCode.BEFORE_REQUEST);
//                            }
//                            else {
//                                boolean againRequest=false;
//                                for (RequestFriend r : user.getRequests()) {
//                                    if(r.getSender().getUsername().equalsIgnoreCase(u.getUsername())){
//                                        againRequest=true;
//                                        break;
//                                    }
//                                }
//                                if(againRequest){
//                                    response=new Response(ResponseCode.REQUEST_AGAIN);
//                                }
//                                else{
//                                    response = new Response(ResponseCode.REQUEST_OK);
//                                    RequestFriend requestFriend = new RequestFriend(user, u);
//                                    u.getRequests().add(requestFriend);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //see list of friends
//            else if(command.getCode()==RequestCode.SEE_FRIENDS){
//                String friends="";
//                if(user.getFriends().size()!=0) {
//                    for (User u : user.getFriends()) {
//                        friends += u.getUsername();
//                        friends+=" ";
//                        friends+=u.getUserStatus();
//                        friends += "@@@";
//                    }
//                    friends = friends.substring(0, friends.length() - 3);
//                }
//                Response response=new Response(ResponseCode.SHOW_FRIENDS);
//                response.addData("friends",friends);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //change user's status
//            else if(command.getCode()==RequestCode.CHANGE_STATUS){
//                int statusNum= (int) command.getData("status");
//                switch (statusNum){
//                    case 1:
//                        user.setSelectedUserStatus(Status.ONLINE);
//                        user.setUserStatus(user.getSelectedUserStatus());
//                        break;
//                    case 2:
//                        user.setSelectedUserStatus(Status.IDLE);
//                        user.setUserStatus(user.getSelectedUserStatus());
//                        break;
//                    case 3:
//                        user.setSelectedUserStatus(Status.DO_NOT_DISTURB);
//                        user.setUserStatus(user.getSelectedUserStatus());
//                        break;
//                    default:
//                        user.setSelectedUserStatus(Status.INVISIBLE);
//                        user.setUserStatus(user.getSelectedUserStatus());
//                }
//            }
//            //block a friend
//            else if(command.getCode()==RequestCode.BLOCK_FRIEND){
//                int blockIndex= (int) command.getData("index");
//                User blockedUser=user.getFriends().get(blockIndex);
//                user.getBlockedFriends().add(blockedUser);
//                user.getFriends().remove(blockIndex);
//            }
//            //exit
//            else if(command.getCode()==RequestCode.EXIT){
//                int userIndex=0;
//                for (User uu:users){
//                    if(uu.getUsername().equalsIgnoreCase(user.getUsername()) && user!=null){
//                        break;
//                    }
//                    userIndex++;
//                }
//                if(user!=null){
//                    users.get(userIndex).setUserStatus(Status.OFFLINE);
//                    user=null;
//                    writeUsersFile();
//                }
//            }
//
//            //log in
//            else if(command.getCode()==RequestCode.LOG_IN){
//                User u= (User) command.getData("user");
//                int userIndex=0;
//                for(User uu:users){
//                    if(uu.getUsername().equalsIgnoreCase(u.getUsername())){
//                        break;
//                    }
//                    userIndex++;
//                }
//
//                if (users.get(userIndex).getSelectedUserStatus() != null) {
//                    users.get(userIndex).setUserStatus(users.get(userIndex).getSelectedUserStatus());
//                } else {
//                    users.get(userIndex).setUserStatus(Status.ONLINE);
//                }
//                user=users.get(userIndex);
//            }
//            else if(command.getCode()==RequestCode.OFFLINE_ACTIVE_USER){
//                int userIndex=0;
//                for(User uu:users){
//                    if(uu.getUsername().equalsIgnoreCase(user.getUsername()) && user!=null){
//                        break;
//                    }
//                    userIndex++;
//                }
//                users.get(userIndex).setUserStatus(Status.OFFLINE);
//                user=null;
//                writeUsersFile();
//            }
////            else if(command.getCode()==RequestCode.LOG_IN_ONCE){
////                String username= (String) command.getData("username");
////                boolean logInOnce=logInOnce(username);
////                Response response=new Response(ResponseCode.SUCCESSFUL);
////                response.addData("once",logInOnce);
////                try {
////                    objectOutputStream.writeObject(response);
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
////            }
//
//            //see blocked friends
//            else if(command.getCode()==RequestCode.SEE_BLOCKED_FRIEND){
//                String blocks="";
//                if(user.getBlockedFriends().size()!=0) {
//                    for (User u : user.getBlockedFriends()) {
//                        blocks += u.getUsername();
//                        blocks += "@@@";
//                    }
//                    int length = blocks.length();
//                    blocks = blocks.substring(0, length - 3);
//                }
//                Response response=new Response(ResponseCode.SHOW_BLOCKED_FRIEND);
//                response.addData("blocks",blocks);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //unblock a friend
//            else if(command.getCode()==RequestCode.UNBLOCK_FRIEND){
//                int index= (int) command.getData("index");
//                User unblockFriend=user.getBlockedFriends().get(index);
//                user.getFriends().add(unblockFriend);
//                user.getBlockedFriends().remove(index);
//            }
//            //add a new server in program
//            else if(command.getCode()==RequestCode.ADD_SERVER){
//                String serverName= (String) command.getData("serverName");
//                DiscordServer discordServer=new DiscordServer(serverName);
//                discordServer.setServerAdmin(user);
//                Role role=new Role("normal");
//                discordServer.getServerRoles().add(role);
//                user.getServers().add(discordServer);
//                discordServers.add(discordServer);
//            }
//            //show list of servers
//            else if(command.getCode()==RequestCode.SHOW_SERVERS){
//                String serverss="";
//                if(user.getServers().size()!=0) {
//                    for (DiscordServer d : user.getServers()) {
//                        serverss += d.getName();
//                        serverss += "@@@";
//                    }
//                    int length = serverss.length();
//                    serverss = serverss.substring(0, length - 3);
//                }
//                String notifications="";
//                if(user.getNotifications().size()!=0){
//                    for (Notification n:user.getNotifications()){
//                        notifications+=n.toString();
//                        notifications+="@@@";
//                    }
//                    int length=notifications.length();
//                    notifications=notifications.substring(0,length-3);
//                    user.getNotifications().clear();
//                }
//
//                Response response=new Response(ResponseCode.SHOW_SERVERS);
//                response.addData("servers",serverss);
//                response.addData("notifications",notifications);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //show is user server's admin or not
//            else if(command.getCode()==RequestCode.IS_SERVER_ADMIN){
//                int index= (int) command.getData("index");
//                boolean isAdmin=false;
//                if(user.getServers().get(index).getServerAdmin().getUsername().equalsIgnoreCase(user.getUsername())){
//                    isAdmin=true;
//                }
//                Response response=new Response(ResponseCode.IS_SERVER_ADMIN);
//                response.addData("isAdmin",isAdmin);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //change server's name
//            else if(command.getCode()==RequestCode.CHANGE_SERVER_NAME){
//                int index= (int) command.getData("index");
//                String name= (String) command.getData("name");
//                Response response;
//                if (name.equalsIgnoreCase(user.getServers().get(index).getName())) {
//                    response=new Response(ResponseCode.NOT_CHANGE);
//                }
//                else {
//                    Boolean isDuplicate=false;
//                    for (DiscordServer discordServer:discordServers) {
//                        if(discordServer.getName().equalsIgnoreCase(name)){
//                            isDuplicate=true;
//                            break;
//                        }
//                    }
//                    if(isDuplicate){
//                        response=new Response(ResponseCode.DUPLICATED);
//                    }
//                    else {
//                        user.getServers().get(index).setName(name);
//                        response=new Response(ResponseCode.NAME_CHANGED);
//                    }
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //check is entered server's name duplicated
//            else if(command.getCode()==RequestCode.CHECK_SERVER_NAME_DUPLICATION){
//                String serverName= (String) command.getData("serverName");
//                boolean isDuplicated=false;
//                for (DiscordServer d:discordServers) {
//                    if(d.getName().equalsIgnoreCase(serverName)){
//                        isDuplicated=true;
//                        break;
//                    }
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("IsDuplicated",isDuplicated);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //check is role exist or not
//            else if(command.getCode()==RequestCode.CHECK_ROLE){
//                String name= (String) command.getData("name");
//                int index= (int) command.getData("index");
//                boolean isExist=false;
//                for (Role r:user.getServers().get(index).getServerRoles()){
//                    if(r.getName().equalsIgnoreCase(name)){
//                        isExist=true;
//                        break;
//                    }
//                }
//
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("exist",isExist);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //assign a role to a user
//            else if(command.getCode()==RequestCode.ASSIGN_ROLE){
//                String username= (String) command.getData("username");
//                int index= (int) command.getData("index");
//                String roleName= (String) command.getData("roleName");
//                Response response;
//                User u=findUser(username);
//                if(u==null){
//                    response=new Response(ResponseCode.NOT_EXIST);
//                }
//                else{
//                    boolean beforeInRole=false;
//                    for (Role role:user.getServers().get(index).getServerRoles()){
//                        if(role.getName().equalsIgnoreCase(roleName)){
//                            for (User user1:role.getUsers()){
//                                if(user1.getUsername().equalsIgnoreCase(username)){
//                                    beforeInRole=true;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (beforeInRole) {
//                        response=new Response(ResponseCode.BEFORE_IN_ROLE);
//                    }
//                    else{
//                        int roleIndex=-1;
//                        for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                            if(user.getServers().get(index).getServerRoles().get(i).getName().equalsIgnoreCase(roleName)){
//                                roleIndex=i;
//                                break;
//                            }
//                        }
//                        int userIndex=-1;
//                        for (int i = 0; i < users.size(); i++) {
//                            if(users.get(i).getUsername().equalsIgnoreCase(username)){
//                                userIndex=i;
//                                break;
//                            }
//                        }
//                        user.getServers().get(index).getServerRoles().get(roleIndex).getUsers().add(users.get(userIndex));
//                        boolean haveServer=false;
//                        for (DiscordServer d: users.get(userIndex).getServers()){
//                            if(d.getName().equalsIgnoreCase(user.getServers().get(index).getName())){
//                                haveServer=true;
//                                break;
//                            }
//                        }
//                        if(!haveServer){
//                            users.get(userIndex).getServers().add(user.getServers().get(index));
//                            Notification notification=new Notification("welcome to "+ user.getServers().get(index).getName()+" server!", LocalDateTime.now());
//                            users.get(userIndex).getNotifications().add(notification);
//                        }
//                        response=new Response(ResponseCode.ROLE_ASSIGNED);
//                    }
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //add a new role with selected permissions
//            else if(command.getCode()==RequestCode.ROLL_AND_PERMISSIONS){
//                HashSet<Integer> permissions= (HashSet<Integer>) command.getData("permissions");
//                String roleName= (String) command.getData("roleName");
//                int index= (int) command.getData("index");
//                Role role=new Role(roleName);
//                for (Integer i:permissions) {
//                    role.getPermissionIndexes().add(i);
//                    switch (i){
//                        case 1:
//                            role.getRolePermissions().add(Permission.CREATE_CHANNEL);
//                            break;
//                        case 2:
//                            role.getRolePermissions().add(Permission.REMOVE_CHANNEL);
//                            break;
//                        case 3:
//                            role.getRolePermissions().add(Permission.REMOVE_MEMBER_FROM_SERVER);
//                            break;
//                        case 4:
//                            role.getRolePermissions().add(Permission.BAN_A_MEMBER);
//                            break;
//                        case 5:
//                            role.getRolePermissions().add(Permission.LIMIT_MEMBER_MESSAGE);
//                            break;
//                        case 6:
//                            role.getRolePermissions().add(Permission.CHANGING_SERVER_NAME);
//                            break;
//                        case 7:
//                            role.getRolePermissions().add(Permission.SEE_CHAT_HISTORY);
//                            break;
//                        case 8:
//                            role.getRolePermissions().add(Permission.PIN_MESSAGE);
//                            break;
//
//                    }
//                }
//                user.getServers().get(index).getServerRoles().add(role);
//
//            }
//            //remove a member form server
//            else if(command.getCode()==RequestCode.REMOVE_MEMBER){
//                String username= (String) command.getData("username");
//                int index= (int) command.getData("index");
//                boolean find=false;
//                for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                    for (int j = 0; j < user.getServers().get(index).getServerRoles().get(i).getUsers().size(); j++) {
//                        if(user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getUsername().equalsIgnoreCase(username)){
//                            find=true;
//                            user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getServers().remove(user.getServers().get(index));
//                            user.getServers().get(index).getServerRoles().get(i).getUsers().remove(j);
//                            break;
//                        }
//                    }
//
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("find",find);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            else if(command.getCode()==RequestCode.REMOVE_MEMBER2){
//                Response response=null;
//                String username= (String) command.getData("username");
//                int index= (int) command.getData("index");
//                if(username.equalsIgnoreCase(user.getServers().get(index).getServerAdmin().getUsername())){
//                    response=new Response(ResponseCode.ADMIN);
//                }
//                else{
//                    boolean find=false;
//                    for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                        for (int j = 0; j < user.getServers().get(index).getServerRoles().get(i).getUsers().size(); j++) {
//                            if(user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getUsername().equalsIgnoreCase(username)){
//                                find=true;
//                                user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getServers().remove(user.getServers().get(index));
//                                user.getServers().get(index).getServerRoles().get(i).getUsers().remove(j);
//                                break;
//                            }
//                        }
//                    }
//                    if(!find){
//                        response=new Response(ResponseCode.NOT_EXIST);
//                    }
//                    else {
//                        response=new Response(ResponseCode.SUCCESSFUL);
//                    }
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //returns channel count in server
//            else if(command.getCode()==RequestCode.CHECK_CHANNEL_COUNT){
//                int index= (int) command.getData("index");
//                boolean isZero=false;
//                if(user.getServers().get(index).getChannels().size()==0){
//                    isZero=true;
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("isZero",isZero);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //check is a channel with entered name exist
//            else if(command.getCode()==RequestCode.IS_CHANNEL_EXIST){
//                int index= (int) command.getData("index");
//                String channelName= (String) command.getData("channel");
//                boolean exist=false;
//                for (Channel c:user.getServers().get(index).getChannels()){
//                    if(c.getName().equalsIgnoreCase(channelName)){
//                        exist=true;
//                        break;
//                    }
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("exist",exist);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //limit a member form a channel in server
//            else if(command.getCode()==RequestCode.LIMIT_MEMBER){
//                int index= (int) command.getData("index");
//                String username= (String) command.getData("username");
//                String channelName= (String) command.getData("channel");
//                User wanted=findUser(username);
//                Response response;
//                if(wanted==null){
//                    response=new Response(ResponseCode.NOT_EXIST);
//                }
//                else{
//                    boolean inServer=false;
//                    for (Role role:user.getServers().get(index).getServerRoles()){
//                        for (User user1:role.getUsers()){
//                            if(user1.getUsername().equalsIgnoreCase(username)){
//                                inServer=true;
//                                break;
//                            }
//                        }
//                    }
//                    if(!inServer){
//                        response=new Response(ResponseCode.NOT_IN_SERVER);
//                    }
//                    else{
//                        int channelIndex=-1;
//                        for (int i = 0; i < user.getServers().get(index).getChannels().size(); i++) {
//                            if(user.getServers().get(index).getChannels().get(i).getName().equalsIgnoreCase(channelName)){
//                                channelIndex=i;
//                                break;
//                            }
//                        }
//                        boolean limitBefore=false;
//                        for (User user1:user.getServers().get(index).getChannels().get(channelIndex).getLimitedMembers()){
//                            if(user1.getUsername().equalsIgnoreCase(username)){
//                                limitBefore=true;
//                                break;
//                            }
//                        }
//                        if(limitBefore){
//                            response=new Response(ResponseCode.LIMIT_BEFORE);
//                        }
//                        else{
//                            boolean bannedBefore=false;
//                            for (User uu : user.getServers().get(index).getBannedUsers()) {
//                                if (uu.getUsername().equalsIgnoreCase(username)) {
//                                    bannedBefore = true;
//                                    break;
//                                }
//                            }
//
//                            if(bannedBefore){
//                                response=new Response(ResponseCode.BANNED_BEFORE);
//                            }
//                            else{
//                                response=new Response(ResponseCode.LIMIT_MEMBER);
//                                User limit=findUser(username);
//                                int userIndex=0;
//                                for (User uu:users){
//                                    if(uu.getUsername().equalsIgnoreCase(username)){
//                                        break;
//                                    }
//                                    userIndex++;
//                                }
//                                user.getServers().get(index).getChannels().get(channelIndex).getLimitedMembers().add(users.get(userIndex));
//                            }
//
//                        }
//                    }
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //ban a member form server
//            else if(command.getCode()==RequestCode.BAN_MEMBER){
//                int index= (int) command.getData("index");
//                String username= (String) command.getData("username");
//                User wanted=findUser(username);
//                Response response;
//                if(wanted==null){
//                    response=new Response(ResponseCode.NOT_EXIST);
//                }
//                else{
//                    boolean inServer=false;
//                    for (Role role:user.getServers().get(index).getServerRoles()){
//                        for (User user1:role.getUsers()){
//                            if(user1.getUsername().equalsIgnoreCase(username)){
//                                inServer=true;
//                                break;
//                            }
//                        }
//                    }
//                    if(!inServer){
//                        response=new Response(ResponseCode.NOT_IN_SERVER);
//                    }
//                    else {
//                        boolean bannedBefore = false;
//                        for (User uu : user.getServers().get(index).getBannedUsers()) {
//                            if (uu.getUsername().equalsIgnoreCase(username)) {
//                                bannedBefore = true;
//                                break;
//                            }
//                        }
//                        if (bannedBefore) {
//                            response = new Response(ResponseCode.BANNED_BEFORE);
//                        } else {
//                            response = new Response(ResponseCode.BAN_MEMBER);
//                            int userIndex = -1;
//                            for (int i = 0; i < users.size(); i++) {
//                                if (users.get(i).getUsername().equalsIgnoreCase(username)) {
//                                    userIndex = i;
//                                    break;
//                                }
//                            }
//                            user.getServers().get(index).getBannedUsers().add(users.get(userIndex));
//                        }
//                    }
//
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //see users list in a server
//            else if(command.getCode()==RequestCode.SEE_MEMBERS_LIST){
//                int index= (int) command.getData("index");
//                String members="";
//                HashSet<String> temp=new HashSet<>();
//                members+=user.getServers().get(index).getServerAdmin().getUsername()+" "+user.getServers().get(index).getServerAdmin().getUserStatus();
//                members+="@@@";
//                for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                    for (int j = 0; j < user.getServers().get(index).getServerRoles().get(i).getUsers().size(); j++) {
//                        int sizeBefore= temp.size();
//                        temp.add(user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getUsername());
//                        int sizeAfter=temp.size();
//                        if(sizeBefore!=sizeAfter){
//                            members+=user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getUsername();
//                            members+=" ";
//                            members+=user.getServers().get(index).getServerRoles().get(i).getUsers().get(j).getUserStatus();
//                            members+="@@@";
//                        }
//                    }
//                }
//                members=members.substring(0,members.length()-3);
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("members",members);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //create a new channel in server
//            else if(command.getCode()==RequestCode.CREATE_CHANNEL){
//                String channelName= (String) command.getData("channel");
//                int index= (int) command.getData("index");
//                boolean isDuplicate=false;
//                for (Channel c:user.getServers().get(index).getChannels()){
//                    if(c.getName().equalsIgnoreCase(channelName)){
//                        isDuplicate=true;
//                        break;
//                    }
//                }
//                if(!isDuplicate){
//                    Channel channel=new Channel(channelName);
//                    user.getServers().get(index).getChannels().add(channel);
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("Duplicate",isDuplicate);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //remove a channel form server
//            else if(command.getCode()==RequestCode.REMOVE_CHANNEL){
//                String channelName= (String) command.getData("channel");
//                int index= (int) command.getData("index");
//                boolean isExist=false;
//                for (Channel c:user.getServers().get(index).getChannels()){
//                    if(c.getName().equalsIgnoreCase(channelName)){
//                        isExist=true;
//                        break;
//                    }
//                }
//                if(isExist){
//                    for (int i = 0; i < user.getServers().get(index).getChannels().size(); i++) {
//                        if(user.getServers().get(index).getChannels().get(i).getName().equalsIgnoreCase(channelName)){
//                            user.getServers().get(index).getChannels().remove(i);
//                        }
//                    }
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("exist",isExist);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //remove a server from program
//            else if(command.getCode()==RequestCode.REMOVE_SERVER){
//                int index= (int) command.getData("index");
//                DiscordServer d=user.getServers().get(index);
//                int totalIndex=0;
//                for (DiscordServer dd:discordServers){
//                    if(dd.getName().equalsIgnoreCase(d.getName())){
//                        break;
//                    }
//                    totalIndex++;
//                }
//                for (int i = 0; i < users.size(); i++) {
//                    for (int j = 0; j < users.get(i).getServers().size(); j++) {
//                        if(users.get(i).getServers().get(j).getName().equalsIgnoreCase(d.getName())){
//                            users.get(i).getServers().remove(j);
//                        }
//                    }
//                }
//                discordServers.remove(totalIndex);
//            }
//            //see list of limited users in server
//            else if(command.getCode()==RequestCode.SEE_LIMITED_MEMBERS){
//                int index= (int) command.getData("index");
//                String limited="";
//                for (Channel c:user.getServers().get(index).getChannels()){
//                    limited+=c.getName();
//                    limited+=": ";
//                    for (User uu:c.getLimitedMembers()){
//                        limited+=uu.getUsername();
//                        limited+=" ";
//                    }
//                    limited+="@@@";
//                }
//                if(user.getServers().get(index).getChannels().size()!=0){
//                    limited=limited.substring(0,limited.length()-3);
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("limited",limited);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see list of banned users in server
//            else if(command.getCode()==RequestCode.SEE_BANNED_MEMBERS){
//                int index= (int) command.getData("index");
//                String banned="";
//                if(user.getServers().get(index).getBannedUsers().size()!=0) {
//                    for (User uu : user.getServers().get(index).getBannedUsers()) {
//                        banned += (uu.getUsername());
//                        banned += "@@@";
//                    }
//                }
//                if(user.getServers().get(index).getBannedUsers().size()!=0) {
//                    banned=banned.substring(0,banned.length()-3);
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("banned",banned);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //returns all user's permissions
//            else if(command.getCode()==RequestCode.ROLE_PERMISSIONS){
//                int index= (int) command.getData("index");
//                String roleName= (String) command.getData("name");
//                HashSet<Integer> permissionIndexes=new HashSet<>();
//                for (Role role:user.getServers().get(index).getServerRoles()){
//                    if(role.getName().equalsIgnoreCase(roleName)){
//                        ArrayList<Integer> arr=role.getPermissionIndexes();
//                        for (Integer i:arr){
//                            permissionIndexes.add(i);
//                        }
//                    }
//                }
//                ArrayList<Integer> finalPermissionIndexes=new ArrayList<>();
//                for (Integer i:permissionIndexes){
//                    finalPermissionIndexes.add(i);
//                }
//                Collections.sort(finalPermissionIndexes);
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("permissions",finalPermissionIndexes);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //add some permissions for a role
//            else if(command.getCode()==RequestCode.CHANGE_PERMISSION_ADD){
//                int index= (int) command.getData("index");
//                String roleName= (String) command.getData("name");
//                HashSet<Integer> tmpPermission= (HashSet<Integer>) command.getData("tmpPermission");
//                ArrayList<Integer> otherPermissionsIndexes= (ArrayList<Integer>) command.getData("otherPermissions");
//                int roleIndex=-1;
//                for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                    if(roleName.equalsIgnoreCase(user.getServers().get(index).getServerRoles().get(i).getName())){
//                        roleIndex=i;
//                        break;
//                    }
//                }
//                for (Integer in:tmpPermission) {
//                    int permission=otherPermissionsIndexes.get(in-1);
//                    user.getServers().get(index).getServerRoles().get(roleIndex).getPermissionIndexes().add(permission);
//                    user.getServers().get(index).getServerRoles().get(roleIndex).getRolePermissions().add(DiscordServer.allPermissions[permission-1]);
//                }
//            }
//            //remove some permissions for a role
//            else if(command.getCode()==RequestCode.CHANGE_PERMISSION_REMOVE){
//                int index= (int) command.getData("index");
//                String roleName= (String) command.getData("name");
//                HashSet<Integer> tmpPermission= (HashSet<Integer>) command.getData("tmpPermission");
//                int roleIndex=-1;
//                for (int i = 0; i < user.getServers().get(index).getServerRoles().size(); i++) {
//                    if(roleName.equalsIgnoreCase(user.getServers().get(index).getServerRoles().get(i).getName())){
//                        roleIndex=i;
//                        break;
//                    }
//                }
//                int length=user.getServers().get(index).getServerRoles().get(roleIndex).getPermissionIndexes().size();
//                int[] arr=new int[length];
//                int j=0;
//                for (Integer i:user.getServers().get(index).getServerRoles().get(roleIndex).getPermissionIndexes()){
//                    arr[j]=i;
//                    j++;
//                }
//                for (Integer in:tmpPermission) {
//                    arr[in-1]=-1;
//                }
//                ArrayList<Integer> newPermissionsIndexes=new ArrayList<>();
//                for (int i = 0; i < arr.length; i++) {
//                    if(arr[i]!=-1){
//                        newPermissionsIndexes.add(arr[i]);
//                    }
//                }
//                user.getServers().get(index).getServerRoles().get(roleIndex).setPermissionIndexes(newPermissionsIndexes);
//                ArrayList<Permission> newRolePermissions=new ArrayList<>();
//                for (Integer i:newPermissionsIndexes) {
//                    switch (i){
//                        case 1:
//                            newRolePermissions.add(Permission.CREATE_CHANNEL);
//                            break;
//                        case 2:
//                            newRolePermissions.add(Permission.REMOVE_CHANNEL);
//                            break;
//                        case 3:
//                            newRolePermissions.add(Permission.REMOVE_MEMBER_FROM_SERVER);
//                            break;
//                        case 4:
//                            newRolePermissions.add(Permission.BAN_A_MEMBER);
//                            break;
//                        case 5:
//                            newRolePermissions.add(Permission.LIMIT_MEMBER_MESSAGE);
//                            break;
//                        case 6:
//                            newRolePermissions.add(Permission.CHANGING_SERVER_NAME);
//                            break;
//                        case 7:
//                            newRolePermissions.add(Permission.SEE_CHAT_HISTORY);
//                            break;
//                        case 8:
//                            newRolePermissions.add(Permission.PIN_MESSAGE);
//                            break;
//                    }
//                }
//                user.getServers().get(index).getServerRoles().get(roleIndex).setRolePermissions(newRolePermissions);
//            }
//
//            else if(command.getCode()==RequestCode.GET_PERMISSIONS){
//                int index= (int) command.getData("index");
//                HashSet<Integer> permissionIndexes=new HashSet<>();
//                for (Role role:user.getServers().get(index).getServerRoles()){
//                    for (User uu:role.getUsers()){
//                        if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
//                            ArrayList<Integer> arr=role.getPermissionIndexes();
//                            for (Integer i:arr){
//                                permissionIndexes.add(i);
//                            }
//                        }
//                    }
//                }
//                ArrayList<Integer> finalPermissionIndexes=new ArrayList<>();
//                for (Integer i:permissionIndexes){
//                    finalPermissionIndexes.add(i);
//                }
//                Collections.sort(finalPermissionIndexes);
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("permissions",finalPermissionIndexes);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //add a member to server
//            else if(command.getCode()==RequestCode.ADD_MEMBER){
//                String username= (String) command.getData("username");
//                int index= (int) command.getData("index");
//                Response response=null;
//                User wanted=findUser(username);
//                if(wanted==null){
//                    response=new Response(ResponseCode.NOT_EXIST);
//                }
//                else{
//                    boolean inServer=false;
//                    if(username.equalsIgnoreCase(user.getServers().get(index).getServerAdmin().getUsername())){
//                        inServer=true;
//                    }
//                    for (Role role:user.getServers().get(index).getServerRoles()){
//                        for (User uu:role.getUsers()){
//                            if(uu.getUsername().equalsIgnoreCase(username)){
//                                inServer=true;
//                                break;
//                            }
//                        }
//                    }
//                    if(inServer){
//                        response=new Response(ResponseCode.IS_EXISTS);
//                    }
//                    else{
//
//                        int userIndex=-1;
//                        for (int i = 0; i < users.size(); i++) {
//                            if(users.get(i).getUsername().equalsIgnoreCase(username)){
//                                userIndex=i;
//                            }
//                        }
//                        users.get(userIndex).getServers().add(user.getServers().get(index));
//                        Notification notification=new Notification("welcome to "+ user.getServers().get(index).getName()+" server!", LocalDateTime.now());
//                        users.get(userIndex).getNotifications().add(notification);
//                        for (Role role:user.getServers().get(index).getServerRoles()){
//                            if(role.getName().equals("normal")){
//                                role.getUsers().add(users.get(userIndex));
//                            }
//                        }
//                        response=new Response(ResponseCode.SUCCESSFUL);
//                    }
//
//                }
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see list of channels in a server
//            else if(command.getCode()==RequestCode.SEE_CHANNELS){
//                int index= (int) command.getData("index");
//                String channels="";
//                if(user.getServers().get(index).getChannels().size()!=0) {
//                    for (Channel c : user.getServers().get(index).getChannels()) {
//                        channels += c.getChannelName();
//                        channels += "@@@";
//                    }
//                    channels = channels.substring(0, channels.length() - 3);
//                }
//                Response response=new Response(ResponseCode.SHOW_CHANNELS);
//                response.addData("channels",channels);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //select a picture for profile
//            else if(command.getCode()==RequestCode.SEND_PICTURE){
//                byte[] content = (byte[]) command.getData("file");
//                String path = (String) command.getData(".");
//                File f = new File("./Images/" + (++imageCount) + "image_"+user.getUsername()+ path);
//                try {
//                    Files.write(f.toPath(), content);
//                }
//                catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see pinned messages in a channel in server
//            else if(command.getCode()==RequestCode.SEE_PINNED_MESSAGE){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                String pin="";
//                if(user.getServers().get(serverIndex).getChannels().get(channelIndex).getPinedMessages().size()!=0){
//                    for(ChannelMessage c:user.getServers().get(serverIndex).getChannels().get(channelIndex).getPinedMessages()){
//                        pin+=c.toString();
//                        pin+="@@@";
//                    }
//                    pin=pin.substring(0,pin.length()-3);
//                }
//                Response response=new Response(ResponseCode.SHOW_PINNED_MESSAGES);
//                response.addData("pin",pin);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see unpinned messages
//            else if(command.getCode()==RequestCode.SHOW_MESSAGES){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                String message="";
//                if(user.getServers().get(serverIndex).getChannels().get(channelIndex).getMessages().size()!=0) {
//                    for (ChannelMessage c : user.getServers().get(serverIndex).getChannels().get(channelIndex).getMessages()) {
//                        message += c.toString();
//                        message += "@@@";
//                    }
//                    message = message.substring(0, message.length() - 3);
//                }
//                Response response=new Response(ResponseCode.SHOW_MESSAGES);
//                response.addData("message",message);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //it checks that is user have permission to pin a message
//            else if(command.getCode()==RequestCode.CAN_PIN){
//                int serverIndex= (int) command.getData("serverIndex");
//                boolean canPin=false;
//                if(user.getServers().get(serverIndex).getServerAdmin().getUsername().equalsIgnoreCase(user.getUsername())){
//                    canPin=true;
//                }
//                else{
//                    HashSet<Integer> permissionIndexes=new HashSet<>();
//                    for (Role role:user.getServers().get(serverIndex).getServerRoles()){
//                        for (User uu:role.getUsers()){
//                            if(uu.getUsername().equalsIgnoreCase(user.getUsername())){
//                                ArrayList<Integer> arr=role.getPermissionIndexes();
//                                for (Integer i:arr){
//                                    permissionIndexes.add(i);
//                                }
//                            }
//                        }
//                    }
//                    for (Integer i:permissionIndexes){
//                        Permission p=DiscordServer.allPermissions[(i)-1];
//                        if(p==Permission.PIN_MESSAGE){
//                            canPin=true;
//                            break;
//                        }
//                    }
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("canPin",canPin);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //pin a message in channel
//            else if(command.getCode()==RequestCode.PIN_MESSAGE){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                int messageIndex= (int) command.getData("index");
//                ChannelMessage c=user.getServers().get(serverIndex).getChannels().get(channelIndex).getMessages().get(messageIndex);
//                user.getServers().get(serverIndex).getChannels().get(channelIndex).getMessages().remove(messageIndex);
//                user.getServers().get(serverIndex).getChannels().get(channelIndex).getPinedMessages().add(c);
//            }
//            //see message's reactions in a channel
//            else if(command.getCode()==RequestCode.SEE_REACTIONS){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                String reaction="";
//                if(user.getServers().get(serverIndex).getChannels().get(channelIndex).getMessages().size()!=0) {
//                    for (ChannelMessage c : user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages()) {
//                        HashMap<Reaction,Integer> reactionsHashmap=c.getReactions();
//                        reaction+=c.toString();
//                        reaction+=" ";
//                        reaction+="Like:";
//                        reaction+=reactionsHashmap.get(Reaction.LIKE);
//                        reaction+=" Dislike:";
//                        reaction+=reactionsHashmap.get(Reaction.DISLIKE);
//                        reaction+=" Smile: ";
//                        reaction+=reactionsHashmap.get(Reaction.SMILE);
//                        reaction+="@@@";
//                    }
//                    reaction=reaction.substring(0,reaction.length()-3);
//                }
//                Response response=new Response(ResponseCode.SHOW_REACTIONS);
//                response.addData("reaction",reaction);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see all messages in a channel
//            else if(command.getCode()==RequestCode.SHOW_ALL_MESSAGES){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                String message="";
//                if(user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().size()!=0) {
//                    for (ChannelMessage c : user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages()) {
//                        message += c.toString();
//                        message += "@@@";
//                    }
//                    message = message.substring(0, message.length() - 3);
//                }
//                Response response=new Response(ResponseCode.SHOW_MESSAGES);
//                response.addData("message",message);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            //checks that user reacted to selected message before or not
//            else if(command.getCode()==RequestCode.CAN_REACT){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                int messageIndex= (int) command.getData("index");
//                boolean beforeReact=false;
//                ChannelMessage m=user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex);
//                HashMap<User,Reaction> reactionHashMap=m.getUserReaction();
//                for (User u:reactionHashMap.keySet()){
//                    if(user.getUsername().equalsIgnoreCase(u.getUsername())){
//                        beforeReact=true;
//                        break;
//                    }
//                }
//                Response response=new Response(ResponseCode.SUCCESSFUL);
//                response.addData("beforeReact",beforeReact);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //remove old reaction to a message
//            else if(command.getCode()==RequestCode.REMOVE_OLD_REACT){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                int messageIndex= (int) command.getData("index");
//                ChannelMessage m=user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex);
//                HashMap<User,Reaction> reactionHashMap=m.getUserReaction();
//                Reaction oldReaction=null;
//                User uu=null;
//                HashMap<User,Reaction> newReactionHashMap=new HashMap<>();
//                for (User u:reactionHashMap.keySet()){
//                    if(user.getUsername().equalsIgnoreCase(u.getUsername())){
//                        oldReaction=reactionHashMap.get(u);
//                    }
//                    else{
//                        newReactionHashMap.put(u,reactionHashMap.get(u));
//                    }
//                }
//                user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).setUserReaction(newReactionHashMap);
//                if(oldReaction==Reaction.LIKE){
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.LIKE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.LIKE,count-1);
//                }
//                else if(oldReaction==Reaction.DISLIKE){
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.DISLIKE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.DISLIKE,count-1);
//                }
//                else{
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.SMILE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.SMILE,count-1);
//                }
//            }
//            //add a reaction to a message
//            else if(command.getCode()==RequestCode.ADD_REACT){
//                int serverIndex= (int) command.getData("serverIndex");
//                int channelIndex= (int) command.getData("channelIndex");
//                int messageIndex= (int) command.getData("index");
//                String react= (String) command.getData("react");
//                if(react.equals("1")){
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.LIKE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.LIKE,count+1);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getUserReaction().put(user,Reaction.LIKE);
//                }
//                else if(react.equals("2")){
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.DISLIKE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.DISLIKE,count+1);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getUserReaction().put(user,Reaction.DISLIKE);
//                }
//                else{
//                    int count= user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().get(Reaction.SMILE);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getReactions().put(Reaction.SMILE,count+1);
//                    user.getServers().get(serverIndex).getChannels().get(channelIndex).getAllMessages().get(messageIndex).getUserReaction().put(user,Reaction.SMILE);
//                }
//            }
//            //see list of private chats
//            else if(command.getCode() == RequestCode.SEE_PVs) {
//                Response response = new Response(ResponseCode.SUCCESSFUL);
//                String chatsNames = "";
//                for(PrivateChat c : user.getPrivateChats()) {
//                    chatsNames += c.getChatName();
//                    chatsNames += "@@@";
//                }
//                response.addData("PVs", chatsNames);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //see messages in a specific private chat
//            else if(command.getCode() == RequestCode.SEE_PV) {
//                String username2 = (String) command.getData("chatName");
//                User user1 = this.user;
//                User user2 = findUser(username2);
//                Response response=null;
//
//                if(user1 == null || user2 == null || user2.getUsername().equalsIgnoreCase(user.getUsername())) {
//                    response = new Response(ResponseCode.INVALID_USERNAME);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    for (User uu:user1.getBlockedFriends()){
//                        if(uu.getUsername().equalsIgnoreCase(user2.getUsername())){
//                            response = new Response(ResponseCode.INVALID_USERNAME);
//                            break;
//                        }
//                    }
//                    for(User uu:user2.getBlockedFriends()){
//                        if(uu.getUsername().equalsIgnoreCase(user1.getUsername())){
//                            response = new Response(ResponseCode.INVALID_USERNAME);
//                            break;
//                        }
//                    }
//                    if(response==null){
//                        PrivateChat privateChat = null;
//                        for(PrivateChat pv : this.user.getPrivateChats()) {
//                            if((pv.getUser2().getUsername().equals(user2.getUsername()) && pv.getUser1().getUsername().equals(user1.getUsername()))){
//                                privateChat = pv;
//                                break;
//                            }
////                        if((pv.getUser2().getUsername().equals(user2.getUsername()) &&  || pv.getUser1().getUsername().equals(user1.getUsername())) {
////                            privateChat = pv;
////                        }
//                        }
//                        if(privateChat == null) {
//                            privateChat = new PrivateChat(user1, user2);
//                            this.user.getPrivateChats().add(privateChat);
//                            PrivateChat privateChat1 = new PrivateChat(user2, user1);
//                            user2.getPrivateChats().add(privateChat1);
//                        }
//                        this.user.setCurrentChat(privateChat);
//                        System.out.println(privateChat.chatMessages());
//                        response = new Response(ResponseCode.YES);
//                        response.addData("privateChat", privateChat);
//                        response.addData("messagesFromThisChat", privateChat.chatMessages());
//                    }
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            //select a channel
//            else if(command.getCode() == RequestCode.ENTER_CHANNEL) {
//                Response response = null;
//                int serverIndex = (int) command.getData("serverIndex");
//                int channelIndex = (int) command.getData("channelIndex");
//                DiscordServer server = this.user.getServers().get(serverIndex);
//                Channel channel = null;
//                if(server != null) {
//                    channel = this.user.getServers().get(serverIndex).getChannels().get(channelIndex);
//                }
//                if(server == null || channel == null) {
//                    response = new Response(ResponseCode.INVALID_INDEX);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//                else{
//                    boolean b = true;
//                    for(User u : server.getBannedUsers()) {
//                        if(u == this.user) {
//                            b = false;
//                            response = new Response(ResponseCode.CANT_SEE_MESSAGES);
//                            try {
//                                objectOutputStream.writeObject(response);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                    if(b) {
//                        this.user.setCurrentChannel(channel);
//                        this.user.setCurrentServer(server);
//                        System.out.println(channel.channelMessages());
//                        response = new Response(ResponseCode.YES);
//                        response.addData("channelMessages", channel.channelMessages());
//                        response.addData("currentChannel", channel);
//                        response.addData("currentServer", server);
//                        try {
//                            objectOutputStream.writeObject(response);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
//                }
//            }
////send file in channel and the channel and server must not be null
//            else if(command.getCode() == RequestCode.SEND_FILE_IN_CHANNEL) {
//                byte[] content = (byte[]) command.getData("file");
//                Channel channel = (Channel) command.getData("channelToSendFile");
//                DiscordServer server = (DiscordServer) command.getData("serverToSendFile");
//                Response response = null;
//                if(server == null || channel == null) {
//                    response = new Response(ResponseCode.TRY_AGAIN);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    String path = (String) command.getData(".");
//                    File f = new File("./Files/" + (++counter) + channel.getChannelName() + path);
//                    try {
//                        Files.write(f.toPath(), content);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    for(Role r : server.getServerRoles()) {
//                        for(User u : r.getUsers()) {
//                            int userIndex=0;
//                            for (User uu:users){
//                                if(uu.getUsername().equalsIgnoreCase(u.getUsername())){
//                                    break;
//                                }
//                                userIndex++;
//                            }
//                            users.get(userIndex).addRequestFile(f);
//                        }
//                    }
//                    int adminIndex=0;
//                    for (User uu:users){
//                        if(uu.getUsername().equalsIgnoreCase(server.getServerAdmin().getUsername())){
//                            break;
//                        }
//                        adminIndex++;
//                    }
//                    users.get(adminIndex).addRequestFile(f);
//                    response = new Response(ResponseCode.REQUEST_OK);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//            }
//            //send file in chat
//            else if(command.getCode() == RequestCode.SEND_FILE_IN_CHAT) {
//                byte[] content = (byte[]) command.getData("file");
//                User user2 = (User) command.getData("friendName");
//                Response response = null;
//                if(user2 == null) {
//                    response = new Response(ResponseCode.TRY_AGAIN);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    int userIndex=0;
//                    for (User uu:users) {
//                        if (uu.getUsername().equalsIgnoreCase(user2.getUsername())) {
//                            break;
//                        }
//                        userIndex++;
//                    }
//                    String path = (String) command.getData(".");
//                    File f = new File("./Files/" + (++counter) + "chat" + path);
//                    try {
//                        Files.write(f.toPath(), content);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    users.get(userIndex).addRequestFile(f);
//                    response = new Response(ResponseCode.REQUEST_OK);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            //check username that user entered in the console
//            else if(command.getCode() == RequestCode.SEE_PV_TO_SEND_FILE) {
//                boolean flag = false;
//                Response response = null;
//                User user2 = null;
//                String friendName = (String) command.getData("friendName");
//                for(PrivateChat p : user.getPrivateChats()) {
//                    if(p.getUser2().getUsername().equals(friendName)) {
//                        flag = true;
//                        user2 = p.getUser2();
//                    }
//                }
//                if(!flag) {
//                    response = new Response(ResponseCode.INVALID_USERNAME);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    response = new Response(ResponseCode.YES);
//                    response.addData("friendUser", user2);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            else if(command.getCode() == RequestCode.SEE_RECEIVED_FILES) {
//                String filenames = "";
//                if(user.getRequestFiles().isEmpty()) {
//                    Response response = new Response(ResponseCode.NO_FILE);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    for(File f : user.getRequestFiles()) {
//                        filenames += f.getName();
//                        filenames += "@@@";
//                    }
//                    Response response = new Response(ResponseCode.REQUEST_OK);
//                    response.addData("fileNames", filenames);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            } else if (command.getCode() == RequestCode.SAVE_FILE) {
//                int index = (int) command.getData("indexOfFile");
//                user.addFiles(user.getRequestFiles().get(index));
//                user.getRequestFiles().remove(index);
//                Response response = new Response(ResponseCode.SAVED_FILE);
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            } else if (command.getCode() == RequestCode.SEE_SAVED_FILES) {
//                String savedFiles = "";
//                if(user.getFiles().isEmpty()) {
//                    Response response = new Response(ResponseCode.NO_SAVED_FILE);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    for(File f : user.getFiles()) {
//                        savedFiles += f.getName();
//                        savedFiles += "@@@";
//                    }
//                    Response response = new Response(ResponseCode.REQUEST_OK);
//                    response.addData("savedFiles", savedFiles);
//                    try {
//                        objectOutputStream.writeObject(response);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//
//            // close thread in a private chat
//            if (command.getCode() == RequestCode.CLOSE_CHAT) {
//                Response response = new Response(ResponseCode.CLOSE_THREAD);
//                this.user.setCurrentChat(null);
//                System.out.println("sent close thread message");
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //send messages
//            if(command.getCode() == RequestCode.SEND_MESSAGE) {
//                PrivateChat privateChat = this.user.getCurrentChat();
//                String text = (String) command.getData("messageFromUser");
//                PrivateMessage message = new PrivateMessage(text, this.user);
//                System.out.println(message);
//                Response response=null;
//                if (!(message.toString().substring(message.toString().indexOf(":") + 2).startsWith("0"))) {
//                    this.user.addMessage(privateChat, message);
//                    response = new Response(ResponseCode.MESSAGE_SENT);
//                    response.addData("messageReceivedCurrently", message);
//                    doThis(response, command);
//                }
//                else {
//                    this.user.setCurrentChat(null);
//                    break;
//                }
//            }
//            //close thread in a channel
//            if(command.getCode() == RequestCode.CLOSE_CHANNEL) {
//                Response response = new Response(ResponseCode.CLOSE_THREAD);
//                this.user.setCurrentServer(null);
//                this.user.setCurrentChannel(null);
//                System.out.println("sent close thread message");
//                try {
//                    objectOutputStream.writeObject(response);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //send message in a channel
//            if(command.getCode() == RequestCode.SEND_MESSAGE_IN_CHANNEL) {
//                boolean b = true;
//                Channel channel = this.user.getCurrentChannel();
//                DiscordServer server = this.user.getCurrentServer();
//                String text = (String) command.getData("messageFromChannel");
//                ChannelMessage message = new ChannelMessage(text, this.user);
//                System.out.println(message);
//                Response response = null;
//                for(User u : channel.getLimitedMembers()) {
//                    if(u == this.user) {
//                        this.user.setCurrentServer(null);
//                        this.user.setCurrentChannel(null);
//                        response = new Response(ResponseCode.CANT_SEND_MESSAGE);
//                        b = false;
//                        doThisForChannel(response, command);
//                    }
//                }
//                if(!(message.toString().substring(message.toString().indexOf(":") + 2).startsWith("0")) && b) {
//                    response = new Response(ResponseCode.MESSAGE_SENT);
//                    channel.addMessage(message);
//                    channel.getMessages().add(message);
//                    response.addData("messageReceivedFromChannel", message);
//                    response.addData("thisCurrentChannel", this.user.getCurrentChannel());
//                    response.addData("thisCurrentServer", this.user.getCurrentServer());
//                    doThisForChannel(response, command);
//                }
//                else {
//                    this.user.setCurrentChannel(null);
//                    this.user.setCurrentServer(null);
//                    break;
//                }
//
//            }
//        }
        }
    }
        /**
         * This method used to check is entered username duplicated or not
         *
         * *@param username
         * *@return boolean isDuplicate
         */

        private static boolean checkDuplication (String username){
            boolean isDuplicate = false;
            for (User user1 : users) {
                if (user1 != user) {
                    if (user1.getUsername().equalsIgnoreCase(username)) {
                        isDuplicate = true;
                        break;
                    }
                }
            }
            return isDuplicate;
        }
        /**
         * This method used to find a user based on entered username
         *
         * *@param username
         * *@return User
         *
         */
        private static User findUser (String username){
            User user2 = null;
            for (User user1 : users) {
                if (user1 != null) {
                    if (user1.getUsername().equalsIgnoreCase(username)) {
                        user2 = user1;
                        break;
                    }
                }
            }
            return user2;
        }
        private static boolean logInOnce (String username){
            boolean res = false;
            for (User user1 : users) {
                if (user1 != null) {
                    if (user1.getUsername().equalsIgnoreCase(username)) {
                        if (user1.getUserStatus() != Status.OFFLINE) {
                            res = true;
                        }
                    }

                }
            }
            return res;
        }
        /**
         * This method used to write in a file
         *
         * *@param users
         * *@return Nothing
         */
        public static void writeUsersFile () {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                oos.writeObject(users);
            } catch (IOException e) {

            }
        }
        public void closeEveryThing (ObjectInputStream in, ObjectOutputStream out, Socket socket){
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//    public void doThis(Response response, Request command)  {
//        if(command.getCode() == RequestCode.SEND_MESSAGE) {
//            try {
//                ClientHandler clientHandler = null;
//                PrivateChat pv = this.user.getCurrentChat();
//                String username = pv.getUser2().getUsername();
//                //          if (username.equals(this.user.getUsername()))
//                //              username = pv.getUser1().getUsername();
//                for(ClientHandler c : clientHandlers) {
//                    if(c.user.getUsername().equals(username)) {
//                        clientHandler = c;
//                    }
//                }
//
//                boolean flag = true;
//                for(User u : this.user.getBlockedFriends()) {
//                    if(clientHandler != null) {
//                        if(clientHandler.getUser() == u) {
//                            flag = false;
//                        }
//                    }
//                }
//
//                if (response == null)
//                    return;
//                if(clientHandler != null) {
//                    PrivateMessage message = (PrivateMessage) response.getData("messageReceivedCurrently");
//                    System.out.println(clientHandler.getUser().isInThisPrivateChat(this.user.getUsername()));
//                    for(PrivateChat p : clientHandler.getUser().getPrivateChats()) {
//                        if(p.getUser1() == pv.getUser2() && flag && p.getUser2()==pv.getUser1()) {
//                            clientHandler.getUser().addMessage(p, message);
//                            System.out.println(p.chatMessages());
//                            if (clientHandler.objectOutputStream != null && clientHandler.getUser().isInThisPrivateChat(this.user.getUsername())) {
//                                response.addData("thisCurrentChat", p);
//                                clientHandler.objectOutputStream.writeObject(response);
//                            }
//                        }
//                    }
//                }
////                        System.out.println("in run method" + ((Message)response.getData()).getContent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void doThisForChannel(Response response, Request command) {
//        if(command.getCode() == RequestCode.SEND_MESSAGE_IN_CHANNEL) {
//            try {
//                Channel channel = this.user.getCurrentChannel();
//                DiscordServer server = this.user.getCurrentServer();
//                if(response.getCode() == ResponseCode.CANT_SEND_MESSAGE) {
//                    for(ClientHandler c : clientHandlers) {
//                        if(c.getUser() == this.user) {
//                            c.objectOutputStream.writeObject(response);
//                        }
//                    }
//                }
//                else {
//                    for(Role r : server.getServerRoles()) {
//                        for(User u : r.getUsers()) {
//                            for(ClientHandler c : clientHandlers) {
//                                if(c.user == u) {
//                                    if(c.objectOutputStream != null && c.user.getCurrentChannel() == channel && c.user.getCurrentServer() == server) {
//                                        c.objectOutputStream.writeObject(response);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    User admin = server.getServerAdmin();
//                    for(ClientHandler c : clientHandlers) {
//                        if(c.user == admin) {
//                            if(c.objectOutputStream != null && c.user.getCurrentChannel() == channel && c.user.getCurrentServer() == server) {
//                                c.objectOutputStream.writeObject(response);
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

}