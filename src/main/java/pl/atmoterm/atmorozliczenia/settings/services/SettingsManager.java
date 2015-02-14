package pl.atmoterm.atmorozliczenia.settings.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ObjectUtils;

public class SettingsManager {

   private static final Logger logger = Logger.getLogger(SettingsManager.class.getName());

   private static SettingsManager manager;

   public static SettingsManager get() {
      if (manager == null) {
         manager = new SettingsManager();
      }
      return manager;
   }

   private final Properties properties;
   private boolean initialized = false;

   private final List<ProjectSettings> projects = new ArrayList<>();

   private SettingsManager() {
      properties = new Properties();
      try (FileInputStream input = new FileInputStream("config.properties")) {
         properties.load(input);
         initialized = true;
      } catch (Exception ex) {
         logger.log(Level.SEVERE, "Błąd podczas otwierania ustawień", ex);
         initialized = false;
      }
      loadProjectSettings();
   }

   private void loadProjectSettings() {
      for (String key : properties.stringPropertyNames()) {
         if (key.startsWith("project")) {
            String[] parts = key.split("\\.");
            if (parts.length == 3) {
               int index = Integer.parseInt(parts[1]);
               Optional<ProjectSettings> setting = projects.stream().filter((p) -> p.getIndex() == index).findFirst();
               ProjectSettings s;
               if (setting.isPresent()) {
                  s = setting.get();
               } else {
                  s = new ProjectSettings(index);
                  projects.add(s);
               }
               switch (parts[2]) {
                  case "from":
                     s.setFrom(properties.getProperty(key));
                     break;
                  case "to":
                     s.setTo(properties.getProperty(key));
                     break;
                  case "id":
                     s.setId(properties.getProperty(key));
                     break;
                  case "leader":
                     s.setLeader(properties.getProperty(key));
                     break;
               }
            }
         }
      }
   }

   public void save() {
      try {
         File fo = new File("config.properties");
         if (!fo.exists()) {
            fo.createNewFile();
         }
         try (FileOutputStream output = new FileOutputStream(fo)) {
            properties.store(output, "Ustawienia dla AtmoRozliczenia");
            initialized = true;
         } catch (Exception ex) {
            logger.log(Level.SEVERE, "Błąd podczas zapisywania ustawień", ex);
         }
      } catch (Exception e) {
         logger.log(Level.SEVERE, "Błąd podczas zapisywania ustawień", e);
      }
   }

   private static final String googleId = "google.clientId";
   private static final String googleClient = "google.clientSecret";

   private static final String exportPerson = "export.person";
   private static final String exportPosition = "export.position";

   public String getGoogleClientId() {
      return properties.getProperty(googleId);
   }

   public void setGoogleClientId(String clientId) {
      properties.setProperty(googleId, clientId);
   }

   public String getGoogleClientSecret() {
      return properties.getProperty(googleClient);
   }

   public void setGoogleClientSecret(String clientSecret) {
      properties.setProperty(googleClient, clientSecret);
   }

   public String getExportPerson() {
      return properties.getProperty(exportPerson);
   }

   public void setExportPerson(String person) {
      properties.setProperty(exportPerson, person);
   }

   public String getExportPosition() {
      return properties.getProperty(exportPosition);
   }

   public void setExportPosition(String position) {
      properties.setProperty(exportPosition, position);
   }

   public boolean isInitialized() {
      return initialized;
   }

   public List<ProjectSettings> getProjects() {
      return projects;
   }

   public void setProject(ProjectSettings setting) {
      if (!projects.contains(setting)) {
         projects.add(setting);
      }
      properties.setProperty("project." + setting.getIndex() + ".from", setting.getFrom());
      properties.setProperty("project." + setting.getIndex() + ".to", setting.getTo());
      properties.setProperty("project." + setting.getIndex() + ".id", setting.getId());
      properties.setProperty("project." + setting.getIndex() + ".leader", setting.getLeader());
   }
   
   public void setProjects() {
      for(ProjectSettings s : projects) {
         setProject(s);
      }
   }

   public ProjectSettings newSetting(String from) {
      Optional<ProjectSettings> max = projects.stream().max((o1, o2) -> ObjectUtils.compare(o1.getIndex(), o2.getIndex()));
      int maxIndex = max.isPresent() ? max.get().getIndex() : 0;
      maxIndex++;
      ProjectSettings settings = new ProjectSettings(maxIndex);
      settings.setFrom(from);
      projects.add(settings);
      return settings;
   }
}
