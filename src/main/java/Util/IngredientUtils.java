/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Blueprints.Ingredient;
import Blueprints.Item;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class IngredientUtils {
    public static List<Ingredient> findAllByItemTypeId(Collection<Ingredient> listBP, String typeId) {
        return FindUtils.findAllByProperty(listBP, bP -> typeId.equals(bP.getItem().getTypeId()));
    }
         
    public static List<Ingredient> findAllByItemSubtypeId(Collection<Ingredient> listBP, String subtypeId) {
        return FindUtils.findAllByProperty(listBP, bP -> subtypeId.equals(bP.getItem().getSubtypeId()));
    } 
    
    public static List<Ingredient> findAllByItem(Collection<Ingredient> listBP, Item item) {
        return FindUtils.findAllByProperty(listBP, bP -> item.equals(bP.getItem()));
    }
}
