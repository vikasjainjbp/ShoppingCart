package com.capita.shopping;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ShoppingCartTest {

    @Test
    public void testScanItemsAndCalculatePriceFor_OneUser_With_Single_Quantity() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.scanItem("user1", "Apple");
        shoppingCart.scanItem("user1", "Banana");
        shoppingCart.scanItem("user1", "Melon");
        shoppingCart.scanItem("user1", "Lime");
        Assert.assertEquals(120, shoppingCart.calculateTotal("user1"));
        shoppingCart.printAndFinishTransaction("user1");
    }

    @Test
    public void testScanItemsAndCalculatePriceFor_OneUser_With_Multiple_Quantity() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.scanItem("user1", "Apple");
        shoppingCart.scanItem("user1", "Apple");
        shoppingCart.scanItem("user1", "Banana");
        shoppingCart.scanItem("user1", "Banana");
        shoppingCart.scanItem("user1", "Melon");
        shoppingCart.scanItem("user1", "Lime");
        shoppingCart.scanItem("user1", "Lime");
        Assert.assertEquals(190, shoppingCart.calculateTotal("user1"));
        shoppingCart.printAndFinishTransaction("user1");
    }

    @Test
    public void testScanItemsAndCalculatePriceFor_OneUser_With_Multiple_PromotionApplicable() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.scanItem("user1", "Apple");
        shoppingCart.scanItem("user1", "Apple");
        shoppingCart.scanItem("user1", "Banana");
        shoppingCart.scanItem("user1", "Banana");
        shoppingCart.scanItem("user1", "Melon");
        shoppingCart.scanItem("user1", "Melon");
        shoppingCart.scanItem("user1", "Melon");
        shoppingCart.scanItem("user1", "Lime");
        shoppingCart.scanItem("user1", "Lime");
        shoppingCart.scanItem("user1", "Lime");
        shoppingCart.scanItem("user1", "Lime");
        shoppingCart.scanItem("user1", "Lime");
        Assert.assertEquals(270, shoppingCart.calculateTotal("user1"));
        shoppingCart.printAndFinishTransaction("user1");
    }

    @Test
    public void testThrowExceptionWhileCalculatingTotal_When_User_ShoppingCart_Is_Empty() {
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            shoppingCart.calculateTotal("user1");
            Assert.fail("This test case should throw an exception");
        } catch (Exception ex) {
            Assert.assertEquals("User [user1] did not buy any items", ex.getMessage());
        }
    }

    @Test
    public void testThrowException_When_Scanned_Item_Not_In_Catalog() {
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            shoppingCart.scanItem("user1", "item");
            Assert.fail("This test case should throw an exception");
        } catch (Exception ex) {
            Assert.assertEquals("Item [item] not exist in catalog", ex.getMessage());
        }
    }

    @Test
    public void testMultipleUserConcurrentlyScanning() throws InterruptedException {
        List<String> user1Cart = Arrays.asList("Apple", "Banana", "Apple", "Melon", "Banana", "Lime", "Lime", "Lime", "Melon", "Melon");
        List<String> user2Cart = Arrays.asList("Apple", "Banana", "Banana", "Lime", "Lime", "Lime", "Melon", "Melon");
        ShoppingCart shoppingCart = new ShoppingCart();
        Thread t1 = new Thread(() -> {
            user1Cart.forEach(i -> {
                shoppingCart.scanItem("user1", i);
            });
            Assert.assertEquals(240, shoppingCart.calculateTotal("user1"));
            shoppingCart.printAndFinishTransaction("user1");
        }) ;

        Thread t2 = new Thread(() -> {
            user2Cart.forEach(i -> {
                shoppingCart.scanItem("user2", i);
            });
            Assert.assertEquals(155, shoppingCart.calculateTotal("user2"));
            shoppingCart.printAndFinishTransaction("user2");
        }) ;
        t1.start();
        t2.start();
        Thread.sleep(2 * 1000);
    }
}
