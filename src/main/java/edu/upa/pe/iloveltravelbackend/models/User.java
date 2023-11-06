package edu.upa.pe.iloveltravelbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    @Getter
    @Column(name = "firs_name")
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

    public String getPassword() {
        return password;
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

}