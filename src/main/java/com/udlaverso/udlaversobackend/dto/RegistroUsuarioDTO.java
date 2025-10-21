package com.udlaverso.udlaversobackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioDTO {

    @NotBlank
    private String nombresUsuario;

    @NotBlank
    private String apellidosUsuario;

    @Email
    private String correoUsuario;

    @NotBlank
    private String contraseniaUsuario;

    private String universidadUsuario;
}
