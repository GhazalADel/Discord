package discord.discord1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Label message;

    Main main = new Main();

    @FXML
    public void signUp() throws IOException, ClassNotFoundException {
        String emailS = email.getText().toString();
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();
        String tel = phoneNumber.getText().toString();
        if(emailS.isEmpty() || usernameS.isEmpty() || passwordS.isEmpty()) {
            message.setText("Fill The Fields");
        }
        else {
            UIRequest request = new UIRequest(UIRequestCode.SIGN_UP);
            request.addData("email", emailS);
            request.addData("username", usernameS);
            request.addData("password", passwordS);
            request.addData("tel", tel);
            UIResponse response = Client.process(request);
            if(response.getCode() == UIResponseCode.OK) {
                main.changeScene("HomePage.fxml");
            }
            else if(response.getCode() == UIResponseCode.DUPLICATE_USERNAME) {
                message.setText("We Have A User With This Username");
            }
            else if(response.getCode() == UIResponseCode.SIX_LETTER) {
                message.setText("Username Should Have 6 Letters");
            }
            else if (response.getCode() == UIResponseCode.EIGHT_LETTER) {
                message.setText("Password Should Have 8 Letters");
            }
            else if(response.getCode() == UIResponseCode.NUMBER_LETTER) {
                message.setText("Username Or Password Should Have Number And Letter");
            }
            else if(response.getCode() == UIResponseCode.LETTER) {
                message.setText("Password Should Have Letter");
            }
            else if(response.getCode() == UIResponseCode.NUMBER) {
                message.setText("Password Should Have Number");
            }
            else if(response.getCode() == UIResponseCode.INVALID_EMAIL) {
                message.setText("Correct Your Email");
            }
            else if(response.getCode() == UIResponseCode.INVALID_TEL) {
                message.setText("Correct Your PhoneNumber");
            }
        }
    }

    public void logIn() throws IOException {
        main.changeScene("logIn.fxml");
    }
}