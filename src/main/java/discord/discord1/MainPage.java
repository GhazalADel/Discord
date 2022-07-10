package discord.discord1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    private Label addFriendText;
    @FXML
    private Label canAddFriendText;
    @FXML
    private TextField request;
    @FXML
    private Button sendRequest;
    @FXML
    private Label message;
    @FXML
    private AnchorPane anchorPane;
    private ListView<Pane> allFriendsView;
    private ListView<Pane> requestsView;
    private ListView<Pane> blockedView;
    private ListView<Pane> onlineView;

    private ObservableList<Pane> allFriendsList;
    private ObservableList<Pane> requestsList;
    private ObservableList<Pane> friendRequests = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
    }

    public void homePage() {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
    }
    public void setting() {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
        //change setting
    }

    public void onlineFriends() {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(true);
    }

    public void allFriends() {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
    }

    public void pending() throws IOException, ClassNotFoundException {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
        UIRequest request = new UIRequest(UIRequestCode.SEE_REQUESTS);
        UIResponse response = Client.process(request);
        if(response.getCode() == UIResponseCode.NO) {
            message.setVisible(true);
            message.setText("You don't have any request");
        }
        else if(response.getCode() == UIResponseCode.SHOW_LIST){
            ArrayList<RequestFriend> requests = new ArrayList<>();
            int size = (int) response.getData("sizeOfRequests");
            for(int i = 0; i < size; i++) {
                RequestFriend r = (RequestFriend) response.getData("request" + (i + 1));
                requests.add(r);
            }
            Circle circle;
            for(RequestFriend r : requests) {
                double y = 59;
                circle = new Circle(70, y, 24);
                Image image = new Image("C:/Users/Asus/Desktop/JavaProjects/Discord1/src/main/resources/discord/discord1/profileDefault.jpg");
                circle.setFill(new ImagePattern(image));
                anchorPane.getChildren().add(circle);
                y += 60;

            }
        }
    }
    public void blockedFriends() {
        addFriendText.setVisible(false);
        canAddFriendText.setVisible(false);
        request.setVisible(false);
        sendRequest.setVisible(false);
        message.setVisible(false);
    }

    public void addFriend() {
        addFriendText.setVisible(true);
        canAddFriendText.setVisible(true);
        request.setVisible(true);
        sendRequest.setVisible(true);
        message.setVisible(false);
    }

    public void sendRequest() throws IOException, ClassNotFoundException {
        String username = request.getText().toString();
        UIRequest request = new UIRequest(UIRequestCode.SEND_REQUEST);
        request.addData("friendUsername", username);
        UIResponse response = Client.process(request);
        if(response.getCode() == UIResponseCode.BEFORE_FRIEND) {
            canAddFriendText.setText("This person is your friend");
        }
        else if(response.getCode() == UIResponseCode.BEFORE_REQUEST) {
            canAddFriendText.setText("You have sent request before");
        }
        else if(response.getCode() == UIResponseCode.FRIEND_NOT_FOUND) {
            canAddFriendText.setText("There is no user with this username");
        }
        else if(response.getCode() == UIResponseCode.OWN_REQUEST) {
            canAddFriendText.setText("You can't send request to yourself");
        }
        else if(response.getCode() == UIResponseCode.REQUEST_AGAIN) {
            canAddFriendText.setText("This person has sent request to you");
        }
        else if(response.getCode() == UIResponseCode.OK) {
            canAddFriendText.setText("Request sent successfully");
        }
    }
    @FXML
    void userSettingClick(MouseEvent event) {
        Stage stage= (Stage) sendRequest.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("profile-settings.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));

    }
}
