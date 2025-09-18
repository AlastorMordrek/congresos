package com.tecn.tijuana.congresos.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public abstract class Api {

  public static final int DEFAULT_PAGE = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;



  public static Pageable pagina () {
    return PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
  }

  public static Pageable pagina (int page) {
    return PageRequest.of(page, DEFAULT_PAGE_SIZE);
  }

  public static Pageable pagina (int page, int pageSize) {
    return PageRequest.of(page, pageSize);
  }
}
