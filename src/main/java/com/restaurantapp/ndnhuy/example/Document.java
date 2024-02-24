package com.restaurantapp.ndnhuy.example;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ra_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class Document {

    @Id
    private Long id;

    private String title;

    @Column(name = "user_id")
    private Long userId;
}
