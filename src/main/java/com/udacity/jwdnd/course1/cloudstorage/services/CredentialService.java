package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public void createNewCredential(Credential credential){
        credentialMapper.insert(credential);
    }

    public List<Credential> getAllCredentialsFromThisUser(Integer userId){
        return credentialMapper.getCredentialsFromThisUser(userId);
    }
}
