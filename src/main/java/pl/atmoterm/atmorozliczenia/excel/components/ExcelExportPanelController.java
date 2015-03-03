package pl.atmoterm.atmorozliczenia.excel.components;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.excel.services.ExcelExportService;

public class ExcelExportPanelController implements Initializable {

   private static final Logger logger = Logger.getLogger(ExcelExportPanelController.class.getName());
   
   private final ExcelExportService service = new ExcelExportService();

   private List<GoogleEvent> events = new ArrayList<>();

   private File outputFile;

   @FXML
   private Label outputFileLabel;

   @FXML
   private Label templateFileLabel;

   @FXML
   private Label errorLabel;
   
   @FXML
   private TextField initialRow;
   
   @FXML
   private TextField initialCol;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      errorLabel.setTextFill(Color.RED);
      errorLabel.setTextAlignment(TextAlignment.CENTER);
      
      initialRow.setText("1");
      initialCol.setText("1");
   }

   @FXML
   private void handleChooseOutput(ActionEvent event) {
      Stage stage = new Stage();
      FileChooser chooser = new FileChooser();
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik Excel", "*.xlsx"));
      outputFile = chooser.showSaveDialog(stage);
      if (outputFile == null) {
         outputFileLabel.setText("");
      } else {
         outputFileLabel.setText(outputFile.getPath());
      }
   }

   @FXML
   private void handleChooseTemplate(ActionEvent event) {
      Stage stage = new Stage();
      FileChooser chooser = new FileChooser();
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik Excel", "*.xlsx"));
      File template = chooser.showOpenDialog(stage);
      if (template == null) {
         templateFileLabel.setText("");
      } else {
         if (service.loadTemplate(template)) {
            templateFileLabel.setText(template.getPath());
            errorLabel.setText("");
         } else {
            errorLabel.setText("Błąd podczas otwierania szablonu.");
         }
      }
   }
   
   @FXML
   private void handleExport(ActionEvent event) {
      service.writeData(events, parseField(initialRow), parseField(initialCol));
      if(service.saveTo(outputFile)) {
         ((Node)event.getSource()).getScene().getWindow().hide();
         try {
            Desktop.getDesktop().open(outputFile);
         } catch (IOException ex) {
            logger.log(Level.SEVERE, "Błąd podczas otwierania excela", ex);
         }
      } else {
         errorLabel.setText("Błąd podczas zapisu exportu.");
      }
   }
   
   private int parseField(TextField field) {
      try {
         return Integer.parseInt(field.getText());
      } catch(NumberFormatException ex) {
         return 1;
      }
   }

   public void setData(List<GoogleEvent> data) {
      events = data;
   }
}
