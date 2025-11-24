package com.tecn.tijuana.congresos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication
  .configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration
  .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration
  .EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers
  .AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication
  .UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtFilter jwtFilter;


  @Autowired
  public WebSecurityConfig (
    UserDetailsService userDetailsService
    , JwtFilter jwtFilter
  ) {
    this.userDetailsService = userDetailsService;
    this.jwtFilter = jwtFilter;
  }


  @Bean
  public SecurityFilterChain securityFilterChain (HttpSecurity http)
    throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)

//      .cors(withDefaults())
      .cors(cors ->
        cors.configurationSource(corsConfigurationSource()))

      .authorizeHttpRequests(req ->
        req
          .requestMatchers(
            // Endpoints abiertos/publicos.

            // Endpoints de Swagger y OpenAPI.
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",

            // Endpoints de Autentication.
            "/api/v1/identidad/autenticacion/iniciar-sesion",
            "/api/v1/identidad/autenticacion/terminar-sesion",

            // Endpoints de Validacion.
            "/api/v1/identidad/validacion/registrarse",

            // Endpoints de Congresos publicos.
            "/api/v1/eventos/congreso/publicados/**",

            // Endpoints de Conferencias publicos.
            "/api/v1/eventos/conferencia/publicadas/**",

            // Endpoints de Boletos publicos.
            "/api/v1/boletos/boleto/publico/**",

            // Endpoints de Asistencias publicos.
            "/api/v1/asistencias/asistencia/publico/**"
          )
          .permitAll()

          // Los demas endpoints deben estar autenticados.
          .anyRequest().authenticated())

      .httpBasic(withDefaults())

      .sessionManagement(sesion ->
        sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

      .userDetailsService(userDetailsService)

      .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
//    configuration.setAllowCredentials(true);
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


  @Bean
  public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public AuthenticationManager authenticationManager (
    AuthenticationConfiguration config
  ) throws Exception {
    return config.getAuthenticationManager();
  }
}
