package com.tecn.tijuana.congresos.utils;

import org.hibernate.annotations.ValueGenerationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@ValueGenerationType(generatedBy = GeneradorDeFolios.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface FolioGenerado {

  int longitud () default 6;
}
