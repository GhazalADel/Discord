package discord.discord1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainPage implements Initializable {
    @FXML
    private Circle discordCircle;

    @FXML
    private VBox serversVBOX;

    private ArrayList<Button> serverButtons=new ArrayList<>();
    private Object selectedServer;

    @FXML
    private Pane createServerPane;

    @FXML
    private Label errorLabel;
    @FXML
    private TextField createServerTextField;

    @FXML
    void addServerClick(MouseEvent event) {
        createServerPane.setVisible(true);
        errorLabel.setVisible(false);
    }
    @FXML
    void exitClick(MouseEvent event) {
        createServerPane.setVisible(false);
        createServerTextField.setText("");
    }
    @FXML
    void createButtonClick(MouseEvent event) throws IOException, ClassNotFoundException {
       String enteredName=createServerTextField.getText();
       if(enteredName.equals("")){
           errorLabel.setText("Enter a name!");
       }
       else{
           UIRequest uiRequest=new UIRequest(UIRequestCode.CREATE_SERVER);
           uiRequest.addData("name",enteredName);
           UIResponse uiResponse=Client.process(uiRequest);
           if(uiResponse.getCode()==UIResponseCode.DUPLICATED){
               errorLabel.setText("Duplicated Name!");
               errorLabel.setVisible(true);
               createServerTextField.setText("");
           }
           else{
               HBox hBox=new HBox();
               hBox.setPrefWidth(70);
               hBox.setPrefHeight(70);
               Button serverButton=new Button();
               serverButton.setPrefWidth(50);
               serverButton.setPrefHeight(50);
               serverButton.setText(enteredName.substring(0,1).toUpperCase());
               serverButton.setStyle("-fx-background-color: #5865f2; -fx-background-radius: 25px;");
               serverButton.setTextFill(Color.WHITE);
               Tooltip tt = new Tooltip();
               tt.setText(enteredName);
               tt.setStyle("-fx-font: normal bold 15 Langdon; "
                       + "-fx-base: #36393f; "
                       + "-fx-text-fill: #397f52;");
               serverButton.setTooltip(tt);
               serverButton.setOnMouseClicked( e ->{
                   selectedServer=e.getSource();
                   try {
                       findServer();
                   } catch (IOException ex) {
                       throw new RuntimeException(ex);
                   } catch (ClassNotFoundException ex) {
                       throw new RuntimeException(ex);
                   }
               });
               serverButtons.add(serverButton);
               hBox.getChildren().add(serverButton);
               serversVBOX.getChildren().add(hBox);
               createServerPane.setVisible(false);
               createServerTextField.setText("");
           }
       }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serversVBOX.getChildren().removeAll(serversVBOX.getChildren());
        createServerPane.setVisible(false);
        Image image=new Image(getClass().getResourceAsStream("pictures/diimg.jpg"));
        discordCircle.setFill(new ImagePattern(image));
        UIRequest uiRequest=new UIRequest(UIRequestCode.GET_SERVERS);
        UIResponse uiResponse;
        try {
            uiResponse=Client.process(uiRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String servers= (String) uiResponse.getData("servers");
        if(servers.length()!=0) {
            String[] serversArr = servers.split("@@@");
            for (int i = 0; i < serversArr.length; i++) {
                HBox hBox = new HBox();
                hBox.setPrefWidth(70);
                hBox.setPrefHeight(70);
                Button serverButton = new Button();
                serverButton.setPrefWidth(50);
                serverButton.setPrefHeight(50);
                serverButton.setText(serversArr[i].substring(0,1).toUpperCase());
                serverButton.setStyle("-fx-background-color: #5865f2; -fx-background-radius: 25px;");
                serverButton.setTextFill(Color.WHITE);
                Tooltip tt = new Tooltip();
                tt.setText(serversArr[i]);
                tt.setStyle("-fx-font: normal bold 15 Langdon; "
                        + "-fx-base: #36393f; "
                        + "-fx-text-fill: #397f52;");
                serverButton.setTooltip(tt);
                serverButton.setOnMouseClicked(e -> {
                    selectedServer = e.getSource();
                    try {
                        findServer();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                serverButtons.add(serverButton);
                hBox.getChildren().add(serverButton);
                serversVBOX.getChildren().add(hBox);
            }
        }

    }
    public void findServer() throws IOException, ClassNotFoundException {
        int serverIndex=0;
        for (Button b:serverButtons){
            if(b.equals(selectedServer)){
                break;
            }
            serverIndex++;
        }
        UIRequest uiRequest=new UIRequest(UIRequestCode.SEt_CURRENT_SERVER);
        uiRequest.addData("index",serverIndex);
        Client.process(uiRequest);
        Stage stage= (Stage) discordCircle.getScene().getWindow();
        Parent root= null;
        try {
            root = FXMLLoader.load(getClass().getResource("serverPage.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }
}
