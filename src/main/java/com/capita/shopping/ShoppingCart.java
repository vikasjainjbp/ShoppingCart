package com.capita.shopping;

import com.capita.shopping.model.PriceCatalog;
import com.capita.shopping.model.Promotion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShoppingCart {

    //This map is used to store shopping cart by user. In real example, this could be a cache which will be then persisted in database.
    private Map<String, Map<String, Integer>> shoppingList = new ConcurrentHashMap<>();

    //This map is hard coded catalog with price and promotions. In real example this should be coming from database and can be updated regularly with items, price and promotions.
    private static Map<String, PriceCatalog> catalog= new ConcurrentHashMap() {{
        put("Apple", new PriceCatalog("Apple", 35));
        put("Banana", new PriceCatalog("Banana", 20));
        put("Melon", new PriceCatalog("Melon", 50, Promotion.BOGO));
        put("Lime", new PriceCatalog("Lime", 15, Promotion.THREEFORTWO));
    }};

    //This method will calculate the total price of shopping cart
    public int calculateTotal(String userId) {
        Map<String, Integer> listOfItemsWithQuantity = shoppingList.get(userId);
        if(listOfItemsWithQuantity == null || listOfItemsWithQuantity.isEmpty()) {
            throw new RuntimeException(String.format("User [%s] did not buy any items", userId));
        }
        int finalPrice = listOfItemsWithQuantity.keySet().stream()
                .map(k -> calculateItemPrice(k, listOfItemsWithQuantity.get(k)))
                .collect(Collectors.summingInt(Integer::intValue));
        return finalPrice;
    }

    // This method is used to calculate the price of each item in the Shopping Cart based on the quantity and promotions if applicable.
    private int calculateItemPrice(String itemId, int quantity) {
        int price = 0;
        int itemPrice = catalog.get(itemId).getPrice();
        if(Promotion.BOGO.equals(catalog.get(itemId).getPromotion()) && quantity > 1) {
            if((quantity % 2) == 0)
                price = (quantity / 2) * itemPrice;
            else
                price = (quantity / 2) * itemPrice + itemPrice;
        } else if (Promotion.THREEFORTWO.equals(catalog.get(itemId).getPromotion()) && quantity > 2) {
            if((quantity % 3) == 0)
                price = (quantity / 3) * 2 * itemPrice;
            else
                price = (quantity / 3) * 2 * itemPrice
                        + (quantity % 3) * itemPrice;
        } else
            price = quantity * itemPrice;
        return price;
    }

    //This method is to scan and add items in to the shopping cart.
    public void scanItem(String userId, String itemId) {
        if(catalog.containsKey(itemId)) {
            shoppingList.computeIfAbsent(userId, v -> new ConcurrentHashMap<>())
                    .computeIfAbsent(itemId, v -> 0);
            shoppingList.get(userId).put(itemId, shoppingList.get(userId).get(itemId) + 1);
        } else
            throw new RuntimeException((String.format("Item [%s] not exist in catalog", itemId)));
    }

    //This method is to mark transaction finished and print receipt
    public synchronized void  printAndFinishTransaction(String userId) {
        System.out.println("**********************");
        System.out.println("Shopping Receipt");
        System.out.println("**********************");
        System.out.println("Item    Qty    Price");
        shoppingList.get(userId).forEach((k,v) -> System.out.println(k + "    " + v + "    " + catalog.get(k).getPrice() + "p each"));
        System.out.println("**********************");
        System.out.println("Total : " + calculateTotal(userId) / 100 + "." + calculateTotal(userId) % 100 + "GBP");

        //Completing transaction and removing from map. If it is DB then we can mark it as finished, but here for in memory just removing it.
        shoppingList.remove(userId);
    }

}
