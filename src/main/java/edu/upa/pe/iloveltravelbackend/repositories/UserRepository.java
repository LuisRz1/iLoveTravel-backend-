package edu.upa.pe.iloveltravelbackend.repositories;

import edu.upa.pe.iloveltravelbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    List<User> findByNationality(String nationality);

}
