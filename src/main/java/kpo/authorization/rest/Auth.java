package kpo.authorization.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

import kpo.authorization.model.LoginForm;
import kpo.authorization.model.LoginResponse;
import kpo.authorization.model.RegistrationForm;
import kpo.authorization.model.UserDTO;
import kpo.authorization.service.UserService;
import kpo.authorization.util.PasswordHasher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class Auth {

    private final UserService userService;

    public Auth(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> register(@RequestBody @Valid final RegistrationForm registrationForm) {
        final Integer createdId = userService.create(registrationForm);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginForm loginForm) {
        var jwt = userService.validate(loginForm);
        return new ResponseEntity<>(new LoginResponse(jwt, "Вы вошли"), HttpStatus.OK);
    }

}
