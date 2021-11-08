package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private UserService userService;

    public HomeController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homePageView(Authentication auth, Model model){
        User user = userService.getUser(auth.getPrincipal().toString());
        Integer userId = user.getUserId();
        model.addAttribute("files", this.fileService.getAllFilesFromThisUser(userId));
        return "home";
    }

    @PostMapping("/file-upload")
    public String fileUpload(@RequestParam("fileUpload") MultipartFile file, Authentication auth, Model model){
        User user = userService.getUser(auth.getPrincipal().toString());

        model.addAttribute("errorMessage", false);
        model.addAttribute("successMessage", false);

        if(file.getOriginalFilename().isEmpty()){
            model.addAttribute("errorMessage", "Please select a file to upload");
            return "result";
        }

        if(!fileService.isFilenameAvailable(file.getOriginalFilename(), user.getUserId())){
            model.addAttribute("errorMessage", "File with the same filename already exists");
            return "result";
        }

        try {
            fileService.insert(new File(file.getOriginalFilename(),
                    file.getContentType(), file.getSize() + "", user.getUserId(), file.getBytes()));

            List<File> files = fileService.getAllFilesFromThisUser(user.getUserId());
            model.addAttribute("files", files);
            model.addAttribute("successMessage", "File saved successfully!");
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "result";

    }

}
