package discord.discord1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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





    private String username;
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
        String[] membersArr=members.split("@@@");
        ArrayList<HBox> membersHboxArr=new ArrayList<>();
        for (int i = 0; i < membersArr.length; i++) {
            String tmp=membersArr[i];
            String[] tmpArr=tmp.split(" ");
            String username2=tmpArr[0];
            String status=tmpArr[1];
            HBox h=new HBox();
            h.setPrefHeight(40);
            h.setPrefWidth(148);
            h.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2f3136"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            AnchorPane ap=new AnchorPane();
            ap.setPrefHeight(40);
            ap.setPrefWidth(148);
            Circle pro=new Circle();
            pro.setRadius(15);
            pro.setCenterX(20);
            pro.setCenterY(20);
            if(getClass().getResourceAsStream(username2+".jpg")!=null || getClass().getResourceAsStream(username2+".png")!=null ){
                if(getClass().getResourceAsStream(username2+".jpg")!=null){
                    Image image1=new Image(getClass().getResourceAsStream(username2+".jpg"));
                    pro.setFill(new ImagePattern(image1));
                }
                else{
                    Image image1=new Image(getClass().getResourceAsStream(username2+".png"));
                    pro.setFill(new ImagePattern(image1));
                }
            }
            else{
                Image image1=new Image(getClass().getResourceAsStream("diimg.jpg"));
                pro.setFill(new ImagePattern(image1));
            }
            Circle back=new Circle();
            back.setRadius(8);
            back.setFill(Color.valueOf("#2f3136"));
            back.setCenterX(30);
            back.setCenterY(28);
            Circle statusCircle2=new Circle();
            statusCircle2.setRadius(5);
            statusCircle2.setCenterX(30);
            statusCircle2.setCenterY(28);
            if(status.equalsIgnoreCase("ONLINE")){
                statusCircle2.setFill(Paint.valueOf("#3ba55d"));
            }
            else if(status.equalsIgnoreCase("DO_NOT_DISTURB")){
                statusCircle2.setFill(Paint.valueOf("#e03f41"));
            }
            else if(status.equalsIgnoreCase("IDLE")){
                statusCircle2.setFill(Paint.valueOf("#eb9e19"));
            }
            else if(status.equalsIgnoreCase("INVISIBLE")){
                statusCircle2.setFill(Paint.valueOf("##747f8d"));
            }
            else{
                statusCircle2.setVisible(false);
            }
            Text usernameText2=new Text();
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
        for (int i = 0; i <membersHboxArr.size() ; i++) {
            usersVBox.getChildren().add(membersHboxArr.get(i));
        }







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
}
