/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.ProductManagement.SolutionOffer;

/**
 *
 * @author kal bugrara
 */
public class MarketChannelAssignment {

  Market market;
  Channel channel;
  ArrayList<SolutionOffer> bundles;
  int advertisingBudget;


  public MarketChannelAssignment(Market m, Channel c) {
    bundles = new ArrayList<SolutionOffer>();
    market = m;
    channel = c;

  }

  public void addSolutionOffer(SolutionOffer solutionoffer){
    if (!bundles.contains(solutionoffer)){
      bundles.add(solutionoffer);
    }
  }

  public Market getMarket(){
    return market;
  }

  public Channel getChannel(){
    return channel;
  }


  public int getAdvertisingBudget() {
    return advertisingBudget;
  }


  public void setAdvertisingBudget(int advertisingBudget) {
    this.advertisingBudget = advertisingBudget;
  }

  

  public int getSalesVolume(){
    int total = 0;
    for (SolutionOffer solutionOffer : bundles){
      total += solutionOffer.getSalesVolume();

    }
    return total;

  }

  public int getAdvertisingBudgetShare(SolutionOffer so) {

    // for each solution offer, its share in budget would be = sales * budget / totalsales
    if (!bundles.contains(so)|| getSalesVolume() == 0)
      return 0;
      
      float ratio = (float) so.getSalesVolume() / getSalesVolume();

      // System.out.println("ratio " + ratio);
      // System.out.println("so_SalesVol:  " +so.getSalesVolume() + " Budget: " +advertisingBudget + " markert_SalesVol: " + getSalesVolume());

    return (int) (ratio * advertisingBudget);  // so.getSalesVolume() * advertisingBudget / getSalesVolume() could cuase integer overflow issue
  }


  public String getName() {

    return market.getMarketName() + "-" + channel.getChannelName();
  }

  public ArrayList<SolutionOffer> getBundles(){
    return bundles;
  }

  public void printShortInfo(){
    System.out.println("Market-Channel Assignment: " + getName());
    System.out.println("Advertising Budget: $" + advertisingBudget);
    System.out.println("Bundles:");
    for (SolutionOffer bundle : bundles) {
      System.out.println(" - " + bundle.getBundleName());
    }
  }
  
}


