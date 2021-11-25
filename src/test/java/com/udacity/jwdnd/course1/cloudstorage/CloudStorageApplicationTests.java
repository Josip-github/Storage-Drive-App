package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;

import java.util.List;

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
		driver.get(baseURL + "/signup");
		assertEquals("Sign Up", driver.getTitle());

		signupPage.signupAction(firstname, lastname, username, password);

		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());

		loginPage.loginAction(username, password);

		driver.get(baseURL + "/home");
		assertEquals("Home", driver.getTitle());

		homePage.logoutAction();
		assertEquals(baseURL + "/login", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		assertNotEquals("Home", driver.getTitle());
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
		assertEquals("Title1", noteTabPage.getListOfNoteElements(driver).get(0));
		assertEquals("Description1", noteTabPage.getListOfNoteElements(driver).get(1));

		driver.get(baseURL + "/home");
		driver.findElement(By.id("nav-notes-tab")).click();
		noteTabPage.editNoteAction(driver, "Title2", "Description2", noteTab); // driver edits the note

		driver.get(baseURL + "/home");
		List<String> updatedListOfNoteElements = noteTabPage.getListOfNoteElements(driver);
		assertEquals("Title2", updatedListOfNoteElements.get(0));
		assertEquals("Description2", updatedListOfNoteElements.get(1));

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

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		credentialTabPage.deleteCredentialAction(driver); // delete credential

		driver.get(baseURL + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		assertEquals(0, this.credentialTabPage.getUrlList().size());
	}



}
