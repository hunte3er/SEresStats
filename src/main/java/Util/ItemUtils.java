/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Blueprints.Item;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class ItemUtils {
        public static List<Item> findAllByTypeId(Collection<Item> listItem, String typeId) {
        return FindUtils.findAllByProperty(listItem, item -> typeId.equals(item.getTypeId()));
    }
         
    public static List<Item> findAllBySubtypeId(Collection<Item> listItem, String subtypeId) {
        return FindUtils.findAllByProperty(listItem, item -> subtypeId.equals(item.getSubtypeId()));
    } 
}
