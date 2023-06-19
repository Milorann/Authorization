package kpo.authorization.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleForm {
    @NotNull
    @Size(max = 100)
    @Email
    private String email;

    @NotNull
    private Role role;
}
