package com.hes;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVPrinter;
import org.junit.*;
import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ypscraper {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("network.proxy.type", 0);
    driver = new FirefoxDriver(profile);
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testInd1() throws Exception {

    String type = "indian restaurant";
    String location = "glenroy";
    driver.get("https://www.yellowpages.com.au/");
    driver.findElement(By.id("clue")).click();
    driver.findElement(By.id("clue")).clear();
    driver.findElement(By.id("clue")).sendKeys(type);
    driver.findElement(By.id("where")).click();
    driver.findElement(By.id("where")).sendKeys(location);
    driver.findElement(By.id("clue")).sendKeys(Keys.ENTER);
    List<WebElement>  listOfElements = driver.findElements(By.cssSelector("div.find-show-more-trial"));
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
    for (int i=1; i<listOfElements.size();i++){
      List<WebElement> elements  = listOfElements.get(i).findElements(By.cssSelector("a.contact[data-email]"));
      if (elements.size() > 0) {
        System.out.println("Email 1:" + elements.get(0).getAttribute("data-email"));
        CVSWriter.writeRecord(elements.get(0).getAttribute("data-email"));
      }
    }

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

    CVSWriter.flush();
    //element.findElements(By.cssSelector("div.call-to-action")).get(0).findElements(By.tagName("a"))
  }

  @Test
  public void testInd() throws Exception {

    CVSWriter.setName("/tmp/sample.csv");

    Configs config = ConfigReader.loadConfig();
    int type_count = 0;
    while (config.types.size() > type_count){
      String type = (String)config.types.get(type_count);
      type_count++;
      int loc_count = 0;
      while (config.locations.size() > loc_count){
        String location = (String)config.locations.get(loc_count);
        getData(type,location);
        loc_count++;
      }
    }
    CVSWriter.flush();
  }

  void getData(String type, String location){
    driver.get("https://www.yellowpages.com.au/");
    driver.findElement(By.id("clue")).click();
    driver.findElement(By.id("clue")).clear();
    driver.findElement(By.id("clue")).sendKeys(type);
    driver.findElement(By.id("where")).click();
    driver.findElement(By.id("where")).sendKeys(location);
    driver.findElement(By.id("clue")).sendKeys(Keys.ENTER);
    List<WebElement>  listOfElements = driver.findElements(By.cssSelector("div.find-show-more-trial"));
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);

    CVSWriter.writeRecord("Priniting emails of " + type + " in " + location, "");
    for (int i=1; i<listOfElements.size();i++){
      List<WebElement> elements_name  = listOfElements.get(i).findElements(By.cssSelector("div.listing-data[data-full-name]"));
      String name = "";
      if (elements_name.size() > 0) {
        name = elements_name.get(0).getAttribute("data-full-name");
      }

      List<WebElement> elements  = listOfElements.get(i).findElements(By.cssSelector("a.contact[data-email]"));
      if (elements.size() > 0) {
        System.out.println("Email 1:" + elements.get(0).getAttribute("data-email"));
        CVSWriter.writeRecord(name , elements.get(0).getAttribute("data-email"));
      }
    }

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

  }
  void getData_1(String type, String location){
    driver.get("https://www.yellowpages.com.au/");
    driver.findElement(By.id("clue")).click();
    driver.findElement(By.id("clue")).clear();
    driver.findElement(By.id("clue")).sendKeys(type);
    driver.findElement(By.id("where")).click();
    driver.findElement(By.id("where")).sendKeys(location);
    driver.findElement(By.id("clue")).sendKeys(Keys.ENTER);

    CVSWriter.println();
    CVSWriter.writeRecord(type + " in " + location + ".");

    List<WebElement> listOfElements = driver.findElements(By.cssSelector("a.contact[data-email]"));
    System.out.println("Priniting emails of " + type + " in " + location);
    for (int i=0; i<listOfElements.size();i++){
      System.out.println("Email:" + listOfElements.get(i).getAttribute("data-email"));
      CVSWriter.writeRecord(listOfElements.get(i).getAttribute("data-email"));
    }
  }
  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
  public static void main(String[] args) throws Exception {
      ypscraper y = new ypscraper();
      y.setUp();
      y.testInd();
  }
}
