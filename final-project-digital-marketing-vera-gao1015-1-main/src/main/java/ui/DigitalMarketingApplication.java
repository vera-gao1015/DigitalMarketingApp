/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.ArrayList;

import com.github.javafaker.Faker;
import com.github.tomaslanger.chalk.Chalk;

import model.Business.AdminReport;
import model.Business.Business;
import model.Business.ConfigureABusiness;


public class DigitalMarketingApplication {


 
  public static void main(String[] args) {

    // TODO - Digital Marketing
    Business CozyNest = ConfigureABusiness.createABusinessAndLoadALotOfData("CozyNest", 3, 30, 
      1000,5000,10,50); 

    // ***********************************Part 1 - Populating the model ****************************
    System.out.println("***********************************Part 1 - Populating the model ****************************");

    
    
    System.out.println("Business Name: " + CozyNest.getName()+"\n");
    System.out.println("Number of Markets: " + CozyNest.getMarketCatalog().getMarkets().size()+"\n");
    System.out.println("Number of Channels: " + CozyNest.getChannelCatalog().getChannels().size()+"\n");

    int totalSpend = CozyNest.getMarketCatalog().getTotalAdvertisingSpend();

    System.out.println("\nTotal Advertising Spend: $" + totalSpend);
    
    CozyNest.printShortInfo();


    


    // *********************************** Part 2 - Build Reports ****************************

    System.out.println("***********************************Part 2 - Build Reports ****************************");
    CozyNest.generateMarketProfitabilityReport();
    CozyNest.generateChannelProfitabilityReport();
    CozyNest.generateAdvertisingEfficiencyReport(); 

    
    // Generate Admin Report.....................

    // AdminReport choose_Cozy_Report = new AdminReport(CozyNest);
    // choose_Cozy_Report.chooseReport();
    



    // *********************************** Part 3 - User Interaction ****************************
    
    System.out.println("*********************************** Part 3 - User Interaction ****************************");

    UserInteraction cozyapp = new UserInteraction(CozyNest);
    cozyapp.start();
  
  





    
    


  }
}

