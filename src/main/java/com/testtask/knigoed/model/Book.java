package com.testtask.knigoed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private Integer year;

    @Column(name = "brand")
    private String brand;

    @Column(name = "stock")
    private String stock;

    @Column(name = "price")
    private Integer price;

}
