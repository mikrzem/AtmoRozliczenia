package pl.atmoterm.atmorozliczenia.settings.components;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.settings.services.ProjectSettings;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class ProjectSettingsPanelController implements Initializable {

   @FXML
   private TableView<ProjectSettings> projectList;

   @FXML
   private TextField newRow;

   private List<GoogleEvent> events;

   private ObservableList<ProjectSettings> allSettings;

   public void setEvents(List<GoogleEvent> data) {
      this.events = data;
   }

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      projectList.setPlaceholder(new Label("Brak danych"));
      projectList.setEditable(true);
      TableColumn<ProjectSettings, String> from = new TableColumn<>("Źródło");
      from.setCellValueFactory(new PropertyValueFactory<>("from"));
      from.setCellFactory(TextFieldTableCell.forTableColumn());
      from.setEditable(true);
      from.prefWidthProperty().bind(projectList.widthProperty().multiply(0.25));
      TableColumn<ProjectSettings, String> to = new TableColumn<>("Projekt");
      to.setCellValueFactory(new PropertyValueFactory<>("to"));
      to.setCellFactory(TextFieldTableCell.forTableColumn());
      to.setEditable(true);
      to.prefWidthProperty().bind(projectList.widthProperty().multiply(0.25));
      TableColumn<ProjectSettings, String> id = new TableColumn<>("ID");
      id.setCellValueFactory(new PropertyValueFactory<>("id"));
      id.setCellFactory(TextFieldTableCell.forTableColumn());
      id.setEditable(true);
      id.prefWidthProperty().bind(projectList.widthProperty().multiply(0.25));
      TableColumn<ProjectSettings, String> leader = new TableColumn<>("Kierownik");
      leader.setCellValueFactory(new PropertyValueFactory<>("leader"));
      leader.setCellFactory(TextFieldTableCell.forTableColumn());
      leader.setEditable(true);
      leader.prefWidthProperty().bind(projectList.widthProperty().multiply(0.25));
      projectList.getColumns().addAll(from, to, id, leader);
      allSettings = FXCollections.observableArrayList(SettingsManager.get().getProjects());
      projectList.setItems(allSettings);
   }

   @FXML
   private void handleNewRow(ActionEvent event) {
      allSettings.add(SettingsManager.get().newSetting(newRow.getText()));
      newRow.setText("");
   }

   @FXML
   private void handleLoadData(ActionEvent event) {
      List<String> currentProjects = allSettings.stream().map((s) -> s.getFrom()).collect(Collectors.toList());
      events.stream().filter((f) -> StringUtils.isNotBlank(f.getProject()) && !currentProjects.contains(f.getProject()))
              .map((f) -> f.getProject()).distinct()
              .forEach((p) -> allSettings.add(SettingsManager.get().newSetting(p)));
   }

   @FXML
   private void handleSave(ActionEvent event) {
      SettingsManager.get().setProjects();
      SettingsManager.get().save();
      
      ((Node)event.getSource()).getScene().getWindow().hide();
   }

}
