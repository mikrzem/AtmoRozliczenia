package pl.atmoterm.atmorozliczenia;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {

   @Override
   public void start(Stage stage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));

      Scene scene = new Scene(root);
      stage.setTitle("AtmoRozliczenia");
      stage.setScene(scene);

      Screen screen = Screen.getPrimary();
      Rectangle2D bounds = screen.getVisualBounds();

      stage.setX(bounds.getMinX());
      stage.setY(bounds.getMinY());
      stage.setWidth(bounds.getWidth());
      stage.setHeight(bounds.getHeight());

      stage.show();
   }

   /**
    * The main() method is ignored in correctly deployed JavaFX application.
    * main() serves only as fallback in case the application can not be launched
    * through deployment artifacts, e.g., in IDEs with limited FX support.
    * NetBeans ignores main().
    *
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);
   }

}
