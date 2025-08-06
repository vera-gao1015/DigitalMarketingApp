/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.javafaker.Faker;
import com.github.tomaslanger.chalk.Chalk;

import model.CustomerManagement.CustomerProfile;
import model.MarketModel.Market;
import model.MarketModel.MarketChannelAssignment;
import model.OrderManagement.OrderItem;

/**
 *
 * @author kal bugrara
 */
public class SolutionOffer {
    String name;
    HashMap<Product, Float> products;
    int targetPrice;// floor, ceiling, and target ideas
    ArrayList<OrderItem> orderItems;
    MarketChannelAssignment marketChannelComb;

    

    public SolutionOffer(MarketChannelAssignment mca, int price, Product p) {
        
        marketChannelComb = mca;
        mca.addSolutionOffer(this);
        
        targetPrice = price;

        products = new HashMap<Product, Float>();
        products.put(p, 1.0f);
        p.addBundle(this);

        this.name = "Default Bundle";

        orderItems = new ArrayList<OrderItem>();
    }

    
    public Set<Product> getProducts() {
        return products.keySet();
    }

    public void updateBundleName(){
        Faker faker = new Faker();

        if (products.size() == 1){
            name = products.keySet().iterator().next().getName();
        }else{
            name = faker.commerce().color() + " Bundle";
        }
    }

    public void addProduct(Product p, float priceFraction) {
        products.put(p, priceFraction);
        p.addBundle(this);
        normalize();
    }

    public void normalize(){     
        float total = 0f;
        for (float share: products.values()){
            total += share;
        }
        for (Product p: products.keySet()){
            float currentShare = products.get(p);
            products.replace(p, currentShare / total);
        }
    }

    public Market getMarket(){
        return marketChannelComb.getMarket();
    }



    public int getSalesQuantity(){
        int total = 0;
        for (OrderItem orderitem : orderItems){
            total += orderitem.getQuantity();
        }
        return total;
    }

    public void setTargetPrice(int p) {
        targetPrice = p;
    }

    public void addOrderItem(OrderItem oi) {
        orderItems.add(oi);
    }

    public int getBeforePrice() {
        int totalTargetPrice = 0;
        for (Product product : products.keySet()) {
            totalTargetPrice += product.getTargetPrice();
        }
        return totalTargetPrice;
    }
    

    public int getTargetPrice() {
        return targetPrice;
    }

    public String getBundleName() {
        if (products.size() == 0) {
            return "No products in the bundle";
        }
        return name;
    }

    public int getSalesVolume() {
        int total = 0;
        for (OrderItem oi : orderItems) {
            total += oi.getOrderItemTotal();
        }
        return total;
    }

    public int getSalesShare(Product p) {
        Float result = products.get(p) == null ? 0 : products.get(p) * getSalesVolume();
        
        return result.intValue();
    }


    public int getAdsBudgetShare(Product p) {
        Float result = products.get(p) == null ? 0 : products.get(p) * marketChannelComb.getAdvertisingBudgetShare(this);

        // System.out.println("prodname: " + p.getName() + "; prodfrac: " + products.get(p) + "; budgetshare: " + marketChannelComb.getAdvertisingBudgetShare(this));
        // System.out.println("***********************");
        
        return result.intValue();
    }

    public void printShortInfo(){
        System.out.println("*******************************************************************");
        System.out.println( Chalk.on(name).bold() + " - Target Price $" +  targetPrice + " in " + Chalk.on(marketChannelComb.getName()).blue()+ "\n");
        System.out.println(products.size() + " Product(s)");
        for (Map.Entry<Product, Float> entry : products.entrySet()){
            Product p = entry.getKey();
            float fraction = entry.getValue();
            p.printShortInfo();
            System.out.println("CurrentPrice: $" + String.format("%.0f",targetPrice * fraction) +"; Fraction Share: " + String.format("%.2f",fraction) + "\n");
        }
    }

}
