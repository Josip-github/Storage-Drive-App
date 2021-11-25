package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

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

    @FindBy(id = "notetitle")
    private List<WebElement> titleList;

    @FindBy(id = "notedescription")
    private List<WebElement> descriptionList;

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
    }

    public List<String> getListOfNoteElements(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 7);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        navNoteTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(addButton));
        List<String> listOfNoteElements = new ArrayList<>(List.of(titleList.get(0).getText(), descriptionList.get(0).getText()));
        return listOfNoteElements;
    }

    public void editNoteAction(WebDriver driver, String title, String description, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 20);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        } catch (TimeoutException ex){
            System.out.println("Timeout exception");
            nav.click();
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputField)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputField)).sendKeys(title);
        wait.until(ExpectedConditions.elementToBeClickable(descriptionInputField)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(descriptionInputField)).sendKeys(description);
        wait.until(ExpectedConditions.elementToBeClickable(this.saveChangesButton)).click();
    }

    public void deleteNoteAction(WebDriver driver, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 20);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        } catch (TimeoutException ex){
            System.out.println("Timeout exception");
            nav.click();
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }

    public int getSizeOfNoteList(){
        return this.titleList.size();
    }

    public List<WebElement> getTitleList(){
        return this.titleList;
    }

    public List<WebElement> getDescriptionList(){
        return this.descriptionList;
    }
}
