/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import Blueprints.Blueprint;
import Blueprints.Ingredient;
import Blueprints.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author eiker
 */
public class IngotToComponentConverter {
    private final Map<Item, Double> ironUnitTable;
    private final List<Blueprint> blueprints;

    public IngotToComponentConverter(Map<Item, Double> ironUnitTable, List<Blueprint> blueprints) {
        this.ironUnitTable = ironUnitTable;
        this.blueprints = blueprints;
    }
    
    public Map<Item, Double> getComponentIronCost(){
        return getComponentIronCost(this.blueprints, this.ironUnitTable);
    }
    
    private Map<Item, Double> getComponentIronCost(List<Blueprint> missingBlueprints, Map<Item, Double> itemMap){
//        List<Blueprint> blueprintsSingleOutput = BlueprintUtils.findAllByOutputIngredientCount(this.blueprints, 1);
        Map<Item, Double> result = new TreeMap();
        if(!missingBlueprints.isEmpty()){
            List<Blueprint> missing = new ArrayList();
            result.putAll(itemMap);
            for(Blueprint blueprint : missingBlueprints/*SingleOutput*/){
                if(!blueprint.getSubtypeId().contains("X10") && !blueprint.getSubtypeId().contains("Disassembly") && !blueprint.getSubtypeId().contains("Adv_") && !blueprint.getSubtypeId().contains("Eli_")){
    //                List<Ingredient> test = IngredientUtils.findAllByItemTypeId(blueprint.getInput(), "Component");
                    boolean isKnown = true;
                    boolean alreadyDone = false;
                    for(Ingredient ingredientInput : blueprint.getInput()){
                        if(!result.containsKey(ingredientInput.getItem())){
                            isKnown = false;
                            break;
                        }
                    }
                    for(Ingredient ingredientOutput : blueprint.getOutput()){
                        if(result.containsKey(ingredientOutput.getItem()) || ingredientOutput.getItem().getSubtypeId().equals("ZoneChip")){
                            alreadyDone = true;
                            System.out.println(blueprint.getSubtypeId());
                            break;
                        }
                    }
                    if(isKnown == true && alreadyDone == false){
                        for(Ingredient ingredientInput : blueprint.getInput()){
    //                        if(!test.isEmpty())
    //                            continue;
                            List<Ingredient> ingredientsOutput = blueprint.getOutput();
                            for(Ingredient ingredientOutput : ingredientsOutput){
                                if(!result.containsKey(ingredientOutput.getItem())){
                                    result.put(ingredientOutput.getItem(), result.get(ingredientInput.getItem()) * ingredientInput.getAmount() / ingredientOutput.getAmount());
                                }
                                else{
                                    result.put(ingredientOutput.getItem(), result.get(ingredientOutput.getItem()) + result.get(ingredientInput.getItem()) * ingredientInput.getAmount() / ingredientOutput.getAmount());
                                }
                            }
                        }
                    }
                    else if(isKnown == false && alreadyDone == false)
                        missing.add(blueprint);
                }
            }
            result.putAll(getComponentIronCost(missing, result));
        }
//        for(Blueprint blueprint : blueprintsSingleOutput){
//            if(!blueprint.getSubtypeId().contains("X10")){
//                List<Ingredient> test = IngredientUtils.findAllByItemTypeId(blueprint.getInput(), "Component");
//                for(Ingredient ingredientInput : blueprint.getInput()){
//                    if(result.containsKey(ingredientInput.getItem())){
//                        System.out.println("---------------------Do we get here?");
//                        if(test.isEmpty())
//                            continue;
//                        if(result.containsKey(ingredientInput.getItem())){
//                            Ingredient ingredientOutput = blueprint.getOutput().get(0);
//                            if(!result.containsKey(ingredientOutput.getItem())){
//                                result.put(ingredientOutput.getItem(), result.get(ingredientInput.getItem()) * ingredientInput.getAmount() / ingredientOutput.getAmount());
//                            }
//                            else{
//                                result.put(ingredientOutput.getItem(), result.get(ingredientOutput.getItem()) + result.get(ingredientInput.getItem()) * ingredientInput.getAmount() / ingredientOutput.getAmount());
//                            }
//                        }
//                    }
//                }
//            }
//        }
        return result;
    }
}
