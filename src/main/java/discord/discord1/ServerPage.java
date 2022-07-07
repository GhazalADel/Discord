package discord.discord1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerPage implements Initializable {
    @FXML
    private Circle statusCircle;

    @FXML
    private Circle profileCircle;

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




    }
}
