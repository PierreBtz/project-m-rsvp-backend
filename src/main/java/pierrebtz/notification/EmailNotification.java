package pierrebtz.notification;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pierrebtz.models.Attendance;

import java.io.IOException;

@Profile("default")
@Service
public class EmailNotification implements NotificationService {

    @Value("${app.notification.to}")
    private String to;
    @Value("${app.notification.from}")
    private String from;
    @Value("${sendgrid.api.key}")
    private String apiKey;

    public void send(String firstName,
                     String lastName,
                     String email,
                     Attendance attendance,
                     int adultCount,
                     int childCount) {
        Email emailFrom = new Email(from);
        String subject = generateSubject(firstName, lastName);
        Email emailTo = new Email(to);
        Content content = new Content("text/plain",
                generateEmailBody(firstName, lastName, email, attendance, adultCount, childCount));
        Mail mail = new Mail(emailFrom, subject, emailTo, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            sg.api(request);
        } catch (IOException ex) {
            // for now let's just dump the exception in heroku console
            System.out.println(ex.getMessage());
        }
    }

    private static String generateSubject(String firstName, String lastName) {
        return "[Mariage RSVP]" + firstName + " " + lastName + " vient de s'inscrire!";
    }

    private static String generateEmailBody(String firstName, String lastName, String email, Attendance attendance, int adultCount, int childCount) {
        return
                firstName + " " + lastName + " (" + email + ") vient de s'inscrire! \n \n" +
                        (attendance != Attendance.ABSENT ? "Il/Elle sera présent(e) au diner"
                                + (attendance == Attendance.ALL ? " et au brunch" : "")
                                + ": " + adultCount + " adulte(s) " + childCount + " enfant(s)."
                                : "Il/Elle ne pourra pas être présent(e).");
    }
}
