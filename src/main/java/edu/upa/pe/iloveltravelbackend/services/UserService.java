package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isEmptyOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }
    public String addUser(User user){
        List<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        user.setRegistrationDate(Instant.now());
        if (isEmptyOrWhitespace(user.getFirstName()) || isEmptyOrWhitespace(user.getLastName()) || isEmptyOrWhitespace(user.getEmail()) || isEmptyOrWhitespace(user.getNationality()) || isEmptyOrWhitespace(user.getPassword()) || user.getBirthdate() == null) {
            throw new IllegalStateException("Todos los campos son requeridos");
        }
        if(!existingUserByEmail.isEmpty()) {
            throw new IllegalStateException("El correo que ingresaste ya esta en uso");
        }
        if(user.getPassword() != null && user.getPassword().length() > 8){
            throw new IllegalStateException("La contraseña debe tener menos de 8 caracteres");
        }

        userRepository.save(user);
        return "Usuario registrado correctamente";

    }

}
