/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;


public class Channel {
    String channelName;
    int price;
    ArrayList<MarketChannelAssignment> marketChannelCombinations;

    public Channel(String channelName, int price){
        this.channelName = channelName;
        this.price = price;
        this.marketChannelCombinations = new ArrayList<MarketChannelAssignment>();

    }

    public String getChannelName(){
        return channelName;
    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<MarketChannelAssignment> getMarketChannelCombinations() {
        return marketChannelCombinations;
    }

    public void addMarketChannelAssignment(MarketChannelAssignment mca) {
        marketChannelCombinations.add(mca);
    }
    




}
