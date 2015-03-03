/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.atmoterm.atmorozliczenia.calendar.components;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.calendar.entities.ProjectSummary;
import pl.atmoterm.atmorozliczenia.calendar.services.GoogleCalendarUtils;


public class ProjectSummaryWindowController implements Initializable {
   
   @FXML
   private TableView<ProjectSummary> summaryTable;
   
   @FXML
   private Label sumText;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      TableColumn<ProjectSummary, String> name = new TableColumn<>("Nazwa");
      name.setCellValueFactory(p -> p.getValue().projectNameProperty());
      name.prefWidthProperty().bind(summaryTable.widthProperty().multiply(0.3));
      TableColumn<ProjectSummary, String> id = new TableColumn<>("Id");
      id.setCellValueFactory(p -> p.getValue().projectIdProperty());
      id.prefWidthProperty().bind(summaryTable.widthProperty().multiply(0.2));
      TableColumn<ProjectSummary, String> leader = new TableColumn<>("Kierownik");
      leader.setCellValueFactory(p -> p.getValue().projectIdProperty());
      leader.prefWidthProperty().bind(summaryTable.widthProperty().multiply(0.3));
      TableColumn<ProjectSummary, Double> hours = new TableColumn<>("NR");
      hours.setCellValueFactory(p -> p.getValue().hoursProperty());
      hours.prefWidthProperty().bind(summaryTable.widthProperty().multiply(0.2));
      summaryTable.getColumns().addAll(name, id, leader, hours);
   }   
   
   public void setData(List<GoogleEvent> events) {
      List<ProjectSummary> res = GoogleCalendarUtils.createSummary(events);
      summaryTable.setItems(FXCollections.observableArrayList(res));
      Double sum = 0d;
      for(ProjectSummary s : res) {
         sum += s.getHours();
      }
      sumText.setText("Suma: " + new DecimalFormat("#0.0").format(sum));
   }   
}
