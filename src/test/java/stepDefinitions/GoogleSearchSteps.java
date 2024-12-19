package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class GoogleSearchSteps {

    WebDriver driver = null;
    @Given("browser is open")
    public void browser_is_open() {
        System.out.println("browser_is_open");
        String driverPath = System.getProperty("user.dir").replace("\\", "/");
        System.setProperty("webdriver.chrome.driver", driverPath + "/src/test/resources/web_drivers/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
    }
    @And("user is oon google search page")
    public void user_is_oon_google_search_page() {
        System.out.println("user_is_oon_google_search_page");
        driver.navigate().to("https://google.com");
    }
    @When("user enters a text in search box")
    public void user_enters_a_text_in_search_box() throws InterruptedException {
        System.out.println("user_enters_a_text_in_search_box");
        driver.findElement(By.name("q")).sendKeys("Automation Step by Step");
        Thread.sleep(2000);
    }
    @And("hit enter")
    public void hit_enter() throws InterruptedException {
        System.out.println("hit_enter");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
    }
    @Then("user is navigated to search result")
    public void user_is_navigated_to_search_result() {
        System.out.println("user_is_navigated_to_search_result");
        driver.getPageSource().contains("Training by Raghav");

        driver.close();
        driver.quit();
    }
}
