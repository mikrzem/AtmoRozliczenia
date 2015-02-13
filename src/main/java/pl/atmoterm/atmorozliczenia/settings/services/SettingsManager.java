package pl.atmoterm.atmorozliczenia.settings.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

   private SettingsManager() {
      properties = new Properties();
      try (FileInputStream input = new FileInputStream("config.properties")) {
         properties.load(input);
         initialized = true;
      } catch (Exception ex) {
         logger.log(Level.SEVERE, "Błąd podczas otwierania ustawień", ex);
         initialized = false;
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
}
