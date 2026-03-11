package com.qa.test;

import com.qa.base.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest{
    TestUtils utils = new TestUtils();
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;
    SettingsPage settingsPage;

    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream datais = null;
        try{
            String dataFileName = "data/loginUsers.json";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(datais != null){
                datais.close();
            }
        }

        //closeApp();
        //launchApp();

    }
    @AfterClass
    public void afterClass(){

    }

    @BeforeMethod
    public void beforeMethod(Method m){
        loginPage = new LoginPage();
        utils.log().info("\n" + "****** starting test:" + m.getName() + " *******" + "\n");
        launchApp();
    }

    @AfterMethod
    public void afterMethod(){
        closeApp();
    }

    @Test
    public void invalidUserName(){
            loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
            loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
            loginPage.pressLoginBtn();


            String actualErrorTxt = loginPage.getErrorTxt();
            utils.log().info("Validando assert:");
            String expectedErrorTxt = getStrings().get("err_invalid_username_or_password");
            utils.log().info("Actual error Text: " + actualErrorTxt + "\n" + "Expected error Text" + expectedErrorTxt);

            Assert.assertEquals(actualErrorTxt,expectedErrorTxt);
    }

    @Test
    public void invalidPassword(){

            loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
            loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
            loginPage.pressLoginBtn();

            String actualErrorTxt = loginPage.getErrorTxt();
            String expectedErrorTxt = getStrings().get("err_invalid_username_or_password");
            utils.log().info("Actual error Text: " + actualErrorTxt + "\n" + "Expected error Text" + expectedErrorTxt);

            Assert.assertEquals(actualErrorTxt,expectedErrorTxt);
    }

    @Test
    public void successfulLogin() throws InterruptedException {
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginBtn();

        String actualProductTitle = productsPage.getTitle();

        String expectedProductTitle = getStrings().get("product_title");
        utils.log().info("Actual product title: " + actualProductTitle + "\n" + "Expected product title" + expectedProductTitle);

        Thread.sleep(1000);
        settingsPage = productsPage.pressSettingbtn();
        loginPage = settingsPage.pressLogoutBtn();

        Assert.assertEquals(actualProductTitle,expectedProductTitle);

    }




}
