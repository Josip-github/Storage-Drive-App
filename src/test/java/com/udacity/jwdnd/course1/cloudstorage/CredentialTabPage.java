package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

    @FindBy(id = "credential-modal-submit") // home.html line 193
    private WebElement saveChangesButton;

    @FindBy(id = "credential-url")
    private List<WebElement> urlList;

    @FindBy(id = "credential-password")
    private List<WebElement> passwordList;

    public CredentialTabPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addNewCredentialAction(WebDriver driver, String url, String username, String password){
        WebDriverWait wait = new WebDriverWait(driver, 30);

        wait.until(ExpectedConditions.elementToBeClickable(navCredentialTab)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addNewCredButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(urlInputField)).sendKeys(url);
        wait.until(ExpectedConditions.elementToBeClickable(usernameInputField)).sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(passwordInputField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton)).click();
    }

    public List<WebElement> getUrlList(){
        return this.urlList;
    }

    public List<WebElement> getPasswordList(){
        return this.passwordList;
    }
}
