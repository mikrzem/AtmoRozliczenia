package pl.atmoterm.atmorozliczenia;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.atmoterm.atmorozliczenia.calendar.components.CalendarListPanel;
import pl.atmoterm.atmorozliczenia.calendar.components.EventListPanel;
import pl.atmoterm.atmorozliczenia.calendar.components.ProjectSummaryWindowController;
import pl.atmoterm.atmorozliczenia.calendar.services.GoogleCalendarUtils;
import pl.atmoterm.atmorozliczenia.excel.components.ExcelExportPanelController;
import pl.atmoterm.atmorozliczenia.settings.components.ProjectSettingsPanelController;

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
   private void handleProjects(ActionEvent event) {
      try {
         Stage newWindow = new Stage();
         newWindow.setTitle("Projekty");
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings/ProjectSettingsPanel.fxml"));
         loader.setBuilderFactory(new JavaFXBuilderFactory());
         Parent root = loader.load();
         ProjectSettingsPanelController controller = (ProjectSettingsPanelController)loader.getController();
         controller.setEvents(eventList.getData());
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
   
   @FXML
   private void handleSaveExcel(ActionEvent event) {
      try {
         Stage newWindow = new Stage();
         newWindow.setTitle("Zapis do Excela");
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/excel/ExcelExportPanel.fxml"));
         loader.setBuilderFactory(new JavaFXBuilderFactory());
         Parent root = loader.load();
         ExcelExportPanelController controller = (ExcelExportPanelController)loader.getController();
         controller.setData(eventList.getData());
         Scene scene = new Scene(root);
         newWindow.setScene(scene);
         newWindow.showAndWait();
      } catch (Exception ex) {
         logger.log(Level.SEVERE, null, ex);
      }
   }
   
   @FXML
   private void handleMergeTasks(ActionEvent event) {
      eventList.setData(GoogleCalendarUtils.mergeSameEvents(eventList.getData()));
   }
   
   @FXML
   private void handleSummary(ActionEvent event) {
      try {
         Stage newWindow = new Stage();
         newWindow.setTitle("Podsumowanie");
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendar/ProjectSummaryWindow.fxml"));
         loader.setBuilderFactory(new JavaFXBuilderFactory());
         Parent root = loader.load();
         ProjectSummaryWindowController controller = (ProjectSummaryWindowController)loader.getController();
         controller.setData(eventList.getData());
         Scene scene = new Scene(root);
         newWindow.setScene(scene);
         newWindow.showAndWait();
      } catch (Exception ex) {
         logger.log(Level.SEVERE, null, ex);
      }
   }
}
