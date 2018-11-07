package com.thd.ecommercespringmvc.storageservices;

import com.thd.ecommercespringmvc.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ImageStorage implements StorageService {
    private Logger logger = LogManager.getLogger(ImageStorage.class);
    private final Path rootLocation = Paths.get(Product.PRODUCT_ROOT_IMAGE);
    @Override
    public void init() {

    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String fileName = String.valueOf(System.currentTimeMillis());
            String fileType = file.getOriginalFilename().split("\\.")[1];
            fileName= fileName + "." + fileType;
            Files.copy(file.getInputStream(), rootLocation.resolve(fileName));
            logger.info("Storing file : " + file.getOriginalFilename());
            return fileName;
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
