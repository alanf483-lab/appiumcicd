package com.qa.pages;

import com.qa.base.BaseTest;
//import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


public class LoginPage extends BaseTest {

    public LoginPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    @AndroidFindBy (accessibility = "test-Username") private WebElement usernameTxtFld;
    @AndroidFindBy (accessibility = "test-Password") private WebElement passwordTxtFld;
    @AndroidFindBy (accessibility = "test-LOGIN") private WebElement loginBtn;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]") private WebElement errorTxt;

    public LoginPage enterUserName(String username){
        /*
        Este metodo permite ingresar el username que se agrega por parametro
         */
        sendKeys(usernameTxtFld, username, "The username is: " + username);
        return this;
    }

    public LoginPage enterPassword(String password){
        sendKeys(passwordTxtFld, password, "The password is: " + password);
        return this;
    }

    public ProductsPage pressLoginBtn(){
        click(loginBtn, "Press login btn");
        return new ProductsPage();
    }

    //Metodo para hacer login
    public ProductsPage login(String username, String password){
        enterUserName(username); //Usamos el metod enterUserName definido en esta misma clase
        enterPassword(password); //Usamos el metodo enterPassword definido en esta misma clase
        return pressLoginBtn(); //Retornamos el metodo pressLoginBtn ya que este metodo nos regresa un ProductsPage
    }

    public String getErrorTxt(){
       return getAttribute(errorTxt, "text", "The error text is:" + getAttribute(errorTxt, "text"));
    }

}
