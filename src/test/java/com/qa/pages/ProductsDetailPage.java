package com.qa.pages;

import com.aventstack.extentreports.Status;
import com.qa.base.MenuPage;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;



public class ProductsDetailPage extends MenuPage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]") private WebElement backpackTitleTxt;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\"]") private WebElement backpackDetailTxt;
    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"BACK TO PRODUCTS\"]") private WebElement backToProductsBtn;
    @AndroidFindBy (accessibility = "test-Price") private WebElement SLBPrice;
    @AndroidFindBy (accessibility =  "test-ADD TO CART") private WebElement btnAddToCart;
    @AndroidFindBy (accessibility = "test-Inventory item page") private WebElement SLBDetailMainPage;

    public ProductsDetailPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    public String getSLBTitle(){
        String title = getAttribute(backpackTitleTxt, "text");
        return getAttribute(backpackTitleTxt, "text" ,"The title is: " + title);
    }

    public String getSLBDetail(){
        String detail = getAttribute(backpackDetailTxt, "text");
        return getAttribute(backpackDetailTxt, "text", "The detail is: " + detail);
    }

    public String getSLBPrice(){
        String price = getAttribute(SLBPrice, "text");
        return getAttribute(SLBPrice, "text", "The price is: " + price);
    }

    public String getAddToCartTxt(){
        String btnText = getAttribute(btnAddToCart, "text");
        ExtentReport.getTest().log(Status.INFO, "The btn is: " + btnText);
        utils.log().info("The btn is: " + btnText);
        return btnText;
    }

    public ProductsDetailPage scrollToAddToCart(){
        ExtentReport.getTest().log(Status.INFO, "Scroll to add to cart btn");
        utils.log().info("Scroll to add to cart btn");
        scrollToElementTest("productDetailPage", "test-ADD TO CART");
        return this;
    }

    public ProductsPage pressBackToProductsBtn(){
        click(backToProductsBtn, "Navigate back to products page");
        return new ProductsPage();
    }
}
