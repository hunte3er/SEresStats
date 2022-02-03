/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Blueprints.Blueprint;
import Blueprints.Item;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class BlueprintUtils {
    public static Blueprint findBySubtypeId(Collection<Blueprint> listBP, String subtypeId) {
        return FindUtils.findByProperty(listBP, bP -> subtypeId.equals(bP.getSubtypeId()));
    }
        
    public static Blueprint findByInputIngredientCount(Collection<Blueprint> listBP, int amount) {
        return FindUtils.findByProperty(listBP, bP -> amount == bP.getInputNumber());
    }
    
    public static Blueprint findByOutputIngredientCount(Collection<Blueprint> listBP, int amount) {
        return FindUtils.findByProperty(listBP, bP -> amount == bP.getOutputNumber());
    }
    
    public static List<Blueprint> findAllByInputIngredientCount(Collection<Blueprint> listBP, int amount) {
        return FindUtils.findAllByProperty(listBP, bP -> amount == bP.getInputNumber());
    }
     
    public static List<Blueprint> findAllByOutputIngredientCount(Collection<Blueprint> listBP, int amount) {
        return FindUtils.findAllByProperty(listBP, bP -> amount == bP.getOutputNumber());
    }
        
    public static List<Blueprint> findAllByOutputItem(Collection<Blueprint> listBP, Item item) {
        return FindUtils.findAllByProperty(listBP, bP -> item == (bP.getOutput().get(0).getItem()));
    }
        
    public static List<Blueprint> findAllByInputItem(Collection<Blueprint> listBP, Item item) {
        return FindUtils.findAllByProperty(listBP, bP -> item == (bP.getInput().get(0).getItem()));
    }
}
