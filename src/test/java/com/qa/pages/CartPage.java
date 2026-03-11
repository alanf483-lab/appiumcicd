package com.qa.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CartPage extends ProductsPage {

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-CHECKOUT\"]") private WebElement checkoutBtn;
    @AndroidFindBy (xpath = "//android.widget.EditText[@content-desc=\"test-First Name\"]") private WebElement firstNameBox;
    @AndroidFindBy (xpath = "//android.widget.EditText[@content-desc=\"test-Last Name\"]") private WebElement lastNameBox;
    @AndroidFindBy (xpath = "//android.widget.EditText[@content-desc=\"test-Zip/Postal Code\"]") private WebElement postalCodeBox;
    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-CONTINUE\"]") private WebElement continueBtn;
    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-FINISH\"]") private WebElement finishBtn;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"FINISH\"]") private WebElement finishBtn2;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"THANK YOU FOR YOU ORDER\"]") private WebElement successMsg;

    public CartPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    public CartPage pressCheckoutBtn(){
        utils.log().info("Checkout button is clicked");
        click(checkoutBtn);
        return this;
    }

    public CartPage setFirstName(String firstName){
        utils.log().info("First name is set to: " + firstName);
        sendKeys(firstNameBox, firstName);
        return this;
    }

    public CartPage setLastName(String lastName){
        utils.log().info("Last name is set to: " + lastName);
        sendKeys(lastNameBox, lastName);
        return this;
    }

    public CartPage setPostalCode(String postalCode){
        utils.log().info("postal code set to: " + postalCode);
        sendKeys(postalCodeBox, postalCode);
        return this;
    }

    public CartPage pressContinueBtn(){
        utils.log().info("Continue button is clicked");
        click(continueBtn);
        return this;
    }

    public CartPage pressFinishBtn(){
        utils.log().info("Finish button is clicked");
        click(finishBtn);
        return this;
    }

    public String getFinalMsg(){
        String title = getAttribute(successMsg, "text");
        utils.log().info("The final msg is: " + title);
        return getAttribute(successMsg, "text");
    }

    public CartPage scrollToFinish(){
        utils.log().info("Se realiza scroll");
        scrollToElementTest("checkoutOverviewPage", "test-FINISH");
        return this;
    }


}
