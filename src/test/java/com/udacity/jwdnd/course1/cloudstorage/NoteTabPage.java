package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NoteTabPage {

    @FindBy(id = "nav-notes-tab")
    private WebElement navNoteTab;

    @FindBy(id = "add-new-note")
    private WebElement addButton;

    @FindBy(id = "edit-note")
    private WebElement editButton;

    @FindBy(id = "delete-note")
    private WebElement deleteButton;

    @FindBy(id = "note-title")
    private WebElement titleInputField;

    @FindBy(id = "note-description")
    private WebElement descriptionInputField;

    @FindBy(id = "note-modal-submit")
    private WebElement saveChangesButton;

    public NoteTabPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addNewNoteAction(WebDriver driver, String title, String description, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 20);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        } catch (TimeoutException ex){
            System.out.println("Timeout exception");
            nav.click();
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputField)).sendKeys(title);
        wait.until(ExpectedConditions.elementToBeClickable(descriptionInputField)).sendKeys(description);
        wait.until(ExpectedConditions.elementToBeClickable(this.saveChangesButton)).click();




        /*titleInputField.sendKeys(title);
        descriptionInputField.sendKeys(description);
        saveChangesButton.click();*/
    }
}
