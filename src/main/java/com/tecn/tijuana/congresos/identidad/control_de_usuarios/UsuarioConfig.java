package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static java.time.Month.JANUARY;

@Configuration
public class UsuarioConfig {

  @Value("${ADMIN_1_EMAIL}")
  private String admin1Email;
  @Value("${ADMIN_1_PWD}")
  private String admin1Password;
  @Value("${ADMIN_1_TEL_PREF")
  private String admin1TelPref;
  @Value("${ADMIN_1_TEL_SUF")
  private String admin1TelSuf;
  @Value("${ADMIN_1_NOMBRE")
  private String admin1Nombre;
  @Value("${ADMIN_1_AP_PATERNO")
  private String admin1ApPaterno;

  @Value("${ORGANIZADOR_1_EMAIL}")
  private String organizador1Email;
  @Value("${ORGANIZADOR_1_PWD}")
  private String organizador1Password;
  @Value("${ORGANIZADOR_1_TEL_PREF")
  private String organizador1TelPref;
  @Value("${ORGANIZADOR_1_TEL_SUF")
  private String organizador1TelSuf;
  @Value("${ORGANIZADOR_1_NOMBRE")
  private String organizador1Nombre;
  @Value("${ORGANIZADOR_1_AP_PATERNO")
  private String organizador1ApPaterno;

  @Value("${STAFF_1_EMAIL}")
  private String staff1Email;
  @Value("${STAFF_1_PWD}")
  private String staff1Password;
  @Value("${STAFF_1_TEL_PREF")
  private String staff1TelPref;
  @Value("${STAFF_1_TEL_SUF")
  private String staff1TelSuf;
  @Value("${STAFF_1_NOMBRE")
  private String staff1Nombre;
  @Value("${STAFF_1_AP_PATERNO")
  private String staff1ApPaterno;

  @Value("${STAFF_2_EMAIL}")
  private String staff2Email;
  @Value("${STAFF_2_PWD}")
  private String staff2Password;
  @Value("${STAFF_2_TEL_PREF")
  private String staff2TelPref;
  @Value("${STAFF_2_TEL_SUF")
  private String staff2TelSuf;
  @Value("${STAFF_2_NOMBRE")
  private String staff2Nombre;
  @Value("${STAFF_2_AP_PATERNO")
  private String staff2ApPaterno;

  @Value("${ALUMNO_1_EMAIL}")
  private String alumno1Email;
  @Value("${ALUMNO_1_PWD}")
  private String alumno1Password;
  @Value("${ALUMNO_1_TEL_PREF")
  private String alumno1TelPref;
  @Value("${ALUMNO_1_TEL_SUF")
  private String alumno1TelSuf;
  @Value("${ALUMNO_1_NOMBRE")
  private String alumno1Nombre;
  @Value("${ALUMNO_1_AP_PATERNO")
  private String alumno1ApPaterno;
  @Value("${ALUMNO_1_NO_CONTROL")
  String alumno1NoControl;
  @Value("${ALUMNO_1_CODIGO_CARRERA")
  String alumno1CodigoCarrera;
  @Value("${ALUMNO_1_SEMESTRE")
  int alumno1Semestre;
  @Value("${ALUMNO_1_GRUPO")
  String alumno1Grupo;
  @Value("${ALUMNO_1_EXTERNO")
  boolean alumno1Externo;
  @Value("${ALUMNO_1_CURP")
  String alumno1Curp;
  @Value("${ALUMNO_1_EMAIL_INSTITUCIONAL")
  String alumno1EmailInstitucional;

  @Value("${ALUMNO_2_EMAIL}")
  private String alumno2Email;
  @Value("${ALUMNO_2_PWD}")
  private String alumno2Password;
  @Value("${ALUMNO_2_TEL_PREF")
  private String alumno2TelPref;
  @Value("${ALUMNO_2_TEL_SUF")
  private String alumno2TelSuf;
  @Value("${ALUMNO_2_NOMBRE")
  private String alumno2Nombre;
  @Value("${ALUMNO_2_AP_PATERNO")
  private String alumno2ApPaterno;
  @Value("${ALUMNO_2_NO_CONTROL")
  String alumno2NoControl;
  @Value("${ALUMNO_2_CODIGO_CARRERA")
  String alumno2CodigoCarrera;
  @Value("${ALUMNO_2_SEMESTRE")
  int alumno2Semestre;
  @Value("${ALUMNO_2_GRUPO")
  String alumno2Grupo;
  @Value("${ALUMNO_2_EXTERNO")
  boolean alumno2Externo;
  @Value("${ALUMNO_2_CURP")
  String alumno2Curp;
  @Value("${ALUMNO_2_EMAIL_INSTITUCIONAL")
  String alumno2EmailInstitucional;



  @Bean
  CommandLineRunner studentCommandLineRunner (
    ControlDeUsuariosService usrSvc
  ) {
    return args -> {

      if (!usrSvc.emailTomado(admin1Email)) {
        usrSvc.registrarAdmin(
          Usuario.nuevoAdmin(
            null, admin1Email, admin1Password, admin1TelPref,
            admin1TelSuf, admin1Nombre, admin1ApPaterno, null,
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
