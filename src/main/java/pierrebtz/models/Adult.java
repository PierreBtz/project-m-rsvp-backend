package pierrebtz.models;

import javax.persistence.Embeddable;

@Embeddable
public class Adult {
    private String firstName;
    private String lastName;
    private String email;

    protected Adult() {
        // used by JPA
    }

    public Adult(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;

        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Adult{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
