/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import OreDefinitionReader.ComplexMaterialGroup;
import Util.FindUtils;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class ComplexMaterialGroupUtils {
    public static List<ComplexMaterialGroup> findAllByValue(Collection<ComplexMaterialGroup> listBP, int value) {
        return FindUtils.findAllByProperty(listBP, bP -> bP.getValue() == value);
    }
}
