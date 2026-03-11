package com.qa.test;
import com.qa.base.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsDetailPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class ProductsTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage, settingsPage2;
    ProductsDetailPage productDetailPage;
    InputStream datais;
    JSONObject loginUsers;

    TestUtils utils = new TestUtils();


    @BeforeClass
    public void beforeClass() throws IOException {
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



    }
    @AfterClass
    public void afterClass(){

    }

    @BeforeMethod
    public void beforeMethod(Method m){
        launchApp();
        loginPage = new LoginPage();
        utils.log("\n" + "****** starting test:" + m.getName() + " *******" + "\n");

        productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
                loginUsers.getJSONObject("validUser").getString("password"));
    }

    @AfterMethod
    public void afterMethod(){
        closeApp();

    }

    @Test
    public void validateProductOnProductsPage() throws InterruptedException {
        SoftAssert sa = new SoftAssert();

        //Se valida el title actual con el title esperado
        String SLBTitle = productsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, getStrings().get("products_page_slb_title"));

        //Se valida el precio actual con el precio esperado
        String SLBPriceTxt = productsPage.getSLBPrice();
        sa.assertEquals(SLBPriceTxt, getStrings().get("products_page_slb_price"));

        //Thread.sleep(1000);
        //settingsPage = productsPage.pressSettingbtn();
        //loginPage = settingsPage.pressLogoutBtn();

        sa.assertAll();
    }

    @Test
    public void validateProductOnProductsDetailPage() throws InterruptedException {
        SoftAssert sa = new SoftAssert();

        //Se entra a la descripcion del producto
        productDetailPage = productsPage.pressSLBTitle();

        //Se valida el title actual con el title esperado
        String SLBTitle = productDetailPage.getSLBTitle();
        sa.assertEquals(SLBTitle, getStrings().get("product_detail_page_slb_title"));

        //Se valida la descripcion del producto
        String SLBDetail = productDetailPage.getSLBDetail();
        sa.assertEquals(SLBDetail, getStrings().get("product_detail_page_slb_detail"));

        //Se realiza scroll
        productDetailPage.scrollToAddToCart();

        String btnAddToCart = productDetailPage.getAddToCartTxt();

        //Se valida el precio actual del producto
        String SLBPrice = productDetailPage.getSLBPrice();
        sa.assertEquals(SLBPrice, getStrings().get("product_detail_page_slb_price"));


        //Se regresa a la pantalla de productos
        productsPage = productDetailPage.pressBackToProductsBtn();

        //Se desplega el menu de Settings
        //Thread.sleep(1000);
        //settingsPage= productsPage.pressSettingbtn();
        //loginPage = settingsPage.pressLogoutBtn();

        sa.assertAll();
    }


}
