package com.qa.listeners;

import com.qa.base.BaseTest;
import com.qa.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.util.Strings;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {
    TestUtils utils = new TestUtils();

    public void onTestFailure(ITestResult result){
        if(result.getThrowable() != null){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            utils.log(sw.toString());
        }

        BaseTest base = new BaseTest();
        File file = base.getDriver().getScreenshotAs(OutputType.FILE);

        Map<String, String> params = new HashMap<String , String>();
        params = result.getTestContext().getCurrentXmlTest().getAllParameters();

        //Se crea la estructura del folder donde almacenaremos las evidencias, usando los parametros globales
        String imagePath = "Screenshot" + File.separator + params.get("platformName") + "_" + params.get("platformVersion")
                + "_" + params.get("deviceName") + File.separator + base.getDateTime() + File.separator +
                result.getTestClass().getRealClass().getSimpleName() + File.separator + result.getName() + ".png";

        //Este variable almacena la ruta completa del ScreenShot que necesito mostrar en el html
        String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;
        try{
            FileUtils.copyFile(file, new File(imagePath));
            //Estas instrucciones me permiten colocar el screenShot que necesito en el html
            Reporter.log("This is the sample ScreenShot");
            Reporter.log("<a href='"+ completeImagePath + "'> <img src = '" + completeImagePath + "' height = '100' width= '100'/> </a>");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
