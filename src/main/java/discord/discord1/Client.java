package discord.discord1;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client class
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class Client {
    //fields
    private Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;
    private Scanner scan;

    //constructor
    public Client(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            scan = new Scanner(System.in);
        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }

    public static UIResponse process(UIRequest uiRequest) throws IOException, ClassNotFoundException {
        UIResponse uiResponse = null;
        if(uiRequest.getCode() == UIRequestCode.SIGN_UP) {
            String email = (String) uiRequest.getData("email");
            String username = (String) uiRequest.getData("username");
            String password = (String) uiRequest.getData("password");
            String tel = (String) uiRequest.getData("tel");
            uiResponse = signUp(email, username, password, tel);
            return uiResponse;
        }
        else if(uiRequest.getCode() == UIRequestCode.LOG_IN) {
            String username = (String) uiRequest.getData("username");
            String password = (String) uiRequest.getData("password");
            uiResponse = logIn(username, password);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_PASSWORD){
            String currentPassword= (String) uiRequest.getData("current");
            String newPassword= (String) uiRequest.getData("new");
            String confirmPassword= (String) uiRequest.getData("confirm");
            uiResponse=changePassword(currentPassword,newPassword,confirmPassword);
        }
        else if(uiRequest.getCode()==UIRequestCode.LOG_OUT){
            Request request = new Request(RequestCode.LOG_OUT);
            objectOutputStream.writeObject(request);
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_PHONE_NUMBER){
            Request request=new Request(RequestCode.GET_PHONE_NUMBER);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            String phoneNumber= (String) resultResponse.getData("phone");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("phone",phoneNumber);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.REMOVE_PHONE_NUMBER){
            Request request=new Request(RequestCode.REMOVE_PHONE_NUMBER);
            objectOutputStream.writeObject(request);
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_PHONE_NUMBER){
            String enteredPhoneNumber= (String) uiRequest.getData("phone");
            Request request=new Request(RequestCode.CHANGE_PHONE_NUMBER);
            request.addData("phone",enteredPhoneNumber);
            objectOutputStream.writeObject(request);
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_EMAIL){
            Request request=new Request(RequestCode.GET_EMAIL);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            String email= (String) resultResponse.getData("email");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("email",email);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_EMAIL){
            String enteredEmail= (String) uiRequest.getData("email");
            Request request=new Request(RequestCode.CHANGE_EMAIL);
            request.addData("email",enteredEmail);
            objectOutputStream.writeObject(request);
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_USERNAME){
            Request request=new Request(RequestCode.GET_USERNAME);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            String username= (String) resultResponse.getData("name");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("name",username);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_USERNAME){
            String enteredUsername= (String) uiRequest.getData("name");
            Request request = new Request(RequestCode.CHECK_USERNAME_DUPLICATION);
            request.addData("username", enteredUsername);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            Boolean isDuplicate = (Boolean) resultResponse.getData("IsDuplicated");
            if (isDuplicate) {
                uiResponse = new UIResponse(UIResponseCode.DUPLICATE_USERNAME);
                return uiResponse;
            }
            else{
                Request request1=new Request(RequestCode.CHANGE_USERNAME);
                request1.addData("name",enteredUsername);
                objectOutputStream.writeObject(request1);
                uiResponse=new UIResponse(UIResponseCode.OK);
                return uiResponse;
            }
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_STATUS){
            Request request=new Request(RequestCode.GET_STATUS);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            Status userStatus= (Status) resultResponse.getData("status");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("status",userStatus);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.SELECT_PROFILE){
             Request request = new Request(RequestCode.SEND_PICTURE);
        String path = String.valueOf(uiRequest.getData("path"));
        byte[] content = (byte[]) uiRequest.getData("content");
        request.addData("file", content);
        request.addData(".", path.substring(path.indexOf(".")));
        objectOutputStream.writeObject(request);
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_SERVER_USERS){
            String username= (String) uiRequest.getData("username");
            String members=membersList(username);
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("members",members);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_STATUS){
            int num= (int) uiRequest.getData("num");
            changeStatus(num);
        }
        else if(uiRequest.getCode()==UIRequestCode.SEE_CHANNELS){
            String channels=seeChannels();
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("channels",channels);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_SERVER_NAME){
            Request request=new Request(RequestCode.GET_SERVER_NAME);
            objectOutputStream.writeObject(request);
            Response resultResponse = (Response) objectInputStream.readObject();
            String name= (String) resultResponse.getData("name");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("name",name);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.IS_ADMIN){
            Request request1 = new Request(RequestCode.IS_SERVER_ADMIN);
            objectOutputStream.writeObject(request1);
            Response response1 = (Response) objectInputStream.readObject();
            boolean isAdmin = (boolean) response1.getData("isAdmin");
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("isAdmin",isAdmin);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.GET_PERMISSIONS){
            Request request2 = new Request(RequestCode.GET_PERMISSIONS);
            objectOutputStream.writeObject(request2);
            Response response2 = (Response) objectInputStream.readObject();
            ArrayList<Integer> permissions = (ArrayList<Integer>) response2.getData("permissions");
            ArrayList<String> permissionsArr=new ArrayList<>();
            if(permissions!=null) {
                for (int i = 0; i < permissions.size(); i++) {
                    String strPer = String.valueOf(DiscordServer.allPermissions[permissions.get(i) - 1]);
                    String[] strPerArr = strPer.split("_");
                    String tmp="";
                    for (int j = 0; j < strPerArr.length; j++) {
                        tmp+=(strPerArr[j].toLowerCase(Locale.ROOT) + " ");
                    }
                   permissionsArr.add(tmp);
                }
            }
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("permissions",permissionsArr);
        }
        else if(uiRequest.getCode()==UIRequestCode.CREATE_CHANNEL){
            String name= (String) uiRequest.getData("name");
            boolean isDuplicated=createChannel(name);
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("isDuplicated",isDuplicated);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.REMOVE_CHANNEL){
            String name= (String) uiRequest.getData("name");
            boolean isExist=removeChannel(name);
            uiResponse=new UIResponse(UIResponseCode.OK);
            uiResponse.addData("isExist",isExist);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.ADD_MEMBER){
            String inputUsername= (String) uiRequest.getData("name");
            Request request3 = new Request(RequestCode.ADD_MEMBER);
            request3.addData("username", inputUsername);
            objectOutputStream.writeObject(request3);
            Response response3 = (Response) objectInputStream.readObject();
            if (response3.getCode() == ResponseCode.NOT_EXIST) {
                uiResponse=new UIResponse(UIResponseCode.NOT_EXIST);
            }
            else if (response3.getCode() == ResponseCode.IS_EXISTS) {
                uiResponse=new UIResponse(UIResponseCode.IS_EXISTS);
            }
            else {
                uiResponse=new UIResponse(UIResponseCode.OK);
            }
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANGE_SERVER_NAME){
            Request request4 = new Request(RequestCode.CHANGE_SERVER_NAME);
            String newName= (String) uiRequest.getData("name");
            request4.addData("name", newName);
            objectOutputStream.writeObject(request4);
            Response response4 = (Response) objectInputStream.readObject();
            if (response4.getCode() == ResponseCode.NOT_CHANGE) {
                uiResponse=new UIResponse(UIResponseCode.NOT_CHANGE);
            } else if (response4.getCode() == ResponseCode.DUPLICATED) {
                uiResponse=new UIResponse(UIResponseCode.DUPLICATED);
            } else {
               uiResponse=new UIResponse(UIResponseCode.OK);
            }
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.CHANNEL_EXIST){
            String channelName= (String) uiRequest.getData("name");
            boolean exist=checkChannel(channelName);
            if(exist){
                uiResponse=new UIResponse(UIResponseCode.OK);
            }
            else{
                uiResponse=new UIResponse(UIResponseCode.NOT_EXIST);
            }
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.LIMIT_MEMBER){
            String enteredChannelName= (String) uiRequest.getData("channel");
            String enteredUsername= (String) uiRequest.getData("username");
            uiResponse=limitMember(enteredUsername,enteredChannelName);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.BAN_MEMBER){
            String enteredUsername= (String) uiRequest.getData("username");
            uiResponse=banMember(enteredUsername);
            return uiResponse;
        }
        else if(uiRequest.getCode()==UIRequestCode.REMOVE_SERVER){
            removeServer();
        }

        return null;
    }

    /**
     * This method is used to close socket and ObjectInputStream and ObjectOutputStream
     * <p>
     * *@param socket
     * *@param objectInputStream
     * *@param objectOutputStream
     * *@return Nothing
     */
    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method receive necessary information and check them for signing up a user
     *
     * *@param -
     * *@return Nothing
     * *@throws IOException
     * *@throws ClassNotFoundException
     */
    private static UIResponse signUp(String email, String username, String password, String tel) throws IOException, ClassNotFoundException {
        UIResponse uiResponse = null;
        //1--> done
        //-1--> back to main menu
        //0--> keep going
        if (username.length() >= 6) {
            boolean numberAndLetter = true;
            for (int i = 0; i < username.length(); i++) {
                if ((int) (username.charAt(i)) < 48 || ((int) (username.charAt(i)) >= 58 && (int) (username.charAt(i)) <= 64) || ((int) (username.charAt(i)) >= 91 && (int) (username.charAt(i)) <= 96) || (int) (username.charAt(i)) > 123) {
                    numberAndLetter = false;
                    break;
                }
            }
            if (numberAndLetter == false) {
                uiResponse = new UIResponse(UIResponseCode.NUMBER_LETTER);
                return uiResponse;
            }
            else {
                Request request = new Request(RequestCode.CHECK_USERNAME_DUPLICATION);
                request.addData("username", username);
                objectOutputStream.writeObject(request);
                Response resultResponse = (Response) objectInputStream.readObject();
                Boolean isDuplicate = (Boolean) resultResponse.getData("IsDuplicated");
                if (isDuplicate) {
                    uiResponse = new UIResponse(UIResponseCode.DUPLICATE_USERNAME);
                    return uiResponse;
                }
            }
        }
        else {
            uiResponse = new UIResponse(UIResponseCode.SIX_LETTER);
            return uiResponse;
        }
        if (password.length() < 8) {
            uiResponse = new UIResponse(UIResponseCode.EIGHT_LETTER);
            return uiResponse;
        } else {
            boolean flag = true;
            boolean haveNumber = false;
            boolean haveLetter = false;
            for (int i = 0; i < password.length(); i++) {
                if ((int) (password.charAt(i)) < 48 || ((int) (password.charAt(i)) >= 58 && (int) (password.charAt(i)) <= 64) || ((int) (password.charAt(i)) >= 91 && (int) (password.charAt(i)) <= 96) || (int) (password.charAt(i)) > 123) {
                    flag = false;
                    break;
                }
                if (!haveNumber) {
                    if ((int) (password.charAt(i)) >= 48 && (int) (password.charAt(i)) <= 57) {
                        haveNumber = true;
                    }
                }
                if (!haveLetter) {
                    if (((int) (password.charAt(i)) >= 65 && (int) (password.charAt(i)) <= 90) || ((int) (password.charAt(i)) >= 97 && (int) (password.charAt(i)) <= 122)) {
                        haveLetter = true;
                    }
                }
            }
            if (!flag) {
                uiResponse = new UIResponse(UIResponseCode.NUMBER_LETTER);
                return uiResponse;
            }
            if (!haveLetter) {
                uiResponse = new UIResponse(UIResponseCode.LETTER);
                return uiResponse;
            }
            if (!haveNumber) {
                uiResponse = new UIResponse(UIResponseCode.NUMBER);
                return uiResponse;
            }
        }
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        boolean emailIsValid = matcher.matches();
        if (!emailIsValid) {
            uiResponse = new UIResponse(UIResponseCode.INVALID_EMAIL);
            return uiResponse;
        }
        if(!tel.equals("")) {
            if (tel.length() == 11) {
                if (!tel.startsWith("09")) {
                    uiResponse = new UIResponse(UIResponseCode.INVALID_TEL);
                    return uiResponse;
                }
            } else {
                uiResponse = new UIResponse(UIResponseCode.INVALID_TEL);
                return uiResponse;
            }
        }
        if (uiResponse == null) {
            User user = new User(username, password, email, tel);
            Request request = new Request(RequestCode.ADD_USER);
            request.addData("user", user);
            objectOutputStream.writeObject(request);
            uiResponse = new UIResponse(UIResponseCode.OK);
            return uiResponse;
        }
        return null;
    }
    private static UIResponse logIn(String username, String password) throws IOException, ClassNotFoundException {
        UIResponse uiResponse = null;
        //-1:back to first menu
        //0:get input again
        //1:input got successfully
        User u = null;
        Request request = new Request(RequestCode.FIND_USER_BY_USERNAME);
        request.addData("username", username);
        objectOutputStream.writeObject(request);
        Response response = (Response) objectInputStream.readObject();
        u = (User) response.getData("user");
        if (u == null) {
            uiResponse = new UIResponse(UIResponseCode.NO_USER);
            return uiResponse;
        }
        if (u.getPassword().equalsIgnoreCase(password)) {
            request = new Request(RequestCode.LOG_IN);
            request.addData("user", u);
            objectOutputStream.writeObject(request);
            uiResponse = new UIResponse(UIResponseCode.OK);
            return uiResponse;
        }
        uiResponse = new UIResponse(UIResponseCode.INVALID_PASSWORD);
        return uiResponse;
    }

    public static UIResponse changePassword(String currentPassword,String newPassword,String confirmPassword) throws IOException, ClassNotFoundException {
       Request request = new Request(RequestCode.CHECK_PASSWORD);
       request.addData("password", currentPassword);
       objectOutputStream.writeObject(request);
       Response response = (Response) objectInputStream.readObject();
       boolean isValid = (boolean) response.getData("valid");
       boolean currentValid=false;
       boolean newValid=false;
       boolean confirmValid=false;
       if (isValid ) {
           currentValid=true;
           if (newPassword.length() >= 8) {
              boolean check = checkPassword(newPassword);
                    if (check) {
                        newValid=true;
                        if (confirmPassword.equalsIgnoreCase(newPassword)) {
                            Request request1 = new Request(RequestCode.CHANGE_PASSWORD);
                            request1.addData("password", newPassword);
                            objectOutputStream.writeObject(request1);
                            confirmValid=true;
                        }
                    }
                }
            }
       UIResponse uiResponse=new UIResponse(UIResponseCode.OK);
       uiResponse.addData("current",currentValid);
       uiResponse.addData("new",newValid);
       uiResponse.addData("confirm",confirmValid);
       return uiResponse;
    }
    public static boolean checkPassword(String password) {
        boolean isValid = true;
        boolean flag = true;
        boolean haveNumber = false;
        boolean haveLetter = false;
        for (int i = 0; i < password.length(); i++) {
            if ((int) (password.charAt(i)) < 48 || ((int) (password.charAt(i)) >= 58 && (int) (password.charAt(i)) <= 64) || ((int) (password.charAt(i)) >= 91 && (int) (password.charAt(i)) <= 96) || (int) (password.charAt(i)) > 123) {
                flag = false;
                break;
            }
            if (!haveNumber) {
                if ((int) (password.charAt(i)) >= 48 && (int) (password.charAt(i)) <= 57) {
                    haveNumber = true;
                }
            }
            if (!haveLetter) {
                if (((int) (password.charAt(i)) >= 65 && (int) (password.charAt(i)) <= 90) || ((int) (password.charAt(i)) >= 97 && (int) (password.charAt(i)) <= 122)) {
                    haveLetter = true;
                }
            }
        }
        if (!flag || !haveNumber || !haveLetter) {
            isValid = false;
        }
        return isValid;
    }
    /**
     //     * This method used to show server's member (username and status)
     //     *
     //     * *@param index index of DiscordServer
     //     * *@throws IOException
     //     * *@throws ClassNotFoundException
     //     * *@return Nothing
     //     */
    public static String membersList(String username) throws IOException, ClassNotFoundException {
        Request request = new Request(RequestCode.SEE_MEMBERS_LIST);
        request.addData("username", username);
        objectOutputStream.writeObject(request);
        Response response = (Response) objectInputStream.readObject();
        String members = (String) response.getData("members");
        return members;
    }
    /**
     //     * This method used to change user's status
     //     *
     //     * *@param -
     //     * *@return Nothing
     //     */
    public static void changeStatus(int num) throws IOException {
           Request request = new Request(RequestCode.CHANGE_STATUS);
           request.addData("status", num);
           objectOutputStream.writeObject(request);
    }
    /**
     //     * This method used to show list of channels in a server
     //     * *@param index  index of DiscordServer
     //     * *@return Nothing
     //     * *@throws IOException
     //     * *@throws ClassNotFoundException
     //     */
    public static String seeChannels() throws IOException, ClassNotFoundException {
        Request request = new Request(RequestCode.SEE_CHANNELS);
        objectOutputStream.writeObject(request);
        Response response = (Response) objectInputStream.readObject();
        String channels = (String) response.getData("channels");
        return channels;
    }
    /**
     //     * This method used to create a new channel in server
     //     * *@param index index of DiscordServer
     //     * *@return Nothing
     //     * *@throws IOException
     //     * *@throws ClassNotFoundException
     //     */
    public static boolean createChannel(String channelName) throws IOException, ClassNotFoundException {
        Request request = new Request(RequestCode.CREATE_CHANNEL);
        request.addData("channel", channelName);
        objectOutputStream.writeObject(request);
        Response response = (Response) objectInputStream.readObject();
        boolean isDuplicate = (boolean) response.getData("Duplicate");
        return isDuplicate;
    }

    /**
     //     * This method used to remove an existing channel in server
     //     * @param index index of DiscordServer
     //     * *@return Nothing
     //     * *@throws IOException
     //     * *@throws ClassNotFoundException
     //     */
    public static boolean removeChannel(String channelName) throws IOException, ClassNotFoundException {
        Request request = new Request(RequestCode.REMOVE_CHANNEL);
        request.addData("channel", channelName);
        objectOutputStream.writeObject(request);
        Response response = (Response) objectInputStream.readObject();
        boolean isExist = (boolean) response.getData("exist");
        return isExist;
    }
    /**
     //     * This method used to limit a member from specific channel
     //     *
     //     * *@param index index of DiscordServer
     //     * *@return Nothing
     //     * *@throws IOException
     //     * *@throws ClassNotFoundException
     //     */
    public static boolean checkChannel(String channelName) throws IOException, ClassNotFoundException {
        Request request1 = new Request(RequestCode.IS_CHANNEL_EXIST);
        request1.addData("channel", channelName);
        objectOutputStream.writeObject(request1);
        Response response1 = (Response) objectInputStream.readObject();
        boolean exist = (boolean) response1.getData("exist");
        return exist;
//
    }
    public static UIResponse limitMember(String username,String channelName) throws IOException, ClassNotFoundException {
        Request request2 = new Request(RequestCode.LIMIT_MEMBER);
        request2.addData("username", username);
        request2.addData("channel", channelName);
        objectOutputStream.writeObject(request2);
        Response response2 = (Response) objectInputStream.readObject();
        UIResponse uiResponse;
        if (response2.getCode() == ResponseCode.NOT_EXIST) {
            uiResponse=new UIResponse(UIResponseCode.NOT_EXIST);

        }
        else if (response2.getCode() == ResponseCode.NOT_IN_SERVER) {
            uiResponse=new UIResponse(UIResponseCode.NOT_IN_SERVER);

        } else if (response2.getCode() == ResponseCode.LIMIT_BEFORE) {
            uiResponse=new UIResponse(UIResponseCode.LIMIT_BEFORE);

        } else if (response2.getCode() == ResponseCode.BANNED_BEFORE) {
           uiResponse=new UIResponse(UIResponseCode.BAN_BEFORE);

        } else {
            uiResponse=new UIResponse(UIResponseCode.OK);
        }
        return uiResponse;

    }
        /**
     * This method used to ban a member from server
     *
     * *@param index index of DiscordServer
     * *@return Nothing
     * *@throws IOException
     * *@throws ClassNotFoundException
     */
//
    public static UIResponse banMember(String username) throws IOException, ClassNotFoundException {
        Request request2 = new Request(RequestCode.BAN_MEMBER);
        request2.addData("username", username);
        objectOutputStream.writeObject(request2);
        Response response2 = (Response) objectInputStream.readObject();
        UIResponse uiResponse=null;
        if (response2.getCode() == ResponseCode.NOT_EXIST) {
           uiResponse=new UIResponse(UIResponseCode.NOT_EXIST);

        } else if (response2.getCode() == ResponseCode.NOT_IN_SERVER) {
            uiResponse=new UIResponse(UIResponseCode.NOT_IN_SERVER);

        } else if (response2.getCode() == ResponseCode.BANNED_BEFORE) {
            uiResponse=new UIResponse(UIResponseCode.BAN_BEFORE);

        } else if (response2.getCode() == ResponseCode.BAN_MEMBER) {
            uiResponse=new UIResponse(UIResponseCode.OK);
        }
        return uiResponse;
    }
    /**
     //     * This method used to remove a server by admin
     //     *
     //     * *@param -
     //     * *@return Nothing
     //     * *@throws IOException
     //     */
    public static void removeServer() throws IOException {
        Request request = new Request(RequestCode.REMOVE_SERVER);
        objectOutputStream.writeObject(request);
    }







}
//
//
//    /**
//     * This method is first method that invoke in this class.it shows some menus and receive user's
//     * selected options and do something
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     * *@throws InterruptedException
//     */
//    public void process() throws IOException, ClassNotFoundException, InterruptedException {
//        boolean logInMenu = false;
//        boolean backToFirstMenu = false;
//        while (socket.isConnected()) {
//            MenuHandler.firstMenu();
//            String firstMenuChoice = scan.nextLine();
//            switch (firstMenuChoice) {
//                case "1":
//                    int returnSignUp = signUp();
//                    if (returnSignUp == -1) {
//                        continue;
//                    } else {
//                        logInMenu = true;
//                    }
//                    break;
//                case "2":
//                    int logInReturn = logIn();
//                    if (logInReturn == -1) {
//                        continue;
//                    } else {
//                        logInMenu = true;
//                    }
//                    break;
//                case "3":
//                    Request request = new Request(RequestCode.EXIT);
//                    objectOutputStream.writeObject(request);
//                    System.out.println("Have a good day!");
//                    closeEverything(socket, objectInputStream, objectOutputStream);
//                    System.exit(130);
//                default:
//                    System.out.println("Invalid Input...try again");
//                    continue;
//            }
//            if (logInMenu) {
//                label:
//                while (true) {
//                    MenuHandler.loginMenu();
//                    String logInChoice = scan.nextLine();
//                    switch (logInChoice) {
//                        case "3":
//                            sendRequests();
//                            continue;
//                        case "2":
//                            seeRequests();
//                            continue;
//                        case "4":
//                            seeFriends();
//                            continue;
//                        case "6":
//                            changeStatus();
//                            continue;
//                        case "7":
//                            blockFriend();
//                            continue;
//                        case "8":
//                            seeBlockedFriends();
//                            continue;
//                        case "9":
//                            changePassword();
//                            continue;
//                        case "10":
//                            serverSettings();
//                            continue;
//                        case "0":
//                            Request request = new Request(RequestCode.LOG_OUT);
//                            objectOutputStream.writeObject(request);
//                            break label;
//                        case "5":
//                            buildServer();
//                            continue;
//                        case "11":
//                            selectPicture();
//                            continue;
//                        case "12":
//                            seeReceivedFiles();
//                            continue;
//                        case "13":
//                            seeSavedFiles();
//                            continue;
//                        case "1":
//                            chats();
//                            continue;
//                        default:
//                            System.out.println("Invalid Input...try again");
//                            continue;
//                    }
//                }
//                if (backToFirstMenu) {
//                    backToFirstMenu = false;
//                    continue;
//                }
//
//            }
//        }
//    }
//
//    /**
//     * This method send friend request to another users
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void sendRequests() throws IOException, ClassNotFoundException {
//        System.out.println("enter a username:");
//        System.out.println("enter 0 to back to menu");
//        String username = scan.nextLine();
//        if (username.equals("0")) {
//            return;
//        }
//        Request request = new Request(RequestCode.SEND_REQUEST);
//        request.addData("username", username);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if (response.getCode() == ResponseCode.BEFORE_FRIEND) {
//            System.out.println(username + "Is your friend now!\n\n");
//        } else if (response.getCode() == ResponseCode.BEFORE_REQUEST) {
//            System.out.println("request sent before!\n\n");
//        } else if (response.getCode() == ResponseCode.FRIEND_NOT_FOUND) {
//            System.out.println("This user doesn't exist\n\n");
//        } else if (response.getCode() == ResponseCode.OWN_REQUEST) {
//            System.out.println("you can't send request to yourself :/\n\n");
//        } else if (response.getCode() == ResponseCode.REQUEST_AGAIN) {
//            System.out.println(username + " sent you a request\n\n");
//        } else {
//            System.out.println("request sent successfully\n\n");
//        }
//    }
//
//    /**
//     * This method show received friend requests and user can accept friend request
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seeRequests() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_REQUEST);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String requests = (String) response.getData("requests");
//        if (requests.equals("")) {
//            System.out.println("There is no request!\n\n");
//            return;
//        }
//        String[] requestsArr = requests.split("@@@");
//        for (int i = 0; i < requestsArr.length; i++) {
//            System.out.println((i + 1) + ". " + requestsArr[i]);
//        }
//        System.out.println("\n");
//        System.out.println("Do you want to accept a request ? yes or no");
//        System.out.println("enter 0 to back to menu");
//        String input = scan.nextLine();
//        if (input.equals("0")) {
//            return;
//        } else if (input.equalsIgnoreCase("no")) {
//            return;
//        } else if (input.equalsIgnoreCase("yes")) {
//            System.out.println("enter the index:");
//            System.out.println("enter 0 to back to menu");
//            String enteredIndex = scan.nextLine();
//            int enteredIndexNum = 0;
//            try {
//                enteredIndexNum = Integer.parseInt(enteredIndex);
//            } catch (Exception e) {
//                System.out.println("Invalid Input\n\n");
//                return;
//            }
//            if (enteredIndexNum == 0) {
//                return;
//            }
//            if (enteredIndexNum < 0 || enteredIndexNum > requestsArr.length) {
//                System.out.println("Invalid Input num\n\n");
//                return;
//            }
//            Request request1 = new Request(RequestCode.ACCEPT_REQUEST_FRIEND);
//            request1.addData("index", enteredIndexNum - 1);
//            objectOutputStream.writeObject(request1);
//
//        } else {
//            System.out.println("Invalid Input\n\n");
//        }
//    }
//


//    /**
//     * This method receive necessary information and check them for log in
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public int logIn() throws IOException, ClassNotFoundException {
//        int logInReturn = 0;
//        //-1:back to first menu
//        //0:get input again
//        //1:input got successfully
//        String username = "";
//        User u = null;
//        while (logInReturn == 0) {
//            System.out.println("Enter your username:");
//            System.out.println("enter 0 to back to first menu.");
//            username = scan.nextLine();
//            if (username.equals("0")) {
//                logInReturn = -1;
//                return logInReturn;
//            }
//            Request request = new Request(RequestCode.FIND_USER_BY_USERNAME);
//            request.addData("username", username);
//            objectOutputStream.writeObject(request);
//            Response response = (Response) objectInputStream.readObject();
//            u = (User) response.getData("user");
//
//            if (u != null) {
////                Request request1 = new Request(RequestCode.LOG_IN_ONCE);
////                request1.addData("username", username);
////                objectOutputStream.writeObject(request1);
////                Response response1 = (Response) objectInputStream.readObject();
////                boolean logInOnce = (boolean) response1.getData("once");
////                if (logInOnce) {
////                    System.out.println(u.getUsername() + " logged in before!");
////                    return -1;
////                }
//                logInReturn = 1;
//                break;
//            }
//            else {
//                System.out.println("we don't have an account with this username...try again\n\n");
//            }
//
//        }
//        String password = "";
//        logInReturn = 0;
//        while (logInReturn == 0) {
//            System.out.println("Enter your password:");
//            System.out.println("enter 0 to back to first menu.");
//            password = scan.nextLine();
//            if (password.equals("0")) {
//                logInReturn = -1;
//                return logInReturn;
//            }
//            if (u.getPassword().equalsIgnoreCase(password)) {
//                Request request = new Request(RequestCode.LOG_IN);
//                request.addData("user", u);
//                objectOutputStream.writeObject(request);
//                logInReturn = 1;
//                System.out.println("you logged in successfully!");
//            } else {
//                System.out.println("Incorrect password...try again\n\n");
//                continue;
//            }
//        }
//        return logInReturn;
//    }
//
//    /**
//     * This method shows user;s friends
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seeFriends() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_FRIENDS);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String friends = (String) response.getData("friends");
//        if (friends.equalsIgnoreCase("")) {
//            System.out.println("you don't have any friend :(");
//            return;
//        }
//        String[] friendsArr = friends.split("@@@");
//        for (int i = 0; i < friendsArr.length; i++) {
//            System.out.println((i + 1) + "." + friendsArr[i]);
//        }
//    }
//
//
//
//    /**
//     * This method used to block a friend
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void blockFriend() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_FRIENDS);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String friends = (String) response.getData("friends");
//        if (friends.equalsIgnoreCase("")) {
//            System.out.println("you don't have any friend  to block :/");
//            return;
//        }
//        String[] friendsArr = friends.split("@@@");
//        for (int i = 0; i < friendsArr.length; i++) {
//            System.out.println((i + 1) + "." + friendsArr[i]);
//        }
//        System.out.println("enter index of friend that you want to block:");
//        System.out.println("enter 0 to back to menu");
//        String blockInput = scan.nextLine();
//        if (blockInput.equals("0")) {
//            return;
//        }
//        int blockInputNum = 0;
//        try {
//            blockInputNum = Integer.parseInt(blockInput);
//        } catch (Exception e) {
//            System.out.println("Invalid Input...");
//            return;
//        }
//        if (blockInputNum < 0 || blockInputNum > friendsArr.length) {
//            System.out.println("Invalid Input Number");
//            return;
//        }
//        Request request1 = new Request(RequestCode.BLOCK_FRIEND);
//        request1.addData("index", blockInputNum - 1);
//        objectOutputStream.writeObject(request1);
//        System.out.println(friendsArr[blockInputNum - 1] + " blocked successfully");
//    }
//
//    /**
//     * This method used to show list of blocked friends and unblock friend if user want
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seeBlockedFriends() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_BLOCKED_FRIEND);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String blocks = (String) response.getData("blocks");
//        if (blocks.equalsIgnoreCase("")) {
//            System.out.println("you don't have any blocked friend :)");
//            return;
//        }
//        String[] blocksArr = blocks.split("@@@");
//        for (int i = 0; i < blocksArr.length; i++) {
//            System.out.println((i + 1) + "." + blocksArr[i]);
//        }
//        System.out.println("\n");
//        System.out.println("Do you want to unblock a friend? yes or no?");
//        System.out.println("enter 0 to back to menu");
//        String input = scan.nextLine();
//        if (input.equalsIgnoreCase("no") || input.equals("0")) {
//            return;
//        }
//        else if (input.equalsIgnoreCase("yes")) {
//            System.out.println("enter index of friend that you want unblock:");
//            System.out.println("enter 0 to back to menu");
//            String unblockInput = scan.nextLine();
//            if (unblockInput.equals("0")) {
//                return;
//            }
//            int unblockInputNum = 0;
//            try {
//                unblockInputNum = Integer.parseInt(unblockInput);
//            } catch (Exception e) {
//                System.out.println("Invalid Input\n\n");
//                return;
//            }
//            if (unblockInputNum < 0 || unblockInputNum > blocksArr.length) {
//                System.out.println("Invalid Input number\n\n");
//                return;
//            }
//            Request request1 = new Request(RequestCode.UNBLOCK_FRIEND);
//            request1.addData("index", unblockInputNum - 1);
//            objectOutputStream.writeObject(request1);
//            System.out.println(blocksArr[unblockInputNum - 1] + " unblocked successfully\n\n");
//
//        } else {
//            System.out.println("Invalid Input\n\n");
//        }
//
//    }
//
//    /**
//     * This method used to change user's password
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void changePassword() throws IOException, ClassNotFoundException {
//        while (true) {
//            System.out.println("enter your current password:");
//            System.out.println("enter 0 to back to menu:");
//            String inputPassword = scan.nextLine();
//            if (inputPassword.equals("0")) {
//                break;
//            }
//            Request request = new Request(RequestCode.CHECK_PASSWORD);
//            request.addData("password", inputPassword);
//            objectOutputStream.writeObject(request);
//            Response response = (Response) objectInputStream.readObject();
//            boolean isValid = (boolean) response.getData("valid");
//            if (isValid ) {
//                System.out.println("enter new password:");
//                String newPassword = scan.nextLine();
//                if (newPassword.length() < 8) {
//                    System.out.println("password should have at least 8 characters...try again");
//                }
//                else {
//                    boolean check = checkPassword(newPassword);
//                    if (check) {
//                        System.out.println("enter confirm password:");
//                        String confirmNewPassword = scan.nextLine();
//                        if (confirmNewPassword.equalsIgnoreCase(newPassword)) {
//                            Request request1 = new Request(RequestCode.CHANGE_PASSWORD);
//                            request1.addData("password", newPassword);
//                            objectOutputStream.writeObject(request1);
//                            System.out.println("password changed successfully");
//                            break;
//                        } else {
//                            System.out.println("password and confirm password are not equal...try again");
//                        }
//                    } else {
//                        System.out.println("Invalid format...try again");
//                    }
//                }
//
//            } else {
//                System.out.println("Incorrect password...try again");
//            }
//        }
//    }
//
//    /**
//     * This method used to check entered password validation
//     *
//     * *@param password
//     * *@return boolean This returns isValid
//     */
//    public boolean checkPassword(String password) {
//        boolean isValid = true;
//        boolean flag = true;
//        boolean haveNumber = false;
//        boolean haveLetter = false;
//        for (int i = 0; i < password.length(); i++) {
//            if ((int) (password.charAt(i)) < 48 || ((int) (password.charAt(i)) >= 58 && (int) (password.charAt(i)) <= 64) || ((int) (password.charAt(i)) >= 91 && (int) (password.charAt(i)) <= 96) || (int) (password.charAt(i)) > 123) {
//                flag = false;
//                break;
//            }
//            if (!haveNumber) {
//                if ((int) (password.charAt(i)) >= 48 && (int) (password.charAt(i)) <= 57) {
//                    haveNumber = true;
//                }
//            }
//            if (!haveLetter) {
//                if (((int) (password.charAt(i)) >= 65 && (int) (password.charAt(i)) <= 90) || ((int) (password.charAt(i)) >= 97 && (int) (password.charAt(i)) <= 122)) {
//                    haveLetter = true;
//                }
//            }
//        }
//        if (!flag || !haveNumber || !haveLetter) {
//            isValid = false;
//        }
//        return isValid;
//    }
//
//    /**
//     * This method used to build a new server in program
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void buildServer() throws IOException, ClassNotFoundException, InterruptedException {
//        while (true) {
//            System.out.println("enter server name:");
//            System.out.println("enter 0 to back to menu:");
//            String serverName = scan.nextLine();
//            if (serverName.equals("0")) {
//                return;
//            }
//            Request request = new Request(RequestCode.CHECK_SERVER_NAME_DUPLICATION);
//            request.addData("serverName", serverName);
//            objectOutputStream.writeObject(request);
//            Response response = (Response) objectInputStream.readObject();
//            Boolean isDuplicate = (Boolean) response.getData("IsDuplicated");
//            if (isDuplicate) {
//                System.out.println("entered name is not unique!...try again");
//                continue;
//            }
//            Request request1 = new Request(RequestCode.ADD_SERVER);
//            request1.addData("serverName", serverName);
//            objectOutputStream.writeObject(request1);
//            System.out.println(serverName + " server created successfully");
//            break;
//        }
//    }
//
//    /**
//     * This method used to show list of servers
//     * then user choose a server
//     * if user is server's admin it shows admin menu
//     * if user is'nt server's admin it shows another menu base on user's role
//     * based on user's input do somehting
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void serverSettings() throws IOException, ClassNotFoundException {
//        while (true) {
//            Request request = new Request(RequestCode.SHOW_SERVERS);
//            objectOutputStream.writeObject(request);
//            Response response = (Response) objectInputStream.readObject();
//            String servers = (String) response.getData("servers");
//            String notifications = (String) response.getData("notifications");
//            if (servers.equalsIgnoreCase("")) {
//                System.out.println("you don't have any server ");
//                break;
//            }
//            if (!notifications.equals("")) {
//                System.out.println("Notifications:");
//                String[] notificationsArr = notifications.split("@@@");
//                for (int i = 0; i < notificationsArr.length; i++) {
//                    System.out.println((i + 1) + "." + notificationsArr[i]);
//                }
//            }
//            System.out.println("Servers");
//            String[] serversArr = servers.split("@@@");
//            for (int i = 0; i < serversArr.length; i++) {
//                System.out.println((i + 1) + "." + serversArr[i]);
//            }
//            System.out.println();
//            System.out.println("select a server:");
//            System.out.println("enter 0 to back to menu");
//            String selectedServer = scan.nextLine();
//            if (selectedServer.equals("0")) {
//                break;
//            }
//            int selectedServerNum;
//            try {
//                selectedServerNum = Integer.parseInt(selectedServer);
//            } catch (Exception e) {
//                System.out.println("Invalid Input...try again");
//                continue;
//            }
//            if (selectedServerNum > serversArr.length || selectedServerNum < 0) {
//                System.out.println("Invalid Input number...try again");
//                continue;
//            }
//            Request request1 = new Request(RequestCode.IS_SERVER_ADMIN);
//            request1.addData("index", selectedServerNum - 1);
//            objectOutputStream.writeObject(request1);
//            Response response1 = (Response) objectInputStream.readObject();
//            boolean isAdmin = (boolean) response1.getData("isAdmin");
//            if (isAdmin) {
//                while (true) {
//                    MenuHandler.adminMenu();
//                    String adminMenuSelected = scan.nextLine();
//                    switch (adminMenuSelected) {
//                        case "1":
//                            System.out.println("Enter new Name:");
//                            System.out.println("Enter 0 to back to menu");
//                            String newName = scan.nextLine();
//                            if (newName.equals("0")) {
//                                continue;
//                            }
//                            Request request2 = new Request(RequestCode.CHANGE_SERVER_NAME);
//                            request2.addData("index", selectedServerNum - 1);
//                            request2.addData("name", newName);
//                            objectOutputStream.writeObject(request2);
//                            Response response2 = (Response) objectInputStream.readObject();
//                            if (response2.getCode() == ResponseCode.NOT_CHANGE) {
//                                System.out.println("you didn't change name");
//                            } else if (response2.getCode() == ResponseCode.DUPLICATED) {
//                                System.out.println("There is a server with this name!");
//                            } else {
//                                System.out.println("server name changed successfully");
//                            }
//                            continue;
//                        case "2":
//                            addRole(selectedServerNum - 1);
//                            continue;
//                        case "3":
//                            removeMember(selectedServerNum - 1);
//                            continue;
//                        case "4":
//                            limitMember(selectedServerNum - 1);
//                            continue;
//                        case "5":
//                            banMember(selectedServerNum - 1);
//                            continue;
//                        case "6":
//                            membersList(selectedServerNum - 1);
//                            continue;
//                        case "7":
//                            createChannel(selectedServerNum - 1);
//                            continue;
//                        case "8":
//                            removeChannel(selectedServerNum - 1);
//                            continue;
//                        case "9":
//                            changeRole(selectedServerNum - 1);
//                            continue;
//                        case "10":
//                            removeServer(selectedServerNum - 1);
//                            return;
//                        case "11":
//                            showHistory(selectedServerNum-1);
//                            continue;
//                        case "12":
//                            seeLimitedMembers(selectedServerNum - 1);
//                            continue;
//                        case "13":
//                            seeBannedMembers(selectedServerNum - 1);
//                            continue;
//                        case "14":
//                            seeChannels(selectedServerNum - 1);
//                            continue;
//                        case "0":
//                            return;
//                        default:
//                            System.out.println("Invalid Input");
//                            continue;
//                    }
//                }
//
//            } else {
//                Request request2 = new Request(RequestCode.GET_PERMISSIONS);
//                request2.addData("index", selectedServerNum - 1);
//                objectOutputStream.writeObject(request2);
//                Response response2 = (Response) objectInputStream.readObject();
//                ArrayList<Integer> permissions = (ArrayList<Integer>) response2.getData("permissions");
//                if (permissions == null) {
//                    System.out.println("1.add a member");
//                    System.out.println("enter 0 to back to menu");
//                    String input = scan.nextLine();
//                    if (input.equalsIgnoreCase("0")) {
//                        return;
//                    } else if (input.equalsIgnoreCase("1")) {
//                        System.out.println("Enter username:");
//                        System.out.println("enter 0 to back to menu");
//                        String inputUsername = scan.nextLine();
//                        if (inputUsername.equals("0")) {
//                            return;
//                        }
//                        Request request3 = new Request(RequestCode.ADD_MEMBER);
//                        request3.addData("index", selectedServerNum - 1);
//                        request3.addData("username", inputUsername);
//                        objectOutputStream.writeObject(request3);
//                        Response response3 = (Response) objectInputStream.readObject();
//                        if (response3.getCode() == ResponseCode.NOT_EXIST) {
//                            System.out.println(inputUsername + " doesn't exist!");
//                        } else if (response3.getCode() == ResponseCode.IS_EXISTS) {
//                            System.out.println(inputUsername + " is in server!");
//                        } else {
//                            System.out.println(inputUsername + " added to server successfully");
//                        }
//                    } else {
//                        System.out.println("Invalid Input");
//                        return;
//                    }
//                }
//                while (true) {
//                    for (int i = 0; i < permissions.size(); i++) {
//                        System.out.print((i + 1) + ". ");
//                        String strPer = String.valueOf(DiscordServer.allPermissions[permissions.get(i) - 1]);
//                        String[] strPerArr = strPer.split("_");
//                        for (int j = 0; j < strPerArr.length; j++) {
//                            System.out.print(strPerArr[j].toLowerCase(Locale.ROOT) + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println(permissions.size() + 1 + ".add a member ");
//                    System.out.println("enter 0 to back to menu");
//                    String selectedWork = scan.nextLine();
//                    if (selectedWork.equals("0")) {
//                        break;
//                    }
//                    int selectedWorkNum = 0;
//                    try {
//                        selectedWorkNum = Integer.parseInt(selectedWork);
//                    } catch (Exception e) {
//                        System.out.println("Invalid Input");
//                        break;
//                    }
//                    if (selectedWorkNum < 0 || selectedWorkNum > permissions.size() + 1) {
//                        System.out.println("Invalid Input Number");
//                        break;
//
//                    }
//                    if (selectedWorkNum == permissions.size() + 1) {
//                        System.out.println("Enter username:");
//                        System.out.println("enter 0 to back to menu");
//                        String inputUsername = scan.nextLine();
//                        if (inputUsername.equals("0")) {
//                            continue;
//                        }
//                        Request request4 = new Request(RequestCode.ADD_MEMBER);
//                        request4.addData("index", selectedServerNum - 1);
//                        request4.addData("username", inputUsername);
//                        objectOutputStream.writeObject(request4);
//                        Response response3 = (Response) objectInputStream.readObject();
//                        if (response3.getCode() == ResponseCode.NOT_EXIST) {
//                            System.out.println(inputUsername + " doesn't exist!");
//                        } else if (response3.getCode() == ResponseCode.IS_EXISTS) {
//                            System.out.println(inputUsername + " is in server!");
//                        } else {
//                            System.out.println(inputUsername + " added to server successfully");
//                        }
//                    } else {
//                        Permission selectedPermission = DiscordServer.allPermissions[(permissions.get(selectedWorkNum - 1)) - 1];
//                        if (selectedPermission == Permission.CREATE_CHANNEL) {
//                            createChannel(selectedServerNum - 1);
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.REMOVE_CHANNEL) {
//                            removeChannel(selectedServerNum - 1);
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.REMOVE_MEMBER_FROM_SERVER) {
//                            removeMember2(selectedServerNum - 1);
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.BAN_A_MEMBER) {
//                            banMember(selectedServerNum - 1);
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.LIMIT_MEMBER_MESSAGE) {
//                            limitMember(selectedServerNum - 1);
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.CHANGING_SERVER_NAME) {
//                            System.out.println("Enter new Name:");
//                            System.out.println("Enter 0 to back to menu");
//                            String newName = scan.nextLine();
//                            if (newName.equals("0")) {
//                                continue;
//                            }
//                            Request request4 = new Request(RequestCode.CHANGE_SERVER_NAME);
//                            request4.addData("index", selectedServerNum - 1);
//                            request4.addData("name", newName);
//                            objectOutputStream.writeObject(request4);
//                            Response response4 = (Response) objectInputStream.readObject();
//                            if (response4.getCode() == ResponseCode.NOT_CHANGE) {
//                                System.out.println("you didn't change name");
//                            } else if (response4.getCode() == ResponseCode.DUPLICATED) {
//                                System.out.println("There is a server with this name!");
//                            } else {
//                                System.out.println("server name changed successfully");
//                            }
//                            continue;
//                        }
//                        else if (selectedPermission == Permission.SEE_CHAT_HISTORY) {
//                            showHistory(selectedServerNum - 1);
//                        }
//                        else if (selectedPermission == Permission.PIN_MESSAGE) {
//                            System.out.println("you can pin message in chats-->servers-->choose a channel-->pin a message");
//                            continue;
//                        }
//
//                    }
//                }
//
//            }
//        }
//    }
//
//
//
//
//    /**
//     * This method used to add a new role and select permissions or add a member to an existing role
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void addRole(int index) throws IOException, ClassNotFoundException {
//        System.out.println("Enter name of role:");
//        System.out.println("enter 0 to back to menu");
//        String roleName = scan.nextLine();
//        if (roleName.equals("0")) {
//            return;
//        }
//        Request request = new Request(RequestCode.CHECK_ROLE);
//        request.addData("name", roleName);
//        request.addData("index", index);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        boolean isExist = (boolean) response.getData("exist");
//        if (isExist) {
//            //no permission
//            System.out.println("enter username of someone you want take this role:");
//            System.out.println("enter 0 to back to menu");
//            String username = scan.nextLine();
//            if (username.equals("0")) {
//                return;
//            }
//            Request request1 = new Request(RequestCode.ASSIGN_ROLE);
//            request1.addData("username", username);
//            request1.addData("index", index);
//            request1.addData("roleName", roleName);
//            objectOutputStream.writeObject(request1);
//            Response response2 = (Response) objectInputStream.readObject();
//            if (response2.getCode() == ResponseCode.NOT_EXIST) {
//                System.out.println(username + " doesn't exist!");
//            } else if (response2.getCode() == ResponseCode.BEFORE_IN_ROLE) {
//                System.out.println(username + " has this role!");
//            } else if (response2.getCode() == ResponseCode.ROLE_ASSIGNED) {
//                System.out.println("role assigned successfully");
//            }
//        } else {
//            MenuHandler.permissionList();
//            System.out.println("how many permission?");
//            int permissionNum = 0;
//            try {
//                permissionNum = scan.nextInt();
//                scan.nextLine();
//            } catch (Exception e) {
//                System.out.println("Invalid input");
//                return;
//            }
//            if (permissionNum < 0 || permissionNum > 8) {
//                System.out.println("Invalid input number");
//            }
//            else if (permissionNum == 0) {
//                return;
//            }
//            else {
//                int counter = 1;
//                HashSet<Integer> tmpPermission = new HashSet<>();
//                int index2 = 0;
//                while (index2 < permissionNum) {
//                    System.out.println("#permission " + counter + " : ");
//                    int selectedPermission = 0;
//                    try {
//                        selectedPermission = scan.nextInt();
//                        scan.nextLine();
//                    } catch (Exception e) {
//                        System.out.println("Invalid Input");
//                        continue;
//                    }
//                    if (selectedPermission == 0) {
//                        return;
//                    }
//                    if (selectedPermission < 0 || selectedPermission > 8) {
//                        System.out.println("Invalid input number");
//                        continue;
//                    }
//                    int sizeBefore = tmpPermission.size();
//                    tmpPermission.add(selectedPermission);
//                    int sizeAfter = tmpPermission.size();
//                    if (sizeAfter == sizeBefore) {
//                        System.out.println("this permission has selected!");
//                    } else {
//                        counter++;
//                        index2++;
//                    }
//                }
//                Request request1 = new Request(RequestCode.ROLL_AND_PERMISSIONS);
//                request1.addData("permissions", tmpPermission);
//                request1.addData("roleName", roleName);
//                request1.addData("index", index);
//                objectOutputStream.writeObject(request1);
//                System.out.println("enter username of someone you want take this role:");
//                System.out.println("enter 0 to back to menu");
//                String username = scan.nextLine();
//                if (username.equals("0")) {
//                    return;
//                }
//                Request request2 = new Request(RequestCode.ASSIGN_ROLE);
//                request2.addData("username", username);
//                request2.addData("index", index);
//                request2.addData("roleName", roleName);
//                objectOutputStream.writeObject(request2);
//                Response response2 = (Response) objectInputStream.readObject();
//                if (response2.getCode() == ResponseCode.NOT_EXIST) {
//                    System.out.println(username + " doesn't exist!");
//                } else if (response2.getCode() == ResponseCode.BEFORE_IN_ROLE) {
//                    System.out.println(username + " have this role!");
//                } else if (response2.getCode() == ResponseCode.ROLE_ASSIGNED) {
//                    System.out.println("role assigned successfully");
//                }
//            }
//
//        }
//    }
//
//    /**
//     * This method used to remove a member by admin
//     *
//     * *@param index index of DiscordServer
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void removeMember(int index) throws IOException, ClassNotFoundException {
//        System.out.println("Enter username:");
//        System.out.println("Enter 0 to back menu");
//        String username = scan.nextLine();
//        if (username.equals("0")) {
//            return;
//        }
//        Request request = new Request(RequestCode.REMOVE_MEMBER);
//        request.addData("index", index);
//        request.addData("username", username);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        boolean find = (boolean) response.getData("find");
//        if (find) {
//            System.out.println(username + " removed successfully");
//        } else {
//            System.out.println("There is no " + username);
//        }
//    }
//
//    /**
//     * This method used to remove a member by users except admin
//     *
//     * *@param index index of DiscordServer
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void removeMember2(int index) throws IOException, ClassNotFoundException {
//        System.out.println("Enter username:");
//        System.out.println("Enter 0 to back menu");
//        String username = scan.nextLine();
//        if (username.equals("0")) {
//            return;
//        }
//        Request request = new Request(RequestCode.REMOVE_MEMBER2);
//        request.addData("index", index);
//        request.addData("username", username);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if (response.getCode() == ResponseCode.NOT_EXIST) {
//            System.out.println("There is no " + username);
//        } else if (response.getCode() == ResponseCode.ADMIN) {
//            System.out.println("you can't remove admin!");
//        } else {
//            System.out.println(username + " removed successfully");
//        }
//    }
//
//
//

//
//
//
//    /**
//     * This method used to change role's permissions(add or remove some permissions) by admin
//     * *@param index index of DiscordServer
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void changeRole(int index) throws IOException, ClassNotFoundException {
//        System.out.println("Enter role name:");
//        String roleName = scan.nextLine();
//        Request request = new Request(RequestCode.CHECK_ROLE);
//        request.addData("name", roleName);
//        request.addData("index", index);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        boolean isExist = (boolean) response.getData("exist");
//        if (!isExist) {
//            System.out.println("There is no " + roleName + " role in this server!");
//            return;
//        }
//        System.out.println("Permissions:");
//        Request request1 = new Request(RequestCode.ROLE_PERMISSIONS);
//        request1.addData("index", index);
//        request1.addData("name", roleName);
//        objectOutputStream.writeObject(request1);
//        Response response1 = (Response) objectInputStream.readObject();
//        ArrayList<Integer> permissions = (ArrayList<Integer>) response1.getData("permissions");
//        for (int i = 0; i < permissions.size(); i++) {
//            System.out.print((i + 1) + ". ");
//            String strPer = String.valueOf(DiscordServer.allPermissions[permissions.get(i) - 1]);
//            String[] strPerArr = strPer.split("_");
//            for (int j = 0; j < strPerArr.length; j++) {
//                System.out.print(strPerArr[j].toLowerCase(Locale.ROOT) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("\n");
//        System.out.println("1.add permission(s)");
//        System.out.println("2.remove permission(s)");
//        System.out.println("0.back");
//        String input = scan.nextLine();
//        if (input.equals("0"))
//            return;
//        int inputNum = 0;
//        try {
//            inputNum = Integer.parseInt(input);
//        } catch (Exception e) {
//            System.out.println("Invalid Input");
//            return;
//        }
//        if (inputNum < 0 || inputNum > 2) {
//            System.out.println("Invalid Input Number");
//            return;
//        }
//        if (inputNum == 1) {
//            System.out.println("how many permissions you want to add?");
//            System.out.println("enter 0 to back to menu");
//            String inputAddPermission = scan.nextLine();
//            if (inputAddPermission.equals("0")) {
//                return;
//            }
//            int inputAddPermissionNum = 0;
//            try {
//                inputAddPermissionNum = Integer.parseInt(inputAddPermission);
//            } catch (Exception e) {
//                System.out.println("Invalid Input");
//                return;
//            }
//            if (inputAddPermissionNum < 0) {
//                System.out.println("Invalid Input number");
//                return;
//            }
//            if (8 - (inputAddPermissionNum + permissions.size()) < 0) {
//                System.out.println("count of permissions you want to add is out of bound!");
//                return;
//            }
//            ArrayList<Integer> otherPermissionsIndexes = new ArrayList<>();
//            int i = 0;
//            for (int j = 0; j < 8; j++) {
//                if (i < permissions.size()) {
//                    if (permissions.get(i) != j + 1) {
//                        otherPermissionsIndexes.add(j + 1);
//                    } else {
//                        i++;
//                    }
//                } else {
//                    otherPermissionsIndexes.add(j + 1);
//                }
//            }
//            Collections.sort(otherPermissionsIndexes);
//            for (int k = 0; k < otherPermissionsIndexes.size(); k++) {
//                String enumToSting = String.valueOf(DiscordServer.allPermissions[otherPermissionsIndexes.get(k) - 1]);
//                String[] enumToStringArr = enumToSting.split("_");
//                System.out.print((k + 1) + ".");
//                for (int j = 0; j < enumToStringArr.length; j++) {
//                    System.out.print(enumToStringArr[j] + " ");
//                }
//                System.out.println();
//            }
//
//            int selectedPermissions = 1;
//            HashSet<Integer> tmpPermission = new HashSet<>();
//            while (selectedPermissions <= inputAddPermissionNum) {
//                System.out.print("#Permission " + selectedPermissions + " : ");
//                System.out.println("enter 0 to back to menu");
//                int selectedPermission = 0;
//                String str = scan.nextLine();
//                if (str.equals("0")) {
//                    return;
//                }
//                try {
//                    selectedPermission = Integer.parseInt(str);
//                } catch (Exception e) {
//                    System.out.println("Invalid Input");
//                    continue;
//                }
//                if (selectedPermission < 0 || selectedPermission > otherPermissionsIndexes.size()) {
//                    System.out.println("Invalid input number");
//                    continue;
//                }
//                int sizeBefore = tmpPermission.size();
//                tmpPermission.add(selectedPermission);
//                int sizeAfter = tmpPermission.size();
//                if (sizeAfter == sizeBefore) {
//                    System.out.println("this permission has selected!");
//                } else {
//                    selectedPermissions++;
//                }
//            }
//            Request request2 = new Request(RequestCode.CHANGE_PERMISSION_ADD);
//            request2.addData("index", index);
//            request2.addData("tmpPermission", tmpPermission);
//            request2.addData("otherPermissions", otherPermissionsIndexes);
//            request2.addData("name", roleName);
//            objectOutputStream.writeObject(request2);
//
//        } else {
//            System.out.println("how many permissions you want to remove?");
//            System.out.println("enter 0 to back to menu");
//            String inputRemovePermission = scan.nextLine();
//            if (inputRemovePermission.equals("0")) {
//                return;
//            }
//            int inputRemovePermissionNum = 0;
//            try {
//                inputRemovePermissionNum = Integer.parseInt(inputRemovePermission);
//            } catch (Exception e) {
//                System.out.println("Invalid Input");
//                return;
//            }
//            if (inputRemovePermissionNum < 0) {
//                System.out.println("Invalid Input number");
//                return;
//            }
//            if (inputRemovePermissionNum > permissions.size()) {
//                System.out.println("count of permissions you want to add is out of bound!");
//                return;
//            }
//            int selectedPermissions = 1;
//            HashSet<Integer> tmpPermission = new HashSet<>();
//            while (selectedPermissions <= inputRemovePermissionNum) {
//                System.out.print("#Permission " + selectedPermissions + " : ");
//                System.out.println("enter 0 to back to menu");
//                int selectedPermission = 0;
//                try {
//                    selectedPermission = scan.nextInt();
//                    scan.nextLine();
//                } catch (Exception e) {
//                    System.out.println("Invalid Input");
//                    continue;
//                }
//                if (selectedPermission == 0) {
//                    return;
//                }
//                if (selectedPermission < 0 || selectedPermission > permissions.size()) {
//                    System.out.println("Invalid input number");
//                    continue;
//                }
//                int sizeBefore = tmpPermission.size();
//                tmpPermission.add(selectedPermission);
//                int sizeAfter = tmpPermission.size();
//                if (sizeAfter == sizeBefore) {
//                    System.out.println("this permission has selected!");
//                } else {
//                    selectedPermissions++;
//                }
//            }
//            Request request3 = new Request(RequestCode.CHANGE_PERMISSION_REMOVE);
//            request3.addData("index", index);
//            request3.addData("tmpPermission", tmpPermission);
//            request3.addData("name", roleName);
//            objectOutputStream.writeObject(request3);
//
//        }
//    }
//
//
//
//
//
//
//    /**
//     * This method used to do something when user become offline
//     * *@param -
//     * *@return Nothing
//     */
//    public static void offlineActiveUser() {
//        Request request = new Request(RequestCode.OFFLINE_ACTIVE_USER);
//        try {
//            objectOutputStream.writeObject(request);
//        } catch (IOException e) {
//            //nothing :)
//        }
//    }
//
//    /**
//     * This method used to show list of limited members in a channel
//     *
//     * *@param index index of DiscordServer
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seeLimitedMembers(int index) throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_LIMITED_MEMBERS);
//        request.addData("index", index);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String limits = (String) response.getData("limited");
//        String[] limitsArr = limits.split("@@@");
//        for (int i = 0; i < limitsArr.length; i++) {
//            System.out.println(limitsArr[i]);
//        }
//    }
//
//    /**
//     * This method used to show list of banned members in a server
//     * *@param index index of DiscordServer
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seeBannedMembers(int index) throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_BANNED_MEMBERS);
//        request.addData("index", index);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String banneds = (String) response.getData("banned");
//        String[] banndsArr = banneds.split("@@@");
//        for (int i = 0; i < banndsArr.length; i++) {
//            System.out.println((i + 1) + ". " + banndsArr[i]);
//        }
//    }
//
//
//
//    /**
//     * This method used to select a picture as profile
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     */
//    public void selectPicture() throws IOException {
//        Request request = new Request(RequestCode.SEND_PICTURE);
//        System.out.println("Enter path of your picture:");
//        String path = scan.nextLine();
//        if(path.equals("0")) {
//            return;
//        }
//        File f = new File(path);
//        byte[] content = Files.readAllBytes(f.toPath());
//        request.addData("file", content);
//        request.addData(".", path.substring(path.indexOf(".")));
//        objectOutputStream.writeObject(request);
//    }
//
//    /**
//     * This method used to show chats menu and do something based on input!
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void chats() throws IOException, ClassNotFoundException {
//        while (true) {
//            MenuHandler.chats();
//            String chatSelect = scan.nextLine();
//            if (chatSelect.equals("0")) {
//                return;
//            }
//            else if (chatSelect.equals("1")) {
//                //private chats
//                seePVs();
//                System.out.println("Enter Username");
//                String chatName = scan.nextLine();
//                if (chatName.equals("0")) {
//                    continue;
//                } else {
//                    seePV(chatName);
//                    continue;
//                }
//            }
//            //servers
//            else if (chatSelect.equals("2")) {
//                Request request = new Request(RequestCode.SHOW_SERVERS);
//                objectOutputStream.writeObject(request);
//                Response response = (Response) objectInputStream.readObject();
//                String servers = (String) response.getData("servers");
//                if (servers.equalsIgnoreCase("")) {
//                    System.out.println("you don't have any server ");
//                    break;
//                }
//                String notifications = (String) response.getData("notifications");
//                if (!notifications.equals("")) {
//                    System.out.println("Notifications:");
//                    String[] notificationsArr = notifications.split("@@@");
//                    for (int i = 0; i < notificationsArr.length; i++) {
//                        System.out.println((i + 1) + "." + notificationsArr[i]);
//                    }
//                }
//                System.out.println("Servers:");
//                String[] serversArr = servers.split("@@@");
//                for (int i = 0; i < serversArr.length; i++) {
//                    System.out.println((i + 1) + "." + serversArr[i]);
//                }
//                System.out.println();
//                System.out.println("select a server:");
//                System.out.println("enter 0 to back to menu");
//                String selectedServer = scan.nextLine();
//                if (selectedServer.equals("0")) {
//                    break;
//                }
//                int selectedServerNum;
//                try {
//                    selectedServerNum = Integer.parseInt(selectedServer);
//                } catch (Exception e) {
//                    System.out.println("Invalid Input...try again");
//                    continue;
//                }
//                if (selectedServerNum > serversArr.length || selectedServerNum < 0) {
//                    System.out.println("Invalid Input number...try again");
//                    continue;
//                }
//                Request request2 = new Request(RequestCode.SEE_CHANNELS);
//                request2.addData("index", selectedServerNum - 1);
//                objectOutputStream.writeObject(request2);
//                Response response2 = (Response) objectInputStream.readObject();
//                String channels = (String) response2.getData("channels");
//                int seeMembersIndex = 1;
//                if (channels.equals("")) {
//                    System.out.println("There is no channel!");
//                } else {
//                    String[] channelsArr = channels.split("@@@");
//                    for (int i = 0; i < channelsArr.length; i++) {
//                        System.out.println((i + 1) + ". " + channelsArr[i]);
//                        seeMembersIndex++;
//                    }
//                }
//                System.out.println((seeMembersIndex) + ". " + "List of members");
//                System.out.println("select a channel");
//                System.out.println("enter 0 to back menu");
//                String input2 = scan.nextLine();
//                if (input2.equals("0")) {
//                    continue;
//                } else if (input2.equals(String.valueOf(seeMembersIndex))) {
//                    Request request3 = new Request(RequestCode.SEE_MEMBERS_LIST);
//                    request3.addData("index", selectedServerNum - 1);
//                    objectOutputStream.writeObject(request3);
//                    Response response3 = (Response) objectInputStream.readObject();
//                    String members = (String) response3.getData("members");
//                    String[] membersArr = members.split("@@@");
//                    for (int i = 0; i < membersArr.length; i++) {
//                        System.out.println((i + 1) + "." + membersArr[i]);
//                    }
//                    continue;
//                } else {
//                    int input2Num = 0;
//                    try {
//                        input2Num = Integer.parseInt(input2);
//                    } catch (Exception e) {
//                        System.out.println("Invalid Input");
//                        continue;
//                    }
//                    if (input2Num < 0 || input2Num > seeMembersIndex) {
//                        System.out.println("Invalid Input number");
//                        continue;
//                    } else {
//                        channelSettings(selectedServerNum - 1, input2Num - 1);
//                        continue;
//                    }
//                }
//            }
//            else if(chatSelect.equals("3")) {
//                Request request = new Request(RequestCode.SEE_PV_TO_SEND_FILE);
//                System.out.println("Enter Your Friend Name");
//                String friendName = scan.nextLine();
//                request.addData("friendName", friendName);
//                objectOutputStream.writeObject(request);
//                Response response = (Response) objectInputStream.readObject();
//                if(response.getCode() == ResponseCode.INVALID_USERNAME) {
//                    System.out.println("Invalid index...try again");
//                    return;
//                }
//                else if(response.getCode() == ResponseCode.YES) {
//                    User user2 = (User) response.getData("friendUser");
//                    sendFileInChat(user2);
//                    continue;
//                }
//            }
//
//            else {
//                System.out.println("Invalid Input");
//                continue;
//            }
//        }
//    }
//
//    /**
//     * This method used to see private chats
//     *
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seePVs() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_PVs);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String chatsNames = (String) response.getData("PVs");
//        if (!(chatsNames.isEmpty())) {
//            String[] chats = chatsNames.split("@@@");
//            for (int i = 0; i < chats.length; i++) {
//                System.out.println((i + 1) + "." + chats[i]);
//            }
//        } else {
//            System.out.println("There is no chat!");
//        }
//        System.out.println("0.Back");
//    }
//
//    /**
//     * This method used to enter a specific private chat and see old messages and send and receive new messages by invoking
//     * some methods
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     */
//    public void seePV(String username) throws IOException, ClassNotFoundException {
//        PrivateChat privateChat = null;
//        Request request = new Request(RequestCode.SEE_PV);
//        request.addData("chatName", username);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if (response.getCode() == ResponseCode.INVALID_USERNAME) {
//            System.out.println("Invalid input");
//        }
//        else if (response.getCode() == ResponseCode.YES) {
//            privateChat = (PrivateChat) response.getData("privateChat");
//            String messages = (String) response.getData("messagesFromThisChat");
//            System.out.println(messages);
//            listenFromPrivateChat(privateChat);
//            sendMessageInChat();
//        }
//    }
//
//    /**
//     * This method used to send message in private chat
//     * *@param -
//     * *@return Nothing
//     * *@throws IOException
//     */
//    public void sendMessageInChat() throws IOException {
//        while (true) {
//            Request request = new Request(RequestCode.SEND_MESSAGE);
//            String text = scan.nextLine();
//            request.addData("messageFromUser", text);
//            if (text.equals("0")) {
//                Request endRequest = new Request(RequestCode.CLOSE_CHAT);
//                objectOutputStream.writeObject(endRequest);
//                break;
//            } else {
//                objectOutputStream.writeObject(request);
//            }
//        }
//    }
//
//
//    /**
//     * This method used to receiving new messages in a private chat
//     * *@param privateChat
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     * *@return Nothing
//     */
//    public void listenFromPrivateChat(PrivateChat privateChat) throws IOException, ClassNotFoundException {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean b = true;
//                while(socket.isConnected() && b) {
//                    try {
//                        Response response = (Response) objectInputStream.readObject();
//
//                        if(response.getCode() == ResponseCode.CLOSE_THREAD) {
////                            Response response1 = (Response) objectInputStream.readObject();
//                            return;
//                        }
//                        PrivateChat currentChat = (PrivateChat) response.getData("thisCurrentChat");
//                        if(currentChat != privateChat) {
//                            b = false;
//                            return;
//                        }
//                        PrivateMessage message = (PrivateMessage) response.getData("messageReceivedCurrently");
//                        String text = message.getText();
//                        if (text.equals("0")) {
//                            return;
//                        }
//                        System.out.println(message);
//                    }
//                    catch (IOException | ClassNotFoundException e) {
//                        closeEverything(socket, objectInputStream, objectOutputStream);
//                    }
//
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * This method used to show channel menu and do something based on user's choices
//     * *@param serverIndex
//     * *@param channelIndex
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     * *@return Nothing
//     */
//    public void channelSettings(int serverIndex,int channelIndex) throws IOException, ClassNotFoundException {
//        while (true){
//            MenuHandler.channelMenu();
//            System.out.println("enter 0 to back menu");
//            String input1=scan.nextLine();
//            switch (input1) {
//                case "0":
//                    return;
//                case "1":
//                    //send message and see other messages in channel
//
//                    Request request0 = new Request(RequestCode.ENTER_CHANNEL);
//                    request0.addData("serverIndex", serverIndex);
//                    request0.addData("channelIndex", channelIndex);
//                    objectOutputStream.writeObject(request0);
//                    Response response = (Response) objectInputStream.readObject();
//                    if(response.getCode() == ResponseCode.INVALID_INDEX) {
//                        System.out.println("Invalid index...try again");
//                        return;
//                    }
//                    else if(response.getCode() == ResponseCode.CANT_SEE_MESSAGES) {
//                        System.out.println("You Cant See Messages");
//                        return;
//                    }
//                    else if(response.getCode() == ResponseCode.YES) {
//                        String channelMessages = (String) response.getData("channelMessages");
//                        Channel channel = (Channel) response.getData("currentChannel");
//                        DiscordServer server = (DiscordServer) response.getData("currentServer");
//                        System.out.println(channelMessages);
//                        listenFromChannel(channel, server);
//                        sendMessageInChannel();
//
//                    }
//                    continue;
//                case "2":
//                    //see pinned messages
//                    Request request=new Request(RequestCode.SEE_PINNED_MESSAGE);
//                    request.addData("serverIndex",serverIndex);
//                    request.addData("channelIndex",channelIndex);
//                    objectOutputStream.writeObject(request);
//                    Response response2= (Response) objectInputStream.readObject();
//                    String pin= (String) response2.getData("pin");
//                    if(pin.equals("")){
//                        System.out.println("There is no pinned message!");
//                        continue;
//                    }
//                    else{
//                        System.out.println("pinned messages:");
//                        String[] pinArr = pin.split("@@@");
//                        for (int i = 0; i < pinArr.length; i++) {
//                            System.out.println((i + 1) + "." + pinArr[i]);
//                        }
//                    }
//                    continue;
//                case "3":
//                    //pin a message
//                    Request request2=new Request(RequestCode.CAN_PIN);
//                    request2.addData("serverIndex",serverIndex);
//                    objectOutputStream.writeObject(request2);
//                    Response response1= (Response) objectInputStream.readObject();
//                    boolean canPin= (boolean) response1.getData("canPin");
//                    if(!canPin){
//                        System.out.println("you can't pin a message!");
//                        continue;
//                    }
//                    Request request1=new Request(RequestCode.SHOW_MESSAGES);
//                    request1.addData("serverIndex",serverIndex);
//                    request1.addData("channelIndex",channelIndex);
//                    objectOutputStream.writeObject(request1);
//                    Response response3= (Response) objectInputStream.readObject();
//                    String message= (String) response3.getData("message");
//                    if(message.equals("")){
//                        System.out.println("There is no message to pin!");
//                        continue;
//                    }
//                    else{
//                        System.out.println("not pinned messages:");
//                        String[] messageArr=message.split("@@@");
//                        for (int i = 0; i < messageArr.length; i++) {
//                            System.out.println((i+1)+". "+messageArr[i]);
//                        }
//                        System.out.println("select a message");
//                        System.out.println("enter 0 to back menu");
//                        String input2=scan.nextLine();
//                        if(input2.equals("0")){
//                            continue;
//                        }
//                        else{
//                            int input2Num=0;
//                            try {
//                                input2Num=Integer.parseInt(input2);
//                            }catch (Exception e){
//                                System.out.println("Invalid Input");
//                            }
//                            if(input2Num<0 || input2Num> messageArr.length){
//                                System.out.println("Invalid Input number");
//                                continue;
//                            }
//                            Request request3=new Request(RequestCode.PIN_MESSAGE);
//                            request3.addData("serverIndex",serverIndex);
//                            request3.addData("channelIndex",channelIndex);
//                            request3.addData("index",input2Num-1);
//                            objectOutputStream.writeObject(request3);
//                        }
//                    }
//                    continue;
//                case "4":
//                    //see messages and reactions
//                    Request request3=new Request(RequestCode.SEE_REACTIONS);
//                    request3.addData("serverIndex",serverIndex);
//                    request3.addData("channelIndex",channelIndex);
//                    objectOutputStream.writeObject(request3);
//                    Response response4= (Response) objectInputStream.readObject();
//                    String reaction= (String) response4.getData("reaction");
//                    if(reaction.equals("")){
//                        System.out.println("There is no message!");
//                        continue;
//                    }
//                    String[] reactionArr=reaction.split("@@@");
//                    for (int i = 0; i < reactionArr.length; i++) {
//                        System.out.println((i+1)+". "+reactionArr[i]);
//                    }
//                    continue;
//                case "5":
//                    //send reaction to a message
//                    Request request4=new Request(RequestCode.SHOW_ALL_MESSAGES);
//                    request4.addData("serverIndex",serverIndex);
//                    request4.addData("channelIndex",channelIndex);
//                    objectOutputStream.writeObject(request4);
//                    Response response5= (Response) objectInputStream.readObject();
//                    String message2= (String) response5.getData("message");
//                    if(message2.equals("")){
//                        System.out.println("There is no message to react!");
//                        continue;
//                    }
//                    else {
//                        System.out.println("messages:");
//                        String[] message2Arr = message2.split("@@@");
//                        for (int i = 0; i < message2Arr.length; i++) {
//                            System.out.println((i + 1) + ". " + message2Arr[i]);
//                        }
//                        System.out.println("select a message:");
//                        System.out.println("enter 0 to back menu");
//                        String input2=scan.nextLine();
//                        if(input2.equals("0")){
//                            continue;
//                        }
//                        else{
//                            int input2Num=0;
//                            try{
//                                input2Num=Integer.parseInt(input2);
//                            }catch (Exception e){
//                                System.out.println("Invalid Input");
//                                continue;
//                            }
//                            if(input2Num<0 || input2Num>message2Arr.length){
//                                System.out.println("Invalid Input Number");
//                                continue;
//                            }
//                            Request request5=new Request(RequestCode.CAN_REACT);
//                            request5.addData("serverIndex",serverIndex);
//                            request5.addData("channelIndex",channelIndex);
//                            request5.addData("index",input2Num-1);
//                            objectOutputStream.writeObject(request5);
//                            Response response6= (Response) objectInputStream.readObject();
//                            boolean beforeReact= (boolean) response6.getData("beforeReact");
//                            if(beforeReact){
//                                System.out.println("you sent reaction for this message");
//                                System.out.println("do you want to remove reaction?yes or no");
//                                System.out.println("enter 0 to back menu");
//                                String input3=scan.nextLine();
//                                if(input3.equals("0")){
//                                    continue;
//                                }
//                                else if(input3.equalsIgnoreCase("no")){
//                                    continue;
//                                }
//                                else if(input3.equalsIgnoreCase("yes")){
//                                    Request request6=new Request(RequestCode.REMOVE_OLD_REACT);
//                                    request6.addData("serverIndex",serverIndex);
//                                    request6.addData("channelIndex",channelIndex);
//                                    request6.addData("index",input2Num-1);
//                                    objectOutputStream.writeObject(request6);
//                                    sendReaction(serverIndex,channelIndex,input2Num-1);
//                                    continue;
//                                }
//                                else{
//                                    System.out.println("Invalid Input");
//                                    continue;
//                                }
//                            }
//                            else{
//                                sendReaction(serverIndex,channelIndex,input2Num-1);
//                                continue;
//                            }
//
//                        }
//                    }
//                case "6":
//                    Request request6 = new Request(RequestCode.ENTER_CHANNEL);
//                    request6.addData("serverIndex", serverIndex);
//                    request6.addData("channelIndex", channelIndex);
//                    objectOutputStream.writeObject(request6);
//                    Response response6 = (Response) objectInputStream.readObject();
//                    if(response6.getCode() == ResponseCode.INVALID_INDEX) {
//                        System.out.println("Invalid index...try again");
//                        return;
//                    }
//                    else if(response6.getCode() == ResponseCode.CANT_SEE_MESSAGES) {
//                        System.out.println("You Cant See Messages");
//                        return;
//                    }
//                    else if(response6.getCode() == ResponseCode.YES) {
//                        Channel channel = (Channel) response6.getData("currentChannel");
//                        DiscordServer server = (DiscordServer) response6.getData("currentServer");
//                        sendFileInChannel(server, channel);
//                        continue;
//                    }
//                default:
//                    System.out.println("Invalid Input");
//                    continue;
//            }
//        }
//    }
//    /**
//     * This method used to send a reaction to a specific message in channel
//     * *@param serverIndex
//     * *@param channelIndex
//     * *@param messageIndex
//     * *@throws IOException
//     * *@return Nothing
//     */
//    public void sendReaction(int serverIndex,int channelIndex,int messageIndex) throws IOException {
//        System.out.println("1.Like\n2.Dislike\n3.Smile");
//        System.out.println("enter 0 to back menu");
//        String input=scan.nextLine();
//        if(input.equals("0")){
//            return;
//        }
//        else if(input.equals("1") || input.equals("2") || input.equals("3")){
//            Request request=new Request(RequestCode.ADD_REACT);
//            request.addData("serverIndex",serverIndex);
//            request.addData("channelIndex",channelIndex);
//            request.addData("index",messageIndex);
//            request.addData("react",input);
//            objectOutputStream.writeObject(request);
//            System.out.println("react sent successfully");
//        }
//        else{
//            System.out.println("Invalid Input");
//        }
//    }
//
//    /**
//     * This method used to send message in channel
//     * *@param -
//     * *@throws IOException
//     * *@return Nothing
//     */
//    public void sendMessageInChannel() throws IOException {
//        while(true) {
//            Request request = new Request(RequestCode.SEND_MESSAGE_IN_CHANNEL);
//            String text = scan.nextLine();
//            request.addData("messageFromChannel", text);
//            if(text.equals("0")) {
//                Request endRequest = new Request(RequestCode.CLOSE_CHANNEL);
//                objectOutputStream.writeObject(endRequest);
//                break;
//            }
//            else {
//                objectOutputStream.writeObject(request);
//            }
//        }
//    }
//    /**
//     * This method used to receive messages in a channel
//     * *@param channel
//     * *@param server
//     * *@return Nothing
//     */
//    public void listenFromChannel(Channel channel, DiscordServer server) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean b = true;
//                while(socket.isConnected() && b) {
//                    try {
//                        Response response = (Response) objectInputStream.readObject();
//                        if(response.getCode() == ResponseCode.CANT_SEND_MESSAGE) {
//                            System.out.println("You can't send message");
//                            b = false;
//                            return;
//                        }
//                        if(response.getCode() == ResponseCode.CLOSE_THREAD) {
////                            Response response1 = (Response) objectInputStream.readObject();
//                            return;
//                        }
//                        Channel currentChannel = (Channel) response.getData("thisCurrentChannel");
//                        DiscordServer currentServer = (DiscordServer) response.getData("thisCurrentServer");
//                        if(currentChannel != channel || currentServer != server) {
//                            b = false;
//                            return;
//                        }
//                        ChannelMessage message = (ChannelMessage) response.getData("messageReceivedFromChannel");
//                        String text = message.getText();
//                        if (text.equals("0")) {
//                            return;
//                        }
//                        System.out.println(message);
//                    }
//                    catch (IOException | ClassNotFoundException e) {
//                        closeEverything(socket, objectInputStream, objectOutputStream);
//                    }
//
//                }
//            }
//        }).start();
//    }
//    /**
//     * This method used to show history of messages in a channel
//     * @param serverIndex
//     * *@throws IOException
//     * *@throws ClassNotFoundException
//     * *@return -
//     */
//    public void showHistory(int serverIndex) throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_CHANNELS);
//        request.addData("index", serverIndex);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        String channels = (String) response.getData("channels");
//        if (channels.equals("")) {
//            System.out.println("There is no channel!");
//        }
//        else {
//            String[] channelsArr = channels.split("@@@");
//            for (int i = 0; i < channelsArr.length; i++) {
//                System.out.println((i + 1) + ". " + channelsArr[i]);
//            }
//            System.out.println("select a channel");
//            System.out.println("enter 0 to back menu");
//            String input2 = scan.nextLine();
//            if (!input2.equals("0")) {
//                int input2Num;
//                try {
//                    input2Num = Integer.parseInt(input2);
//                } catch (Exception e) {
//                    System.out.println("Invalid Input");
//                    return;
//                }
//                if (input2Num < 0 || input2Num > channelsArr.length ) {
//                    System.out.println("Invalid Input number");
//                    return;
//                }
//                Request request4=new Request(RequestCode.SHOW_ALL_MESSAGES);
//                request4.addData("serverIndex",serverIndex);
//                request4.addData("channelIndex",input2Num-1);
//                objectOutputStream.writeObject(request4);
//                Response response5= (Response) objectInputStream.readObject();
//                String message2= (String) response5.getData("message");
//                if(message2.equals("")){
//                    System.out.println("There is no message!");
//                }
//                else {
//                    String[] message2Arr = message2.split("@@@");
//                    for (int i = 0; i < message2Arr.length; i++) {
//                        System.out.println((i + 1) + ". " + message2Arr[i]);
//                    }
//                }
//
//            }
//
//        }
//
//    }
//    /**
//     * this method check if this user has a chat with a friend send a file
//     * @param user
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//
//    public void sendFileInChat(User user) throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEND_FILE_IN_CHAT);
//        System.out.println("Enter path of your file");
//        String path = scan.nextLine();
//        if(path.equals("0")) {
//            return;
//        }
//        File f = new File(path);
//        byte[] content = Files.readAllBytes(f.toPath());
//        request.addData("file", content);
//        request.addData("friendName", user);
//        request.addData(".", path.substring(path.indexOf(".")));
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if(response.getCode() == ResponseCode.TRY_AGAIN) {
//            System.out.println("Can't Send File...Try Again");
//            return;
//        }
//        else if(response.getCode() == ResponseCode.REQUEST_OK) {
//            System.out.println("File Sent");
//            return;
//        }
//    }
//
//    /**
//     * this method send a file in channel
//     * @param server
//     * @param channel
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//
//    public void sendFileInChannel(DiscordServer server, Channel channel) throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEND_FILE_IN_CHANNEL);
//        System.out.println("Enter path of your file");
//        String path = scan.nextLine();
//        if(path.equals("0")) {
//            return;
//        }
//        File f = new File(path);
//        byte[] content = Files.readAllBytes(f.toPath());
//        request.addData("file", content);
//        request.addData("channelToSendFile", channel);
//        request.addData("serverToSendFile", server);
//        request.addData(".", path.substring(path.indexOf(".")));
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if(response.getCode() == ResponseCode.TRY_AGAIN) {
//            System.out.println("Can't Send File...Try Again");
//            return;
//        }
//        else if(response.getCode() == ResponseCode.REQUEST_OK) {
//            System.out.println("File Sent");
//            return;
//        }
//    }
//
//    public void seeReceivedFiles() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_RECEIVED_FILES);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if(response.getCode() == ResponseCode.NO_FILE) {
//            System.out.println("You haven't received any file");
//            return;
//        }
//        String fileNames = (String) response.getData("fileNames");
//        String[] names = fileNames.split("@@@");
//        for(int i = 0; i < names.length; i++) {
//            System.out.println((i + 1) + "." + names[i]);
//        }
//        System.out.println("Do you want to save a file or not?(yes or no)");
//        String choice = scan.nextLine();
//        if(choice.equals("0")) {
//            return;
//        }
//        else if(choice.equals("no")) {
//            return;
//        }
//        else if(choice.equals("yes")) {
//            System.out.println("Enter index");
//            String index = scan.nextLine();
//            if(index.equals("0")){
//                return;
//            }
//            int indexNum=-1;
//            try{
//                indexNum =Integer.parseInt(index);
//            }
//            catch (Exception e){
//                System.out.println("Invalid Input");
//                return;
//            }
//            if(indexNum>names.length || indexNum<0){
//                System.out.println("Invalid Input");
//                return;
//            }
//            indexNum-=1;
//            Request request1 = new Request(RequestCode.SAVE_FILE);
//            request1.addData("indexOfFile", indexNum);
//            objectOutputStream.writeObject(request1);
//            Response response1 = (Response) objectInputStream.readObject();
//            if(response1.getCode() == ResponseCode.SAVED_FILE) {
//                System.out.println("Your file saved");
//                return;
//            }
//            else {
//                System.out.println("Your file didn't saved...Try again");
//                return;
//            }
//        }
//
//    }
//
//    public void seeSavedFiles() throws IOException, ClassNotFoundException {
//        Request request = new Request(RequestCode.SEE_SAVED_FILES);
//        objectOutputStream.writeObject(request);
//        Response response = (Response) objectInputStream.readObject();
//        if(response.getCode() == ResponseCode.NO_SAVED_FILE) {
//            System.out.println("No saved File");
//            return;
//        }
//        else {
//            String sevedFiles = (String) response.getData("savedFiles");
//            String[] s = sevedFiles.split("@@@");
//            for(int i =0; i < s.length; i++) {
//                System.out.println((i + 1) + "." + s[i]);
//            }
//        }
//
//    }
//}
