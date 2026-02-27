package com.qa.pages;

import com.qa.base.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


public class LoginPage extends BaseTest {
    TestUtils utils = new TestUtils();

    public LoginPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    @AndroidFindBy (accessibility = "test-Username") private WebElement usernameTxtFld;
    @AndroidFindBy (accessibility = "test-Password") private WebElement passwordTxtFld;
    @AndroidFindBy (accessibility = "test-LOGIN") private WebElement loginBtn;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]") private WebElement errorTxt;
    //WebElement usernameTxtFld = driver.findElement(AppiumBy.accessibilityId("test-Username"));
    //WebElement passwordTxtFld = driver.findElement(AppiumBy.accessibilityId("test-Password"));
    //WebElement loginBtn = driver.findElement(AppiumBy.accessibilityId("test-LOGIN"));
    //WebElement errorTxt = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]"));

    public LoginPage enterUserName(String username){
        utils.log("The username is: " + username);
        sendKeys(usernameTxtFld, username);
        return this;
    }

    public LoginPage enterPassword(String password){
        utils.log("The password is: " + password);
        sendKeys(passwordTxtFld, password);
        return this;
    }

    public ProductsPage pressLoginBtn(){
        utils.log("Press login btn");
        click(loginBtn);
        return new ProductsPage();
    }

    //Metodo para hacer login
    public ProductsPage login(String username, String password){
        enterUserName(username); //Usamos el metod enterUserName definido en esta misma clase
        enterPassword(password); //Usamos el metodo enterPassword definido en esta misma clase
        return pressLoginBtn(); //Retornamos el metodo pressLoginBtn ya que este metodo nos regresa un ProductsPage
    }

    public String getErrorTxt(){
        utils.log("The error text is:" + getAttribute(errorTxt, "text"));
       return getAttribute(errorTxt, "text");
    }

}
