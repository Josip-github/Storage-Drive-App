package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.repository.FileMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("Creating FileService bean.");
    }

    public void insert(File file){
        fileMapper.insert(file);
    }

    public boolean isFilenameAvailable(String filename, Integer userId){
        return this.fileMapper.getFile(filename, userId) == null;

    }

    public List<File> getAllFilesFromThisUser(Integer id){
        return this.fileMapper.getFilesFromThisUser(id);
    }
}
