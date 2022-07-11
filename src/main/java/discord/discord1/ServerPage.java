package discord.discord1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ServerPage implements Initializable {
    @FXML
    private Circle statusCircle;

    @FXML
    private Circle profileCircle;

    @FXML
    private Text usernameText;

    @FXML
    private Circle discordCircle;
    @FXML
    private VBox usersVBox;

    @FXML
    private VBox changeStatusVBox;
    @FXML
    private VBox channelVBox;

    @FXML
    private Text serverNameText;

    @FXML
    private HBox sendMessageHBox;

    @FXML
    private Label settingsLabel;

    private String serverName;

    private String username;

    private Object selectedChannel;
   private ArrayList<HBox> channelsHBox=new ArrayList<>();

   private ArrayList<MenuItem> settingMenuItems=new ArrayList<>();
   private boolean isAdmin;
   private Object selectedPermission;
   private ArrayList<String> permssions;
    @FXML
    private ScrollPane chatBox;

    @FXML
    private Pane dialogPane;

    @FXML
    private Text dialogText;

    @FXML
    private Button dialogButton;

    @FXML
    private TextField dialogTextField;

    @FXML
    private Text errorDialogText;

    @FXML
    private Pane roleDialog;

    @FXML
    private Text roleDialogText1;

    @FXML
    private Text roleDialogText2;

    @FXML
    private CheckBox banMemberCheckBox;

    @FXML
    private CheckBox changeNameCheckBox;

    @FXML
    private CheckBox chatHistoryCheckBox;

    @FXML
    private CheckBox createChannelCheckBox;

    @FXML
    private CheckBox limitMemberCheckBox;

    @FXML
    private CheckBox pinMessageCheckBox;

    @FXML
    private CheckBox removeChannelCheckBox;

    @FXML
    private CheckBox removeMemberCheckBox;







    @FXML
    void settingClick(MouseEvent event) {
        Stage stage= (Stage) statusCircle.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("profile-settings.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleDialog.setVisible(false);
        dialogPane.setVisible(false);
        UIRequest uiRequest=new UIRequest(UIRequestCode.GET_STATUS);
        UIResponse uiResponse;
        try {
            uiResponse=Client.process(uiRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Status userStatus= (Status) uiResponse.getData("status");
        if(userStatus==Status.ONLINE){
            statusCircle.setFill(Paint.valueOf("#3ba55d"));
        }
        else if(userStatus==Status.DO_NOT_DISTURB){
            statusCircle.setFill(Paint.valueOf("#e03f41"));
        }
        else if(userStatus==Status.IDLE){
            statusCircle.setFill(Paint.valueOf("#eb9e19"));
        }
        else if(userStatus==Status.INVISIBLE){
            statusCircle.setFill(Paint.valueOf("##747f8d"));
        }
        UIRequest uiRequest2=new UIRequest(UIRequestCode.GET_USERNAME);
        UIResponse uiResponse2;
        try {
            uiResponse2=Client.process(uiRequest2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        username= (String) uiResponse2.getData("name");
        if(getClass().getResourceAsStream(username+".jpg")!=null || getClass().getResourceAsStream(username+".png")!=null ){
            if(getClass().getResourceAsStream(username+".jpg")!=null){
                Image image=new Image(getClass().getResourceAsStream(username+".jpg"));
                profileCircle.setFill(new ImagePattern(image));
            }
            else{
                Image image=new Image(getClass().getResourceAsStream(username+".png"));
                profileCircle.setFill(new ImagePattern(image));
            }
        }
        else{
            Image image=new Image(getClass().getResourceAsStream("diimg.jpg"));
            profileCircle.setFill(new ImagePattern(image));
        }
        usernameText.setText(username);
        Image image=new Image(getClass().getResourceAsStream("diimg.jpg"));
        discordCircle.setFill(new ImagePattern(image));


        UIRequest uiRequest1=new UIRequest(UIRequestCode.GET_SERVER_USERS);
        uiRequest1.addData("username",username);
        UIResponse uiResponse1;
        try {
            uiResponse1=Client.process(uiRequest1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String members= (String) uiResponse1.getData("members");
        if(!members.equals("")) {
            String[] membersArr = members.split("@@@");
            ArrayList<HBox> membersHboxArr = new ArrayList<>();
            for (int i = 0; i < membersArr.length; i++) {
                String tmp = membersArr[i];
                String[] tmpArr = tmp.split(" ");
                String username2 = tmpArr[0];
                String status = tmpArr[1];
                HBox h = new HBox();
                h.setPrefHeight(40);
                h.setPrefWidth(148);
                h.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2f3136"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                AnchorPane ap = new AnchorPane();
                ap.setPrefHeight(40);
                ap.setPrefWidth(148);
                Circle pro = new Circle();
                pro.setRadius(15);
                pro.setCenterX(20);
                pro.setCenterY(20);
                if (getClass().getResourceAsStream(username2 + ".jpg") != null || getClass().getResourceAsStream(username2 + ".png") != null) {
                    if (getClass().getResourceAsStream(username2 + ".jpg") != null) {
                        Image image1 = new Image(getClass().getResourceAsStream(username2 + ".jpg"));
                        pro.setFill(new ImagePattern(image1));
                    } else {
                        Image image1 = new Image(getClass().getResourceAsStream(username2 + ".png"));
                        pro.setFill(new ImagePattern(image1));
                    }
                } else {
                    Image image1 = new Image(getClass().getResourceAsStream("diimg.jpg"));
                    pro.setFill(new ImagePattern(image1));
                }
                Circle back = new Circle();
                back.setRadius(8);
                back.setFill(Color.valueOf("#2f3136"));
                back.setCenterX(30);
                back.setCenterY(28);
                Circle statusCircle2 = new Circle();
                statusCircle2.setRadius(5);
                statusCircle2.setCenterX(30);
                statusCircle2.setCenterY(28);
                if (status.equalsIgnoreCase("ONLINE")) {
                    statusCircle2.setFill(Paint.valueOf("#3ba55d"));
                } else if (status.equalsIgnoreCase("DO_NOT_DISTURB")) {
                    statusCircle2.setFill(Paint.valueOf("#e03f41"));
                } else if (status.equalsIgnoreCase("IDLE")) {
                    statusCircle2.setFill(Paint.valueOf("#eb9e19"));
                } else if (status.equalsIgnoreCase("INVISIBLE")) {
                    statusCircle2.setFill(Paint.valueOf("#747f8d"));
                } else {
                    statusCircle2.setVisible(false);
                    back.setVisible(false);
                }
                Text usernameText2 = new Text();
                Font font = Font.font("System", FontWeight.BOLD, 12);
                usernameText2.setFont(font);
                usernameText2.setX(40);
                usernameText2.setY(23);
                usernameText2.setText(username2);
                usernameText2.setFill(Color.WHITE);
                ap.getChildren().add(pro);
                ap.getChildren().add(back);
                ap.getChildren().add(statusCircle2);
                ap.getChildren().add(usernameText2);
                h.getChildren().add(ap);
                membersHboxArr.add(h);
            }
            for (int i = 0; i < membersHboxArr.size(); i++) {
                usersVBox.getChildren().add(membersHboxArr.get(i));
            }
        }
        changeStatusVBox.setVisible(false);
        UIRequest uiRequest3=new UIRequest(UIRequestCode.SEE_CHANNELS);
        UIResponse uiResponse3;
        try {
            uiResponse3=Client.process(uiRequest3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String channels= (String) uiResponse3.getData("channels");
        if(!channels.equals("")){
            String[] channelsArr=channels.split("@@@");
            for (int i = 0; i <channelsArr.length ; i++) {
                String channelName=channelsArr[i];
                HBox h=new HBox();
                h.setPrefHeight(30);
                h.setPrefWidth(186);
                h.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2f3136"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                AnchorPane ap=new AnchorPane();
                ap.setPrefHeight(30);
                ap.setPrefWidth(186);
                ImageView imageView=new ImageView();
                imageView.setFitWidth(30);
                imageView.setFitHeight(20);
                Image channelImage=new Image(getClass().getResourceAsStream("channel.png"));
                imageView.setImage(channelImage);
                Text channelText=new Text();
                Font font = Font.font("System", FontWeight.BOLD, 14);
                channelText.setFont(font);
                channelText.setX(30);
                channelText.setY(12);
                channelText.setText(channelName);
                channelText.setFill(Paint.valueOf("#96989d"));
                ap.getChildren().add(imageView);
                ap.getChildren().add(channelText);
                h.getChildren().add(ap);
                h.setOnMouseClicked((MouseEvent e)->{
                    selectedChannel=e.getSource();
                    findChannel();
                });
                channelsHBox.add(h);
            }
            for (int i = 0; i <channelsHBox.size() ; i++) {
                channelVBox.getChildren().add(channelsHBox.get(i));
            }
        }
        UIRequest uiRequest4=new UIRequest(UIRequestCode.GET_SERVER_NAME);
        UIResponse uiResponse4;
        try {
            uiResponse4=Client.process(uiRequest4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        serverName= (String) uiResponse4.getData("name");
        serverNameText.setText(serverName);

        sendMessageHBox.setVisible(false);
        UIRequest uiRequest5=new UIRequest(UIRequestCode.IS_ADMIN);
        UIResponse uiResponse5;
        try {
            uiResponse5=Client.process(uiRequest5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        isAdmin= (boolean) uiResponse5.getData("isAdmin");
        if(isAdmin){
            ContextMenu adminSettingsMenu=new ContextMenu();
            MenuItem menuItem1=new MenuItem("change server name");
            menuItem1.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem1);
            settingMenuItems.add(menuItem1);

            MenuItem menuItem2=new MenuItem("Add role");
            menuItem2.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem2);
            settingMenuItems.add(menuItem2);

            MenuItem menuItem3=new MenuItem("limit member");
            menuItem3.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem3);
            settingMenuItems.add(menuItem3);

            MenuItem menuItem4=new MenuItem("ban a member");
            menuItem4.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem4);
            settingMenuItems.add(menuItem4);

            MenuItem menuItem5=new MenuItem("create channel");
            menuItem5.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem5);
            settingMenuItems.add(menuItem5);

            MenuItem menuItem6=new MenuItem("remove channel");
            menuItem6.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem6);
            settingMenuItems.add(menuItem6);

            MenuItem menuItem7=new MenuItem("change role");
            menuItem7.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem7);
            settingMenuItems.add(menuItem7);

            MenuItem menuItem8=new MenuItem("remove server");
            menuItem8.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem8);
            settingMenuItems.add(menuItem8);

            MenuItem menuItem9=new MenuItem("see chat history");
            menuItem9.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            adminSettingsMenu.getItems().add(menuItem9);
            settingMenuItems.add(menuItem9);
            settingsLabel.setContextMenu(adminSettingsMenu);
        }
        else{
            UIRequest uiRequest6=new UIRequest(UIRequestCode.GET_PERMISSIONS);
            UIResponse uiResponse6;
            try {
              uiResponse6=Client.process(uiRequest6);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
          permssions= (ArrayList<String>) uiResponse6.getData("permissions");
          ContextMenu contextMenu=new ContextMenu();
          MenuItem menuItem1=new MenuItem("add a member");
          menuItem1.setOnAction((ActionEvent e)->{
                selectedPermission=e.getSource();
                findPermission();
            });
            contextMenu.getItems().add(menuItem1);
            settingMenuItems.add(menuItem1);
            if(permssions!=null){
                for (int i = 0; i < permssions.size(); i++) {
                    MenuItem menuItem=new MenuItem(permssions.get(i));
                    menuItem.setOnAction((ActionEvent e)->{
                        selectedPermission=e.getSource();
                        findPermission();
                    });
                    contextMenu.getItems().add(menuItem);
                    settingMenuItems.add(menuItem);
                }
            }
            settingsLabel.setContextMenu(contextMenu);
        }
        chatBox.setVisible(false);
    }
    @FXML
    void discordCircleClick(MouseEvent event) {
        Stage stage= (Stage) statusCircle.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }
    @FXML
    void changeStatusDisturbClick(MouseEvent event) throws IOException, ClassNotFoundException {
        UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_STATUS);
        uiRequest.addData("num",3);
        Client.process(uiRequest);
        statusCircle.setFill(Paint.valueOf("#e03f41"));
        changeStatusVBox.setVisible(false);
    }

    @FXML
    void changeStatusIdleClick(MouseEvent event) throws IOException, ClassNotFoundException {
        UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_STATUS);
        uiRequest.addData("num",2);
        Client.process(uiRequest);
        statusCircle.setFill(Paint.valueOf("#eb9e19"));
        changeStatusVBox.setVisible(false);
    }

    @FXML
    void changeStatusInvisibleClick(MouseEvent event) throws IOException, ClassNotFoundException {
        UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_STATUS);
        uiRequest.addData("num",4);
        Client.process(uiRequest);
        statusCircle.setFill(Paint.valueOf("#747f8d"));
        changeStatusVBox.setVisible(false);
    }

    @FXML
    void changeStatusOnlineClick(MouseEvent event) throws IOException, ClassNotFoundException {
        UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_STATUS);
        uiRequest.addData("num",1);
        Client.process(uiRequest);
        statusCircle.setFill(Paint.valueOf("#3ba55d"));
        changeStatusVBox.setVisible(false);
    }
    @FXML
    void profileClick(MouseEvent event) {
        if(changeStatusVBox.isVisible()){
            changeStatusVBox.setVisible(false);
        }
        else{
            changeStatusVBox.setVisible(true);
        }
    }
    public void findChannel(){
        int channelIndex=-1;
        for (int i = 0; i <channelsHBox.size() ; i++) {
            if(selectedChannel.equals(channelsHBox.get(i))){
               channelIndex=i;
               break;
            }
        }
    }
    public void findPermission(){
        for (int i = 0; i < settingMenuItems.size(); i++) {
            if(selectedPermission.equals(settingMenuItems.get(i))){
                if(settingMenuItems.get(i).getText().equalsIgnoreCase("create channel")){
                   chatBox.setVisible(false);
                   createChannel();
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("remove channel")){
                    if(channelsHBox.size()!=0){
                        chatBox.setVisible(false);
                        removeChannel();
                    }
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("add a member")){
                    chatBox.setVisible(false);
                    addMember();
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("change server name")){
                    chatBox.setVisible(false);
                    changeServerName();
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("limit member")){
                    if(channelsHBox.size()!=0) {
                        chatBox.setVisible(false);
                        limitMember();
                    }
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("ban a member")){
                    chatBox.setVisible(false);
                    banMember();
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("remove server")){
                    UIRequest uiRequest=new UIRequest(UIRequestCode.REMOVE_SERVER);
                    try {
                        Client.process(uiRequest);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage= (Stage) statusCircle.getScene().getWindow();
                    Parent root= null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(new Scene(root));
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("Add role")){
                    chatBox.setVisible(false);
                    addRole();
                }
                else if(settingMenuItems.get(i).getText().equalsIgnoreCase("change role")){
                    chatBox.setVisible(false);
                    changeRole();
                }



































            }


        }


    }
    public void createChannel(){
        dialogPane.setVisible(true);
        dialogText.setText("Channel Name");
        dialogButton.setText("Create Channel");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void removeChannel(){
        dialogPane.setVisible(true);
        dialogText.setText("Channel Name");
        dialogButton.setText("Remove Channel");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void addMember(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter Username");
        dialogButton.setText("Add to server");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void changeServerName(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter new Name");
        dialogButton.setText("Change");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void limitMember(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter Channel's name");
        dialogButton.setText("limit member");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void banMember(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter username");
        dialogButton.setText("ban member");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void addRole(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter Role name");
        dialogButton.setText("add role");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    public void changeRole(){
        dialogPane.setVisible(true);
        dialogText.setText("Enter Role name");
        dialogButton.setText("change permissions");
        errorDialogText.setText("");
        dialogTextField.setText("");
    }
    @FXML
    void exitImageClick(MouseEvent event) {
        dialogPane.setVisible(false);
    }
    @FXML
    void dialogButtonClick(MouseEvent event) throws IOException, ClassNotFoundException {
        if(dialogButton.getText().equalsIgnoreCase("Create Channel")){
            String enteredChannelName=dialogTextField.getText();
            if(enteredChannelName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CREATE_CHANNEL);
                uiRequest.addData("name",enteredChannelName);
                UIResponse uiResponse=Client.process(uiRequest);
                boolean isDuplicated= (boolean) uiResponse.getData("isDuplicated");
                if(isDuplicated){
                    errorDialogText.setText("Duplicated Name");
                }
                else{
                    HBox h=new HBox();
                    h.setPrefHeight(30);
                    h.setPrefWidth(186);
                    h.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2f3136"),
                            CornerRadii.EMPTY,
                            Insets.EMPTY)));
                    AnchorPane ap=new AnchorPane();
                    ap.setPrefHeight(30);
                    ap.setPrefWidth(186);
                    ImageView imageView=new ImageView();
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(20);
                    Image channelImage=new Image(getClass().getResourceAsStream("channel.png"));
                    imageView.setImage(channelImage);
                    Text channelText=new Text();
                    Font font = Font.font("System", FontWeight.BOLD, 14);
                    channelText.setFont(font);
                    channelText.setX(30);
                    channelText.setY(12);
                    channelText.setText(enteredChannelName);
                    channelText.setFill(Paint.valueOf("#96989d"));
                    ap.getChildren().add(imageView);
                    ap.getChildren().add(channelText);
                    h.getChildren().add(ap);
                    h.setOnMouseClicked((MouseEvent e)->{
                        selectedChannel=e.getSource();
                        findChannel();
                    });
                    channelsHBox.add(h);
                    channelVBox.getChildren().add(h);
                    dialogPane.setVisible(false);
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("Remove Channel")){
            String enteredChannelName=dialogTextField.getText();
            if(enteredChannelName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.REMOVE_CHANNEL);
                uiRequest.addData("name",enteredChannelName);
                UIResponse uiResponse=Client.process(uiRequest);
                boolean isExist= (boolean) uiResponse.getData("isExist");
                if(!isExist){
                    errorDialogText.setText("Invalid Name");
                }
                else{
                    int channelIndexHBox=0;
                    for (int i = 0; i < channelVBox.getChildren().size(); i++) {
                        HBox a= (HBox) channelVBox.getChildren().get(i);
                        AnchorPane b= (AnchorPane) a.getChildren().get(0);
                        Text c= (Text) b.getChildren().get(1);
                        if(c.getText().equals(enteredChannelName)) {
                            break;
                        }
                        channelIndexHBox++;
                    }
                    channelVBox.getChildren().remove(channelIndexHBox);
                    channelsHBox.remove(channelIndexHBox);
                    dialogPane.setVisible(false);
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("Add to server")){
            String enteredUsername=dialogTextField.getText();
            if(enteredUsername.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.ADD_MEMBER);
                uiRequest.addData("name",enteredUsername);
                UIResponse uiResponse=Client.process(uiRequest);
                if(uiResponse.getCode()==UIResponseCode.NOT_EXIST){
                    errorDialogText.setText("Invalid Username!");
                }
                else if(uiResponse.getCode()==UIResponseCode.IS_EXISTS){
                    errorDialogText.setText(enteredUsername+" added before!");
                }
                else{
                    dialogPane.setVisible(false);
                    //////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    ////////////////////////////////////
                    // ///////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    ////////////////////////////////////.////////////////////////////////////////
                    /////////////////////////////////.///////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("Change")){
            String enteredName=dialogTextField.getText();
            if(enteredName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_SERVER_NAME);
                uiRequest.addData("name",enteredName);
                UIResponse uiResponse=Client.process(uiRequest);
                if(uiResponse.getCode()==UIResponseCode.NOT_CHANGE){
                    errorDialogText.setText("you didn't change server's name");
                }
                else if(uiResponse.getCode()==UIResponseCode.DUPLICATED){
                    errorDialogText.setText("Duplicated name");
                }
                else{
                    serverNameText.setText(enteredName);
                    dialogPane.setVisible(false);
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("limit member")){
            String enteredChannelName=dialogTextField.getText();
            if(enteredChannelName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CHANNEL_EXIST);
                uiRequest.addData("name",enteredChannelName);
                UIResponse uiResponse=Client.process(uiRequest);
                if(uiResponse.getCode()==UIResponseCode.NOT_EXIST){
                    errorDialogText.setText("Invalid Channel Name");
                }
                else{
                    dialogText.setText("enter username");
                    dialogTextField.setText("");
                    String enteredUsername=dialogTextField.getText();
                    UIRequest uiRequest2=new UIRequest(UIRequestCode.LIMIT_MEMBER);
                    uiRequest2.addData("username",enteredUsername);
                    uiRequest2.addData("channel",enteredChannelName);
                    UIResponse uiResponse2=Client.process(uiRequest2);
                    if(uiResponse2.getCode()==UIResponseCode.NOT_EXIST){
                        errorDialogText.setText("Invalid Username");
                    }
                    else if(uiResponse2.getCode()==UIResponseCode.NOT_IN_SERVER){
                        errorDialogText.setText("This user is not in server!");
                    }
                    else if(uiResponse2.getCode()==UIResponseCode.LIMIT_BEFORE){
                        errorDialogText.setText("This user limitted before!");
                    }
                    else if(uiResponse2.getCode()==UIResponseCode.BAN_BEFORE){
                        errorDialogText.setText("This user banned before!");
                    }
                    else{
                        dialogPane.setVisible(false);
                    }

                }

            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("ban member")){
            String enteredUsername=dialogTextField.getText();
            if(enteredUsername.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.BAN_MEMBER);
                uiRequest.addData("username",enteredUsername);
                UIResponse uiResponse=Client.process(uiRequest);
                if(uiResponse.getCode()==UIResponseCode.NOT_EXIST){
                    errorDialogText.setText("Invalid Username");
                }
                else if(uiResponse.getCode()==UIResponseCode.NOT_IN_SERVER){
                    errorDialogText.setText("This user is not in server!");
                }
                else if(uiResponse.getCode()==UIResponseCode.BAN_BEFORE){
                    errorDialogText.setText("This user banned before!");
                }
                else{
                    dialogPane.setVisible(false);
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("add role")){
            String enteredRoleName=dialogTextField.getText();
            if(enteredRoleName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CHECK_ROLE);
                uiRequest.addData("role",enteredRoleName);
                UIResponse uiResponse=Client.process(uiRequest);
                boolean isExist= (boolean) uiResponse.getData("exist");
                if(isExist){
                    dialogText.setText("Enter username");
                    dialogButton.setText("Assign Role");
                    errorDialogText.setText("");
                    dialogTextField.setText("");
                    String enteredUsername=dialogTextField.getText();
                    if(enteredUsername.equals("")){
                        errorDialogText.setText("enter a name!");
                    }
                    else{
                        UIRequest uiRequest1=new UIRequest(UIRequestCode.ASSIGN_EXIST_ROLE);
                        uiRequest1.addData("role",enteredRoleName);
                        uiRequest1.addData("name",enteredUsername);
                        UIResponse uiResponse1=Client.process(uiRequest1);
                        if(uiResponse1.getCode()==UIResponseCode.NOT_EXIST){
                            errorDialogText.setText("Invalid Username");
                        }
                        else if(uiResponse1.getCode()==UIResponseCode.BEFORE_IN_ROLE){
                            errorDialogText.setText(enteredUsername+" has this role!");
                        }
                        else{
                            roleDialog.setVisible(false);
                        }
                    }
                }
                else{
                    dialogPane.setVisible(false);
                    roleDialog.setVisible(true);
                    roleDialogText1.setText(enteredRoleName+" is a new Role!");
                    roleDialogText2.setText("Select permissions");
                }
            }
        }
        else if(dialogButton.getText().equalsIgnoreCase("change permissions")){
            String enteredRoleName=dialogTextField.getText();
            if(enteredRoleName.equals("")){
                errorDialogText.setText("enter a name!");
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CHECK_ROLE);
                uiRequest.addData("role",enteredRoleName);
                UIResponse uiResponse=Client.process(uiRequest);
                boolean isExist= (boolean) uiResponse.getData("exist");
                if(!isExist){
                    errorDialogText.setText("Invalid Role name");
                }
                else{
                    UIRequest uiRequest1=new UIRequest(UIRequestCode.GET_ROLE_PERMISSIONS);
                    uiRequest1.addData("role",enteredRoleName);
                    UIResponse uiResponse1=Client.process(uiRequest1);
                    ArrayList<Permission> rolePermssions= (ArrayList<Permission>) uiResponse1.getData("permissions");
                    for(Permission p:rolePermssions){
                        if(p==Permission.CREATE_CHANNEL){
                            createChannelCheckBox.setSelected(true);
                        }
                        else if(p==Permission.REMOVE_CHANNEL){
                            removeChannelCheckBox.setSelected(true);
                        }
                        else if(p==Permission.REMOVE_MEMBER_FROM_SERVER){
                            removeMemberCheckBox.setSelected(true);
                        }
                        else if(p==Permission.BAN_A_MEMBER){
                            banMemberCheckBox.setSelected(true);
                        }
                        else if(p==Permission.LIMIT_MEMBER){
                            limitMemberCheckBox.setSelected(true);
                        }
                        else if(p==Permission.CHANGE_SERVER_NAME){
                            changeNameCheckBox.setSelected(true);
                        }
                        else if(p==Permission.SEE_CHAT_HISTORY){
                            chatHistoryCheckBox.setSelected(true);
                        }
                        else{
                            pinMessageCheckBox.setSelected(true);
                        }
                    }
                    dialogPane.setVisible(false);
                    roleDialog.setVisible(true);
                    roleDialogText1.setText(enteredRoleName);
                    roleDialogText2.setText("Select permissions");

                }

            }
        }
    }
    @FXML
    void exitRoleDialogClick(MouseEvent event) {
      roleDialog.setVisible(false);
    }
    @FXML
    void doneRoleDialogClick(MouseEvent event) throws IOException, ClassNotFoundException {
        HashSet<Integer> tmpPermission = new HashSet<>();
        ArrayList<Integer> permissionIndexes=new ArrayList<>();
        ArrayList<Permission> permissionsArr=new ArrayList<>();
        if(createChannelCheckBox.isSelected()){
            tmpPermission.add(1);
            permissionIndexes.add(1);
            permissionsArr.add(Permission.CREATE_CHANNEL);
        }
        if(removeChannelCheckBox.isSelected()){
            tmpPermission.add(2);
            permissionIndexes.add(2);
            permissionsArr.add(Permission.REMOVE_CHANNEL);
        }
        if(removeMemberCheckBox.isSelected()){
            tmpPermission.add(3);
            permissionIndexes.add(3);
            permissionsArr.add(Permission.REMOVE_MEMBER_FROM_SERVER);
        }
        if(banMemberCheckBox.isSelected()){
            tmpPermission.add(4);
            permissionIndexes.add(4);
            permissionsArr.add(Permission.BAN_A_MEMBER);
        }
        if(limitMemberCheckBox.isSelected()){
            tmpPermission.add(5);
            permissionIndexes.add(5);
            permissionsArr.add(Permission.LIMIT_MEMBER);
        }
        if(changeNameCheckBox.isSelected()){
            tmpPermission.add(6);
            permissionIndexes.add(6);
            permissionsArr.add(Permission.CHANGE_SERVER_NAME);

        }
        if(chatHistoryCheckBox.isSelected()){
            tmpPermission.add(7);
            permissionIndexes.add(7);
            permissionsArr.add(Permission.SEE_CHAT_HISTORY);
        }
        if(pinMessageCheckBox.isSelected()){
            tmpPermission.add(8);
            permissionIndexes.add(8);
            permissionsArr.add(Permission.PIN_MESSAGE);
        }
        String s=roleDialogText1.getText();
        String[] sArr=s.split(" ");
        if(sArr.length==1){
            UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_PERMISSION);
            String roleName=sArr[0];
            uiRequest.addData("role",roleName);
            uiRequest.addData("permissionsIndex",permissionIndexes);
            uiRequest.addData("permissionsArr",permissionsArr);
            Client.process(uiRequest);
            roleDialog.setVisible(false);
        }
        else{
            UIRequest uiRequest=new UIRequest(UIRequestCode.ROLL_AND_PERMISSIONS);
            String roleName=sArr[0];
            uiRequest.addData("role",roleName);
            uiRequest.addData("permissions",tmpPermission);
            Client.process(uiRequest);
            roleDialog.setVisible(false);
            assignRole(roleName);
        }
    }
    public void assignRole(String roleName) throws IOException, ClassNotFoundException {
        dialogPane.setVisible(true);
        dialogText.setText("Enter Username");
        dialogButton.setText("Assign Role");
        errorDialogText.setText("");
        dialogTextField.setText("");
        String enteredUsername=dialogTextField.getText();
        if(enteredUsername.equals("")){
            errorDialogText.setText("enter a username!");
        }
        else{
            UIRequest uiRequest=new UIRequest(UIRequestCode.ASSIGN_ROLE);
            uiRequest.addData("role",roleName);
            uiRequest.addData("name",username);
            UIResponse uiResponse=Client.process(uiRequest);
            if(uiResponse.getCode()==UIResponseCode.NOT_EXIST){
                errorDialogText.setText("Invalid Username");
            }
            else if(uiResponse.getCode()==UIResponseCode.BEFORE_IN_ROLE){
                errorDialogText.setText(enteredUsername+" has this role!");
            }
            else{
                dialogPane.setVisible(false);
            }
        }

    }
}


