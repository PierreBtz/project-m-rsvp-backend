package pierrebtz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pierrebtz.models.Rsvp;
import pierrebtz.models.TokenizedRsvp;
import pierrebtz.repositories.RsvpRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@ResponseBody
public class RsvpRestController {
    private static final String TOKEN = "0000";

    @Autowired
    private RsvpRepository repository;

    @RequestMapping(path = "/rsvp/create", method = RequestMethod.POST, consumes = "application/json")
    public String createRsvp(@RequestBody TokenizedRsvp receivedRsvp) {
        if (!TOKEN.equals(receivedRsvp.getToken())) {
            return "Token not correct";
        }
        Rsvp rsvp = new Rsvp.Builder()
                .firstName(receivedRsvp.getPrimaryAdult().getFirstName())
                .lastName(receivedRsvp.getPrimaryAdult().getLastName())
                .email(receivedRsvp.getPrimaryAdult().getEmail())
                .present(receivedRsvp.isPresent())
                .build();
        repository.save(rsvp);
        return "Successfully created rsvp with id " + rsvp.getId();
    }

    @RequestMapping("/rsvp/report/all")
    public @ResponseBody Iterable<Rsvp> getReport() {
        return repository.findAll();
    }

    @RequestMapping("/rsvp/report/present")
    public @ResponseBody Iterable<Rsvp> getPresent() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(Rsvp::isPresent)
                .collect(Collectors.toList());
    }

    @RequestMapping("/rsvp/report/absent")
    public @ResponseBody Iterable<Rsvp> getAbsent() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(rsvp -> !rsvp.isPresent())
                .collect(Collectors.toList());
    }
}
