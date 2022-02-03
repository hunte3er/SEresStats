/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import VoxelMaterial.VoxelMaterial;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author eiker
 */
public class OreStatisticsPlanet {
    private final Map < VoxelMaterial, OreStatisticsSingle > oreStatistics;

    public OreStatisticsPlanet() {
        this.oreStatistics = new TreeMap();
    }

    public void addOre(VoxelMaterial type, double kgPerLiter, double yield) {
        if (!this.oreStatistics.containsKey(type))
            this.oreStatistics.put(type, new OreStatisticsSingle(kgPerLiter, yield));
    }

    public Set < VoxelMaterial > getOreList() {
        return this.oreStatistics.keySet();
    }

    public void setOreStatistics(VoxelMaterial type, int a, int d) {
        this.oreStatistics.get(type).addArea(a);
        this.oreStatistics.get(type).addVolume(a * d);
    }

    public OreStatisticsSingle getOreStatistics(VoxelMaterial type) {
        return this.oreStatistics.get(type);
    }
}