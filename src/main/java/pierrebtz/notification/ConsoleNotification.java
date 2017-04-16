package pierrebtz.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pierrebtz.models.Attendance;

@Profile({"dev", "test"})
@Service
public class ConsoleNotification implements NotificationService {
    @Override
    public void send(String firstName, String lastName, String email, Attendance attendance, int adultCount, int childCount) {
        System.out.println(
                "Notification sent for " + firstName + " " + lastName + " (" + email + ") . \n" +
                        "User will be " + attendance + " with " + adultCount + " adults and " + childCount + " enfants.");
    }
}
