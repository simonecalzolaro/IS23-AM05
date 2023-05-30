package view;

import javafx.application.Application;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("login.fxml"));
        FXMLLoader fxmlLoader1 = new FXMLLoader(GUIApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1250,650);
        stage.setScene(scene);
        stage.setTitle("MyShelfie");
        //stage.setMaximized(true);
        stage.show();
        LoginController controller=fxmlLoader.getController();
        GUI gui=new GUI();
        stage.setOnHiding(event->exit());
        gui.setLoginController(controller);
        controller.setScene(gui,stage);
        //GameController controller1=fxmlLoader1.getController();
        //controller1.setScene(gui, stage);
    }
    public static void main(String[] args) {
        launch();
    }
}
