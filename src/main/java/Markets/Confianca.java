
package Markets;
import helpers.CheckIntegrity;
import helpers.WaitDealer;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.atp.Switch;
import org.apache.poi.ss.formula.functions.WeekNum;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.sql.Struct;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Confianca {
    private WebDriver driver;
    private Map<Integer, List<String>> shoppingList;
    private WaitDealer waitDealer;
    private CheckIntegrity checkIntegrity;
    private String local = "";

    public Confianca(Map<Integer, List<String>> data) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        //options.addArguments("--headless=new");
        //options.addArguments("--user-agent='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36'");
        this.driver = new ChromeDriver(options);
        this.waitDealer = new WaitDealer(driver);
        this.checkIntegrity = new CheckIntegrity(data);
        shoppingList = data;
        testaDriver();
    }


    private void testaDriver() {
        try {
            driver.get("https://www.confianca.com.br/");
            System.out.println("We ara in: " + driver.getCurrentUrl());
            openLocationOptions();
            defineLocation();
            goToLogin();
            loginWaiter();
            int retry = 0;

            do{
                try{
                    buscaItens(shoppingList);
                }catch (WebDriverException e){
                    //talvez seja necessário reinvocar esse método em outras partes do código
                    checkIntegrity.rebuildShoppingList(shoppingList); //
                    e.printStackTrace();
                    System.err.println("There was an error, retrying");
                    retry = 1;

                }
            }while (retry==1);



        } catch (WebDriverException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void goToLogin() {
       WebElement btnGoToLogin = waitDealer.waitVisibleElement(10, By.xpath("//*[@id=\"header\"]/div/div[1]/div/div/div[2]/div[3]/span/button[1]"));
        waitDealer.waitClickableElement(5, btnGoToLogin);
       btnGoToLogin.click();
    }

    private boolean loginWaiter() {
        WebElement btnLogin = null;

            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(60L))
                    .pollingEvery(Duration.ofSeconds(5L))
                    .ignoring(NoSuchElementException.class);
        try{
            btnLogin = wait.until(this::loginTrigger);
        }catch (TimeoutException e){
            System.out.println("Waiting time for login button has expired");
        }


        if(btnLogin!=null){
            btnLogin.click();
        }else{
            System.err.println("error in login button search");
        }

        return false;
    }

    private WebElement loginTrigger(WebDriver driver) {
        WebElement element = driver.findElement(By.cssSelector(".btn.primary"));
        return element.isEnabled() ? element : null;
    }

    //this allows you to choose your location, but for now I will use only Bauru by default.
    private void defineLocation() {
        List<WebElement> locationButtonList = waitDealer.waitVisibleListOfElements( 10, By.className("box__button"));
        WebElement locationButton;
        switch(local.toLowerCase()){
            case "bauru":
                locationButton = locationButtonList.get(0);
                break;
            case "botucatu":
                locationButton = locationButtonList.get(1);
                break;
            case "marilia":
                locationButton = locationButtonList.get(2);
                break;
            case "jau":
                locationButton = locationButtonList.get(3);
                break;
            case "Sorocoba":
                locationButton = locationButtonList.get(4);
                break;
            default:
                locationButton = locationButtonList.get(0);
        }
        waitDealer.waitClickableElement(10, locationButton);
    }

public void cookieAccept(){
    try {
        WebElement cookieAccept = waitDealer.waitVisibleElement("fast", By.xpath("//*[@id=\"privacytools-banner-consent\"]/div/div[2]/div[1]/a"));
        waitDealer.waitClickableElement(10,cookieAccept);

    }catch (WebDriverException e){
        System.err.println("Cookies not found");
    }
}
public void openLocationOptions(){

        try {
           WebElement modalLocation = waitDealer.waitVisibleElement("10", By.cssSelector(".modal__content.close_modal"));
       }catch (WebDriverException e){
           WebElement btnLocation = waitDealer.waitVisibleElement("fast", By.xpath("//*[@id=\"header\"]/div/div[1]/div/div/div[2]/div[2]/span/button"));
           waitDealer.waitClickableElement(10, btnLocation);
       }

    }


    private void buscaItens(Map<Integer, List<String>> shoppingList) throws InterruptedException, WebDriverException {
        Iterator<Map.Entry<Integer, List<String>>> iterator = shoppingList.entrySet().iterator();

        iterator.next();




            while (iterator.hasNext() || !checkIntegrity.verifyIntegrity()) {
                int collumn = 1;
                Map.Entry<Integer, List<String>> entry = iterator.next();
                List<String> item = entry.getValue();
                String actualProduct = null;

            for (String cell: item) {
                waitDealer.driverWait(1);
                if(collumn == 1){
                System.out.println("Lógica de encontar item: "+cell);
                WebElement searchBar = waitDealer.waitVisibleElement("fast", By.className("search-header__input"));
                actualProduct = cell;
                searchBar.sendKeys(cell+"\n");




                }else{
                    System.out.println("Lógica de quantidade de item: "+cell);
                    //Click Shelf
                     WebElement productShelf = waitDealer.waitVisibleElement("normal", By.className("product-shelf__img"));
                     waitDealer.waitClickableElement(10,productShelf);


                    WebElement buyDiv = waitDealer.waitVisibleElement("normal", By.className("product-info__buy"));
                    WebElement selectDiv = buyDiv.findElement(By.className("container-component"));
                    // buy button
                    WebElement buyButton = buyDiv.findElement(By.tagName("button"));

                    //reveal select options
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));

                    WebElement elementQuantity = null;


                            elementQuantity = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                                    selectDiv, By.tagName("select"))).get(0);


                    //WebElement elementQuantity  = selectDiv.findElement(By.tagName("select"));
                    Select selectQuantity = new Select(elementQuantity);

                    List<WebElement> allOptions = selectQuantity.getOptions();

                    //debugging
                    // Iterar sobre as opções e exibir os valores
//                    for (WebElement option : allOptions) {
//                        String optionText = option.getText();  // Texto visível da opção
//                        String optionValue = option.getAttribute("value");  // Valor do atributo value
//                        System.out.println("Texto: " + optionText + " | Valor: " + optionValue);
//                    }


                    //select an option from cell value

                        if(Double.parseDouble(cell)<=0){
                            System.err.println("Quantidade menor ou igual a 0");
                            checkIntegrity.removeFromCheckList(actualProduct);

                        }else if(Double.parseDouble(cell) <= 9){
                            selectQuantity.selectByValue(String.valueOf((int) Double.parseDouble(cell)));
                            waitDealer.waitClickableElement(30,buyButton);
                            System.out.println("Compra concluída");
                            checkIntegrity.removeFromCheckList(actualProduct);

                        }else{ //if value is < 9, than a input box must appear
                            selectQuantity.selectByValue("10");
                           // WebElement inputQuantity = waitDealer.waitVisibleElement(3 , By.xpath("//*[@id=\"213020\"]/div[2]/div/div/div[2]/div[3]/div/label/input"));
                            WebElement labelImput = waitDealer.waitVisibleElement("fast",By.className("form-group__label"));
                            WebElement inputQuantity = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                                    labelImput, By.tagName("input"))).get(0);
                            if(Double.parseDouble(inputQuantity.getAttribute("max"))< Double.parseDouble(cell)){
                                System.err.println("A quantidade ultrapassa o disponível no estoque");
                                System.out.println("Máximo em estoque: "+ inputQuantity.getAttribute("max"));

                            }else{
                                inputQuantity.clear();
                               // waitDealer.driverWait(2);
                                inputQuantity.sendKeys(cell+"\n");
                                //press buy button
                                waitDealer.waitClickableElement(30,buyButton);
                                System.out.println("Compra concluída");
                                checkIntegrity.removeFromCheckList(actualProduct);
                                waitDealer.driverWait(5);
                            }


                        }





                }
                collumn++;
            }
            System.out.println(entry.getKey() + ":" + entry.getValue());

        }
        System.out.println("Fim de execução");
    }

//    private void verifyChart(int actualValue, ){
//
//    }
}


