package com.epam.esm.certificate.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationCriteria {
   private String page;
   private String size;
}
