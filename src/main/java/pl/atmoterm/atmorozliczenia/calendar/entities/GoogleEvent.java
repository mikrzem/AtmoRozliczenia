package pl.atmoterm.atmorozliczenia.calendar.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

public class GoogleEvent {
   
   private final StringProperty projectProperty = new SimpleStringProperty();
   private final StringProperty taskProperty = new SimpleStringProperty();
   private final Property<LocalDateTime> startTimeProperty = new SimpleObjectProperty<>();
   private final Property<LocalDateTime> endTimeProperty = new SimpleObjectProperty<>();
   private final FloatProperty hoursProperty = new SimpleFloatProperty();
   
   public String getFullName() {
      if(StringUtils.isNotBlank(getProject())) {
         return getProject() + ": " + getTask();
      }
      return getTask();
   }
   
   public void setFullName(String fullName) {
      String task = "";
      String project = "";
      if(fullName != null) {
         String[] parts = fullName.split(fullName);
         if(parts.length == 1) {
            task = fullName;
            project = "";
         } else {
            project = parts[0];
            if(parts.length > 2) {
               task = "";
               for(int i = 1; i < parts.length; i++) {
                  task += parts[i];
               }
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
   }

   public LocalDateTime getEndTime() {
      return endTimeProperty.getValue();
   }

   public void setEndTime(LocalDateTime endTime) {
      endTimeProperty.setValue(endTime);
   }

   public Float getHours() {
      if(hoursProperty.getValue() == null && getStartTime() != null && getEndTime() != null) {
         Float hours = (float)ChronoUnit.DAYS.between(getStartTime(), getEndTime()) * 24;
         hours += (float)ChronoUnit.HOURS.between(getStartTime(), getEndTime());
         hours += (float)ChronoUnit.MINUTES.between(getStartTime(), getEndTime()) / 60f;
         setHours(hours);
      }
      return hoursProperty.getValue();
   }

   public void setHours(Float hours) {
      hoursProperty.setValue(hours);
   }
}
