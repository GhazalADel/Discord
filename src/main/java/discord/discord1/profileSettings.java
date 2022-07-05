package discord.discord1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profileSettings implements Initializable {
    @FXML
    private Text changePasswordCancle;

    @FXML
    private Pane changePasswordDialog;

    @FXML
    private Button changePasswordDone;

    @FXML
    private ImageView changePasswordExit;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField currentPassword;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField newPassword;


    @FXML
    private Text newPasswordText;

    @FXML
    private Text currentPasswordText;

    @FXML
    private Text confirmNewPasswordText;


    @FXML
    void changePassword(MouseEvent event) {
        currentPasswordText.setText("CURRENT PASSWORD");
        currentPasswordText.setFont(Font.font("System", FontWeight.BOLD, 13));
        currentPasswordText.setFill(Color.WHITE);
        newPasswordText.setText("NEW PASSWORD");
        newPasswordText.setFont(Font.font("System", FontWeight.BOLD, 13));
        newPasswordText.setFill(Color.WHITE);
        confirmNewPasswordText.setText("CONFIRM NEW PASSWORD");
        confirmNewPasswordText.setFont(Font.font("System", FontWeight.BOLD, 13));
        confirmNewPasswordText.setFill(Color.WHITE);
        currentPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
        changePasswordDialog.setVisible(true);
    }

    @FXML
    void changePasswordCancleClick(MouseEvent event) {
        changePasswordDialog.setVisible(false);
    }

    @FXML
    void changePasswordDoneClick(MouseEvent event) throws IOException, ClassNotFoundException {
        String currentPass=currentPassword.getText();
        String newPass=newPassword.getText();
        String confirmPass=confirmPassword.getText();
        boolean emptyCurrent=false;
        boolean emptyNew=false;
        boolean emptyConfirm=false;
        if(currentPass.equals("")){
            currentPasswordText.setText("CURRENT PASSWORD - Your current password cannot be empty.");
            currentPasswordText.setFont(Font.font("System", FontWeight.BOLD, 9.5));
            currentPasswordText.setFill(Paint.valueOf("#d57a7c"));
            emptyCurrent=true;
        }
        if(!emptyCurrent){
            if(newPass.equals("")){
                newPasswordText.setText("NEW PASSWORD - Your new password cannot be empty.");
                newPasswordText.setFont(Font.font("System", FontWeight.BOLD, 9.5));
                newPasswordText.setFill(Paint.valueOf("#d57a7c"));
                emptyNew=true;
            }
            if(!emptyNew){
                if(confirmPass.equals("")){
                    confirmNewPasswordText.setText("CONFIRM NEW PASSWORD - Your confirm password cannot be empty.");
                    confirmNewPasswordText.setFont(Font.font("System", FontWeight.BOLD, 8.5));
                    confirmNewPasswordText.setFill(Paint.valueOf("#d57a7c"));
                    emptyConfirm=true;
                }
                if(emptyConfirm==false && emptyCurrent==false && emptyNew==false){
                    UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_PASSWORD);
                    uiRequest.addData("current",currentPass);
                    uiRequest.addData("new",newPass);
                    uiRequest.addData("confirm",confirmPass);
                    UIResponse uiResponse=Client.process(uiRequest);
                    boolean validCurrent= (boolean) uiResponse.getData("current");
                    boolean validNew= (boolean) uiResponse.getData("new");
                    boolean validConfirm= (boolean) uiResponse.getData("confirm");
                    if(!validCurrent){
                        currentPasswordText.setText("CURRENT PASSWORD - Your current password is wrong.");
                        currentPasswordText.setFont(Font.font("System", FontWeight.BOLD, 9.5));
                        currentPasswordText.setFill(Paint.valueOf("#d57a7c"));
                        currentPassword.setText("");
                    }
                    else if(!validNew){
                        newPasswordText.setText("NEW PASSWORD - Your new password has incorrect format.");
                        newPasswordText.setFont(Font.font("System", FontWeight.BOLD, 9.5));
                        newPasswordText.setFill(Paint.valueOf("#d57a7c"));
                        newPassword.setText("");
                    }
                    else if(!validConfirm){
                        confirmNewPasswordText.setText("CONFIRM NEW PASSWORD - Your confirm password is wrong.");
                        confirmNewPasswordText.setFont(Font.font("System", FontWeight.BOLD, 8.5));
                        confirmNewPasswordText.setFill(Paint.valueOf("#d57a7c"));
                        confirmPassword.setText("");
                    }
                    else{
                        changePasswordDialog.setVisible(false);
                    }
            }

        }
        }
    }

    @FXML
    void changePasswordEscClick(MouseEvent event) {
        changePasswordDialog.setVisible(false);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changePasswordDialog.setVisible(false);



    }
    @FXML
    void logout(MouseEvent event){
        UIRequest uiRequest=new UIRequest(UIRequestCode.LOG_OUT);
        try {
            Client.process(uiRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Stage stage= (Stage) logOutButton.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }

}
