package pl.atmoterm.atmorozliczenia.calendar.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class LocalDateTimeToStringConverter extends StringConverter<LocalDateTime> {

   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
   
   @Override
   public String toString(LocalDateTime object) {
      if(object == null) {
         return "";
      }
      return object.format(formatter);
   }

   @Override
   public LocalDateTime fromString(String string) {
      if(StringUtils.isBlank(string)) {
         return null;
      }
      return LocalDateTime.parse(string, formatter);
   }

}
