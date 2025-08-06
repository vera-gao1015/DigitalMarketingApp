/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.CustomerManagement.CustomerDirectory;
import model.MarketModel.Channel;
import model.MarketModel.ChannelCatalog;
import model.MarketModel.ChannelSummary;
import model.MarketModel.Market;
import model.MarketModel.MarketCatalog;
import model.MarketModel.MarketChannelAssignment;
import model.MarketingManagement.MarketingPersonDirectory;
import model.OrderManagement.MasterOrderList;
import model.Personnel.EmployeeDirectory;
import model.Personnel.PersonDirectory;
import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;
import model.ProductManagement.ProductSummary;
import model.ProductManagement.ProductsReport;
import model.ProductManagement.SolutionOffer;
import model.ProductManagement.SolutionOfferCatalog;
import model.SalesManagement.SalesPersonDirectory;
import model.Supplier.Supplier;
import model.Supplier.SupplierDirectory;
import model.UserAccountManagement.UserAccountDirectory;



public class Business {
    String name;
    PersonDirectory persondirectory;
    MasterOrderList masterorderlist;
    SupplierDirectory suppliers;
    MarketCatalog marketcatalog;
    ChannelCatalog channelcatalog;

    SolutionOfferCatalog solutionoffercatalog;
    CustomerDirectory customerdirectory;
    EmployeeDirectory employeedirectory;
    SalesPersonDirectory salespersondirectory;
    UserAccountDirectory useraccountdirectory;
    MarketingPersonDirectory marketingpersondirectory;

    public Business(String n) {
        name = n;
        masterorderlist = new MasterOrderList();
        suppliers = new SupplierDirectory(this);
        solutionoffercatalog = new SolutionOfferCatalog(this);
        persondirectory = new PersonDirectory();
        customerdirectory = new CustomerDirectory(this);
        salespersondirectory = new SalesPersonDirectory(this);
        useraccountdirectory = new UserAccountDirectory();
        marketingpersondirectory = new MarketingPersonDirectory(this);
        employeedirectory = new EmployeeDirectory(this);

        marketcatalog = new MarketCatalog();
        channelcatalog = new ChannelCatalog();

    }

    public String getName(){
        return name;
    }
    public int getSalesVolume() {
        return masterorderlist.getSalesVolume();

    }

    public PersonDirectory getPersonDirectory() {
        return persondirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return useraccountdirectory;
    }

    public MarketingPersonDirectory getMarketingPersonDirectory() {
        return marketingpersondirectory;
    }

    public SupplierDirectory getSupplierDirectory() {
        return suppliers;
    }

    public SolutionOfferCatalog getSolutionOfferCatalog(){
        return solutionoffercatalog;
    }

    public ProductsReport getSupplierPerformanceReport(String n) {
        Supplier supplier = suppliers.findSupplier(n);
        if (supplier == null) {
            return null;
        }
        return supplier.prepareProductsReport();

    }

    public ArrayList<ProductSummary> getSupplierProductsAlwaysAboveTarget(String n) {

        ProductsReport productsreport = getSupplierPerformanceReport(n);
        return productsreport.getProductsAlwaysAboveTarget();

    }

    public int getHowManySupplierProductsAlwaysAboveTarget(String n) {
        ProductsReport productsreport = getSupplierPerformanceReport(n); // see above
        int i = productsreport.getProductsAlwaysAboveTarget().size(); // return size of the arraylist
        return i;
    }

    public CustomerDirectory getCustomerDirectory() {
        return customerdirectory;
    }

    public SalesPersonDirectory getSalesPersonDirectory() {
        return salespersondirectory;
    }

    public MasterOrderList getMasterOrderList() {
        return masterorderlist;
    }

    public EmployeeDirectory getEmployeeDirectory() {
        return employeedirectory;
    }

    public MarketCatalog getMarketCatalog() {
        return marketcatalog;
    }

    public ChannelCatalog getChannelCatalog() {
        return channelcatalog;
    }

    public Set<Product> getAllProducts(){
        Set<Product> allProducts = new HashSet<>();
        for (Supplier supplier : getSupplierDirectory().getSupplierList()) {
            allProducts.addAll(supplier.getProductCatalog().getProductList());
        }
        return allProducts;
    }
    
    public Product pickRandomProductFromRandomSupplier(){
        SupplierDirectory supplierDirectory = this.getSupplierDirectory();
        Supplier randomSupplier = supplierDirectory.pickRandomSupplier();
        if (randomSupplier == null) {
            System.out.println("No suppliers available to create bundles.");
            return null;
        }
        ProductCatalog productCatalog = randomSupplier.getProductCatalog();
        // System.out.println("xxth of bundleCount: " + i);
        Product randomProduct = productCatalog.pickRandomProduct();  // Pick a random product
        if (randomProduct == null) {
            System.out.println("Cannot generate orders. No products in the product catalog.");
            return null;
        }
        return randomProduct;
    }

    public void printShortInfo() {
        System.out.println("\nChecking what's inside the business hierarchy.\n");
        suppliers.printShortInfo();
        customerdirectory.printShortInfo();
        masterorderlist.printShortInfo();
        solutionoffercatalog.printShortInfo();
    }

    public Market getMarketByName(String name) {
        for (Market market : this.getMarketCatalog().getMarkets()) {
            if (market.getMarketName().equals(name)) {
                return market;
            }
        }
        return null;
    }
    


    public void generateMarketProfitabilityReport() {
        System.out.println("\n=== Market Profitability Report ===\n");
        System.out.printf("%-3s|%-30s|%-10s|%-10s|%-10s|%-11s|%-11s|%-11s|%-11s|%-11s|%-11s|%-11s|%-11s\n\n", 
                          "#", "ProductName", 
                          "US(Qty)", "Asia(Qty)", "UK(Qty)",
                          "US(Rev)", "Asia(Rev)", "UK(Rev)","SalesVolume",
                          "US(Ad)", "Asia(Ad)", "UK(Ad)", "Profit");
    
        int count = 1;
    
        for (Product product : getAllProducts()) {
            HashMap<Market, Integer> salesQuantities = product.getSalesQuantityList(); // Quantities
            HashMap<Market, Integer> salesRevenues = product.getSalesVolumeList();     // Revenue
            HashMap<Market, Integer> adBudgets = product.getAdBudgetList();            // Ad budgets
            
            Market northAmerica = getMarketByName("North America");
            Market asia = getMarketByName("Asia");
            Market europe = getMarketByName("Europe");
    
            int qtyNA = northAmerica != null ? salesQuantities.getOrDefault(northAmerica, 0) : 0;
            int qtyAsia = asia != null ? salesQuantities.getOrDefault(asia, 0) : 0;
            int qtyEurope = europe != null ? salesQuantities.getOrDefault(europe, 0) : 0;
    
            int revenueNA = northAmerica != null ? salesRevenues.getOrDefault(northAmerica, 0) : 0;
            int revenueAsia = asia != null ? salesRevenues.getOrDefault(asia, 0) : 0;
            int revenueEurope = europe != null ? salesRevenues.getOrDefault(europe, 0) : 0;
    
            int adNA = northAmerica != null ? adBudgets.getOrDefault(northAmerica, 0) : 0;
            int adAsia = asia != null ? adBudgets.getOrDefault(asia, 0) : 0;
            int adEurope = europe != null ? adBudgets.getOrDefault(europe, 0) : 0;
            
            // SalesVolume
            int totalSalesVolume = product.getTotalSalesVolume();

            // Profit
            int profit = (revenueNA + revenueAsia + revenueEurope) - (adNA + adAsia + adEurope);

            System.out.printf("%-3d|%-30s|%-10d|%-10d|%-10d|$%-10d|$%-10d|$%-10d|$%-10d|$%-10d|$%-10d|$%-10d|$%-10d|\n", 
                              count++, product.getName(), qtyNA, qtyAsia, qtyEurope,
                              revenueNA, revenueAsia, revenueEurope,totalSalesVolume, 
                              adNA, adAsia, adEurope, profit);
        }
    }




    public void generateChannelProfitabilityReport() {
        System.out.println("\n=== Channel Profitability Report ===\n");
        String str_format_titles = "%-2s|%-25s|%-7s|%-7s|%-7s|%-7s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-7s|%-7s|%-7s|%-7s|%-7s|%-7s|%-7s\n";
        System.out.printf(str_format_titles,
                "#", "ProductName",
                "Ins(Q)", "Goo(Q)", "Tt(Q)", "Yout(Q)",
                "Ins(R)", "Goo(R)", "Tt(R)", "Yout(R)",
                "Ins(A)", "Goo(A)", "Tt(A)", "Yout(A)",
                "Ins(P)", "Goo(P)", "Tt(P)", "Yout(P)",
                "Ins(%)", "Goo(%)", "Tt(%)", "Yout(%)\n");
    
        int rank = 1;

        for (Product product : getAllProducts()) {
 
            Map<String, Integer> qtyMap = new HashMap<>();
            Map<String, Integer> revenueMap = new HashMap<>();
            Map<String, Integer> adMap = new HashMap<>();
            Map<String, Integer> profitMap = new HashMap<>();
            Map<String, Float> profitPercentMap = new HashMap<>();
    

            List<String> channels = List.of("Instagram", "Google", "TikTok", "Youtube");
    
            for (String channel : channels) {
                qtyMap.put(channel, 0);
                revenueMap.put(channel, 0);
                adMap.put(channel, 0);
                profitMap.put(channel, 0);
                profitPercentMap.put(channel, 0f);
            }
    

            for (Market market : getMarketCatalog().getMarkets()) {
                for (Channel channel : getChannelCatalog().getChannels()) {
                    MarketChannelAssignment mca = market.getMarketChannelComb(channel);
                    if (mca != null) {
                        for (SolutionOffer offer : mca.getBundles()) {
                            if (offer.getProducts().contains(product)) {
                                String channelName = channel.getChannelName();

                                
                                int qty = offer.getSalesQuantity();
                                int revenue = offer.getSalesVolume();
                                int adBudget = mca.getAdvertisingBudgetShare(offer);
                                int profit = revenue - adBudget;
                                float profitPercent = adBudget > 0 ? (float) profit / adBudget * 100 : 0;
                                // if (channelName == "Youtube"){
                                //     System.out.println(channelName);
                                //     System.out.println(qty);
                                //     System.out.println(revenue);
                                //     System.out.println(profit);
                                // }
                                qtyMap.put(channelName, qtyMap.getOrDefault(channelName, 0) + qty);
                                revenueMap.put(channelName, revenueMap.getOrDefault(channelName, 0) + revenue);
                                adMap.put(channelName, adMap.getOrDefault(channelName, 0) + adBudget);
                                profitMap.put(channelName, profitMap.getOrDefault(channelName, 0) + profit);
                                profitPercentMap.put(channelName, profitPercentMap.getOrDefault(channelName, 0f) + profitPercent);
                            }
                        }
                    }
                }
            }
        
        String str_format = "%-2d|%-25s|%-7d|%-7d|%-7d|%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|%-7d|%-7d|%-7d|%-6.2f%%|%-6.2f%%|%-6.2f%%|%-6.2f%%\n";
        System.out.printf(str_format,
        rank++, product.getName(),
        qtyMap.get("Instagram"), qtyMap.get("Google"), qtyMap.get("TikTok"), qtyMap.get("Youtube"),
        revenueMap.get("Instagram"), revenueMap.get("Google"), revenueMap.get("TikTok"), revenueMap.get("Youtube"),
        adMap.get("Instagram"), adMap.get("Google"), adMap.get("TikTok"), adMap.get("Youtube"),
        profitMap.get("Instagram"), profitMap.get("Google"), profitMap.get("TikTok"), profitMap.get("Youtube"),
        profitPercentMap.get("Instagram"), profitPercentMap.get("Google"), profitPercentMap.get("TikTok"), profitPercentMap.get("Youtube"));
        // System.out.printf("%-2d|%-25s|%-4d|%-4d|%-4d|%-4d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|$%-7d|%-6.2f%%|%-6.2f%%|%-6.2f%%|%-6.2f%%\n",
        // rank++, product.getName(),
        // qtyMap.getOrDefault("Instagram", 0), qtyMap.getOrDefault("Google", 0), qtyMap.getOrDefault("TikTok", 0), qtyMap.getOrDefault("Youtube", 0),
        // revenueMap.getOrDefault("Instagram", 0), revenueMap.getOrDefault("Google", 0), revenueMap.getOrDefault("TikTok", 0), revenueMap.getOrDefault("Youtube", 0),
        // adMap.getOrDefault("Instagram", 0), adMap.getOrDefault("Google", 0), adMap.getOrDefault("TikTok", 0), adMap.getOrDefault("Youtube", 0),
        // profitMap.getOrDefault("Instagram", 0), profitMap.getOrDefault("Google", 0), profitMap.getOrDefault("TikTok", 0), profitMap.getOrDefault("Youtube", 0),
        // profitPercentMap.getOrDefault("Instagram", 0.0f), profitPercentMap.getOrDefault("Google", 0.0f), profitPercentMap.getOrDefault("TikTok", 0.0f), profitPercentMap.getOrDefault("Youtube", 0.0f));

        }
}
    
    




    public void generateAdvertisingEfficiencyReport() {
        System.out.println("\n=== Advertising Efficiency Report ===\n");
        System.out.printf("%-30s|%-14s|%-14s|%-15s|%-14s|%-15s|%-15s|%-14s\n", 
                          "Solution Offer", "Market", "Channel", 
                          "Ad Budget($)", "Quantity", "Revenue($)", "Profit($)", "Ad Efficiency\n");

        for (Market market : getMarketCatalog().getMarkets()) {
            for (Channel channel : getChannelCatalog().getChannels()) {
                MarketChannelAssignment mca = market.getMarketChannelComb(channel);
    
                if (mca != null) {
                    int totalAdBudget = mca.getAdvertisingBudget();
                    int totalRevenue = 0;

                    for (SolutionOffer solutionOffer : mca.getBundles()) {
                        totalRevenue += solutionOffer.getSalesVolume();
                    }

                    for (SolutionOffer solutionOffer : mca.getBundles()) {
                        int revenue = solutionOffer.getSalesVolume(); 
                        int quantity = solutionOffer.getSalesQuantity(); 
                        
                        float budgetShare = totalRevenue > 0 ? (float) revenue / totalRevenue : 0; //advertising budget allocated to this solution offer
                        int allocatedAdBudget = Math.round(budgetShare * totalAdBudget);
    
                        // Profit = Revenue - Allocated AdBudget
                        int profit = revenue - allocatedAdBudget;
    
                        // Ad efficiency = Revenue / Allocated AdBudget
                        float adEfficiency = allocatedAdBudget > 0 ? (float) revenue / allocatedAdBudget : 0;
    
                        System.out.printf("%-30s|%-14s|%-14s|$%-14d|%-14d|$%-14d|$%-14d|%14.2f|\n",
                                            solutionOffer.getBundleName(), market.getMarketName(),channel.getChannelName(),allocatedAdBudget,
                                            quantity,revenue,profit,adEfficiency);
                    }
                }
            }
        }
    }
    
    

}
    
    
    

