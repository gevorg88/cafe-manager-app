package org.example.cafemanager.dto.user;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserCreateRequestBody {
    @Length(max = 255, message = "First name is very long")
    private String firstName;

    @Length(max = 255, message = "Last name is very long")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Length(max = 255, min = 5, message = "Username must contain at from 5 to 32 symbols")
    private String username;

    @NotBlank(message = "Email is required")
    @Length(max = 255, min = 5, message = "Email must contain at from 5 to 32 symbols")
    @Email(message = "Email is not valid")
    private String email;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
