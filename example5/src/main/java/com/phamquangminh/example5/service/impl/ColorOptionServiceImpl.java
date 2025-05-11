package com.phamquangminh.example5.service.impl;

import com.phamquangminh.example5.entity.ColorOption;
import com.phamquangminh.example5.entity.Product;
import com.phamquangminh.example5.exceptions.ResourceNotFoundException;
import com.phamquangminh.example5.payloads.ColorOptionDto;
import com.phamquangminh.example5.repository.ColorOptionRepository;
import com.phamquangminh.example5.repository.ProductRepo;
import com.phamquangminh.example5.service.ColorOptionService;
import com.phamquangminh.example5.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class ColorOptionServiceImpl implements ColorOptionService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ColorOptionRepository colorOptionRepo;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String uploadDir;

    @Override
    public ColorOptionDto addColorToProduct(Long productId, ColorOptionDto dto) throws IOException {
        // 1. Lấy product
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // 2. Upload file nếu có
        MultipartFile file = dto.getImageFile();
        String storedFileName = null;
        if (file != null && !file.isEmpty()) {
            storedFileName = fileService.uploadImage(uploadDir, file);
        }

        // 3. Tạo entity và lưu
        ColorOption opt = new ColorOption();
        opt.setColorName(dto.getColorName());
        opt.setPrice(dto.getPrice());
        opt.setImage(storedFileName);
        opt.setProduct(product);
        ColorOption saved = colorOptionRepo.save(opt);

        // 4. Map lại vào DTO trả về: imageFile = null, image = tên file
        ColorOptionDto resp = new ColorOptionDto();
        resp.setId(saved.getId());
        resp.setColorName(saved.getColorName());
        resp.setPrice(saved.getPrice());
        resp.setImageFile(null);
        resp.setImage(saved.getImage());
        return resp;
    }

    @Override
    public List<ColorOptionDto> getColorsByProduct(Long productId) {
        return colorOptionRepo.findByProductProductId(productId).stream()
                .map(opt -> {
                    ColorOptionDto dto = new ColorOptionDto();
                    dto.setId(opt.getId());
                    dto.setColorName(opt.getColorName());
                    dto.setPrice(opt.getPrice());
                    dto.setImage(opt.getImage());
                    dto.setImageFile(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
