/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import static Util.Constants.LITER_PER_KG;
import VoxelMaterial.VoxelMaterial;
import java.util.Map;

/**
 *
 * @author eiker
 */
public class OreAnalyzerAsteroids {
    private final double baseVolume;
    private final Map < String, VoxelMaterial > voxelMaterials;

    public OreAnalyzerAsteroids(double baseVolume, Map<String, VoxelMaterial> voxelMaterials) {
        this.baseVolume = baseVolume;
        this.voxelMaterials = voxelMaterials;
    }
    
    public OreStatistics getOreStatistics(){
        OreStatistics oreStatistics  = new OreStatistics();
        
        oreStatistics.addOreStatisticsPlanet("Asteroids");
        OreStatisticsPlanet oreStatisticsAsteroids = oreStatistics.getOreStatisticsPlanet("Asteroids");
        
        double currentVolume = this.baseVolume;
        
        for(Map.Entry < String, VoxelMaterial > entry : voxelMaterials.entrySet()){
            if(entry.getValue().getAsteroidProbability() > 0){
                double oreVolume = this.baseVolume * (entry.getValue().getAsteroidProbability() / 100);
                currentVolume -= oreVolume;
                oreStatisticsAsteroids.addOre(entry.getValue(), LITER_PER_KG, entry.getValue().getMinedOreRatio());
                oreStatisticsAsteroids.getOreStatistics(entry.getValue()).addArea(0);
                oreStatisticsAsteroids.getOreStatistics(entry.getValue()).addVolume(oreVolume);
            }
        }
        oreStatisticsAsteroids.addOre(this.voxelMaterials.get("Stone"), LITER_PER_KG, this.voxelMaterials.get("Stone").getMinedOreRatio());
        oreStatisticsAsteroids.getOreStatistics(this.voxelMaterials.get("Stone")).addVolume(currentVolume);
        
        return oreStatistics;
    }
}
