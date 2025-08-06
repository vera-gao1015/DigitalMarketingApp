/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;
import java.util.Random;


public class ChannelCatalog {

    ArrayList<Channel> channels;

    public ChannelCatalog(){
        channels = new ArrayList<Channel>();
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Channel addChannel(String name, int price){
        Channel channel = new Channel(name, price);
        channels.add(channel);
        return channel;
    }

    public Channel pickRandomChannel(){
        if (channels.size() == 0)
            return null;
        Random random = new Random();
        return channels.get(random.nextInt(channels.size()));
    }

}
