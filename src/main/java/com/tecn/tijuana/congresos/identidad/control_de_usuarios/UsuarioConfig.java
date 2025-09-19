package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static java.time.Month.JANUARY;

@Configuration
public class UsuarioConfig {

  @Value("${ADMINISTRADOR_1_EMAIL:administrador.1@example.com}")
  private String administrador1Email;
  @Value("${ADMINISTRADOR_1_PWD:12345}")
  private String administrador1Password;
  @Value("${ADMINISTRADOR_1_TEL_PREF:52}")
  private String administrador1TelPref;
  @Value("${ADMINISTRADOR_1_TEL_SUF:6641112222}")
  private String administrador1TelSuf;
  @Value("${ADMINISTRADOR_1_NOMBRE:Administrador}")
  private String administrador1Nombre;
  @Value("${ADMINISTRADOR_1_AP_PATERNO:Uno}")
  private String administrador1ApPaterno;

  @Value("${ORGANIZADOR_1_EMAIL:organizador.1@example.com}")
  private String organizador1Email;
  @Value("${ORGANIZADOR_1_PWD:12345}")
  private String organizador1Password;
  @Value("${ORGANIZADOR_1_TEL_PREF:52}")
  private String organizador1TelPref;
  @Value("${ORGANIZADOR_1_TEL_SUF:6641113333}")
  private String organizador1TelSuf;
  @Value("${ORGANIZADOR_1_NOMBRE:Organizador}")
  private String organizador1Nombre;
  @Value("${ORGANIZADOR_1_AP_PATERNO:Uno}")
  private String organizador1ApPaterno;

  @Value("${STAFF_1_EMAIL:staff.1@example.com}")
  private String staff1Email;
  @Value("${STAFF_1_PWD:12345}")
  private String staff1Password;
  @Value("${STAFF_1_TEL_PREF:52}")
  private String staff1TelPref;
  @Value("${STAFF_1_TEL_SUF:6641114444}")
  private String staff1TelSuf;
  @Value("${STAFF_1_NOMBRE:Staff}")
  private String staff1Nombre;
  @Value("${STAFF_1_AP_PATERNO:Uno}")
  private String staff1ApPaterno;

  @Value("${STAFF_2_EMAIL:staff.2@example.com}")
  private String staff2Email;
  @Value("${STAFF_2_PWD:12345}")
  private String staff2Password;
  @Value("${STAFF_2_TEL_PREF:52}")
  private String staff2TelPref;
  @Value("${STAFF_2_TEL_SUF:6641115555}")
  private String staff2TelSuf;
  @Value("${STAFF_2_NOMBRE:Staff}")
  private String staff2Nombre;
  @Value("${STAFF_2_AP_PATERNO:Dos}")
  private String staff2ApPaterno;

  @Value("${ALUMNO_1_EMAIL:alumno.1@example.com}")
  private String alumno1Email;
  @Value("${ALUMNO_1_PWD:12345}")
  private String alumno1Password;
  @Value("${ALUMNO_1_TEL_PREF:52}")
  private String alumno1TelPref;
  @Value("${ALUMNO_1_TEL_SUF:6641116666}")
  private String alumno1TelSuf;
  @Value("${ALUMNO_1_NOMBRE:Alumno}")
  private String alumno1Nombre;
  @Value("${ALUMNO_1_AP_PATERNO:Uno}")
  private String alumno1ApPaterno;
  @Value("${ALUMNO_1_NO_CONTROL:25211001}")
  String alumno1NoControl;
  @Value("${ALUMNO_1_CODIGO_CARRERA:ISC}")
  String alumno1CodigoCarrera;
  @Value("${ALUMNO_1_SEMESTRE:1}")
  int alumno1Semestre;
  @Value("${ALUMNO_1_GRUPO:A}")
  String alumno1Grupo;
  @Value("${ALUMNO_1_EXTERNO:false}")
  boolean alumno1Externo;
  @Value("${ALUMNO_1_CURP:AAAA050130BBBCCC01}")
  String alumno1Curp;
  @Value("${ALUMNO_1_EMAIL_INSTITUCIONAL:alumno.1@example.com}")
  String alumno1EmailInstitucional;

  @Value("${ALUMNO_2_EMAIL:alumno.2@example.com}")
  private String alumno2Email;
  @Value("${ALUMNO_2_PWD:12345}")
  private String alumno2Password;
  @Value("${ALUMNO_2_TEL_PREF:52}")
  private String alumno2TelPref;
  @Value("${ALUMNO_2_TEL_SUF:6641117777}")
  private String alumno2TelSuf;
  @Value("${ALUMNO_2_NOMBRE:Alumno}")
  private String alumno2Nombre;
  @Value("${ALUMNO_2_AP_PATERNO:Uno}")
  private String alumno2ApPaterno;
  @Value("${ALUMNO_2_NO_CONTROL:25211002}")
  String alumno2NoControl;
  @Value("${ALUMNO_2_CODIGO_CARRERA:IND}")
  String alumno2CodigoCarrera;
  @Value("${ALUMNO_2_SEMESTRE:1}")
  int alumno2Semestre;
  @Value("${ALUMNO_2_GRUPO:A}")
  String alumno2Grupo;
  @Value("${ALUMNO_2_EXTERNO:false}")
  boolean alumno2Externo;
  @Value("${ALUMNO_2_CURP:AAAA050130BBBCCC02}")
  String alumno2Curp;
  @Value("${ALUMNO_2_EMAIL_INSTITUCIONAL:alumno.2@example.com}")
  String alumno2EmailInstitucional;



  @Bean
  CommandLineRunner studentCommandLineRunner (
    ControlDeUsuariosService usrSvc
  ) {
    return args -> {

      if (!usrSvc.emailTomado(administrador1Email)) {
        usrSvc.registrarAdmin(
          Usuario.nuevoAdmin(
            null, administrador1Email, administrador1Password,
            administrador1TelPref, administrador1TelSuf, administrador1Nombre,
            administrador1ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20)),
          null);
      }

      if (!usrSvc.emailTomado(organizador1Email)) {
        usrSvc.registrarOrganizador(
          Usuario.nuevoOrganizador(
            null, organizador1Email, organizador1Password,
            organizador1TelPref, organizador1TelSuf, organizador1Nombre,
            organizador1ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20)),
          null);
      }

      if (!usrSvc.emailTomado(staff1Email)) {
        usrSvc.registrarStaff(
          Usuario.nuevoStaff(
            null, staff1Email, staff1Password, staff1TelPref,
            staff1TelSuf, staff1Nombre, staff1ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20),
            "Staff con todas las responsabilidades",
            true, true, true,
            true),
          null);
      }

      if (!usrSvc.emailTomado(staff2Email)) {
        usrSvc.registrarStaff(
          Usuario.nuevoStaff(
            null, staff2Email, staff2Password, staff2TelPref,
            staff2TelSuf, staff2Nombre, staff2ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20),
            "Staff sin responsabilidades",
            false, false, false,
            false),
          null);
      }

      if (!usrSvc.emailTomado(alumno1Email)) {
        usrSvc.registrarAlumno(
          Usuario.nuevoAlumno(
            null, alumno1Email, alumno1Password, alumno1TelPref,
            alumno1TelSuf, alumno1Nombre, alumno1ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20),
            alumno1NoControl, alumno1CodigoCarrera, alumno1Semestre,
            alumno1Grupo, alumno1Externo, alumno1Curp,
            alumno1EmailInstitucional),
          null);
      }

      if (!usrSvc.emailTomado(alumno2Email)) {
        usrSvc.registrarAlumno(
          Usuario.nuevoAlumno(
            null, alumno2Email, alumno2Password, alumno2TelPref,
            alumno2TelSuf, alumno2Nombre, alumno2ApPaterno, null,
            LocalDate.of(1990, JANUARY, 20),
            alumno2NoControl, alumno2CodigoCarrera, alumno2Semestre,
            alumno2Grupo, alumno2Externo, alumno2Curp,
            alumno2EmailInstitucional),
          null);
      }
    };
  }
}
