package com.tecn.tijuana.congresos.security;

public class ExpresionSeguridad {
  public static final String CONSULTAR_USUARIOS =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR') " +
      "or (hasRole('STAFF') and hasAuthority('STAFF_AUTORIZADO'))";

  public static final String REGISTRAR_USUARIOS =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR') " +
      "or (hasRole('STAFF') and hasAuthority('STAFF_AUTORIZADO') " +
      "    and hasAuthority('STAFF_ALUMNOS'))";

  public static final String EDITAR_USUARIOS = REGISTRAR_USUARIOS;

  public static final String ELIMINAR_USUARIOS = EDITAR_USUARIOS;

  public static final String ACREDITAR_ALUMNOS =
    "hasRole('ORGANIZADOR')";



  public static final String CONSULTAR_CONGRESOS_PROPIOS =
    "hasRole('ORGANIZADOR')";

  public static final String CONSULTAR_CONGRESOS_NO_PUBLICADOS =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR', 'STAFF')";

  public static final String REGISTRAR_CONGRESOS =
    "hasRole('ORGANIZADOR')";

  public static final String EDITAR_CONGRESOS =
    REGISTRAR_CONGRESOS;

  public static final String ELIMINAR_CONGRESOS =
    REGISTRAR_CONGRESOS;



  public static final String CONSULTAR_CONFERENCIAS_PROPIAS =
    "hasRole('ORGANIZADOR')";

  public static final String CONSULTAR_CONFERENCIAS_NO_PUBLICADAS =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR', 'STAFF')";

  public static final String REGISTRAR_CONFERENCIAS =
    "hasRole('ORGANIZADOR')";

  public static final String EDITAR_CONFERENCIAS =
    REGISTRAR_CONFERENCIAS;

  public static final String ELIMINAR_CONFERENCIAS =
    REGISTRAR_CONFERENCIAS;



  public static final String INSCRIBIR_ALUMNOS =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR') " +
      "or (hasRole('STAFF') and hasAuthority('STAFF_AUTORIZADO') " +
      "    and hasAuthority('STAFF_INSCRIPCIONES'))";

  public static final String CONSULTAR_BOLETOS_AJENOS =
    INSCRIBIR_ALUMNOS;

  public static final String GESTIONAR_BOLETOS =
    INSCRIBIR_ALUMNOS;



  public static final String CUSTODIAR_ENTRADA =
    "hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR') " +
      "or (hasRole('STAFF') and hasAuthority('STAFF_AUTORIZADO') " +
      "    and hasAuthority('STAFF_CUSTODIO'))";

  public static final String CONSULTAR_ASISTENCIAS_AJENAS =
    CUSTODIAR_ENTRADA;
}
