package com.qa.test;
import com.qa.base.BaseTest;
import com.qa.pages.*;
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

public class CartTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
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
    public void validateBuyingAProduct() throws InterruptedException {
        SoftAssert sa = new SoftAssert();

        //Se da click al producto para agregar al carrito:
        productsPage.addBackpackToCart();

        //Se da click al boton del carrito y se envia al flujo de carrito
        cartPage = productsPage.pressCartBtn();

        //Se da click al boton de checkout para iniciar el proceso de compra
        cartPage.pressCheckoutBtn();

        //Se coloca info de usuario en el formulario
        cartPage.setFirstName(loginUsers.getJSONObject("UserData").getString("firstName"));
        cartPage.setLastName(loginUsers.getJSONObject("UserData").getString("lastName"));
        cartPage.setPostalCode(loginUsers.getJSONObject("UserData").getString("postalCode"));

        //Se hace scroll al btn y se presiona
        cartPage.pressContinueBtn();

        //Se presiona boton de finish para finalizar la compra
        cartPage.scrollToFinish().pressFinishBtn();

        //Se valida si el mensaje final de compra es el esperado
        String finalMsg = cartPage.getFinalMsg();
        sa.assertEquals(finalMsg, getStrings().get("cart_page_final_msg"));

        //esto sirve para validar todos los assert y que no se detenga el flujo al encontrar un error,
        // ademas de mostrar todos los errores encontrados al finalizar el test
        sa.assertAll();
    }
}
