package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.base.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
//import org.testng.util.Strings;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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

        byte[] encoded = null;
        try{
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        }catch(IOException e1){
            e1.printStackTrace();
        }

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

        try{
            ExtentReport.getTest().fail("Test failed", MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());
            //De esta manera convertimos el archivo byte -> String
            ExtentReport.getTest().fail("Test failed",	MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
        }catch(Exception e){
            e.printStackTrace();
        }
//Esta linea nos permite agregar la excepcion al reporte
        ExtentReport.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestStart(ITestResult result){
        BaseTest base = new BaseTest();
        //getName() -> Me trae el nombre del nombre del metodo de TestNG
        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
                .assignCategory(base.getPlatform() + "_" + base.getDeviceName())
                .assignAuthor("Alan Flores");
    }

    @Override
    public void onTestSuccess(ITestResult result){
        ExtentReport.getTest().log(Status.PASS, "Test Passed");
        ExtentReport.getReporter().flush();
    }

    @Override
    public void onTestSkipped(ITestResult result){
        ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
        ExtentReport.getReporter().flush();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result){

    }

    @Override
    public void onStart(ITestContext context){

    }

    //Este metodo se ejecuta despues de que todos mis Test methods se han ejecutado
    @Override
    public void onFinish(ITestContext context){
        ExtentReport.getReporter().flush(); //Escribe toda la informacion al reporte
    }














}
