package pl.atmoterm.atmorozliczenia.calendar.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

public class GoogleEvent {

   private final StringProperty projectProperty = new SimpleStringProperty();
   private final StringProperty taskProperty = new SimpleStringProperty();
   private final Property<LocalDateTime> startTimeProperty = new SimpleObjectProperty<>();
   private final Property<LocalDateTime> endTimeProperty = new SimpleObjectProperty<>();
   private final Property<Double> hoursProperty = new SimpleObjectProperty<>();

   public String getFullName() {
      if (StringUtils.isNotBlank(getProject())) {
         return getProject() + ": " + getTask();
      }
      return getTask();
   }

   public void setFullName(String fullName) {
      String task = "";
      String project = "";
      if (fullName != null) {
         String[] parts = fullName.split(":");
         if (parts.length == 1) {
            task = fullName;
            project = "";
         } else {
            project = parts[0];
            if (parts.length > 2) {
               List<String> rejoin = Arrays.asList(parts);
               task = StringUtils.join(rejoin.subList(1, rejoin.size()), ":");
            } else {
               task = parts[1];
            }
         }
      }
      task = task.trim();
      project = project.trim();
      setTask(task);
      setProject(project);
   }

   public String getProject() {
      return projectProperty.getValueSafe();
   }

   public void setProject(String project) {
      projectProperty.set(project);
   }

   public String getTask() {
      return taskProperty.getValueSafe();
   }

   public void setTask(String task) {
      taskProperty.set(task);
   }

   public LocalDateTime getStartTime() {
      return startTimeProperty.getValue();
   }

   public void setStartTime(LocalDateTime startTime) {
      startTimeProperty.setValue(startTime);
      calculateHours();
   }

   public LocalDateTime getEndTime() {
      return endTimeProperty.getValue();
   }

   public void setEndTime(LocalDateTime endTime) {
      endTimeProperty.setValue(endTime);
      calculateHours();
   }

   private void calculateHours() {
      if (getStartTime() != null && getEndTime() != null) {
         setHours((double) ChronoUnit.MINUTES.between(getStartTime(), getEndTime()) / 60f);
      }
   }

   public Double getHours() {
      if (hoursProperty.getValue() == null) {
         calculateHours();
      }
      return hoursProperty.getValue();
   }

   public void setHours(Double hours) {
      hoursProperty.setValue(hours);
   }

   public StringProperty getProjectProperty() {
      return projectProperty;
   }

   public StringProperty getTaskProperty() {
      return taskProperty;
   }

   public Property<LocalDateTime> getStartTimeProperty() {
      return startTimeProperty;
   }

   public Property<LocalDateTime> getEndTimeProperty() {
      return endTimeProperty;
   }

   public Property<Double> getHoursProperty() {
      return hoursProperty;
   }
}
