/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.Random;

import model.Business.Business;
import model.MarketModel.MarketChannelAssignment;

/**
 *
 * @author kal bugrara
 */
public class SolutionOfferCatalog {
    Business business;
    public ArrayList<SolutionOffer> solutionoffers;

    public SolutionOfferCatalog(Business b){
        business = b;
        solutionoffers = new ArrayList<SolutionOffer>();
    }

    public SolutionOffer newSolutionOffer(MarketChannelAssignment mca, int targetPrice, Product product){
        SolutionOffer bundle = new SolutionOffer(mca, targetPrice, product);
        solutionoffers.add(bundle);
        return bundle;
    }

    public SolutionOffer pickRandomBundle(){
        if (solutionoffers.isEmpty()){
            return null;
        }
        Random random = new Random();
        return solutionoffers.get(random.nextInt(solutionoffers.size()));
    }

    public void printShortInfo(){
        System.out.println("\nThe number of solution offers: " + solutionoffers.size() + "\n");
        for (SolutionOffer so : solutionoffers){
            so.printShortInfo();
        }
    }
    



}
