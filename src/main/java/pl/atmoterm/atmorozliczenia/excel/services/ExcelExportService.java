package pl.atmoterm.atmorozliczenia.excel.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.atmoterm.atmorozliczenia.calendar.components.LocalDateToStringConverter;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.settings.services.ProjectSettings;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class ExcelExportService {

   private final static Logger logger = Logger.getLogger(ExcelExportService.class.getName());

   private Workbook workbook;
   private final String person = SettingsManager.get().getExportPerson();
   private final String position = SettingsManager.get().getExportPosition();
   private Integer year;
   private Integer month;
   private final LocalDateToStringConverter dateConverter = new LocalDateToStringConverter();

   public ExcelExportService() {
      workbook = new XSSFWorkbook();
   }

   public boolean loadTemplate(File file) {
      if (file == null) {
         workbook = new XSSFWorkbook();
         return true;
      } else {
         try {
            workbook = new XSSFWorkbook(file);
            return true;
         } catch (IOException | InvalidFormatException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
         }
      }
   }

   public void writeData(List<GoogleEvent> events, int initialRow, int initialCol) {
      Sheet sheet = workbook.getNumberOfSheets() == 0 ? workbook.createSheet() : workbook.getSheetAt(0);
      if (!events.isEmpty()) {
         LocalDateTime maxDate = events.stream()
                 .max((GoogleEvent o1, GoogleEvent o2) -> ObjectUtils.compare(o1.getStartTime(), o2.getStartTime()))
                 .get().getStartTime();
         year = maxDate.getYear();
         month = maxDate.getMonthValue();
         int row = initialRow;
         for (GoogleEvent e : events) {
            writeRow(sheet.createRow(row), e, initialCol);
            row++;
         }
      }
   }

   private void writeRow(Row row, GoogleEvent event, int offset) {
      write(row, offset, year.doubleValue());
      write(row, offset + 1, month.doubleValue());
      write(row, offset + 2, person);
      write(row, offset + 3, position);
      write(row, offset + 4, event.getTask());
      write(row, offset + 5, event.getHours());
      write(row, offset + 6, dateConverter.toString(event.getEndTime().toLocalDate()));
      ProjectSettings project = SettingsManager.get().getForProject(event.getProject());
      write(row, offset + 8, project.getTo());
      write(row, offset + 9, project.getId());
      write(row, offset + 10, project.getLeader());
   }

   private void write(Row r, int col, String data) {
      if (data == null) {
         data = "";
      }
      getCell(r, col).setCellValue(data);
   }

   private void write(Row r, int col, Double data) {
      if (data == null) {
         data = 0d;
      }
      getCell(r, col).setCellValue(data);
   }
   
   private Cell getCell(Row r, int col) {
      Cell c = r.getCell(col);
      if(c == null) {
         c = r.createCell(col);
      }
      return c;
   }

   public boolean saveTo(File file) {
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
         }
      }
      try (FileOutputStream output = new FileOutputStream(file)) {
         workbook.write(output);
         return true;
      } catch (IOException ex) {
         logger.log(Level.SEVERE, null, ex);
         return false;
      }
   }
}
