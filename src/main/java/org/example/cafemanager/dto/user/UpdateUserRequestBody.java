package org.example.cafemanager.dto.user;

import org.hibernate.validator.constraints.Length;

public class UpdateUserRequestBody {
    @Length(max = 255, message = "First name is is very long")
    private String firstName;

    @Length(max = 255, message = "Last name is very long")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
