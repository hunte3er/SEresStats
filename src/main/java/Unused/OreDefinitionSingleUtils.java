/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import OreDefinitionReader.OreDefinitionSingle;
import Util.FindUtils;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class OreDefinitionSingleUtils {
    public static List<OreDefinitionSingle> findAllByValue(Collection<OreDefinitionSingle> listOD, int value) {
        return FindUtils.findAllByProperty(listOD, od -> od.getColor() == value);
    }
}
