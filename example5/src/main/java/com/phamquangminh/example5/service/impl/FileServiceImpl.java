package com.phamquangminh.example5.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phamquangminh.example5.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Lấy tên tệp gốc
        String originalFileName = file.getOriginalFilename();

        // Tạo ID ngẫu nhiên
        String randomId = UUID.randomUUID().toString();

        // Tạo tên tệp mới
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));

        // Tạo đường dẫn đầy đủ tới tệp
        String filePath = path + File.separator + fileName;

        // Tạo thư mục nếu chưa tồn tại
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir(); // Sửa lại từ "Ifolder.exists()"
        }

        // Sao chép tệp vào thư mục
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Trả về tên tệp đã được tải lên
        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        // Tạo đường dẫn tệp đầy đủ
        String filePath = path + File.separator + fileName;

        // Trả về InputStream để đọc tệp
        InputStream inputStream = new FileInputStream(filePath);
        return inputStream;
    }
}
