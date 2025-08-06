package model.Business;

import java.util.Scanner;

public class AdminReport {
    Business business;

    public AdminReport(Business business) {
        this.business = business;
    }

    public void chooseReport() {
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        while (!choice.equals("0")) {
            System.out.println("\n=== Admin Report Menu ===\n");
            System.out.println("1. Market Profitability Report");
            System.out.println("2. Channel Profitability Report");
            System.out.println("3. Advertising Efficiency Report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextLine();

            if (choice.equals("1")){
                
                System.out.println("\nGenerating Market Profitability Report...");
                        business.generateMarketProfitabilityReport();
            
            }else if (choice.equals("2")) {
                
                System.out.println("\nGenerating Channel Profitability Report...");
                business.generateChannelProfitabilityReport();
            
            } else if (choice.equals("3")) {
                
                System.out.println("\nGenerating Advertising Efficiency Report...");
                business.generateAdvertisingEfficiencyReport();
            
            } else if (choice.equals("0")) {
                
                System.out.println("\nExiting Admin Menu. Goodbye!");
            
            } else {
                
                System.out.println("Please enter a valid number (0-3).");
            }
        }

        scanner.close();
    } 

}
