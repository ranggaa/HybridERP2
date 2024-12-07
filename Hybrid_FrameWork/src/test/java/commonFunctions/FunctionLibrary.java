package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
public static Properties conpro;
public static WebDriver driver;
//method for launching browser
public static WebDriver startBrowser()throws Throwable
{
	conpro = new Properties();
	//load property file
	conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser Value is Not Matching",true);
	}
	return driver;
}
//method for launch url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for wait for any element
public static void waitForElement(String LocatorType,String LocatorValue,String wait)
{
	WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(wait)));
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		//wait unti element is visible
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		//wait unti element is visible
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		//wait unti element is visible
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
	
}
public static void typeAction(String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}
}
//method for buttons,links,radiobutton,checkboxes and images
public static void clickAction(String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
}
//method for validateTitle
public static void validateTitle(String Expected_Title)
{
	String Actual_Title =driver.getTitle();
	try {
		Assert.assertEquals(Actual_Title, Expected_Title,"Title is Not Matching");
	} catch (AssertionError e) {
		System.out.println(e.getMessage());
	}
}
public static void closeBrowser()
{
	driver.quit();
}
//method for any listbox
public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
	//convert testdata into integer
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
		element.selectByIndex(value);
		
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
	//convert testdata into integer
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.name(LocatorValue)));
		element.selectByIndex(value);
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
	//convert testdata into integer
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.id(LocatorValue)));
		element.selectByIndex(value);
		
	}
}
//capture stock number and write into notepad under CaptureData Folder
public static void captureStock(String LocatorType,String LocatorValue)throws Throwable
{
	String stockNumber="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		stockNumber=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		stockNumber=driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		stockNumber=driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	//create noteppad and write stocknumber
	FileWriter fw = new FileWriter("./CaptureData/stoccknum.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(stockNumber);
	bw.flush();
	bw.close();
	
}
//method for verify stock number in table
public static void stockTable()throws Throwable
{
	//read stock number from notepad
	FileReader fr = new FileReader("./CaptureData/stoccknum.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	//capture stock nyumber from table
	String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	Reporter.log(Exp_Data+"         "+Act_data,true);
	try {
		Assert.assertEquals(Act_data, Exp_Data, "Stock Number NNot Matching");
	} catch (AssertionError e) {
		System.out.println(e.getMessage());
	}
	
}
//method for capture supplier number into notepad
public static void captureSup(String LocatorType,String LocatorValue)throws Throwable
{
//capture supplier number 
	String supplierNum="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		supplierNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		supplierNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		supplierNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		
	}
	//create note pad into capturedata folder and write
	FileWriter fw = new FileWriter("./CaptureData/suplliernum.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNum);
	bw.flush();
	bw.close();
}
//method for verify supplier number in table
public static void supplierTable()throws Throwable
{
	FileReader fr = new FileReader("./CaptureData/suplliernum.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data =br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(Exp_Data+"      "+Act_data,true);
	try {
		Assert.assertEquals(Act_data, Exp_Data, "Supplier Number Not Matching");
	} catch (AssertionError e) {
		System.out.println(e.getMessage());
	}
}
//method for captuser customer number into notepad
public static void captureCus(String LocatorType,String LocatorValue)throws Throwable
{
	String customerNum="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		customerNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		customerNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		customerNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		
	}
	FileWriter fw = new FileWriter("./CaptureData/customernum.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(customerNum);
	bw.flush();
	bw.close();
}
//method for reading customer number from notepad and verify in table
public static void CustomerTable()throws Throwable
{
	FileReader fr = new FileReader("./CaptureData/customernum.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data =br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
	Reporter.log(Exp_Data+"      "+Act_data,true);
	try {
		Assert.assertEquals(Act_data, Exp_Data, "Customer Number Not Matching");
	} catch (AssertionError e) {
		System.out.println(e.getMessage());
	}
}
//method for generate time stamp
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd");
	return df.format(date);
}
}












