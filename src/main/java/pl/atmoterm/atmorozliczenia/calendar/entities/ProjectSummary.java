package pl.atmoterm.atmorozliczenia.calendar.entities;

import java.util.Objects;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.atmoterm.atmorozliczenia.settings.services.ProjectSettings;

public class ProjectSummary {

   private final StringProperty projectName = new SimpleStringProperty();

   public String getProjectName() {
      return projectName.get();
   }

   public void setProjectName(String value) {
      projectName.set(value);
   }

   public StringProperty projectNameProperty() {
      return projectName;
   }
   private final StringProperty projectId = new SimpleStringProperty();

   public String getProjectId() {
      return projectId.get();
   }

   public void setProjectId(String value) {
      projectId.set(value);
   }

   public StringProperty projectIdProperty() {
      return projectId;
   }
   private final StringProperty projectLeader = new SimpleStringProperty();

   public String getProjectLeader() {
      return projectLeader.get();
   }

   public void setProjectLeader(String value) {
      projectLeader.set(value);
   }

   public StringProperty projectLeaderProperty() {
      return projectLeader;
   }
   private final Property<Double> hours = new SimpleObjectProperty<>(0d);

   public Double getHours() {
      return hours.getValue();
   }

   public void setHours(Double value) {
      hours.setValue(value);
   }

   public Property<Double> hoursProperty() {
      return hours;
   }

   public ProjectSummary(ProjectSettings settings, Double hours) {
      setProjectName(settings.getTo());
      setProjectId(settings.getId());
      setProjectLeader(settings.getLeader());
      setHours(hours);
   }

   public boolean matches(ProjectSettings settings) {
      return Objects.equals(settings.getTo(), getProjectName())
              && Objects.equals(settings.getId(), getProjectId())
              && Objects.equals(settings.getLeader(), getProjectLeader());
   }
}
