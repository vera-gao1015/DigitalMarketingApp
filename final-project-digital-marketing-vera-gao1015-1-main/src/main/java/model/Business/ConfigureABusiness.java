/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.github.javafaker.Faker;

import model.CustomerManagement.CustomerDirectory;
import model.CustomerManagement.CustomerProfile;
import model.MarketModel.Channel;
import model.MarketModel.ChannelCatalog;
import model.MarketModel.Market;
import model.MarketModel.MarketCatalog;
import model.MarketModel.MarketChannelAssignment;
import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import model.Personnel.Person;
import model.Personnel.PersonDirectory;
import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;
import model.ProductManagement.SolutionOffer;
import model.ProductManagement.SolutionOfferCatalog;
import model.Supplier.Supplier;
import model.Supplier.SupplierDirectory;


public class ConfigureABusiness {

  static int upperPriceLimit = 500;
  static int lowerPriceLimit = 200;
  static int range = 50;
  static int productMaxQuantity = 5;

  public static Business createABusinessAndLoadALotOfData(String name, int supplierCount, int productCount, 
    int customerCount, int orderCount, int itemCount, int bundleCount) {
    Business business = new Business(name);

    // Add Suppliers +
    loadSuppliers(business, supplierCount);

    // Add Products +
    loadProducts(business, productCount);

    // Add Markets and Channels
    loadMarketsAndChannels(business);


    // Add Bundles 
    loadBundles(business, bundleCount);

    // Add Customers
    loadCustomers(business, customerCount);

    // Add Order
    loadOrders(business, orderCount, itemCount);

    return business;
  }

  public static void loadMarketsAndChannels(Business business){
    MarketCatalog marketCatalog = business.getMarketCatalog();
    ChannelCatalog channelCatalog = business.getChannelCatalog();

    Market market1 = marketCatalog.newMarket("North America");
    Market market2 = marketCatalog.newMarket("Europe");
    Market market3 = marketCatalog.newMarket("Asia");

    Channel channel1 = channelCatalog.addChannel("Instagram", 0);
    Channel channel2 = channelCatalog.addChannel("TikTok", 0);
    Channel channel3 = channelCatalog.addChannel("Google", 0);
    Channel channel4 = channelCatalog.addChannel("Youtube", 0);

    setAdvertisingBudget(market1, channel1, 1414001);
    setAdvertisingBudget(market2, channel1, 1663397);
    setAdvertisingBudget(market3, channel1, 1167187);
    setAdvertisingBudget(market1, channel2, 1072266);
    setAdvertisingBudget(market2, channel2, 1040847);
    setAdvertisingBudget(market3, channel2, 626880);
    setAdvertisingBudget(market1, channel3, 509258);
    setAdvertisingBudget(market2, channel3, 1805058);
    setAdvertisingBudget(market3, channel3, 821457);
    setAdvertisingBudget(market1, channel4, 688083);
    setAdvertisingBudget(market2, channel4, 587244);
    setAdvertisingBudget(market3, channel4, 707078);
    
  }


  public static void setAdvertisingBudget(Market market, Channel channel, int budget){
    MarketChannelAssignment mca = market.getMarketChannelComb(channel);
    mca.setAdvertisingBudget(budget);
  }



  public static void loadSuppliers(Business b, int supplierCount) {
    Faker faker = new Faker();

    SupplierDirectory supplierDirectory = b.getSupplierDirectory();
    
    for (int index = 1; index <= supplierCount; index++) {
      String supplierName = faker.company().name();
      supplierName = supplierName.split(" ")[0];
      supplierDirectory.newSupplier(supplierName);
    }

  }



  static void loadProducts(Business b, int productCount) {
    SupplierDirectory supplierDirectory = b.getSupplierDirectory();
    ArrayList<Supplier> suppliers = supplierDirectory.getSupplierList();

    if (suppliers.isEmpty()) {
        System.out.println("No suppliers found. Cannot distribute products.");
        return;
    }

    Faker faker = new Faker();

    int supplierCount = suppliers.size();
    int productsPerSupplier = productCount / supplierCount;
    int remainingProducts = productCount % supplierCount;     // Extra products to distribute

    String[] furnitureTypes = { "Sofa", "Table", "Chair", "Bed", "Wardrobe", "Dresser", "Buffet", "Lamp", "Bookshelf" };
    
    int productIndex = 1;

    for (Supplier supplier : suppliers) {
        ProductCatalog productCatalog = supplier.getProductCatalog();

        int currentSupplierProductCount = productsPerSupplier + (remainingProducts > 0 ? 1 : 0);    // Each supplier = productsPerSupplier + 1 (if there is remaining products)

        // Reduce the remainingProducts if this supplier gets an extra product to meet the product number
        
        if (remainingProducts > 0) {
            remainingProducts--;
        }

        for (int i = 0; i < currentSupplierProductCount; i++) {
          String type = furnitureTypes[getRandom(0, furnitureTypes.length)];
          String productName = faker.commerce().material() + " " +type;
          int randomFloor = getRandom(lowerPriceLimit, lowerPriceLimit + range);
          int randomCeiling = getRandom(upperPriceLimit - range, upperPriceLimit);
          int randomTarget = getRandom(randomFloor, randomCeiling);

          productCatalog.newProduct(productName, randomFloor, randomCeiling, randomTarget);
          productIndex++;

        }

    }

  }


  static int getRandom(int lower, int upper) {
    Random r = new Random();

    // nextInt(n) will return a number from zero to 'n'. Therefore e.g. if I want
    // numbers from 10 to 15
    // I will have result = 10 + nextInt(5)
    int randomInt = lower + r.nextInt(upper - lower);
    return randomInt;
  }





  static void loadCustomers(Business b, int customerCount) {
    CustomerDirectory customerDirectory = b.getCustomerDirectory();
    PersonDirectory personDirectory = b.getPersonDirectory();

    Faker faker = new Faker();

    for (int index = 1; index <= customerCount; index++) {
      Person newPerson = personDirectory.newPerson(faker.name().fullName());
      customerDirectory.newCustomerProfile(newPerson);
    }
  }



  

  static void loadOrders(Business b, int orderCount, int itemCount) {

    // reach out to masterOrderList
    MasterOrderList mol = b.getMasterOrderList();

    // pick a random customer (reach to customer directory)
    CustomerDirectory cd = b.getCustomerDirectory();
    SupplierDirectory sd = b.getSupplierDirectory();

    for (int index = 0; index < orderCount; index++) {

      CustomerProfile randomCustomer = cd.pickRandomCustomer();
      
      if (randomCustomer == null) {
        System.out.println("Cannot generate orders. No customers in the customer directory.");
        return;
      }

      // create an order for that customer
      Order randomOrder = mol.newOrder(randomCustomer);

      // add order items
      // -- pick a supplier first (randomly)
      // -- pick a product (randomly)
      // -- actual price, quantity

      int randomItemCount = getRandom(1, itemCount);
      for (int itemIndex = 0; itemIndex < randomItemCount; itemIndex++) {

        Supplier randomSupplier = sd.pickRandomSupplier();
        if (randomSupplier == null) {
          System.out.println("Cannot generate orders. No supplier in the supplier directory.");
          return;
        }
        SolutionOfferCatalog solutionOfferCatalog = b.getSolutionOfferCatalog();;
        SolutionOffer randomBundle = solutionOfferCatalog.pickRandomBundle();
        if (randomBundle == null) {
          System.out.println("Cannot generate orders. No products in the product catalog.");
          return;
        }

        // int randomPrice = getRandom(randomBundle.getFloorPrice(), randomBundle.getCeilingPrice());
        int randomQuantity = getRandom(1, productMaxQuantity);

        randomOrder.newOrderItem(randomBundle, randomBundle.getTargetPrice(), randomQuantity);
      }
    }
    // Make sure order items are connected to the order

  }



  public static void loadBundles(Business business, int bundleCount) {
    Random random = new Random();
    SolutionOfferCatalog solutionOfferCatalog = business.getSolutionOfferCatalog();
    int max_num_product = 3;
    
    // System.out.println("num_products= " + num_products);
    for (int i = 0; i < bundleCount; i++) {
      Product randomProduct = business.pickRandomProductFromRandomSupplier();

      int num_products = random.nextInt(1, max_num_product + 1);

      // Assign to a random Market-Channel Assignment
      Market randomMarket = business.getMarketCatalog().pickRandomMarket();
      Channel randomChannel = business.getChannelCatalog().pickRandomChannel();

      MarketChannelAssignment mca = randomMarket.getMarketChannelComb(randomChannel);

      // create selectedProducts Set
      Set<Product> selectedProducts = new HashSet<>();
      selectedProducts.add(randomProduct);

      //check if (number of products of a bundle) >1, then add addtional products into the selectedProducts

      for (int j = 1; j < num_products; j++ ){
        Product additionalProduct = business.pickRandomProductFromRandomSupplier();
        
        if (additionalProduct != null && selectedProducts.add(additionalProduct)){
        }
      }

      // int totalFloorPrice = selectedProducts.stream().mapToInt(Product::getFloorPrice).sum();
      int totalTargetPrice = selectedProducts.stream().mapToInt(Product::getTargetPrice).sum();

      int bundleTargetPrice = 0 ;
      if (selectedProducts.size() > 1) {
        bundleTargetPrice = getRandom((int)(totalTargetPrice * 0.5), totalTargetPrice);
      } else {
        bundleTargetPrice = totalTargetPrice;
      }

      // Create a bundle
      SolutionOffer so = solutionOfferCatalog.newSolutionOffer(mca, bundleTargetPrice, randomProduct);
      
      for (Product additionalProduct : selectedProducts) {
        if (!additionalProduct.equals(randomProduct)) {
          // float randomFraction = random.nextFloat(0.1f, 1.0f);
          float fraction = (float)additionalProduct.getTargetPrice()/randomProduct.getTargetPrice();
          so.addProduct(additionalProduct, fraction);
        }
    }
    
      so.updateBundleName();
      so.normalize();

    }
  }

  
}
