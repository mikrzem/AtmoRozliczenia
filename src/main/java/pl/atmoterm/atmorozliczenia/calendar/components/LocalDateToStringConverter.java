package pl.atmoterm.atmorozliczenia.calendar.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class LocalDateToStringConverter extends StringConverter<LocalDate> {

   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
   
   @Override
   public String toString(LocalDate object) {
      if(object == null) {
         return "";
      }
      return object.format(formatter);
   }

   @Override
   public LocalDate fromString(String string) {
      if(StringUtils.isBlank(string)) {
         return null;
      }
      return LocalDate.parse(string, formatter);
   }

}
