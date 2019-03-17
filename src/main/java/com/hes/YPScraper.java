package com.hes;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


import java.util.List;

public class YPScraper {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  /*@Before
  public void setUp2() throws Exception {
    FirefoxProfile profile = new FirefoxProfile();
    //FirefoxOptions profile = new FirefoxOptions();
    profile.setPreference("network.proxy.type", 0);
    //profile.addPreference("network.proxy.type", 0);
    //FirefoxOptions op = new FirefoxOptions();
    //op.
    driver = new FirefoxDriver(profile);
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }*/

  @Before
  public void setUp() throws Exception {
    // https://stackoverflow.com/questions/49789963/org-openqa-selenium-sessionnotcreatedexception-unable-to-find-a-matching-set-of
    DesiredCapabilities dc = new DesiredCapabilities();
    dc.setCapability("marionatte", false);
    FirefoxOptions opt = new FirefoxOptions();
    opt.addPreference("network.proxy.type", 0);
    opt.merge(dc);
    driver = new FirefoxDriver(opt);
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testInd() throws Exception {

    CVSWriter.setName("./sample_a.csv");

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

    CVSWriter.writeRecord("---------- " + location + " ----------");

    for (int i=1; i<listOfElements.size();i++){
      List<WebElement> elements_name  = listOfElements.get(i).findElements(By.cssSelector("div.listing-data[data-full-name]"));
      String name = "";
      String email = "";
      String phone = "";
      String full_address = "";
      String sububrb = "";
      String address = "";
      if (elements_name.size() > 0) {
        name = elements_name.get(0).getAttribute("data-full-name");
        full_address = elements_name.get(0).getAttribute("data-full-address");
        sububrb = elements_name.get(0).getAttribute("data-suburb");
        address = full_address + " " + sububrb;
      }

      // Email
      List<WebElement> elements_email  = listOfElements.get(i).findElements(By.cssSelector("a.contact[data-email]"));
      if (elements_email.size() > 0) {
        email = elements_email.get(0).getAttribute("data-email");
      }

      // Phone
      List<WebElement> elements_phone  = listOfElements.get(i).findElements(By.cssSelector("a.contact-phone[href]"));
      if (elements_phone.size() > 0) {
        phone = elements_phone.get(0).getAttribute("href").replace("tel:","");
      }

      CVSWriter.writeRecord(name , email , phone, address);
    }

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

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
      YPScraper y = new YPScraper();
      y.setUp();
      y.testInd();
  }
}
