package com.phamquangminh.example5.payloads;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorOptionDto {
    private ProductDTO product;
    private Long id;
    private String colorName;
    private MultipartFile imageFile; // cho request
    private String image; // cho response
    private double price;
}
