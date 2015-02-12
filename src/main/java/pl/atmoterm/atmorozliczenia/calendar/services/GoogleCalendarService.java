package pl.atmoterm.atmorozliczenia.calendar.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Events;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ObjectUtils;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleCalendar;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class GoogleCalendarService {

   private static final Logger logger = Logger.getLogger(SettingsManager.class.getName());

   private final HttpTransport httpTransport;
   private final JacksonFactory jsonFactory;
   private final FileDataStoreFactory dataStoreFactory;

   public GoogleCalendarService() {
      try {
         httpTransport = GoogleNetHttpTransport.newTrustedTransport();
         jsonFactory = JacksonFactory.getDefaultInstance();
         File datastore = new File("datastore");
         if (!datastore.isDirectory()) {
            datastore.mkdir();
         }
         dataStoreFactory = new FileDataStoreFactory(datastore);
      } catch (GeneralSecurityException | IOException ex) {
         throw new RuntimeException("Błąd podczas tworzenia serwisu", ex);
      }
   }

   private Calendar getService() throws Exception {
      Calendar service = new Calendar.Builder(httpTransport, jsonFactory, authorize())
              .setApplicationName("AtmoRozliczenia").build();
      return service;
   }

   private Credential authorize() throws Exception {
      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
              httpTransport,
              jsonFactory,
              SettingsManager.get().getGoogleClientId(),
              SettingsManager.get().getGoogleClientSecret(),
              Collections.singleton(CalendarScopes.CALENDAR))
              .setDataStoreFactory(dataStoreFactory)
              .build();
      return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
   }

   public List<GoogleCalendar> getCalendars() {
      List<GoogleCalendar> res = new ArrayList<>();
      try {
         Calendar service = getService();
         Calendar.CalendarList.List request = service.calendarList().list();
         do {
            CalendarList list = request.execute();
            request.setPageToken(list.getNextPageToken());
            list.getItems().stream().forEach((e) -> {
               GoogleCalendar c = new GoogleCalendar();
               c.setId(e.getId());
               c.setName(e.getSummary());
               res.add(c);
            });
         } while (request.getPageToken() != null);
      } catch (Exception ex) {
         logger.log(Level.SEVERE, "Błąd podczas odczytywania listy kalendarzy", ex);
      }
      return res;
   }

   public List<GoogleEvent> getEvents(GoogleCalendar calendar, LocalDate dateFrom, LocalDate dateTo) {
      List<GoogleEvent> res = new ArrayList<>();
      try {
         Calendar service = getService();
         Calendar.Events.List request = service.events().list(calendar.getId());
         request.setTimeMin(new DateTime(Date.from(dateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
         request.setTimeMax(new DateTime(Date.from(dateTo.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
         do {
            Events list = request.execute();
            request.setPageToken(list.getNextPageToken());
            list.getItems().stream().forEach((i) -> {
               if (!i.isEndTimeUnspecified() && !i.getStart().getDate().isDateOnly()) {
                  GoogleEvent e = new GoogleEvent();
                  e.setFullName(i.getSummary());
                  e.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(i.getStart().getDateTime().getValue()), ZoneId.systemDefault()));
                  e.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(i.getEnd().getDateTime().getValue()), ZoneId.systemDefault()));
                  res.add(e);
               }
            });
         } while (request.getPageToken() != null);
      } catch (Exception ex) {
         logger.log(Level.SEVERE, "Błąd podczas odczytywania listy kalendarzy", ex);
      }
      return res;
   }
   
   public List<GoogleEvent> getEvents(List<GoogleCalendar> calendars, final LocalDate dateFrom, final LocalDate dateTo) {
      List<GoogleEvent> res = new ArrayList<>();
      calendars.forEach((c) -> {
         res.addAll(getEvents(c, dateFrom, dateTo));
      });
      res.sort((GoogleEvent o1, GoogleEvent o2) -> ObjectUtils.compare(o1.getStartTime(), o2.getStartTime()));
      return res;
   }
}
