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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private VBox v;




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
//        HBox h1=new HBox();
//        h1.setBackground(new Background(new BackgroundFill(Color.RED,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//        h1.setPrefHeight(100);
//        Circle c=new Circle();
//        c.setRadius(10);
//        c.setFill(Color.valueOf("#ffaa12"));
//        h1.getChildren().add(c);
//        HBox h2=new HBox();
//        h2.setBackground(new Background(new BackgroundFill(Color.BLUE,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//        h2.setPrefHeight(100);
//        Circle c2=new Circle();
//        c2.setRadius(10);
//        c2.setFill(Color.valueOf("#ffaa12"));
//        h2.getChildren().add(c2);
//        HBox h3=new HBox();
//        h3.setBackground(new Background(new BackgroundFill(Color.PINK,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//        h3.setPrefHeight(100);
//        HBox h4=new HBox();
//        h4.setBackground(new Background(new BackgroundFill(Color.GREEN,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//        h4.setPrefHeight(100);
//        HBox h5=new HBox();
//        h5.setBackground(new Background(new BackgroundFill(Color.YELLOW,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//        h5.setPrefHeight(100);
//        HBox h6=new HBox();
//        h6.setBackground(new Background(new BackgroundFill(Color.ORANGE,
//                CornerRadii.EMPTY,
//                Insets.EMPTY)));
//
//        h6.setPrefHeight(100);
//        v.getChildren().add(h1);
//        v.getChildren().add(h2);
//        v.getChildren().add(h3);
//        v.getChildren().add(h4);
//        v.getChildren().add(h5);
//        v.getChildren().add(h6);
//        h1.setSpacing(30);
//        h2.setSpacing(30);
//        h1.setPadding(new Insets(50,0,0,0));
//        h2.setPadding(new Insets(50,0,0,0));





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
