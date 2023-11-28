package edu.upa.pe.iloveltravelbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Getter
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tip> tips = new ArrayList<>();

    @Getter
    @Column(name = "first_name")
    private String firstName;
    @Getter
    @Column(name = "last_name")
    private String lastName;
    @Getter
    @Column(name = "email")
    private String email;
    @Getter
    @Column(name = "password")
    private String password;
    @Getter
    @Column(name = "nationality")
    private String nationality;
    @Getter
    @Column(name = "birthdate")
    private LocalDate birthdate;
    @Getter
    @Column(name = "registration_date")
    private Instant registrationDate;

    public Long getUserid() {
        return userid;
    }
    public void setUserid(Long userid) {

        this.userid = userid;
    }
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    public void setPassword(String password) {
        this.password = password;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public Instant getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void addTip(Tip tip) {
        tips.add(tip);
        tip.setUser(this);
    }

    public void removeTip(Tip tip) {
        tips.remove(tip);
        tip.setUser(null);
    }

    public User(Long userid, String firstName, String lastName, String email, String password, String nationality, LocalDate birthdate, Instant registrationDate) {
        this.userid = userid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.birthdate = birthdate;
        this.registrationDate = registrationDate;
    }
}