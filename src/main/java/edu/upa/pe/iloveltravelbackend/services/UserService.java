package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<UserDTO> getAllUserProfiles() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userProfiles = new ArrayList<>();
        for (User user : users) {
            userProfiles.add(new UserDTO(user));
        }
        return userProfiles;
    }
    public List<UserDTO> searchUsers(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        // Realizar la búsqueda en la base de datos
        Optional<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);

        if (users.isEmpty()) {
            throw new IllegalStateException("Usuario no encontrado");
        }

        // Mapear los resultados a UserDTO y devolverlos
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)  // Aquí asumo que tienes un constructor en UserDTO que acepta un User
                .collect(Collectors.toList());
        return userDTOs;
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

    public User verifyAccount(String email, String password) {
        if (isEmptyOrWhitespace(email) || isEmptyOrWhitespace(password)) {
            throw new IllegalStateException("Correo y contraseña son campos requeridos");
        }
        List<User> existingUserByCount = userRepository.findByEmail(email);
        if (!existingUserByCount.isEmpty()) {
            User useremail = existingUserByCount.get(0);
            // Verificar si la contraseña coincide
            if (useremail.getPassword().equals(password)) {
                // Las credenciales son válidas
                return useremail;
            }else{
                throw new IllegalStateException("contraseña incorrecta");
            }
        }else{
            throw new IllegalStateException("Correo y contraseña incorrectas");
        }
    }
}
