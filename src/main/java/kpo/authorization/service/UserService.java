package kpo.authorization.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import kpo.authorization.domain.Session;
import kpo.authorization.domain.User;
import kpo.authorization.model.*;
import kpo.authorization.repos.SessionRepository;
import kpo.authorization.repos.UserRepository;
import kpo.authorization.util.JWTUtil;
import kpo.authorization.util.NotFoundException;
import kpo.authorization.util.PasswordHasher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public UserService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public UserDTO get(final String token) {
        Session session = sessionRepository.findBySessionToken(token);
        return userRepository.findById(session.getUser().getId())
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }
    private String CreateSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(OffsetDateTime.now().plusDays(7));

        // Создание JWT
        String jwt = JWTUtil.generateJWT(user);
        session.setSessionToken(jwt);
        sessionRepository.save(session);
        return jwt;
    }

    public String validate(final LoginForm loginForm) {
        User user = userRepository.findByEmail(loginForm.getEmail());
        if (user == null) {
            throw new NotFoundException("User with this email was not found");
        }
        if (!PasswordHasher.verify(user.getPasswordHash(), loginForm.getPassword())) {
            throw new NotFoundException("Wrong password\n");
        }

        return CreateSession(user);
    }

    public Integer create(final RegistrationForm registrationForm) {
        final User user = new User();
        mapToEntity(registrationForm, user);
        return userRepository.save(user).getId();
    }

    public void update(final ChangeRoleForm changeRoleForm) {
        final User user = userRepository.findByEmail(changeRoleForm.getEmail());
        user.setRole(changeRoleForm.getRole());
        userRepository.save(user);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(final RegistrationForm registrationForm, final User user) {
        user.setUsername(registrationForm.getUsername());
        user.setEmail(registrationForm.getEmail());
        user.setPasswordHash(PasswordHasher.hashPassword(registrationForm.getPassword()));
        user.setRole(Role.CUSTOMER);
        return user;
    }
}
