package com.qa.pages;

import com.qa.base.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SettingsPage extends BaseTest {
    TestUtils utils = new TestUtils();

    @AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-LOGOUT\"]") private WebElement logoutBtn;

    public SettingsPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    public LoginPage pressLogoutBtn(){
        utils.log("Press Settings Btn");
        click(logoutBtn);
        return new LoginPage();
        /*Se retorna settingsPage ya que al presionar el btn de settings la siguiente pantalla
            que se muestra en el dispositivo es la pagina de Settings y ya no la de MenuPage
            Esto nos permitira que al llamar pressSettingsBtn(), podamos concatener otros metodos                 pero de la clase settingsPage
        */
    }
}
