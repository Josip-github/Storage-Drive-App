package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createNewCredential(Credential credential){
        credential.setKey(this.encryptionService.generateKey());
        credential.setPassword(this.encryptPassword(credential));
        this.credentialMapper.insert(credential);
    }

    public void updateCredential(Credential credential){
        String key = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialId());
        String encodedPassword = this.encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encodedPassword);
        this.credentialMapper.updateCredential(credential);
    }

    public List<Credential> getAllCredentialsFromThisUser(Integer userId){
        return credentialMapper.getCredentialsFromThisUser(userId);
    }

    public String encryptPassword(Credential credential){
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
    }

    public String decryptPassword(Credential credential){
        return this.encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

}
