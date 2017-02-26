package pierrebtz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pierrebtz.models.Rsvp;
import pierrebtz.repositories.RsvpRepository;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping(path = "/api/rsvp")
@RestController
@ResponseBody
public class RsvpRestController {
    private static final String TOKEN = "0000";
    private static final String ADMIN_TOKEN = "1111";

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

        if (!TOKEN.equals(token)) {
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
        if (!ADMIN_TOKEN.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(Rsvp::isPresent)
                .collect(Collectors.toList());
    }

    @RequestMapping("/report/absent")
    public @ResponseBody Iterable<Rsvp> getAbsent(@RequestParam("token") String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(rsvp -> !rsvp.isPresent())
                .collect(Collectors.toList());
    }
}
