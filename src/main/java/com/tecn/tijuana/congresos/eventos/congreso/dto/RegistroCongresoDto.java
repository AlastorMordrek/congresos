package com.tecn.tijuana.congresos.eventos.congreso.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * DTO para el registro de CONGRESOS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistroCongresoDto {


  /**
   * Nombre del CONGRESO.
   * */
  @NotNull(message = "Debe especificar un nombre de Congreso")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  public String nombre;

  /**
   * Descripcion corta del CONGRESO.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  public String resumen = "";

  /**
   * Descripcion detallada del CONGRESO.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  public String descripcion = "";

  /**
   * Direccion donde tendra lugar el CONGRESO.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 200,
    message = "La direccion debe tener entre 0 y 200 caracteres")
  public String direccion = "";

  /**
   * Carrera a la que se limita el CONGRESO.
   * <p>
   * Clave oficial del catalogo de carreras del TecNM. Si se omite, el CONGRESO
   * no tiene restriccion por carrera. Solo aplica al inscribir ALUMNOS; los
   * BOLETOS existentes conservan su acceso.
   * */
  @Size(max = 13,
    message = "El codigo de carrera no puede exceder 13 caracteres")
  public String codigoCarrera;



  /**
   * Cuando iniciara el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de inicio")
  public LocalDateTime fechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de terminacion")
  public LocalDateTime fechaFin;



  /**
   * Cuando inicia el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre y cuando el CONGRESO no haya concluido aun.
   * */
  @NotNull(message = "Debe especificar una fecha de inicio de inscripciones")
  public LocalDateTime inscripcionesFechaInicio;

  /**
   * Cuando termina el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre y cuando el CONGRESO no haya concluido aun.
   * */
  @NotNull(message = "Debe especificar una fecha de fin de inscripciones")
  public LocalDateTime inscripcionesFechaFin;



  /**
   * Determina si el CONGRESO tiene costo. {@code true = No tiene costo}.
   * {@code default: true}
   * */
  @JsonSetter(nulls = Nulls.SKIP)
  public boolean gratuito = Congreso.GRATUITO;



  /**
   * Cuantos espacios para inscripciones hay para el CONGRESO.
   * {@code 0 = sin limite}.
   * */
  @Min(value = 0, message = "El cupo debe ser mayor a 0")
  @Max(value = 5000, message = "El cupo debe ser menor a 5000")
  public int cupo = 0;



  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * {@code 0 = sin staff requerido}.
   * */
  @Min(value = 0, message = "El staff requerido debe ser mayor o igual a 0")
  @Max(value = 100, message = "El staff requerido debe ser menor o igual a 100")
  public int staffCantidad = 0;

  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  public String staffRequerimientos = "";



  /**
   * Cuantas ASISTENCIAS distintas requiere el ALUMNO para conseguir su
   * acreditacion.
   * {@code default: 1}.
   * */
  @Min(value = 1, message = "Se debe requerir al menos una Asistencia")
  @Max(value = 40, message = "Se deben requerir como maximo 40 Asistencias")
  public int alumnoAcreditacionAsistenciasRequeridas = 1;

  /**
   * Cuanto Tiempo Asistido total requiere el ALUMNO para conseguir su
   * acreditacion (expresado en segundos).
   * {@code default: 1}.
   * */
  @Min(value = 1,
    message = "Se debe requerir al menos 1 segundo de tiempo asistido")
  @Max(value = 144000,
    message = "Se deben requerir como maximo 40 horas de tiempo asistido")
  public long alumnoAcreditacionTiempoAsistidoRequerido = 1;



  /**
   * Creditos que otorga la constancia de acreditacion.
   * */
  @JsonSetter(nulls = Nulls.SKIP)
  @Min(value = 1, message = "La constancia debe otorgar al menos 1 credito")
  @Max(value = 20, message = "La constancia no puede otorgar mas de 20 creditos")
  public int constanciaCreditos = 1;

  /**
   * Nombre de la institucion que aparece en la constancia.
   * */
  @Size(max = 150)
  public String constanciaInstituto;

  /**
   * Subtitulo oficial de la constancia.
   * */
  @Size(max = 150)
  public String constanciaSubtitulo;

  /**
   * Ciudad de emision.
   * */
  @Size(max = 80)
  public String constanciaCiudad;

  /**
   * Estado de emision.
   * */
  @Size(max = 80)
  public String constanciaEstado;

  /**
   * Pais de emision.
   * */
  @Size(max = 80)
  public String constanciaPais;

  /**
   * Nombre de la jefatura de servicios escolares.
   * */
  @Size(max = 100)
  public String constanciaJefaNombre;

  /**
   * Cargo de la jefatura de servicios escolares.
   * */
  @Size(max = 150)
  public String constanciaJefaCargo;

  /**
   * Nombre de la persona que firma.
   * */
  @Size(max = 100)
  public String constanciaJefeNombre;

  /**
   * Departamento de la persona que firma.
   * */
  @Size(max = 150)
  public String constanciaDepartamento;

  /**
   * Primer lema institucional.
   * */
  @Size(max = 150)
  public String constanciaLema1;

  /**
   * Segundo lema institucional.
   * */
  @Size(max = 150)
  public String constanciaLema2;

  /**
   * Texto del pie de la constancia.
   * */
  @Size(max = 500)
  public String constanciaPieTexto;

  /**
   * Nota opcional de copia de correspondencia.
   * */
  @Size(max = 100)
  public String constanciaNotaCcp;

  /**
   * Iniciales opcionales de la persona que firma y de quien captura.
   * */
  @Size(max = 50)
  public String constanciaIniciales;

  /**
   * Actividad realizada, opcional.
   * */
  @Size(max = 200)
  public String constanciaActividad;

  /**
   * Nivel de desempeno, opcional.
   * */
  @Size(max = 100)
  public String constanciaNivelDesempeno;
}
