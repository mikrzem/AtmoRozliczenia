package pl.atmoterm.atmorozliczenia.calendar.components;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleCalendar;
import pl.atmoterm.atmorozliczenia.calendar.services.GoogleCalendarService;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class CalendarListPanel extends VBox {

   @FXML
   private DatePicker dateFrom;

   @FXML
   private DatePicker dateTo;

   @FXML
   private TableView<GoogleCalendarItem> calendarList;

   private List<GoogleCalendarItem> items;

   private final GoogleCalendarService googleCalendarService;

   public CalendarListPanel() {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/calendar/CalendarListPanel.fxml"));
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);

      try {
         fxmlLoader.load();
      } catch (IOException exception) {
         throw new RuntimeException(exception);
      }

      googleCalendarService = new GoogleCalendarService();

      dateTo.setValue(LocalDate.now());
      dateFrom.setValue(LocalDate.now().minusMonths(1));
      TableColumn<GoogleCalendarItem, Boolean> selected = new TableColumn<>();
      selected.setCellValueFactory((TableColumn.CellDataFeatures<GoogleCalendarItem, Boolean> param) -> param.getValue().selectedProperty);
      selected.setEditable(true);
      selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
      TableColumn<GoogleCalendarItem, String> name = new TableColumn<>("Kalendarz");
      name.setCellValueFactory((TableColumn.CellDataFeatures<GoogleCalendarItem, String> param) -> param.getValue().nameProperty);
      calendarList.getColumns().addAll(selected, name);
      calendarList.setPlaceholder(new Label("Brak danych"));
   }

   @FXML
   private void handleLoadCalendars(ActionEvent event) {
      if (SettingsManager.get().isInitialized()) {
         items = googleCalendarService.getCalendars().stream()
                 .map((c) -> new GoogleCalendarItem(c))
                 .collect(Collectors.toList());
         calendarList.setItems(FXCollections.observableArrayList(items));
      }
   }

   public List<GoogleCalendar> getSelected() {
      if (items == null) {
         return new ArrayList<>();
      }
      return items.stream()
              .filter((i) -> i.isSelected())
              .map((i) -> i.getCalendar())
              .collect(Collectors.toList());
   }

   public LocalDate getDateFrom() {
      return dateFrom.getValue();
   }

   public LocalDate getDateTo() {
      return dateTo.getValue();
   }

   private class GoogleCalendarItem {

      private final Property<GoogleCalendar> calendarProperty = new SimpleObjectProperty<>();
      private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
      private final StringProperty nameProperty = new SimpleStringProperty();

      public GoogleCalendarItem(GoogleCalendar c) {
         setCalendar(c);
         setName(c.getName());
      }

      public GoogleCalendar getCalendar() {
         return calendarProperty.getValue();
      }

      public void setCalendar(GoogleCalendar calendar) {
         calendarProperty.setValue(calendar);
      }

      public boolean isSelected() {
         return selectedProperty.get();
      }

      public void setSelected(boolean selected) {
         selectedProperty.set(selected);
      }

      public String getName() {
         return nameProperty.get();
      }
      
      public void setName(String name) {
         nameProperty.set(name);
      }
   }
}
