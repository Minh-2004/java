package com.phamquangminh.example5.service;

import java.io.IOException;
import com.phamquangminh.example5.payloads.ColorOptionDto;
import java.util.List;

public interface ColorOptionService {
    List<ColorOptionDto> getColorsByProduct(Long productId);

    ColorOptionDto addColorToProduct(Long productId, ColorOptionDto colorOptionDTO) throws IOException; // Đổi từ void
                                                                                                        // sang
                                                                                                        // ColorOptionDTO
}
