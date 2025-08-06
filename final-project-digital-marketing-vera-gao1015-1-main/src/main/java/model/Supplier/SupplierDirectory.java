/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Supplier;

import java.util.ArrayList;
import java.util.Random;

import model.Business.Business;

/**
 *
 * @author kal bugrara
 */
public class SupplierDirectory {

    Business business;
    ArrayList<Supplier> suppliers;

    public SupplierDirectory(Business b) {
        business = b;
        suppliers = new ArrayList<Supplier>();
    }

    public Supplier newSupplier(String n) {
        Supplier supplier = new Supplier(n, this);
        suppliers.add(supplier);
        return supplier;

    }

    public Supplier findSupplier(String id) {

        for (Supplier supplier : suppliers) {

            if (supplier.getName().equals(id))
                return supplier;
        }
        return null;
    }

    public ArrayList<Supplier> getSupplierList() {
        return suppliers;
    }

    public Business getBusiness() {
        return business;
    }

    public Supplier pickRandomSupplier() {
        if (suppliers.size() == 0)
            return null;
        Random r = new Random();
        int randomIndex = r.nextInt(suppliers.size());
        return suppliers.get(randomIndex);
    }

    public void printShortInfo() {
        System.out.println("\nChecking what's inside the supplier directory.");
        System.out.println("\nThere are " + suppliers.size() + " suppliers.");
        for (Supplier s : suppliers) {
            s.printShortInfo();
        }
    }

}