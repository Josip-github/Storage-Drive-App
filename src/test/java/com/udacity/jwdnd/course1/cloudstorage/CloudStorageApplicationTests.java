package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private String baseURL;

	private String firstname = "Anakin";
	private String lastname = "Skywalker";
	private String username = "DarthVader";
	private String password = "Sith";

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;
	private NoteTabPage noteTabPage;
	private CredentialTabPage credentialTabPage;

	@Autowired
	private CredentialService credentialService;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@BeforeEach
	public void beforeEach() {
		this.baseURL = "http://localhost:" + port;
		this.signupPage = new SignupPage(driver);
		this.loginPage = new LoginPage(driver);
		this.homePage = new HomePage(driver);
		this.noteTabPage = new NoteTabPage(driver);
		this.credentialTabPage = new CredentialTabPage(driver);
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testValidLoginAndLogout(){

		driver.get(baseURL + "/login");  // go to the login page
		assertEquals("Login", driver.getTitle());

		driver.get(baseURL + "/signup");  // go to the signup page
		assertEquals("Sign Up", driver.getTitle());

		signupPage.signupAction(firstname, lastname, username, password); // signup action

		loginPage.loginAction(username, password);  // login action

		driver.get(baseURL + "/home");  // go to the home page
		assertEquals("Home", driver.getTitle());

		homePage.logoutAction();  // logout action
		assertEquals(baseURL + "/login", driver.getCurrentUrl());

		driver.get(baseURL + "/home");  // go to the home page again
		assertNotEquals("Home", driver.getTitle());  // but it doesn't display the home page because driver is unauthorized
	}

	@Test
	@Order(2)
	public void testNoteCRUDFunctions(){
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.get(baseURL + "/signup");
		signupPage.signupAction(firstname, lastname, username, password); // driver signs up a user

		driver.get(baseURL + "/login");
		loginPage.loginAction(username, password); // driver logs the user in

		driver.get(baseURL + "/home");
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();
		noteTabPage.addNewNoteAction(driver,"Title1", "Description1", noteTab); // driver adds a new note

		driver.get(baseURL + "/home");
		driver.findElement(By.id("nav-notes-tab")).click();
		assertEquals("Title1", noteTabPage.getListOfNoteElements(driver).get(0));
		assertEquals("Description1", noteTabPage.getListOfNoteElements(driver).get(1));
		assertEquals("Title1", noteTabPage.getTitleList().get(0).getText());
		assertEquals("Description1", noteTabPage.getDescriptionList().get(0).getText());

		driver.get(baseURL + "/home");
		driver.findElement(By.id("nav-notes-tab")).click();
		noteTabPage.editNoteAction(driver, "Title2", "Description2", noteTab); // driver edits the note

		driver.get(baseURL + "/home");
		List<String> updatedListOfNoteElements = noteTabPage.getListOfNoteElements(driver);
		assertEquals("Title2", updatedListOfNoteElements.get(0));
		assertEquals("Description2", updatedListOfNoteElements.get(1));
		assertEquals("Title2", noteTabPage.getTitleList().get(0).getText());
		assertEquals("Description2", noteTabPage.getDescriptionList().get(0).getText());

		driver.get(baseURL + "/home");
		driver.findElement(By.id("nav-notes-tab")).click();
		noteTabPage.deleteNoteAction(driver, noteTab); // driver deletes the note

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-notes-tab"))).click();
		assertEquals(0, this.noteTabPage.getSizeOfNoteList());
	}

	@Test
	@Order(3)
	public void testCredentialCRUDFunctions(){
		String url = "udacity.com";
		String usernameCredential = "udacityUser123";
		String passwordCredential = "pass246!word";

		/*driver.get(baseURL + "/signup");
		signupPage.signupAction(firstname, lastname, username, password);*/

		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());

		loginPage.loginAction(username, password); // login
		driver.get(baseURL + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click(); // go to the credential tab
		int sizeOfUrlList = this.credentialTabPage.getUrlList().size();
		int sizeOfUsernameList = this.credentialTabPage.getUsernameList().size();
		int sizeOfPasswordList = this.credentialTabPage.getPasswordList().size();
		this.credentialTabPage.addNewCredentialAction(driver, url, usernameCredential, passwordCredential); // add new credential

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		assertEquals(sizeOfUrlList + 1, this.credentialTabPage.getUrlList().size());
		assertEquals(sizeOfUsernameList + 1, this.credentialTabPage.getUsernameList().size());
		assertEquals(sizeOfPasswordList + 1, this.credentialTabPage.getPasswordList().size());

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		List<String> detailsOfCredential = credentialTabPage.getAttributesOfCredentialInstance(driver, 0);
		assertEquals(url, detailsOfCredential.get(0));
		assertEquals(usernameCredential, detailsOfCredential.get(1));
		assertNotEquals(passwordCredential, detailsOfCredential.get(2));
		assertEquals(url, credentialTabPage.getUrlList().get(0).getText());
		assertEquals(usernameCredential, credentialTabPage.getUsernameList().get(0).getText());
		assertNotEquals(passwordCredential, credentialTabPage.getPasswordList().get(0).getText()); // assertion that this element is not equal to the unencrypted password
		assertNotNull(credentialTabPage.getPasswordList().get(0).getText()); // assertion that this element is not null

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(credentialTabPage.getEditButtonList().get(0))).click();

		// In the following lines of code driver tests that viewable password
		// in Credential modal is equal to the original password that was typed in when credential was created

		WebElement passwordInputField = credentialTabPage.getPasswordInputField();
		wait.until(ExpectedConditions.elementToBeClickable(passwordInputField));
		String decryptedPassword = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value", passwordInputField);
		String scriptHtml = "return document.getElementById('credential-id').getAttribute('value');";
		int credentialId = Integer.parseInt( ((JavascriptExecutor) driver).executeScript(scriptHtml).toString());
		Credential credential = credentialService.getCredentialByItsId(credentialId);
		String passwordAcquiredfromCredentialId = credentialService.decryptPassword(credential);
		assertEquals(passwordCredential, decryptedPassword);
		assertEquals(passwordCredential, passwordAcquiredfromCredentialId);



		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		String newUrl = "udemy.com";
		String newUsername = "udemyUser456";
		String newPassword = "a1.r3-op().";
		this.credentialTabPage.editCredential(driver, 0, newUrl, newUsername, newPassword); // edit credential

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		detailsOfCredential = credentialTabPage.getAttributesOfCredentialInstance(driver, 0);
		assertEquals(newUrl, detailsOfCredential.get(0));
		assertEquals(newUsername, detailsOfCredential.get(1));
		assertNotEquals(newPassword, detailsOfCredential.get(2));
		assertEquals(newUrl, credentialTabPage.getUrlList().get(0).getText());
		assertEquals(newUsername, credentialTabPage.getUsernameList().get(0).getText());
		assertNotEquals(newPassword, credentialTabPage.getPasswordList().get(0).getText()); // assertion that updated password is encrypted
		assertNotNull(credentialTabPage.getPasswordList().get(0).getText()); // assertion that this element is not null


		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		credentialTabPage.deleteCredentialAction(driver); // delete credential

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		assertEquals(0, credentialTabPage.getUrlList().size());
		assertEquals(0, credentialTabPage.getUsernameList().size());
		assertEquals(0, credentialTabPage.getPasswordList().size());
	}




}
