package com.tecn.tijuana.congresos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;


/**
 * Servicio encargado de generar, firmar y validar JSON Web Tokens (JWTs).
 *
 * <p>Un <b>JWT</b> es un estandar abierto (RFC 7519) que permite transmitir
 * informacion entre partes de forma compacta y verificable. Tiene tres partes
 * separadas por puntos: {@code HEADER.PAYLOAD.SIGNATURE}.
 * <ul>
 *   <li><b>Header:</b> tipo de token y algoritmo de firma usado.</li>
 *   <li><b>Payload (Claims):</b> datos del usuario (subject, expiracion, etc.).</li>
 *   <li><b>Signature:</b> garantiza que el token no fue alterado despues de
 *       su emision.</li>
 * </ul>
 *
 * <p>Esta clase utiliza la libreria <b>JJWT</b> ({@code io.jsonwebtoken}) para
 * construir, firmar y parsear tokens, y se integra con <b>Spring Security</b>
 * a traves de {@link UserDetails}.
 *
 * <p>La clave secreta usada para firmar tokens se lee desde la variable de
 * entorno {@code JWT_SECRET_KEY}. Si no se configura (o se deja el valor de
 * ejemplo {@code "EXAMPLE"}), el servicio genera una clave aleatoria segura
 * en cada arranque — lo que invalida todos los tokens al reiniciar la app.
 * Para evitar eso en produccion, siempre configure {@code JWT_SECRET_KEY} con
 * una clave Base64 fija y secreta.
 */
@Service
public class JwtService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  /**
   * Clave secreta en formato Base64 usada para firmar los tokens.
   *
   * <p>Se resuelve al momento de construccion: si la variable de entorno
   * {@code JWT_SECRET_KEY} tiene un valor real, se usa ese valor. Si tiene el
   * valor de ejemplo {@code "EXAMPLE"}, se genera una clave aleatoria.
   */
  private final String secretKeyStr;

  /**
   * Objeto {@link SecretKey} derivado de {@code secretKeyStr}.
   *
   * <p>JJWT no trabaja con Strings sino con objetos criptograficos. Este campo
   * es la representacion interna de la clave lista para usarse en firmas y
   * verificaciones. Se construye una sola vez al arrancar el servicio.
   */
  private final SecretKey signingKey;


  /**
   * CONSTRUCTOR principal de esta clase/servicio, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * <p>La anotacion {@code @Value("${JWT_SECRET_KEY:EXAMPLE}")} le indica a
   * Spring que inyecte el valor de la variable de entorno {@code JWT_SECRET_KEY}
   * en el parametro {@code secretKeyEnv}. El texto despues de los dos puntos
   * ({@code :EXAMPLE}) es el valor por defecto si la variable no existe.
   *
   * <p><b>Por que va en el constructor y no en el campo?</b>
   * Si se pone {@code @Value} directamente sobre un campo {@code final},
   * Spring intenta inyectarlo despues de que el constructor termina, pero el
   * campo ya fue asignado por el constructor. Usar inyeccion por constructor
   * garantiza que Spring provee el valor <i>antes</i> de que el cuerpo del
   * constructor se ejecute — que es la forma recomendada en Spring.
   *
   * @param secretKeyEnv
   * Valor de la variable de entorno {@code JWT_SECRET_KEY}. Si vale
   * {@code "EXAMPLE"}, se genera una clave aleatoria en su lugar.
   */
  public JwtService (@Value("${JWT_SECRET_KEY:EXAMPLE}") String secretKeyEnv) {
    this.secretKeyStr = resolverClaveSecreta(secretKeyEnv);
    this.signingKey   = getSigningKey();
  }


  /**
   * Genera un JWT firmado para el usuario indicado.
   *
   * <p>El token resultante contiene los siguientes <b>claims</b> (datos en el
   * payload):
   * <ul>
   *   <li>{@code subject}: nombre de usuario (identificador en el sistema).</li>
   *   <li>{@code issuedAt}: instante de emision del token.</li>
   *   <li>{@code expiration}: instante de vencimiento (7 dias desde emision).</li>
   * </ul>
   *
   * <p>El token se firma con <b>HMAC-SHA256</b> usando {@code signingKey}. HMAC
   * (Hash-based Message Authentication Code) es un algoritmo simetrico: la
   * misma clave sirve tanto para firmar como para verificar, por eso es
   * fundamental mantenerla en secreto.
   *
   * @param username
   * Nombre de usuario que se almacenara como {@code subject} en el token.
   *
   * @return
   * Cadena de texto compacta con el JWT firmado en formato
   * {@code HEADER.PAYLOAD.SIGNATURE}.
   */
  public String generateToken (String username) {
    Map<String, Object> claims = new HashMap<>();
    var                 now    = System.currentTimeMillis();
    return Jwts.builder()
      .claims(claims)
      .subject(username)                                    // Identificador del usuario.
      .issuedAt(new Date(now))                              // Marca de tiempo de emision.
      .expiration(new Date(now + 7 * 24 * 60 * 60 * 1000)) // Expira en 7 dias.
      .signWith(signingKey)                                 // Firma con HMAC-SHA256.
      .compact();                                           // Serializa a HEADER.PAYLOAD.SIGNATURE.
  }


  /**
   * Determina la clave secreta que usara el servicio para firmar los tokens.
   *
   * <p>Si el administrador no configuro la variable de entorno
   * ({@code JWT_SECRET_KEY=EXAMPLE}), se interpreta como una mala practica y
   * se genera una clave aleatoria segura automaticamente para no exponer una
   * clave conocida publicamente. El efecto secundario es que los tokens se
   * invalidan en cada reinicio de la app.
   *
   * <p>Si se configuro una clave real, se usa tal cual. Eso garantiza que los
   * tokens permanecen validos entre reinicios — el comportamiento deseado en
   * produccion.
   *
   * @param configurada
   * Valor leido desde la variable de entorno {@code JWT_SECRET_KEY}.
   *
   * @return
   * La clave secreta en formato Base64 que se usara para firmar los tokens.
   */
  private String resolverClaveSecreta (String configurada) {
    // Si el admin dejo la clave de ejemplo, forzar la generacion de una
    // clave aleatoria para protegerlo de esa mala practica.
    if (Objects.equals(configurada, "EXAMPLE")) {
      // KeyGenerator (javax.crypto) genera claves criptograficamente seguras.
      // Se solicita una clave para el algoritmo HmacSHA256.
      KeyGenerator keyGenerator;
      try {
        keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
      SecretKey secretKey = keyGenerator.generateKey();
      // Se codifica en Base64 para representarla como texto imprimible.
      return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Usar la clave real configurada por el administrador.
    return configurada;
  }


  /**
   * Convierte la clave secreta en Base64 al objeto {@link SecretKey} que
   * JJWT necesita para firmar y verificar tokens.
   *
   * <p>JJWT no opera directamente sobre Strings sino sobre objetos
   * criptograficos del JDK. El proceso de conversion es:
   * <ol>
   *   <li>Decodificar la cadena Base64 a los bytes crudos de la clave.</li>
   *   <li>Construir un {@link SecretKey} HMAC-SHA256 a partir de esos bytes.</li>
   * </ol>
   *
   * @return
   * Objeto {@link SecretKey} listo para usarse en operaciones de firma JWT.
   */
  private SecretKey getSigningKey () {
    // Decodificar Base64 → bytes crudos.
    byte[] keyBytes = Decoders.BASE64.decode(secretKeyStr);
    // Construir la SecretKey HMAC-SHA256 a partir de los bytes.
    return Keys.hmacShaKeyFor(keyBytes);
  }


  /**
   * Extrae el nombre de usuario ({@code subject}) contenido en el token JWT.
   *
   * <p>En este sistema, el {@code subject} del JWT siempre es el nombre de
   * usuario, que se establece al generar el token en
   * {@link #generateToken(String)}.
   *
   * @param token
   * JWT firmado del que se desea extraer el nombre de usuario.
   *
   * @return
   * Nombre de usuario almacenado en el campo {@code subject} del token.
   */
  public String extractUsername (String token) {
    return extractClaim(token, Claims::getSubject);
  }


  /**
   * Extrae un claim especifico del token JWT usando una funcion extractora.
   *
   * <p>Un <b>claim</b> es cualquier dato almacenado en el payload del token
   * (subject, expiration, issuedAt, o campos personalizados).
   *
   * <p>Este metodo aplica el patron de <b>funcion de orden superior</b>:
   * recibe como parametro una {@link Function} que describe <i>que extraer</i>
   * del objeto {@link Claims}. Esto permite reutilizar la logica de parseo
   * para obtener cualquier campo sin duplicar codigo.
   *
   * <p>Ejemplos de uso:
   * <pre>
   *   // Extraer el subject (username):
   *   extractClaim(token, Claims::getSubject);
   *
   *   // Extraer la fecha de expiracion:
   *   extractClaim(token, Claims::getExpiration);
   * </pre>
   *
   * @param token
   * JWT firmado del que se desea extraer informacion.
   *
   * @param claimResolver
   * Funcion que, dado el objeto {@link Claims} del token, retorna el valor
   * deseado. Se puede pasar como referencia a metodo o como lambda.
   *
   * @param <T>
   * Tipo de retorno del claim que se desea extraer (String, Date, etc.).
   *
   * @return
   * El valor del claim solicitado, del tipo {@code T} indicado.
   */
  private <T> T extractClaim (String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    // Aplicar la funcion extractora sobre el objeto de claims parseado.
    return claimResolver.apply(claims);
  }


  /**
   * Parsea el JWT y extrae todos sus claims (datos del payload).
   *
   * <p>Durante el parseo, JJWT realiza automaticamente las siguientes
   * verificaciones de seguridad:
   * <ol>
   *   <li>Verifica la firma del token usando {@code signingKey}.</li>
   *   <li>Lanza excepcion si la firma no coincide (token alterado o clave erronea).</li>
   *   <li>Lanza excepcion si el token ya expiro.</li>
   * </ol>
   *
   * <p>Si el token pasa todas las verificaciones, retorna el objeto
   * {@link Claims} con todos los datos del payload listos para consultarse.
   *
   * @param token
   * JWT firmado a parsear y verificar.
   *
   * @return
   * Objeto {@link Claims} con los datos del payload del token.
   *
   * @throws io.jsonwebtoken.JwtException
   * Si el token es invalido, fue alterado, o ya expiro.
   */
  private Claims extractAllClaims (String token)
    throws io.jsonwebtoken.JwtException
  {
    return Jwts.parser()
      .verifyWith(signingKey)   // Configura la clave para verificar la firma.
      .build()
      .parseSignedClaims(token) // Parsea Y verifica la firma en un solo paso.
      .getPayload();            // Retorna el payload (Claims) si es valido.
  }


  /**
   * Valida que un token JWT sea autentico y corresponda al usuario indicado.
   *
   * <p>La validacion comprueba dos condiciones de forma conjunta:
   * <ol>
   *   <li>El {@code subject} del token coincide con el username de
   *       {@code userDetails}.</li>
   *   <li>El token no ha expirado.</li>
   * </ol>
   *
   * <p>{@link UserDetails} es una interfaz de <b>Spring Security</b> que
   * representa los datos de autenticacion de un usuario (username, password,
   * roles, estado de la cuenta, etc.). Al comparar contra este objeto se
   * asegura que el token pertenece a un usuario que aun existe y es valido
   * en el sistema.
   *
   * @param token
   * JWT firmado a validar.
   *
   * @param userDetails
   * Detalles del usuario contra los que se validara el token.
   *
   * @return
   * {@code true} si el token es valido y pertenece al usuario indicado;
   * {@code false} en cualquier otro caso.
   */
  public boolean validateToken (String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    // El token es valido solo si el subject coincide Y el token no expiro.
    return
      (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }


  /**
   * Determina si un token JWT ya supero su fecha de expiracion.
   *
   * @param token
   * JWT firmado a evaluar.
   *
   * @return
   * {@code true} si el token esta expirado; {@code false} si aun es vigente.
   */
  private boolean isTokenExpired (String token) {
    return extractExpiration(token).before(new Date());
  }


  /**
   * Extrae la fecha y hora de expiracion del token JWT.
   *
   * @param token
   * JWT firmado del que se desea leer la fecha de expiracion.
   *
   * @return
   * Objeto {@link Date} con la fecha y hora en que el token deja de ser valido.
   */
  private Date extractExpiration (String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
