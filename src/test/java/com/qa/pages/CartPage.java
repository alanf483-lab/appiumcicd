package com.qa.pages;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
//import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
//import io.appium.java_client.pagefactory.AndroidFindBys;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
//import org.openqa.selenium.WebDriver;
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
        click(checkoutBtn, "Checkout button is clicked");
        return this;
    }

    public CartPage setFirstName(String firstName){
        sendKeys(firstNameBox, firstName, "First name is set to: " + firstName);
        return this;
    }

    public CartPage setLastName(String lastName){
        sendKeys(lastNameBox, lastName, "Last name is set to: " + lastName);
        return this;
    }

    public CartPage setPostalCode(String postalCode){
        sendKeys(postalCodeBox, postalCode, "postal code set to: " + postalCode);
        return this;
    }

    public CartPage pressContinueBtn(){
        click(continueBtn, "Continue button is clicked");
        return this;
    }

    public CartPage pressFinishBtn(){
        click(finishBtn, "Finish button is clicked");
        return this;
    }

    public String getFinalMsg(){
        String title = getAttribute(successMsg, "text");
        return getAttribute(successMsg, "text", "The final msg is: " + title);

    }

    public CartPage scrollToFinish(){
        utils.log().info("Scroll to finish btn");
        ExtentReport.getTest().log(Status.INFO, "Scroll to Finish btn");
        scrollToElementTest("checkoutOverviewPage", "test-FINISH");
        return this;
    }
}
