package com.example;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class AppTest {
    private WebDriver driver;
    private WebDriverWait wait;
    public String excelFilePath = "C:\\Users\\sunda\\Downloads\\Firstcry.xlsx";
    @BeforeClass
    public void setUp()
    {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public String readFromExcel(int rowNumber)
    {
        String searchTerm = "";
        try {
            FileInputStream file = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet("Sheet1");
            Row row = sheet.getRow(rowNumber);
            Cell cell = row.getCell(0);
            searchTerm = cell.getStringCellValue();
            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchTerm;
    }
    @Test(priority=1)
    public void testSearch() {
        driver.get("https://www.firstcry.com");
        driver.manage().window().maximize();
        String searchTerm = readFromExcel(0);
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_box")));
        searchInput.click();
        searchInput.sendKeys(searchTerm, Keys.ENTER);
        Assert.assertTrue(driver.getCurrentUrl().contains("kids-toys"), "URL contains 'kids-toys'. Failed!");
        System.out.println("Test Case 1: URL contains 'kids-toys'. Passed!");
    }
    @Test(priority=2)
    public void testFilter() {
        driver.get("https://www.firstcry.com");
        driver.manage().window().maximize();
        String searchTerm = readFromExcel(1);
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_box")));
        searchInput.click();
        searchInput.sendKeys(searchTerm , Keys.ENTER);
        WebElement ethnicWearCheckbox  = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.sprite_list.sprt.add_clk[data-val='246']")));
        ethnicWearCheckbox.click();
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div.list_back > div.list_ctr_sec.lft > div > div.list_rightp.lft > div.top.fw.lft > div.titleleft > h1"))).isDisplayed(), "Ethnic wear filter applied. Failed!");
        System.out.println("Test Case 2: Ethnic wear filter applied. Passed!");
    }
    @Test(priority=3)
    public void testProductDetails() {
        driver.get("https://www.firstcry.com");
        driver.manage().window().maximize();
        String searchTerm = readFromExcel(2);
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_box")));
        searchInput.click();
        searchInput.sendKeys(searchTerm , Keys.ENTER);
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#maindiv > div:nth-child(1) > div > div.lblock.lft > div.li_txt1.wifi.lft.R13_42")));
        String firstProductText = firstProduct.getText();
        System.out.println("First product text: " + firstProductText);
        WebElement firstProductclk = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'maindiv\']/div[1]/div/div[1]/div[2]/a")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProductclk);
        firstProductclk.click();
        Set<String> handles = driver.getWindowHandles();
        List<String> tabs = new ArrayList<>(handles);
        String secondTab = tabs.get(1);
        driver.switchTo().window(secondTab);
        String productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("prod_name"))).getText();
        Assert.assertEquals(firstProductText, productTitle, "Product title does not match. Failed!");
        System.out.println("Test Case 3: Product title matches. Passed!");
    }
    @AfterClass
    public void CleanUp() {
        driver.quit();
    }
}
