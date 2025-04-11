package com.testtask.knigoed.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookFilterDto {
    private String title;
    private String brand;
    private Integer year;

}
