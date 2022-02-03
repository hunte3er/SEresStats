/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreDefinitionReader;

import PlanetMappings.VoxelLayer;
import static Util.Constants.PLANET_RADIANS;
import VoxelMaterial.VoxelMaterial;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eiker
 */
public class OreDefinitionPlanet {
    private final String subtypeId;
    private final Map < String, OreDefinitionSingle > oreDefinitions;
    private final Map < String, OreDefinitionSingle > materialDefinitions;
    private final Map < Integer, ComplexMaterialGroup > complexMaterialDefinitions;
    private VoxelMaterial defaultSubsurfaceMaterial;
    private VoxelLayer defaultSurfaceMaterial;
    private float hillMin = 0f;
    private float hillMax = 0f;
    private final int radius;

    public OreDefinitionPlanet(String subtypeId) {
        this.subtypeId = subtypeId;
        this.oreDefinitions = new HashMap();
        this.materialDefinitions = new HashMap();
        this.complexMaterialDefinitions = new HashMap();
        this.radius = PLANET_RADIANS.get(this.subtypeId);
    }

    public Map < Integer, OreDefinitionSingle > getOreDefinitionsByValue() {
        Map < Integer, OreDefinitionSingle > result = new HashMap();
        this.oreDefinitions.entrySet().forEach(entry -> {
            result.put(entry.getValue().getColor(), entry.getValue());
        });
        return result;
    }

    public Map < Integer, OreDefinitionSingle > getMaterialDefinitionsByValue() {
        Map < Integer, OreDefinitionSingle > result = new HashMap();
        this.materialDefinitions.entrySet().forEach(entry -> {
            result.put(entry.getValue().getColor(), entry.getValue());
        });
        return result;
    }

    public void addOreDefinition(String type, int value, int depth, int start) {
        if (!this.oreDefinitions.containsKey(type))
            this.oreDefinitions.put(type, new OreDefinitionSingle(value, start, depth, type));
    }

    public void addMaterialDefinition(String type, int value, int depth, int start) {
        if (!this.materialDefinitions.containsKey(type))
            this.materialDefinitions.put(type, new OreDefinitionSingle(value, start, depth, type));
    }

    public void addComplexMaterialDefinition(int value, ComplexMaterialGroup complexMaterial) {
        this.complexMaterialDefinitions.put(value, complexMaterial);
    }

    public void addOreDefinitions(Map < String, OreDefinitionSingle > oreDefs) {
        this.oreDefinitions.putAll(oreDefs);
    }

    public void addMaterialDefinitions(Map < String, OreDefinitionSingle > matDefs) {
        matDefs.entrySet().forEach(matDef -> {
            addMaterialDefinition(matDef.getKey(), matDef.getValue().getColor(), matDef.getValue().getDepth(), matDef.getValue().getStart());
        });
    }

    public void addComplexMaterialDefinitions(Map < Integer, ComplexMaterialGroup > CompMatDefs) {
        CompMatDefs.entrySet().forEach(matDef -> {
            addComplexMaterialDefinition(matDef.getKey(), matDef.getValue());
        });
    }

    public String getSubtypeId() {
        return subtypeId;
    }

    public Map < Integer, ComplexMaterialGroup > getComplexMaterialDefinitions() {
        return complexMaterialDefinitions;
    }

    public OreDefinitionSingle getOreDefinition(String type) {
        return this.oreDefinitions.get(type);
    }

    public OreDefinitionSingle getMaterialDefinition(String type) {
        return this.materialDefinitions.get(type);
    }

    public Set < String > getOreList() {
        return oreDefinitions.keySet();
    }

    public Set < String > getMaterialList() {
        return materialDefinitions.keySet();
    }

    public Set < Integer > getComplexMaterialList() {
        return complexMaterialDefinitions.keySet();
    }

    public float getHillMin() {
        return hillMin;
    }

    public void setHillMin(float hillMin) {
        this.hillMin = hillMin;
    }

    public float getHillMax() {
        return hillMax;
    }

    public void setHillMax(float hillMax) {
        this.hillMax = hillMax;
    }

    public float getHeightRange() {
        return (1 + this.hillMax) * this.radius - (1 + this.hillMin) * this.radius;
    }

    public int getRadius() {
        return this.radius;
    }

    public double getHullArea() {
        return 4 * Math.PI * Math.pow(this.radius, 2);
    }

    public VoxelMaterial getDefaultSubsurfaceMaterial() {
        return defaultSubsurfaceMaterial;
    }

    public void setDefaultSubsurfaceMaterial(VoxelMaterial defaultSubsurfaceMaterial) {
        this.defaultSubsurfaceMaterial = defaultSubsurfaceMaterial;
    }

    public VoxelLayer getDefaultSurfaceMaterial() {
        return defaultSurfaceMaterial;
    }

    public void setDefaultSurfaceMaterial(VoxelLayer defaultSurfaceMaterial) {
        this.defaultSurfaceMaterial = defaultSurfaceMaterial;
    }
}