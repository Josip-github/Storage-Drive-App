package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.repository.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("Creating FileService bean.");
    }

    public int storeFile(MultipartFile file, Authentication authentication) throws IOException {
        InputStream fis = file.getInputStream();
        String nameOfFile = file.getOriginalFilename();
        String typeOfContent = file.getContentType();
        String sizeOfFile = String.valueOf(file.getSize());
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        Integer idOfUser = user.getUserId();
        byte[] dataOfFile = fis.readAllBytes();
        File fileToStore = new File(nameOfFile, typeOfContent,sizeOfFile, idOfUser, dataOfFile);
        return fileMapper.insert(fileToStore);
    }
}
