package com.qa.base;

import com.qa.pages.CartPage;
import com.qa.pages.SettingsPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class MenuPage extends BaseTest{

    @AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView") private WebElement settingsBtn;
    @AndroidFindBy (xpath="//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.ImageView") private WebElement cartBtn;

    public SettingsPage pressSettingbtn(){
        utils.log().info("Pressing settings button");
        click(settingsBtn);
        return new SettingsPage();
        /*Se retorna settingsPage ya que al presionar el btn de settings la siguiente pantalla
            que se muestra en el dispositivo es la pagina de Settings y ya no la de MenuPage
            Esto nos permitira que al llamar pressSettingsBtn(), podamos concatener otros metodos                 pero de la clase settingsPage
        */
    }

    public CartPage pressCartBtn(){
        utils.log().info("Cart button is clicked");
        click(cartBtn);
        return new CartPage();
    }
}
