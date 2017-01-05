package pierrebtz.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rsvp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Adult primaryAdult;
    private boolean present;

    protected Rsvp() {
        // used by JPA
    }

    private Rsvp(Adult primaryAdult, boolean present) {
        this.primaryAdult = primaryAdult;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public Adult getPrimaryAdult() {
        return primaryAdult;
    }

    public boolean isPresent() {
        return present;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private boolean present;

        public Builder() {
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder present(boolean present) {
            this.present = present;
            return this;
        }

        public Rsvp build() {
            return new Rsvp(new Adult(firstName, lastName, email), present);
        }
    }
}
