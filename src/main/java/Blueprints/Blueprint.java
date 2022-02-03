/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Blueprints;

import Util.IngredientUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eiker
 */
public class Blueprint {
    private final String subtypeId;
    private final List<Ingredient> input;
    private final List<Ingredient> output;
    
    public Blueprint(String subtypeId){
        this.subtypeId = subtypeId;
        this.input = new ArrayList();
        this.output = new ArrayList();
    }
    
    public Blueprint(String subtypeId, List<Ingredient> input, List<Ingredient> output){
        this.subtypeId = subtypeId;
        this.input = input;
        this.output = output;
    }
    
    public void addInput(String typeId, String type, double amount){
        this.input.add(new Ingredient(new Item (subtypeId, type), amount));
    }
    
    public void addOutput(String typeId, String type, double amount){
        this.output.add(new Ingredient(new Item (subtypeId, type), amount));
    }
    
    @Override
    public String toString(){
        String result = "";
        
        result += this.subtypeId + "\r\n";
        for(Ingredient in: input){
            result += "\tIn: " + in.getItem().getTypeId() + " - " + in.getItem().getSubtypeId() + " x " + in.getAmount() + "\r\n";
        }
        for(Ingredient out: output){
            result += "\tOut: " + out.getItem().getTypeId() + " - " + out.getItem().getSubtypeId() + " x " + out.getAmount() + "\r\n";
        }
        
        return result;
    }

    public String getSubtypeId() {
        return subtypeId;
    }

    public List<Ingredient> getInput() {
        return input;
    }

    public List<Ingredient> getOutput() {
        return output;
    }
    
    public int getInputNumber(){
        return this.input.size();
    }
    
    public int getOutputNumber(){
        return this.output.size();
    }
    
    public List<Ingredient> getInputsByType(String type){
        return IngredientUtils.findAllByItemTypeId(this.input, type);
    }
    
    public List<Ingredient> getOutputsByType(String type){
        return IngredientUtils.findAllByItemTypeId(this.output, type);
    }
        
    public List<Ingredient> getInputsById(String type){
        return IngredientUtils.findAllByItemSubtypeId(this.input, type);
    }
    
    public List<Ingredient> getOutputsById(String type){
        return IngredientUtils.findAllByItemSubtypeId(this.output, type);
    }       
        
    public List<Ingredient> getInputsByItem(Item type){
        return IngredientUtils.findAllByItem(this.input, type);
    }
    
    public List<Ingredient> getOutputsByItem(Item type){
        return IngredientUtils.findAllByItem(this.output, type);
    }  
}
