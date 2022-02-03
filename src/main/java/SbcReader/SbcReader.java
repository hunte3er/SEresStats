/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SbcReader;

import Blueprints.Blueprint;
import OreDefinitionReader.OreDefinition;
import PlanetMappings.PlanetModel;
import VoxelMaterial.VoxelMaterial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eiker
 */
public class SbcReader {
    private final OreDefinition oreDefinition;
    private final List < Blueprint > blueprints;
    private final Map < String, PlanetModel > planetModels;
    private final Map < String, VoxelMaterial > voxelMaterials;
    
    
    public SbcReader(String path){
        this.blueprints = new BlueprintReader(path).getBlueprints();
        this.voxelMaterials = new VoxelMaterialReader(path).getVoxelMaterials();
        this.oreDefinition = new OreDefinitionReader(path,this.voxelMaterials).getOreDefinition();
        this.planetModels = new HashMap();
        this.loadPlanetModels(path);
    }
    
    public List < Blueprint > getBlueprints(){
        return this.blueprints;
    }
    
    public Map < String, VoxelMaterial > getVoxelMaterials(){
        return this.voxelMaterials;
    }
    
    public OreDefinition getOreDefinition(){
        return this.oreDefinition;
    }
    
    public Map < String, PlanetModel > getPlanetModels(){
        return this.planetModels;
    }
    
    private void loadPlanetModels(String path){
        this.oreDefinition.getPlanetList().forEach(planetName -> {
            this.planetModels.put(planetName, new PlanetModel(path, this.oreDefinition.getOreDefinitionPlanet(planetName)));
        });
    }
}
