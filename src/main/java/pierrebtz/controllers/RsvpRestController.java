package pierrebtz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pierrebtz.models.Rsvp;
import pierrebtz.repositories.RsvpRepository;

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

    @RequestMapping(path = "/create", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public String createRsvp(String token,
                             String firstName,
                             String lastName,
                             String email,
                             boolean present,
                             int adultCount,
                             int childCount) {

        if (!appToken.equals(token)) {
            return "Token not correct";
        }
        Rsvp rsvp = new Rsvp.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .present(present)
                .adultCount(adultCount)
                .childCount(childCount)
                .build();
        repository.save(rsvp);
        return "Successfully created rsvp with id " + rsvp.getId();
    }

    @RequestMapping("/report/present")
    public @ResponseBody Iterable<Rsvp> getPresent(@RequestParam("token") String token) {
        if (!adminToken.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(Rsvp::isPresent)
                .collect(Collectors.toList());
    }

    @RequestMapping("/report/absent")
    public @ResponseBody Iterable<Rsvp> getAbsent(@RequestParam("token") String token) {
        if (!adminToken.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(rsvp -> !rsvp.isPresent())
                .collect(Collectors.toList());
    }
}
