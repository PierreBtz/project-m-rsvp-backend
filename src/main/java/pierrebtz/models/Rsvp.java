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
    private Person person;
    private boolean present;
    private int adultCount;
    private int childCount;

    protected Rsvp() {
        // used by JPA
    }

    private Rsvp(Person person, boolean present, int adultCount, int childCount) {
        this.person = person;
        this.present = present;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isPresent() {
        return present;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private boolean present;
        private int adultCount;
        private int childCount;

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

        public Builder adultCount(int adultCount) {
            this.adultCount = adultCount;
            return this;
        }
        public Builder childCount(int childCount) {
            this.childCount = childCount;
            return this;
        }

        public Rsvp build() {
            return new Rsvp(new Person(firstName, lastName, email), present, adultCount, childCount);
        }
    }
}
