package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

//A classe a seguir serve para fornecer números inteiros em intervalo de 1 a 30 segundos
public class WaitDealer {
    WebDriver driver;

    public WaitDealer(WebDriver driver){
        this.driver = driver;
    }

    private int randomSecond() {
        int max = 20;
        int min = 5;
        int sec = (int) ((Math.random() * (max - min)) + min);
        System.out.println("Waiting: " + sec);
        return sec;
    }

    //I think this makes things more standardized
    private int keyWordSecond(String keyword) {
        int sec;
        WebElement element = null;
        switch (keyword) {
            case "fast":
                sec = 10;
                break;
            case "normal":
                sec = 30;
                break;
            case "long":
                sec = 60;
                break;
            default:
                sec = 1;
        }

        return sec;
    }

    //Since Thread.sleep is unstable, this is the best solution I have found at the moment to delay browser
    //activity and act more "human like" in terms of time
    public void driverWait(int seconds) throws InterruptedException {
        boolean lock = false;
        WebDriverWait tempWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            tempWait.until(lockCondition -> lock); // condition you are certain won't be true
        } catch (TimeoutException e) {
            System.out.println("Waiting for: " + seconds);
            return; // catch the exception and continue the code
        }
    }

    public WebElement waitVisibleElement(String waitType, By elementIdentifier) {
        WebElement element = null;
        try {
            element = (WebElement) (new WebDriverWait
                    (driver, Duration.ofSeconds((long) keyWordSecond(waitType))))
                    .until(ExpectedConditions.visibilityOfElementLocated(elementIdentifier));

        } catch (WebDriverException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return element;

    }

    public WebElement waitVisibleElement( int seconds, By elementIdentifier) {

        WebElement element = null;
        try {
            element = (WebElement) (new WebDriverWait
                    (driver, Duration.ofSeconds((long) seconds)))
                    .until(ExpectedConditions.visibilityOfElementLocated(elementIdentifier));

        } catch (WebDriverException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();

        }

        return element;
    }

    //In very especific cases, you may need a list of elements that owns a same class name, this method cover this
    public List<WebElement> waitVisibleListOfElements(int seconds, By elementIdentifier) {
        List<WebElement> listOfWebElements = null;
        try {
            listOfWebElements = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementIdentifier));

        } catch (WebDriverException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return listOfWebElements;
    }

    public boolean waitClickableElement(int seconds, WebElement element){
        try{
            new WebDriverWait(driver, Duration.ofSeconds((long) seconds)).until(ExpectedConditions.elementToBeClickable(element));
        }catch(ElementClickInterceptedException e){
            System.err.println("Not a clickable Element, or intercepted click");
            return false;
        }catch (WebDriverException e){
            System.err.println(e.getMessage());
        }
        element.click();
        return true;

    }
}

