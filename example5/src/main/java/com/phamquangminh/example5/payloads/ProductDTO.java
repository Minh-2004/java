package com.phamquangminh.example5.payloads;

import java.util.List;
import lombok.Data;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image; // Đường dẫn ảnh
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private Long categoryId;
    private List<ColorOptionDto> colorOptions;
}
