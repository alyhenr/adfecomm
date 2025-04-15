package com.adfecomm.adfecomm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements  FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        throw new IOException("error");
    }
}
