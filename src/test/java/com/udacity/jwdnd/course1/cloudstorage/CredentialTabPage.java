package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialTabPage {

    @FindBy(id = "nav-credentials-tab") // home.html line 23
    private WebElement navCredentialTab;

    @FindBy(id = "add-new-credential") // home.html line 134
    private WebElement addNewCredButton;

    @FindBy(id = "credential-url") // home.html line 178
    private WebElement urlInputField;

    @FindBy(id = "credential-username") // home.html line 182
    private WebElement usernameInputField;

    @FindBy(id = "credential-password") // home.html line 186
    private WebElement passwordInputField;

    @FindBy(id = "credentialSubmit") // home.html line 188
    private WebElement saveChangesButton;

    public CredentialTabPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }


}
