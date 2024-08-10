package main;

import Markets.Confianca;
import models.ExcelList;
import org.openqa.selenium.WebDriverException;

import java.io.IOException;

public class RunBuyForMe {

    public static void main (String[] args){
            try {
            new Confianca(new ExcelList().retornaValores());
        } catch (IOException e) {
            e.printStackTrace();
        }catch (WebDriverException e){
           System.err.println("Error during search");
        }
    }
}
