package org.example.cafemanager.domain;

import org.example.cafemanager.domain.enums.Role;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Component
@Table(name = "users", indexes = { @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_username", columnList = "username") })
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @Length(max = 255, message = "Your first_name is very long")
    private String firstName;

    @Column(name = "last_name")
    @Length(max = 255, message = "Your first_name is very long")
    private String lastName;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Username is required")
    @Length(max = 255, message = "Username is very long")
    @Length(min = 5, message = "Username is very short")
    private String username;

    @NotBlank(message = "Email is required")
    @Length(max = 255, message = "Email is very long")
    @Length(min = 5, message = "Email is very short")
    @Email(message = "Email is not valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Length(max = 255, message = "Password is very long")
    @Length(min = 6, message = "Password must contains at least symbols")
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<CafeTable> tables = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTables(Set<CafeTable> tables) {
        this.tables = tables;
    }

    public void addTable(CafeTable table) {
        this.tables.add(table);
    }

    public boolean removeTable(CafeTable table) {
        return this.tables.remove(table);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<CafeTable> getTables() {
        return tables;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorites = new HashSet<>();
        authorites.add(getRole());
        return authorites;
    }

    public boolean isManager() {
        return Role.MANAGER.equals(this.getRole());
    }

    public boolean isWaiter() {
        return Role.WAITER.equals(this.getRole());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
