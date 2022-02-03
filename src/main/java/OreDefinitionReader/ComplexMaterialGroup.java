/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreDefinitionReader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eiker
 */
public class ComplexMaterialGroup {
    private final String name;
    private final int value;
    private final List < ComplexMaterialRule > complexMaterials;

    public ComplexMaterialGroup(String name, int value) {
        this.name = name;
        this.value = value;
        this.complexMaterials = new ArrayList();
    }

    public void addComplexMaterial(ComplexMaterialRule compMat) {
        this.complexMaterials.add(compMat);
    }

    public void addComplexMaterials(List < ComplexMaterialRule > compMat) {
        this.complexMaterials.addAll(compMat);
    }

    public List < ComplexMaterialRule > getComplexMaterials() {
        return this.complexMaterials;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}