package pl.atmoterm.atmorozliczenia;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.atmoterm.atmorozliczenia.calendar.components.CalendarListPanel;
import pl.atmoterm.atmorozliczenia.calendar.components.EventListPanel;

public class MainWindowController implements Initializable {

   private static final Logger logger = Logger.getLogger(MainWindowController.class.getName());

   @FXML
   private CalendarListPanel filterList;

   @FXML
   private EventListPanel eventList;

   @Override
   public void initialize(URL url, ResourceBundle rb) { }

   @FXML
   private void handleSettings(ActionEvent event) {
      try {
         Stage newWindow = new Stage();
         newWindow.setTitle("Ustawienia ogólne");
         Parent root = FXMLLoader.load(getClass().getResource("/fxml/settings/SettingsPanel.fxml"));
         Scene scene = new Scene(root);
         newWindow.setScene(scene);
         newWindow.showAndWait();
      } catch (Exception ex) {
         logger.log(Level.SEVERE, null, ex);
      }
   }
   
   @FXML
   private void handleEventLoad(ActionEvent event) {
      eventList.loadData(filterList.getSelected(), filterList.getDateFrom(), filterList.getDateTo());
   }
}
