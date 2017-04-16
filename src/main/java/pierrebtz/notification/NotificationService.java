package pierrebtz.notification;

public interface NotificationService {

    void send(String firstName,
              String lastName,
              String email,
              boolean present,
              int adultCount,
              int childCount);
}
