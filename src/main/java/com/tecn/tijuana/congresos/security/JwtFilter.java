package com.tecn.tijuana.congresos.security;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final ApplicationContext context;
  private final UsuarioDetailsService usrDtsSvc; // Direct injection


  public JwtFilter (
    JwtService jwtService,
    ApplicationContext context,
    UsuarioDetailsService usrDtsSvc
  ) {
    this.jwtService = jwtService;
    this.context = context;
    this.usrDtsSvc = usrDtsSvc;
  }


  @Override
  protected void doFilterInternal (
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  )
    throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");
    String token_str = (
      authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
        ? authorizationHeader.substring(7)
        : null);
    String username =
      token_str == null ? null : jwtService.extractUsername(token_str);

    if (username != null
      && SecurityContextHolder.getContext().getAuthentication() == null) {

      System.out.println("JWT Filter: Processing token for username: " + username);

//      UserDetails userDetails = context
//        .getBean(UsuarioDetailsService.class).loadUserByUsername(username);

      UserDetails userDetails = usrDtsSvc.loadUserByUsername(username);

      System.out.println("JWT Filter: UserDetails loaded: " + (userDetails != null ? userDetails.getUsername() : "null"));
      System.out.println("JWT Filter: UserDetails type: " + (userDetails != null ? userDetails.getClass().getName() : "null"));

      if(jwtService.validateToken(token_str, userDetails)) {
        UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        token.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);

        System.out.println("JWT Filter: Authentication set in SecurityContext");
      }
    }
    filterChain.doFilter(request, response);
  }
}
