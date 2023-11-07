package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.models.User;

import java.time.LocalDate;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthdate;
    private String email;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nationality = user.getNationality();
        this.birthdate = user.getBirthdate();
        this.email = user.getEmail();
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getNationality() {
        return nationality;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public String getEmail() {
        return email;
    }

}