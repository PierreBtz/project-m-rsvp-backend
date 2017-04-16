package pierrebtz.notification;

import pierrebtz.models.Attendance;

public interface NotificationService {

    void send(String firstName,
              String lastName,
              String email,
              Attendance attendance,
              int adultCount,
              int childCount);
}
