package pierrebtz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pierrebtz.models.Attendance;
import pierrebtz.notification.NotificationService;
import pierrebtz.models.Rsvp;
import pierrebtz.repositories.RsvpRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping(path = "/api/rsvp")
@RestController
@ResponseBody
public class RsvpRestController {
    @Value("${app.token}")
    private String appToken;
    @Value("${app.admin.token}")
    private String adminToken;

    @Autowired
    private RsvpRepository repository;
    @Autowired
    private NotificationService notification;

    @RequestMapping(path = "/create", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public String createRsvp(String token,
                             String firstName,
                             String lastName,
                             String email,
                             boolean presentDinner,
                             boolean presentBrunch,
                             boolean absent,
                             int adultCount,
                             int childCount) {

        if (!appToken.equals(token)) {
            throw new IllegalStateException("Token is not correct");
        }

        // this is a simple assertion as the API will be only called from a known client,
        // and unknown caller sending incoherent data is not really important here...
        assert !absent && presentDinner || absent && ! presentDinner && !presentBrunch;

        Attendance attendance = Attendance.get(presentBrunch, absent);

        // sending the notification before saving in base in case something is wrong we can always double check with the person
        notification.send(firstName, lastName, email, attendance, adultCount, childCount);

        Rsvp rsvp = new Rsvp.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .attendance(attendance)
                .adultCount(adultCount)
                .childCount(childCount)
                .build();

        repository.save(rsvp);
        return "Successfully created rsvp with id " + rsvp.getId();
    }

    @RequestMapping("/report/present")
    public @ResponseBody
    Iterable<Rsvp> getPresent(@RequestParam("token") String token) {
        if (!adminToken.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(Rsvp::isPresent)
                .collect(Collectors.toList());
    }

    @RequestMapping("/report/absent")
    public @ResponseBody
    Iterable<Rsvp> getAbsent(@RequestParam("token") String token) {
        if (!adminToken.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(rsvp -> !rsvp.isPresent())
                .collect(Collectors.toList());
    }

    @ExceptionHandler(IllegalStateException.class)
    private void handleTokenExceptions(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Provided token is not correct");
    }
}
