package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homePageView(Authentication auth, Model model){
        User user = userService.getUser(auth.getPrincipal().toString());
        Integer userId = user.getUserId();
        model.addAttribute("files", this.fileService.getAllFilesFromThisUser(userId));
        model.addAttribute("notes", this.noteService.getAllNotesFromThisUser(userId));
        model.addAttribute("credentials", this.credentialService.getAllCredentialsFromThisUser(userId));
        model.addAttribute("newCredential", new Credential());
        model.addAttribute("newNote", new Note());

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

    @GetMapping("/file-delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, Model model){
        User user = userService.getUser(authentication.getPrincipal().toString());

        model.addAttribute("successMessage", false);
        model.addAttribute("errorMessage", false);

        try{
            fileService.deleteFile(fileId);
            List<File> files = fileService.getAllFilesFromThisUser(user.getUserId());
            model.addAttribute("successMessage", "File is deleted successfully!");
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "result";
    }

    @GetMapping("/file-view/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

}
