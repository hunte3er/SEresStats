/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import OreDefinitionReader.ComplexMaterialRule;
import Util.FindUtils;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class ComplexMaterialRuleUtils {
    public static List<ComplexMaterialRule> findAllByHeight(Collection<ComplexMaterialRule> listCMR, double height) {
        return FindUtils.findAllByProperty(listCMR, cmr -> cmr.getHeightMin() <= height || cmr.getHeightMax() >= height);
    }
        
    public static List<ComplexMaterialRule> findAllBySlope(Collection<ComplexMaterialRule> listCMR, double slope) {
        return FindUtils.findAllByProperty(listCMR, cmr -> cmr.getSlopeMin() <= slope || cmr.getSlopeMax() >= slope);
    }    
    
    public static List<ComplexMaterialRule> findAllByLatitude(Collection<ComplexMaterialRule> listCMR, double latitude) {
        return FindUtils.findAllByProperty(listCMR, cmr -> cmr.getLatMin() <= latitude || cmr.getLatMax() >= latitude);
    }    
    
    public static List<ComplexMaterialRule> findAllByHSL(Collection<ComplexMaterialRule> listCMR, double height, double slope, double latitude) {
        return findAllByHeight(findAllBySlope(findAllByLatitude(listCMR, latitude), slope), height);
    }
}
