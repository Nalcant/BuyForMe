package models;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelList {
    Map<Integer, List<String>> dados = new HashMap<Integer, List<String>>();
    public  Map<Integer, List<String>> retornaValores() throws IOException {

        FileInputStream file = new FileInputStream(new File("Y:\\ideaProjects\\BuyForMe\\data\\data_BuyForMe.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        int i = 0;

        //Pegar os dados de cada linha
        for (Row row : sheet) {
            dados.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        dados.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        dados.get(i).add(cell.getNumericCellValue() + "");
                        break;
                    default:
                        dados.get(i).add(" ");
                }
            }
            i++;
        }
        testaValores();
        return dados;
    }
  public void testaValores(){
        Iterator<Map.Entry<Integer, List<String>>> iterator = dados.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}


