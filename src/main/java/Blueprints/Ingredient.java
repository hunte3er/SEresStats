/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Blueprints;

/**
 *
 * @author eiker
 */
public class Ingredient {
    private final Item item;
    private final double amount;
    
    public Ingredient(Item item, double amount){
        this.item = item;
        this.amount = amount;
    }

    public Item getItem() {
        return this.item;
    }

    public double getAmount() {
        return amount;
    } 
}
