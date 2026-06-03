package com.qa.base;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;


import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    //protected static WebDriverWait wait;
    protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected static ThreadLocal <Properties> props = new ThreadLocal <Properties>();
    protected static ThreadLocal <HashMap<String, String>> strings = new ThreadLocal <HashMap<String, String>>();
    protected static ThreadLocal <String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal <String> deviceName =  new ThreadLocal<String>();
    protected static ThreadLocal <String> platform = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;

    protected TestUtils utils = new TestUtils();;

    //Se crean los metodos getter y setter para todos los metodos globales que tienen ThreadSafe
   public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver_parameter){
        driver.set(driver_parameter);
    }

    public String getDateTime(){
        return dateTime.get();
    }

    public void setDateTime(String dateTime_parameter){
        dateTime.set(dateTime_parameter);
    }

    public Properties getProps() {
        return props.get();
    }

    public void setProps(Properties props_parameter){
        props.set(props_parameter);
    }

    public HashMap<String,String> getStrings() {
        return strings.get();
    }

    public void setString(HashMap<String,String> strings_parameter){
        strings.set(strings_parameter);
    }

    //Se crean los metodos getter y setter de deviceName para poder obtener la variable en el metodo logs de utils
    public String getDeviceName(){ return deviceName.get(); }

    public void setDeviceName(String deviceName_parameter){
        deviceName.set(deviceName_parameter);
    }

    public String getPlatform(){ return platform.get(); }

    public void setPlatform(String platform_parameter){
       platform.set(platform_parameter);
   }


    @BeforeMethod
    public void beforeMethod(){
        utils.log("VIDEO STARTS RECORDING");
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result){
        utils.log("VIDEO STOPS RECORDING");
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

        //Esta condicion nos permitira hacer la grabacion solo si el test case falla
        if(result.getStatus() == 2) {
            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();

            String dir = "Videos" + File.separator + params.get("platformName") + "_" + params.get("platformVersion")
                    + "_" + params.get("deviceName") + dateTime + File.separator + result.getTestClass().getRealClass().getSimpleName();

            File videoDir = new File(dir);

            synchronized (videoDir){
                if (!videoDir.exists()) {
                    videoDir.mkdirs();
                }
            }

            try {
                FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                stream.write(Base64.decodeBase64(media));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeSuite
    public void beforeSuite() throws Exception, Exception{
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
        server = getAppiumService();
        server.start();
        server.clearOutPutStreams();
        utils.log().info("Appium Server is started");
    }

    @AfterSuite
    public void afterSuite(){
        server.stop();
        utils.log().info("Appium Server is stopped");
    }

    public AppiumDriverLocalService getAppiumService(){
       return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
               .withLogOutput(ByteStreams.nullOutputStream())
               .withLogFile(new File("ServerLogs/server.log")));
    }

    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }

    @Parameters({"platformName", "udid", "deviceName", "systemPort", "chromeDriverPort"})
    @BeforeTest
    public void beforeTest(String platformName, String udid, @Optional("androidOnly")String deviceName, @Optional("androidOnly")int systemPort, @Optional("androidOnly")int chromeDriverPort) throws Exception{

        setDateTime(utils.getDateTime()); //Asignamos la fecha traida desde utils y la colocamos en dateTime de BaseTest
        setDeviceName(deviceName);
        setPlatform(platformName);
        InputStream inputStream = null;
        InputStream stringIs = null;
        Properties props = new Properties();
        AppiumDriver driver;

        String strFile = "logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY", strFile);
        
        try{
            props = new Properties(); //revisar si podemos presindir de esta variable
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";

            //Se crea un objeto inputStream para el archivo config.properties
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            //Cargando el archivo config.properties
            props.load(inputStream);

            setProps(props);

            stringIs = getClass().getClassLoader().getResourceAsStream(xmlFileName);

            setString(utils.parseStringXML(stringIs)); //Asignamos el returno de parseStringXML a la variable strings de BaseTest

            String appUrl = System.getProperty("user.dir") + File.separator +
                    "src" + File.separator +
                    "apps" + File.separator +
                    "swagLabs.apk";

            UiAutomator2Options caps = new UiAutomator2Options();
            //Parametros Especificos
            caps.setPlatformName(platformName);
            caps.setUdid(udid);
            caps.setDeviceName(deviceName);
            caps.setSystemPort(systemPort);
            caps.setChromedriverPort(chromeDriverPort);
            //Parametros Globales
            caps.setAutomationName(props.getProperty("androidAutomationName"));
            caps.setAppPackage(props.getProperty("androidAppPackage"));
            caps.setAppActivity(props.getProperty("androidAppActivity"));

            //caps.setAvd(deviceName);
            //String AndroidAppURL = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
            //caps.setApp(appURL);
            caps.setAvdLaunchTimeout(Duration.ofSeconds(180000));
            URL url = new URL(props.getProperty("appiumURL"));

            driver = new AndroidDriver(url, caps);
            setDriver(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(inputStream != null){
                inputStream.close();
            }
            if (stringIs != null){
                stringIs.close();
            }
        }
    }

    public void waitForVisibility(WebElement e){
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void click(WebElement e){
        waitForVisibility(e);
        e.click();
    }

    public void click(WebElement e, String msg){
        waitForVisibility(e);
        utils.log().info(msg); // <- Enviamos el log para log4J
        ExtentReport.getTest().log(Status.INFO,msg); //Enviamos el log para los reportes
        e.click();
    }

    public WebElement scrollToElementTest(String scrollPage, String contentDescription) {
        WebElement e = null;
        switch (scrollPage) {
            case "productDetailPage":
                e = getDriver().findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().description(\"test-Inventory item page\"))" +
                                ".scrollIntoView(new UiSelector().description(\"" + contentDescription + "\"))"));
                break;
            case "checkoutOverviewPage":
                e = getDriver().findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().description(\"test-CHECKOUT: OVERVIEW\"))" +
                                ".scrollIntoView(new UiSelector().description(\"" + contentDescription + "\"))"));
                break;
            default:
                throw new IllegalArgumentException("Invalid scroll page: " + scrollPage);
        }
        return e;
    }

    // Scroll to element checkout overview page
    public WebElement scrollToElement(String contentDescription) {
        return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().description(\"test-CHECKOUT: OVERVIEW\"))" +
                        ".scrollIntoView(new UiSelector().description(\"" + contentDescription + "\"))"));
    }

    // Scroll to element by content-description (parametrizado)
    public WebElement scrollToElementByParam(String scrollablePage,String contentDescription) {
       return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().description(\"" + scrollablePage + "\"))" +
                        ".scrollIntoView(new UiSelector().description(\"" + contentDescription + "\"))"));
    }

    // Scroll to element by text visible
    public WebElement scrollToElementByText(String text) {
        return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    // Scroll to element by partial text (contains)
    public WebElement scrollToElementContainingText(String partialText) {
        return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().textContains(\"" + partialText + "\"))"));
    }

    // Scroll to end of list
    public void scrollToEnd() {
        getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(10)"));
    }

    // Scroll to beginning of list
    public void scrollToBeginning() {
        getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(10)"));
    }

    public void sendKeys(WebElement e, String txt){
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    public void sendKeys(WebElement e, String txt, String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO,msg);
        e.sendKeys(txt);
    }

    public String getAttribute(WebElement e, String attribute){
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getAttribute(WebElement e, String attribute, String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO,msg);
        return e.getAttribute(attribute);
    }

    public void closeApp(){
        ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("androidAppPackage"));
    }

    public void launchApp(){
        ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("androidAppPackage"));
    }

    @AfterTest
    public void afterTest(){
        getDriver().quit();
    }

}
