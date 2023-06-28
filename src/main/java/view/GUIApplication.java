package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

public class GUIApplication extends Application {

    private static String[] args;
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("login.fxml"));
        Parent root= fxmlLoader.load();
        Scene scene = new Scene(root, 1250,650);
        stage.setScene(scene);
        stage.setTitle("MyShelfie");
        LoginController controller=fxmlLoader.getController();
        controller.setRoot(root);
        GUI gui=new GUI();
        gui.setArgs(args);
        stage.setOnHidden(event->System.exit(0));
        gui.setLoginController(controller);
        controller.setScene(gui,stage);
        gui.startGame();
        stage.getIcons().add(new Image(getClass().getResource("/view/17_MyShelfie_BGA/publisher_material/Icon 50x50px.png").toString()));

        FXMLLoader fxmlLoader1 = new FXMLLoader(GUIApplication.class.getResource("chat.fxml"));
        Stage stage1=new Stage();
        Parent root1= fxmlLoader1.load();
        Scene scene1 = new Scene(root1, 250,300);
        ChatController chatController= fxmlLoader1.getController();
        stage1.setScene(scene1);
        stage1.setTitle("Chat");
        stage1.initOwner(stage);
        chatController.setRoot(root1);
        chatController.setScene(gui, stage1);
        stage1.getIcons().add(new Image(getClass().getResource("/view/17_MyShelfie_BGA/publisher_material/Icon 50x50px.png").toString()));

        gui.setChatController(chatController);

        gui.setChatStage(stage1);

    }
    public static void main(String[] args) {
        GUIApplication.args=args;
        launch();
    }

}
