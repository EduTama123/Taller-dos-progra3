package com.itsqmet.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
//JWT - Json Web Token:Estándar para enviar información de forma segura entre Angular y Spring en formato JSON

    // Clave secreta para firmar el token
    private final String SECRET_KEY = "ItsqmetDesarrolloSoftware2026!";

    // algoritmo que usaremos (HMAC256) -modo en el que vamos a unir emailRolSECRET_KEY
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generarToken(String email, String rol) {
        return JWT.create()
                .withSubject(email)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                //duración de 1 hora
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
    }

    //Método para revisar el token que llega del navegador para ver si es verdadero
    //Si la SECRET_KEY es válida
    //Si está vigente
    public DecodedJWT validarYDecodificarToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}
