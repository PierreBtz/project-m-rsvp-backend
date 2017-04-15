package pierrebtz;

public interface NotificationService {

    void send(String firstName,
              String lastName,
              String email,
              boolean present,
              int adultCount,
              int childCount);
}
