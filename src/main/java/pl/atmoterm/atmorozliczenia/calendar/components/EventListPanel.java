package pl.atmoterm.atmorozliczenia.calendar.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleCalendar;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.calendar.services.GoogleCalendarService;

public class EventListPanel extends VBox {

   private final TableView<GoogleEvent> table;
   
   private final GoogleCalendarService googleCalendarService = new GoogleCalendarService();
   
   public EventListPanel() {
      
      table = new TableView<>();
      VBox.setVgrow(table, Priority.ALWAYS);
      VBox.setMargin(table, new Insets(10, 10, 10, 10));
      table.setPlaceholder(new Label("Brak danych"));
      TableColumn<GoogleEvent, String> project = new TableColumn<>("Projekt");
      project.setCellValueFactory(new PropertyValueFactory<>("project"));
      project.setCellFactory(TextFieldTableCell.forTableColumn());
      project.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
      project.setEditable(true);
      TableColumn<GoogleEvent, String> task = new TableColumn<>("Zadanie");
      task.setCellValueFactory(new PropertyValueFactory<>("task"));
      task.setCellFactory(TextFieldTableCell.forTableColumn());
      task.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
      task.setEditable(true);
      TableColumn<GoogleEvent, LocalDateTime> start = new TableColumn<>("Od");
      start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
      start.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
      TableColumn<GoogleEvent, LocalDateTime> end = new TableColumn<>("Do");
      end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
      end.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
      TableColumn<GoogleEvent, Float> hours = new TableColumn<>("NR");
      hours.setCellValueFactory(new PropertyValueFactory<>("hours"));
      hours.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
         @Override
         public String toString(Float object) {
            if(object == null) {
               return "";
            }
            return object.toString();
         }
         
         @Override
         public Float fromString(String string) {
            if(StringUtils.isBlank(string)) {
               return null;
            }
            try {
               return Float.parseFloat(string);
            } catch(NumberFormatException ex) {
               return null;
            }
         }
      }));
      hours.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
      hours.setEditable(true);
      table.getColumns().addAll(project, task, start, end, hours);
      
      this.getChildren().add(table);
   }   
   
   public void loadData(List<GoogleCalendar> calendars, LocalDate dateFrom, LocalDate dateTo) {
      table.setItems(FXCollections.observableList(googleCalendarService.getEvents(calendars, dateFrom, dateTo)));
   }
   
   public List<GoogleEvent> getData() {
      return table.getItems();
   }
   
   public void clearData() {
      table.setItems(FXCollections.observableList(new ArrayList<>()));
   }
}
