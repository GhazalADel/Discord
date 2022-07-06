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
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Button phoneNumberEdit;

    @FXML
    private Text phoneNumberText;

    @FXML
    private Text revealPhoneNumber;

    @FXML
    private Text removePhoneNumberText;

    private String phoneNumber;

    @FXML
    private Pane phoneNumberDialog;

    @FXML
    private Text invalidPhoneNumberText;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Text emailText;

    @FXML
    private Text revealEmailText;

    @FXML
    private Pane emailDialog;

    @FXML
    private TextField emailTextField;

    @FXML
    private Text invalidEmailText;

    private String email;

    @FXML
    private Text userText;

    @FXML
    private Text usernameText;
    private String username;

    @FXML
    private Pane usernameDialog;

    @FXML
    private Text usernameInvalidText;

    @FXML
    private TextField usernameTextField;
    @FXML
    private Circle statusCircle;

    @FXML
    private Circle profileCircle;






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changePasswordDialog.setVisible(false);
        phoneNumberDialog.setVisible(false);
        emailDialog.setVisible(false);
        usernameDialog.setVisible(false);
        UIRequest uiRequest=new UIRequest(UIRequestCode.GET_PHONE_NUMBER);
        UIResponse uiResponse;
        try {
            uiResponse=Client.process(uiRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        phoneNumber= (String) uiResponse.getData("phone");
        if(phoneNumber.equals("")){
            phoneNumberEdit.setText("Add");
            removePhoneNumberText.setVisible(false);
            phoneNumberText.setText("You haven't added a phone number yet.");
            revealPhoneNumber.setVisible(false);
        }
        else{
            phoneNumberEdit.setText("Edit");
            removePhoneNumberText.setVisible(true);
            phoneNumberText.setText("*********"+phoneNumber.substring(9));
            revealPhoneNumber.setVisible(true);
            revealPhoneNumber.setText("Reveal");
        }
        UIRequest uiRequest1=new UIRequest(UIRequestCode.GET_EMAIL);
        UIResponse uiResponse1;
        try {
            uiResponse1=Client.process(uiRequest1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        email= (String) uiResponse1.getData("email");
        String showHide="";
        int index=email.indexOf('@');
        for (int i = 0; i <index ; i++) {
            showHide+="*";
        }
        if(!showHide.equals("")){
            showHide+=email.substring(index);
        }
        emailText.setText(showHide);
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
        usernameText.setText(username);
        userText.setText(username);
        UIRequest uiRequest3=new UIRequest(UIRequestCode.GET_STATUS);
        UIResponse uiResponse3;
        try {
            uiResponse3=Client.process(uiRequest3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Status userStatus= (Status) uiResponse3.getData("status");
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
        username="ghazal";

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

    @FXML
    void phoneNumberEditClick(MouseEvent event) {
      phoneNumberDialog.setVisible(true);
      invalidPhoneNumberText.setVisible(false);
      revealPhoneNumber.setVisible(false);
    }

    @FXML
    void revealPhoneNumerClick(MouseEvent event) {
        if(revealPhoneNumber.getText().equals("Reveal")){
            phoneNumberText.setText(phoneNumber);
            revealPhoneNumber.setText("Hide");
        }
        else{
            phoneNumberText.setText("*********"+phoneNumber.substring(9));
            revealPhoneNumber.setText("Reveal");
        }
    }

    @FXML
    void removePhoneNumberClick(MouseEvent event) throws IOException, ClassNotFoundException {
        phoneNumberEdit.setText("Add");
        removePhoneNumberText.setVisible(false);
        phoneNumberText.setText("You haven't added a phone number yet.");
        revealPhoneNumber.setVisible(false);
        UIRequest uiRequest=new UIRequest(UIRequestCode.REMOVE_PHONE_NUMBER);
        Client.process(uiRequest);
    }
    @FXML
    void phoneNumberDone(MouseEvent event) throws IOException, ClassNotFoundException {
        String enteredPhoneNumber=phoneNumberTextField.getText();
        if(enteredPhoneNumber.equals("")){
            invalidPhoneNumberText.setVisible(true);
        }
        else if(enteredPhoneNumber.length()!=10){
            invalidPhoneNumberText.setVisible(true);
        }
        else if(!enteredPhoneNumber.startsWith("9")){
            invalidPhoneNumberText.setVisible(true);
        }
        else{
            phoneNumber="098"+enteredPhoneNumber;
            UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_PHONE_NUMBER);
            uiRequest.addData("phone",enteredPhoneNumber);
            Client.process(uiRequest);
            phoneNumberDialog.setVisible(false);
            phoneNumberEdit.setText("Edit");
            removePhoneNumberText.setVisible(true);
            phoneNumberText.setText("*********"+phoneNumber.substring(9));
            revealPhoneNumber.setVisible(true);
            revealPhoneNumber.setText("Reveal");
        }
    }
    @FXML
    void phoneNumberCancle(MouseEvent event) {
        phoneNumberDialog.setVisible(false);
        if(!phoneNumber.equals("")){
            revealPhoneNumber.setVisible(true);
        }
    }

    @FXML
    void editEmailClick(MouseEvent event) {
      emailDialog.setVisible(true);
      invalidEmailText.setVisible(false);
      revealEmailText.setVisible(false);
    }
    @FXML
    void revealEmailClick(MouseEvent event) {
       if(revealEmailText.getText().equals("Reveal")){
           emailText.setText(email);
           revealEmailText.setText("Hide");
       }
       else{
           String showHide="";
           int index=email.indexOf('@');
           for (int i = 0; i <index ; i++) {
               showHide+="*";
           }
           if(!showHide.equals("")){
               showHide+=email.substring(index);
           }
           emailText.setText(showHide);
           revealEmailText.setText("Reveal");
       }
    }
    @FXML
    void exitEmailDialogCLick(MouseEvent event) {
      emailDialog.setVisible(false);
      revealEmailText.setVisible(true);
    }

    @FXML
    void emailDone(MouseEvent event) throws IOException, ClassNotFoundException {
        String enteredEmail=emailTextField.getText();
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(enteredEmail);
        boolean emailIsValid = matcher.matches();
        if (!emailIsValid) {
           invalidEmailText.setVisible(true);
        }
        else{
            email=enteredEmail;
            UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_EMAIL);
            uiRequest.addData("email",enteredEmail);
            Client.process(uiRequest);
            String showHide="";
            int index=email.indexOf('@');
            for (int i = 0; i <index ; i++) {
                showHide+="*";
            }
            showHide+=email.substring(index);
            emailText.setText(showHide);
            emailDialog.setVisible(false);
            revealEmailText.setVisible(true);
        }
    }

    @FXML
    void editUsernameClick(MouseEvent event) {
        usernameDialog.setVisible(true);
        usernameInvalidText.setVisible(false);
    }

    @FXML
    void changeUsernameEscClick(MouseEvent event) {
        usernameDialog.setVisible(false);
    }
    @FXML
    void usernameDoneClick(MouseEvent event) throws IOException, ClassNotFoundException {
        if(usernameTextField.getText().equals("") || usernameTextField.getText().length()<6){
            usernameInvalidText.setText("Entered username has invalid format.");
            usernameInvalidText.setVisible(true);
        }
        else{
            String input=usernameTextField.getText();
            boolean numberAndLetter = true;
            for (int i = 0; i < input.length(); i++) {
                if ((int) (input.charAt(i)) < 48 || ((int) (input.charAt(i)) >= 58 && (int) (input.charAt(i)) <= 64) || ((int) (input.charAt(i)) >= 91 && (int) (input.charAt(i)) <= 96) || (int) (input.charAt(i)) > 123) {
                    numberAndLetter = false;
                    break;
                }
            }
            if (!numberAndLetter) {
                usernameInvalidText.setText("Entered username has invalid format.");
                usernameInvalidText.setVisible(true);
            }
            else{
                UIRequest uiRequest=new UIRequest(UIRequestCode.CHANGE_USERNAME);
                uiRequest.addData("name",input);
                UIResponse uiResponse=Client.process(uiRequest);
                if(uiResponse.getCode()==UIResponseCode.DUPLICATE_USERNAME){
                    usernameInvalidText.setText("Entered username is Duplicated.");
                    usernameInvalidText.setVisible(true);
                }
                else{
                    usernameInvalidText.setText("");
                    usernameDialog.setVisible(false);
                    username=input;
                    usernameText.setText(username);
                    userText.setText(username);
                }
            }
        }

    }

    @FXML
    void escProfileClick(MouseEvent event) {
        Stage stage= (Stage) logOutButton.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }






}
