package discord.discord1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
        ShutDownTask shutDownTask = new ShutDownTask();
        Runtime.getRuntime().addShutdownHook(shutDownTask);
        Socket socket;
        try {
            socket=new Socket("localhost",11000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            //creating client object and invoking process method!
        Client client=new Client(socket);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Discord");
//        Image image=new Image(getClass().getResourceAsStream("diimg.jpg"));
//        BackgroundImage bg=new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(900,550,true,true,true,true));
//        Background b=new Background(bg);
//
//        root.setBackground(b);
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