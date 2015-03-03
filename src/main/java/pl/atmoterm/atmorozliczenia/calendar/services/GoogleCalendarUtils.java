package pl.atmoterm.atmorozliczenia.calendar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import pl.atmoterm.atmorozliczenia.calendar.entities.GoogleEvent;
import pl.atmoterm.atmorozliczenia.calendar.entities.ProjectSummary;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class GoogleCalendarUtils {

   public static List<GoogleEvent> mergeSameEvents(List<GoogleEvent> events) {
      List<GoogleEvent> res = new ArrayList<>();
      for (GoogleEvent e : events) {
         if (e.getHours() != null && e.getHours() != 0) {
            Optional<GoogleEvent> r = res.stream()
                    .filter(v -> Objects.equals(v.getProject(), e.getProject())
                            && Objects.equals(v.getTask(), e.getTask()))
                    .findAny();
            if (r.isPresent()) {
               if (r.get().getHours() == null) {
                  r.get().setHours(0d);
               }
               r.get().setHours(r.get().getHours() + e.getHours());
               r.get().setStartTime(null);
               if (ObjectUtils.compare(r.get().getEndTime(), e.getEndTime()) < 0) {
                  r.get().setEndTime(e.getEndTime());
               }
            } else {
               res.add(e);
            }
         }
      }
      return res;
   }

   public static List<ProjectSummary> createSummary(List<GoogleEvent> events) {
      List<ProjectSummary> res = new ArrayList<>();
      for (GoogleEvent e : events) {
         if (e.getHours() != null && e.getHours() != 0) {
            Optional<ProjectSummary> current = res.stream()
                    .filter(s -> s.matches(SettingsManager.get().getForProject(e.getProject())))
                    .findAny();
            if (current.isPresent()) {
               current.get().setHours(current.get().getHours() + e.getHours());
            } else {
               res.add(new ProjectSummary(SettingsManager.get().getForProject(e.getProject()), e.getHours()));
            }
         }
      }
      return res;
   }
}
