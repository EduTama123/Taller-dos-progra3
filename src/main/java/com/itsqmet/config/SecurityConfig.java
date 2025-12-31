package com.itsqmet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ========== RUTAS PÚBLICAS ==========
                        .requestMatchers("/", "/inicio", "/login").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // ========== REGISTRO PÚBLICO (NUEVO) ==========
                        .requestMatchers("/auth/registro").permitAll()  // <-- AÑADIR ESTA LÍNEA

                        // ========== REGISTRO COMPLETO PÚBLICO ==========
                        .requestMatchers("/cuentas/formCuenta", "/cuentas/registrarCuenta").permitAll()
                        .requestMatchers("/usuarios/formUsuario", "/usuarios/guardar").permitAll()

                        // ========== PANELES SEGÚN ROL ==========
                        .requestMatchers("/admin/panel", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/especialista/panel", "/especialista/**").hasRole("ESPECIALISTA")
                        .requestMatchers("/usuario/panel", "/usuario/**").hasRole("USUARIO")

                        // ========== CRUD CUENTAS (solo admin) ==========
                        .requestMatchers("/cuentas").hasRole("ADMIN")
                        .requestMatchers("/cuentas/editarCuenta/**", "/cuentas/eliminarCuenta/**").hasRole("ADMIN")

                        // ========== CRUD USUARIOS (solo admin) ==========
                        .requestMatchers("/usuarios").hasRole("ADMIN")
                        .requestMatchers("/usuarios/eliminar/**").hasRole("ADMIN")

                        // ========== RUTAS COMPARTIDAS ==========
                        .requestMatchers("/test/**").hasAnyRole("ADMIN", "ESPECIALISTA", "USUARIO")
                        .requestMatchers("/recomendaciones/**").hasAnyRole("ADMIN", "ESPECIALISTA", "USUARIO")

                        // CUALQUIER OTRA RUTA
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/login/postLogin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}