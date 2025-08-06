/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.HashMap;
import com.github.tomaslanger.chalk.Chalk;

import model.MarketModel.Market;
import model.OrderManagement.OrderItem;

/**
 *
 * @author kal bugrara
 */
public class Product {
    private String name;
    private int floorPrice;
    private int ceilingPrice;
    private int targetPrice;
    ArrayList<OrderItem> orderItems;  
    ArrayList<SolutionOffer> bundles; // all bundles that contain this product
    
    public void printShortInfo() {
        String info = Chalk.on("Product:").red()+ " " + Chalk.on(name).red() + "; ";
        info += "OriginTargetPrice: $" + targetPrice + "; ";
        System.out.println(info);
    }

    public Product(int fp, int cp, int tp) {

        floorPrice = fp;
        ceilingPrice = cp;
        targetPrice = tp;
        bundles = new ArrayList<SolutionOffer>();
    }

    public Product(String n, int fp, int cp, int tp) {
        name = n;
        floorPrice = fp;
        ceilingPrice = cp;
        targetPrice = tp;
        bundles = new ArrayList<SolutionOffer>();
    }

    public Product updateProduct(int fp, int cp, int tp) {
        floorPrice = fp;
        ceilingPrice = cp;
        targetPrice = tp;
        return this; // returns itself
    }

    public int getTargetPrice() {
        return targetPrice;
    }

    public void addBundle(SolutionOffer solutionOffer) {
        bundles.add(solutionOffer);
    }

    public HashMap<Market, Integer> getSalesQuantityList() {    //get sales quantity from a market
        HashMap<Market, Integer> resultsByMarket = new HashMap<Market, Integer>();
        for (SolutionOffer so : bundles) {
            Market m = so.getMarket();
            if (resultsByMarket.get(m) != null) {
                resultsByMarket.replace(m, resultsByMarket.get(m) + so.getSalesQuantity());
            } else {
                resultsByMarket.put(m, so.getSalesQuantity());
            }
        }
        return resultsByMarket;
    }


    public int getSalesVolume() {
        int total = 0;
        for (SolutionOffer so : bundles) {
            total += so.getSalesShare(this);
        }
        return total;
    }


    public HashMap<Market, Integer> getSalesVolumeList() {
        HashMap<Market, Integer> resultsByMarket = new HashMap<Market, Integer>();
        for (SolutionOffer so : bundles) {
            Market m = so.getMarket();
            if (resultsByMarket.get(m) != null) {
                resultsByMarket.replace(m, resultsByMarket.get(m) + so.getSalesShare(this));
            } else {
                resultsByMarket.put(m, so.getSalesShare(this));
            }
        }

        return resultsByMarket;
    }

    public int getTotalSalesVolume(){
        int total = 0;
        for (SolutionOffer so : bundles){
            total += so.getSalesShare(this);
        }
        return total;
    }


    public HashMap<Market, Integer> getAdBudgetList() {
        HashMap<Market, Integer> resultsByMarket = new HashMap<Market, Integer>();
        for (SolutionOffer so : bundles) {
            Market m = so.getMarket();
            if (resultsByMarket.get(m) != null) {
                
                resultsByMarket.replace(m, resultsByMarket.get(m) + so.getAdsBudgetShare(this));
                
            } else {
                resultsByMarket.put(m, so.getAdsBudgetShare(this));
            }
            // System.out.println("marketname: " + m.getMarketName() + ", getAdsBudgetShare: " + so.getAdsBudgetShare(this));
        }
        return resultsByMarket;
    }

    public HashMap<Market, Integer> getProfitList() {
        HashMap<Market, Integer> profitByMarket = new HashMap<>();
        HashMap<Market, Integer> salesVolume = getSalesVolumeList();
        HashMap<Market, Integer> adBudget = getAdBudgetList();
    
        for (Market market : salesVolume.keySet()) {
            int revenue = salesVolume.get(market);
            int cost = adBudget.getOrDefault(market, 0);
            profitByMarket.put(market, revenue - cost);
        }
        return profitByMarket;
    }
    


    // Number of item sales above target
    public int getNumberOfProductSalesAboveTarget() {
        int sum = 0;
        for (OrderItem oi : orderItems) {
            if (oi.isActualAboveTarget() == true)
                sum = sum + 1;
        }
        return sum;
    }

    public int getNumberOfProductSalesBelowTarget() {
        int sum = 0;
        for (OrderItem oi : orderItems) {
            if (oi.isActualBelowTarget() == true)
                sum = sum + 1;
        }
        return sum;
    }

    public boolean isProductAlwaysAboveTarget() {
        for (OrderItem oi : orderItems) {
            if (oi.isActualAboveTarget() == false)
                return false; //
        }
        return true;
    }
    // calculates the revenues gained or lost (in relation to the target)
    // For example, if target is at $2000 and actual is $2500 then revenue gained
    // is $500 above the expected target. If the actual is $1800 then the lose will
    // be $200
    // Add all these difference to get the total including wins and loses

    public int getOrderPricePerformance() {
        int sum = 0;
        for (OrderItem oi : orderItems) {
            sum = sum + oi.calculatePricePerformance(); // positive and negative values
        }
        return sum;
    }


    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (OrderItem oi : orderItems) {
            totalQuantity = totalQuantity + oi.getQuantity();
        }
        return totalQuantity;
    }

    public float getAveragePrice() {
        if (getTotalQuantity() == 0)
            return 0;
        return (float) getSalesVolume() / getTotalQuantity();
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getFloorPrice() {
        return floorPrice;
    }

    public int getCeilingPrice() {
        return ceilingPrice;
    }

}
