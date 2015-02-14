package pl.atmoterm.atmorozliczenia.settings.services;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectSettings {

   public static ProjectSettings emptySetting(String from) {
      ProjectSettings res = new ProjectSettings(-1);
      res.setFrom(from);
      res.setTo(from);
      return res;
   }
   
   private final int index;
   private final StringProperty fromProperty = new SimpleStringProperty();
   private final StringProperty toProperty = new SimpleStringProperty();
   private final StringProperty idProperty = new SimpleStringProperty();
   private final StringProperty leaderProperty = new SimpleStringProperty();

   public ProjectSettings(int index) {
      this.index = index;
   }
   
   public StringProperty getFromProperty() {
      return fromProperty;
   }

   public StringProperty getToProperty() {
      return toProperty;
   }

   public StringProperty getIdProperty() {
      return idProperty;
   }

   public StringProperty getLeaderProperty() {
      return leaderProperty;
   }

   public int getIndex() {
      return index;
   }
   
   public String getFrom() {
      return fromProperty.getValueSafe();
   }
   
   public void setFrom(String from) {
      fromProperty.set(from);
   }
   
   public String getTo() {
      return toProperty.getValueSafe();
   }
   
   public void setTo(String to) {
      toProperty.set(to);
   }
   
   public String getId() {
      return idProperty.getValueSafe();
   }
   
   public void setId(String id) {
      idProperty.set(id);
   }
   
   public String getLeader() {
      return leaderProperty.getValueSafe();
   }
   
   public void setLeader(String leader) {
      leaderProperty.set(leader);
   }
}
