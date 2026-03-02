package com.itsqmet.config;

import com.itsqmet.component.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Inyectamos el filtro JWT
    @Autowired
    private JwtFilter jwtFilter;


    //Encriptador para verificar la contraseña
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Configuración de la cadena de filtros o de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ========== RUTAS PÚBLICAS ==========
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/usuarios/register").permitAll()
                        .requestMatchers("/api/usuarios/test").permitAll()

                        // ========== RUTAS DE ADMIN ==========
                        // Admin puede ver todos los usuarios
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority("ROLE_ADMIN")
                        // Admin puede eliminar usuarios
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("ROLE_ADMIN")

                        // ========== RUTAS DE USUARIO NORMAL ==========
                        // Usuario puede ver su propio perfil (GET /api/usuarios/{id})
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").hasAuthority("ROLE_USUARIO")
                        // Usuario puede editar su propio perfil (PUT /api/usuarios/{id})
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/{id}").hasAuthority("ROLE_USUARIO")
                        // Usuario puede acceder a test e historial
                        .requestMatchers("/api/tests/**").hasAuthority("ROLE_USUARIO")

                        // ========== CUALQUIER OTRA RUTA ==========
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        // ========== RUTAS PÚBLICAS ==========
//                        .requestMatchers("/", "/inicio", "/login").permitAll()
//                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//
//                        // ========== REGISTRO PÚBLICO (NUEVO) ==========
//                        .requestMatchers("/auth/registro").permitAll()  // <-- AÑADIR ESTA LÍNEA
//
//                        // ========== REGISTRO COMPLETO PÚBLICO ==========
//                        .requestMatchers("/cuentas/formCuenta", "/cuentas/registrarCuenta").permitAll()
//                        .requestMatchers("/usuarios/formUsuario", "/usuarios/guardar").permitAll()
//
//                        // ========== PANELES SEGÚN ROL ==========
//                        .requestMatchers("/admin/panel", "/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/especialista/panel", "/especialista/**").hasRole("ESPECIALISTA")
//                        .requestMatchers("/usuario/panel", "/usuario/**").hasRole("USUARIO")
//
//                        // ========== CRUD CUENTAS (solo admin) ==========
//                        .requestMatchers("/cuentas").hasRole("ADMIN")
//                        .requestMatchers("/cuentas/editarCuenta/**", "/cuentas/eliminarCuenta/**").hasRole("ADMIN")
//
//                        // ========== CRUD USUARIOS (solo admin) ==========
//                        .requestMatchers("/usuarios").hasRole("ADMIN")
//                        .requestMatchers("/usuarios/eliminar/**").hasRole("ADMIN")
//
//                        // ========== RUTAS COMPARTIDAS ==========
//                        .requestMatchers("/test/**").hasAnyRole("ADMIN", "ESPECIALISTA", "USUARIO")
//                        .requestMatchers("/recomendaciones/**").hasAnyRole("ADMIN", "ESPECIALISTA", "USUARIO")
//
//                        // CUALQUIER OTRA RUTA
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/login/postLogin", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
}