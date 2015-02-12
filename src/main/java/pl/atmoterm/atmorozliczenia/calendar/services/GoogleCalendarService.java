package pl.atmoterm.atmorozliczenia.calendar.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleCalendar;
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
            } while(request.getPageToken() != null);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Błąd podczas odczytywania listy kalendarzy", ex);
        }
        return res;
    }
}
