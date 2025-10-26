package com.udlaverso.udlaversobackend.util;

import com.udlaverso.udlaversobackend.entity.Usuario;

public class PermisoUtils {

    public static boolean tienePermiso(Usuario usuario, String permisoBuscado) {
        if (usuario == null || usuario.getRolUsuario() == null) return false;

        return usuario.getRolUsuario().getPermisosRol().stream()
                .anyMatch(p -> p.getNombrePermiso().equalsIgnoreCase(permisoBuscado));
    }
}
