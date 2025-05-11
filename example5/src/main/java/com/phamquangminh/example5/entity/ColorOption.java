package com.phamquangminh.example5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String colorName; // Tên màu sắc
    private double price; // Giá cho màu sắc này
    private String image; // Hình ảnh cho màu sắc này

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Mối quan hệ với Product
}
