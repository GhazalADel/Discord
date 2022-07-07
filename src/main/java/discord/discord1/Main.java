package discord.discord1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class
Main extends Application {

    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;

        Socket socket;
        try {
            socket=new Socket("localhost",11000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            //creating client object and invoking process method!
        Client client=new Client(socket);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("serverPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();

    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}