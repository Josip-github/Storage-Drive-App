package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {

    private UserService userService;
    private CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @ModelAttribute("credential")
    public Credential getCredential(){
        return new Credential();
    }

    @PostMapping("/add-credential")
    public String addNewCredential(Credential credential, Authentication authentication, Model model){
        User user = userService.getUser(authentication.getPrincipal().toString());
        credential.setUserId(user.getUserId());

        try {
            credentialService.createNewCredential(credential);
            model.addAttribute("successMessage", "The credential added successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was something wrong with adding credential. Please try again.");
        }
        return "result";
    }
}
