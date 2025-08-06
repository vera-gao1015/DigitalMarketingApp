/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;
import java.util.Random;

import model.Business.Business;

/**
 *
 * @author kal bugrara
 */
public class MarketCatalog {
    Business business;
    ArrayList<Market> markets;

    
    public MarketCatalog(){
        markets = new ArrayList<Market>();
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

    public Market newMarket(String name) {
        Market market = new Market(name);
        markets.add(market);
        return market;
    }

    public Market pickRandomMarket(){
        if (markets.size() == 0)
            return null;
        Random random = new Random();
        return markets.get(random.nextInt(markets.size()));
    }

    public int getTotalAdvertisingSpend() {
        int totalSpend = 0;
        for (Market market : markets) {
            for (MarketChannelAssignment mca : market.marketChannelCombs) {
                totalSpend += mca.getAdvertisingBudget();
            }
        }
        return totalSpend;
    }


}


