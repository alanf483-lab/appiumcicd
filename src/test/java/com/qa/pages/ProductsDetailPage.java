package com.qa.pages;

import com.qa.base.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLOutput;

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
        utils.log().info("The title is: " + title);
        return getAttribute(backpackTitleTxt, "text");
    }

    public String getSLBDetail(){
        String detail = getAttribute(backpackDetailTxt, "text");
        utils.log().info("The detail is: " + detail);
        return getAttribute(backpackDetailTxt, "text");
    }

    public String getSLBPrice(){
        String price = getAttribute(SLBPrice, "text");
        utils.log().info("The price is: " + price);
        return getAttribute(SLBPrice, "text");
    }

    public String getAddToCartTxt(){
        String btnText = getAttribute(btnAddToCart, "text");
        utils.log().info("The btn is: " + btnText);
        return btnText;
    }

    public ProductsDetailPage scrollToAddToCart(){
        utils.log().info("Se realiza Scroll");
        scrollToElementTest("productDetailPage", "test-ADD TO CART");
        return this;
    }

    public ProductsPage pressBackToProductsBtn(){
        utils.log().info("Navigate back to products page");
        click(backToProductsBtn);
        return new ProductsPage();
    }
}
