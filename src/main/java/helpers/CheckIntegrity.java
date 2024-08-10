package helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class CheckIntegrity {
    private List<String> checkList;

    public CheckIntegrity(Map shoppingList) {
        checkList = new ArrayList<>();
        buildCheckList(shoppingList);
    }


    public void buildCheckList(Map shoppingList) {

        Iterator<Map.Entry<Integer, List<String>>> iterator = shoppingList.entrySet().iterator();
        iterator.next();

        while (iterator.hasNext()) {
            int collumn = 1;
            Map.Entry<Integer, List<String>> entry = iterator.next();
            List<String> item = entry.getValue();
            for (String cell : item) {

                if (collumn == 1) {
                    checkList.add(cell);
                }
                collumn++;

            }

        }
        System.out.println("CheckListBuilt: "+ checkList);


    }

    public void rebuildShoppingList(Map<Integer, List<String>> shoppingList) {
        System.out.println("Checklist during Rebuild: "+checkList);
        Iterator<Map.Entry<Integer, List<String>>> iterator = shoppingList.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            List<String> itens = entry.getValue();

            if (itens.isEmpty() || !checkList.contains(itens.get(0))) {
                iterator.remove();
            }
        }

        System.out.println("ShoppingList after rebuild: "+shoppingList);

    }

    public boolean verifyIntegrity(){
        if(checkList.size() == 0){
            System.out.println("There aren't items in checkList");
            return true;
        }else{
           System.out.println("There is items in checklist");
            return false;
        }
    }

    public void removeFromCheckList(String item) {
        if(!verifyIntegrity()){
           System.out.println("Removed: "+ item);
           System.out.println("Checklist after remove: "+checkList);
           checkList.remove(item);
        }else{
            System.err.println("There is no item to be removed");
        }

    }
}
