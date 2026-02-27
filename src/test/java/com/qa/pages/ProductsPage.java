package com.qa.pages;

import com.qa.base.BaseTest;
import com.qa.base.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends MenuPage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy (xpath = "//android.widget.TextView[@text=\"PRODUCTS\"]") private WebElement productsTitleTxt;
    @AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]") private WebElement backpackTitleTxt;
    @AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"test-Price\" and @text=\"$29.99\"]") private WebElement backpackPrice;
    @AndroidFindBy (xpath = "(//android.view.ViewGroup[@content-desc=\"test-ADD TO CART\"])[1]") private WebElement addToCartBtn;

    public ProductsPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    public String getTitle(){
        String title = getAttribute(productsTitleTxt, "text");
        utils.log("Product page title is: " + title);
        return getAttribute(productsTitleTxt, "text");
    }

    public String getSLBTitle(){
        String title = getAttribute(backpackTitleTxt, "text");
        utils.log("Backpack title is: " + title);
        return getAttribute(backpackTitleTxt, "text");
    }

    public String getSLBPrice(){
        String title = getAttribute(backpackPrice, "text");
        utils.log("Backpack price is: " + title);
        return getAttribute(backpackPrice, "text");
    }

    public ProductsDetailPage pressSLBTitle(){
        click(backpackTitleTxt);
        return new ProductsDetailPage();
    }

    public ProductsPage addBackpackToCart(){
        utils.log("Backpack is added to cart");
        click(addToCartBtn);
        return this;
    }
}
