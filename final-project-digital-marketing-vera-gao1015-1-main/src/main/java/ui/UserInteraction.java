package ui;

import com.github.tomaslanger.chalk.Chalk;
import model.Business.Business;
import model.CustomerManagement.CustomerDirectory;
import model.CustomerManagement.CustomerProfile;
import model.MarketModel.Channel;
import model.MarketModel.Market;
import model.MarketModel.MarketChannelAssignment;
import model.Personnel.Person;
import model.ProductManagement.SolutionOffer;
import model.ProductManagement.SolutionOfferCatalog;

import java.util.ArrayList;
import java.util.Scanner;


public class UserInteraction {
    Scanner scanner = new Scanner(System.in);
    Business business;
    Market selectedMarket;
    Channel selectedChannel;
    ArrayList<SolutionOffer> shoppingCart;
    CustomerDirectory customerDirectory;
    CustomerProfile customerProfile;

    public UserInteraction(Business business){
        this.business = business;
        this.customerDirectory = business.getCustomerDirectory();
        this.shoppingCart = new ArrayList<>();
    }


    public void start(){
        System.out.println(Chalk.on("Welcome to CozyNest!").bold() + " One-stop destination for cozy furnitures for your family — find your perfect choice today!\n");
        registerUserOrLoginUser();
        pickMarketAndChannel();
        displayAdvertisement();
        browseSolutionOffers();
    }

    private void registerUserOrLoginUser(){
        System.out.print(Chalk.on("Please enter your name to register or login: ").bold());
        String name = scanner.nextLine();

        CustomerProfile currentCustomer = customerDirectory.findCustomer(name);

        if (currentCustomer != null) {

            System.out.println("\nWelcome back, " + Chalk.on(name).blue() + "!");

        } else {
            // Create a new Person and CustomerProfile
            Person newPerson = new Person(name); // Create a new Person
            customerDirectory.newCustomerProfile(newPerson); // Add to CustomerDirectory
            System.out.println("\nRegistration successful! Welcome to CozyNest, " + Chalk.on(name).blue() + ".");
        }
    }

    private void pickMarketAndChannel() {
        System.out.println(Chalk.on("\n=== Please Choose your Area ===\n").bold());
        ArrayList<Market> markets = business.getMarketCatalog().getMarkets();
        for (int i = 0; i < markets.size(); i++) {
            System.out.println((i + 1) + ". " + markets.get(i).getMarketName());
        }
        System.out.print("\nEnter the number of your choice: ");
        int marketChoice = Integer.parseInt(scanner.nextLine());
    
        // Check if the input is valid
        if (marketChoice < 1 || marketChoice > markets.size()) {
            System.out.println("\nInvalid choice. Please try again.");
            pickMarketAndChannel();
            return;
        }
        selectedMarket = markets.get(marketChoice - 1);
    
        System.out.println(Chalk.on("\n=== Please Choose your Channel ===\n").bold());
        ArrayList<Channel> channels = business.getChannelCatalog().getChannels();
        for (int i = 0; i < channels.size(); i++) {
            System.out.println((i + 1) + ". " + channels.get(i).getChannelName());
        }
        System.out.print("\nEnter the number of your choice: ");
        int channelChoice = Integer.parseInt(scanner.nextLine());
    
        // Check if the input is valid
        if (channelChoice < 1 || channelChoice > channels.size()) {
            System.out.println("\nInvalid choice. Please try again.");
            pickMarketAndChannel();
            return;
        }
        selectedChannel = channels.get(channelChoice - 1);
    
        System.out.println(Chalk.on("\nGreat! You have chosen successfully!\n").red());
    }
    

    private void displayAdvertisement() {
        System.out.println("\nWelcome to the " + selectedMarket.getMarketName() + " Market via " + selectedChannel.getChannelName() + " Channel !");
        System.out.println("\nActually it's an ad >v<... Hurry up! Enjoy the exclusive offers tailored only for " + 
        Chalk.on(selectedMarket.getMarketName()).bold() + Chalk.on(" - ").bold() + Chalk.on(selectedChannel.getChannelName()).bold() + " !");

        System.out.println(Chalk.on("\n=== Here are the Best Solution Offers for You ===\n").bold());
        MarketChannelAssignment mca = selectedMarket.getMarketChannelComb(selectedChannel);
        ArrayList<SolutionOffer> offers = mca.getBundles();
    
        if (offers.isEmpty()) {
            System.out.println("\n So sorry...No offers are currently available for this combination of Market and Channel.");
            return;
        }

        for (int i = 0; i < offers.size(); i++) {
            SolutionOffer offer = offers.get(i);
            System.out.println((i + 1) + ". " + Chalk.on(offer.getBundleName()).blue() + " with " + offer.getProducts().size() + " Product(s) " + 
            "   -CurrentPrice: $" + offer.getTargetPrice() + "   -BeforePrice: $" + offer.getBeforePrice());
        }
    }

    private void browseSolutionOffers() {
        MarketChannelAssignment mca = selectedMarket.getMarketChannelComb(selectedChannel);
        ArrayList<SolutionOffer> offers = mca.getBundles();
        

        while (true){
            System.out.println("\n"+ Chalk.on("Options:").bold());
            System.out.println("1. View detailed products for an offer");
            System.out.println("2. Add offers to cart");
            System.out.println("3. Choose a different Market and Channel");
            System.out.println("4. Go to Shopping Cart");
            System.out.println("5. Exit to Main Menu");
            System.out.print("\nEnter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> viewOfferDetails(offers);
                case 2 -> addOffersToCart(offers);
                case 3 -> {
                    pickMarketAndChannel();
                    displayAdvertisement();
                }
                case 4 -> {
                    manageShoppingCart();
                    return; 
                }
                case 5 -> {
                    return; 
                }
                
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void viewOfferDetails(ArrayList<SolutionOffer> offers) {
        System.out.print("\nEnter the number of the offer to view details: ");
        int offerIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (offerIndex >= 0 && offerIndex < offers.size()) {
            SolutionOffer selectedOffer = offers.get(offerIndex);
            System.out.println(Chalk.on("\n=== Offer Details ===").bold());
            System.out.println(Chalk.on(selectedOffer.getBundleName()).bold() +
                " - Target Price: $" + selectedOffer.getTargetPrice());
            System.out.println("Products included:");
            selectedOffer.getProducts().forEach(product ->
                System.out.println("- " + product.getName() +
                    " - Target Price: $" + product.getTargetPrice()));
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addOffersToCart(ArrayList<SolutionOffer> offers) {
        System.out.print("\nEnter the numbers of the offers you'd like to add to your cart (comma-separated): ");
        String[] choices = scanner.nextLine().split(",");
        for (String choice : choices) {
            int offerIndex = Integer.parseInt(choice.trim()) - 1;
            if (offerIndex >= 0 && offerIndex < offers.size()) {
                shoppingCart.add(offers.get(offerIndex));
                System.out.println(Chalk.on(offers.get(offerIndex).getBundleName() + " has been added to your cart.").green());
            } else {
                System.out.println("Invalid choice: " + choice);
            }
        }
    }
    
    

    private void manageShoppingCart() {
        while (true) {
            System.out.println(Chalk.on("\n=== Your Shopping Cart ===").bold());
            if (shoppingCart.isEmpty()) {
                System.out.println("Your shopping cart is empty.");
                return;
            }
    
            System.out.println("\nHere are the items in your cart:");
            for (int i = 0; i < shoppingCart.size(); i++) {
                SolutionOffer offer = shoppingCart.get(i);
                System.out.println((i + 1) + ". " + offer.getBundleName() + " - Price: $" + offer.getTargetPrice());
            }
    
            System.out.println("\n"+ Chalk.on("Options:").bold());
            System.out.println("1. Remove an item");
            System.out.println("2. Proceed to checkout");
            System.out.print("\nEnter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
    
            switch (choice) {
                case 1 -> {
                    System.out.print("\nEnter the number of the item you want to remove: ");
                    int itemIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (itemIndex >= 0 && itemIndex < shoppingCart.size()) {
                        System.out.println(shoppingCart.get(itemIndex).getBundleName() + " has been removed from your cart.");
                        shoppingCart.remove(itemIndex);
                    
                    } else {
                        
                        System.out.println("Invalid item number. Please try again.");
                    }
                }
                case 2 -> {
                    checkout();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void checkout() {
        if (shoppingCart.isEmpty()) {
            System.out.println(Chalk.on("\n=== Checkout Page ===").bold());
            System.out.println("\nYour cart is empty. No order has been placed.\n");
            return;
        }

        System.out.println(Chalk.on("\n=== Checkout Page ===").bold());
        System.out.println("\nYou have ordered the following solution offers:\n");
        int totalCost = 0;
        for (SolutionOffer offer : shoppingCart) {
            System.out.println("- " + offer.getBundleName() + " - Price: $" + offer.getTargetPrice());
            totalCost += offer.getTargetPrice();
        }

        System.out.println(("Total Cost: $") + totalCost);
        System.out.println("\nThank you for your purchase! Your order has been placed successfully.");
        
    }


}







