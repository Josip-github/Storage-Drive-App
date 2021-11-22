package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private String baseURL;

	private String firstname = "Anakin";
	private String lastname = "Skywalker";
	private String username = "Darth Vader";
	private String password = "Sith";

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;

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
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
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

}
