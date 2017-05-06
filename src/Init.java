/**
 * Created by User on 2016/3/25.
 */

import views.LokiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.LokiSession;

public class Init extends Application {
    public static LokiSession lokiSession=new LokiSession();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.getIcons().add(new Image(getClass().getResource("/icons/lock.png").toString()));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Loki.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Loki");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            LokiController controller = loader.getController();
            controller.init();
            controller.setStage(primaryStage);
            primaryStage.show();
        }catch (Exception e){e.printStackTrace();}
    }
}
