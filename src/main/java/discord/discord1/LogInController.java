package discord.discord1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label messageLabel;


    Main main = new Main();

    public void login() throws IOException, ClassNotFoundException {
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();
        if(usernameS.isEmpty() || passwordS.isEmpty()) {
            messageLabel.setText("Fill The Fields");
        }
        else {
            UIRequest request = new UIRequest(UIRequestCode.LOG_IN);
            request.addData("username", usernameS);
            request.addData("password", passwordS);
            UIResponse response = Client.process(request);
            if(response.getCode() == UIResponseCode.OK) {
                main.changeScene("HomePage.fxml");
            }
            else if(response.getCode() == UIResponseCode.NO_USER) {
                messageLabel.setText("No User With This Username");
            }
            else if(response.getCode() == UIResponseCode.INVALID_PASSWORD) {
                messageLabel.setText("Correct Your password");
            }
        }
    }

    public void signUp() throws IOException {
        main.changeScene("signUp.fxml");
    }
}