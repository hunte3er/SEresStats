/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import VoxelMaterial.VoxelMaterial;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author eiker
 */
public class OreStatistics {
    private final Map < String, OreStatisticsPlanet > oreStatisticsPlanets;

    public OreStatistics() {
        this.oreStatisticsPlanets = new TreeMap();
    }

    public void addOreStatisticsPlanet(String subtypeId) {
        this.oreStatisticsPlanets.put(subtypeId, new OreStatisticsPlanet());
    }

    public Map < String, OreStatisticsPlanet > getOreStatisticsPlanets() {
        return this.oreStatisticsPlanets;
    }

    public OreStatisticsPlanet getOreStatisticsPlanet(String subtypeId) {
        return this.oreStatisticsPlanets.get(subtypeId);
    }

    public Set < String > getPlanetList() {
        return this.oreStatisticsPlanets.keySet();
    }

    public void addPlanet(String subtypeId) {
        this.oreStatisticsPlanets.put(subtypeId, null);
    }

    @Override
    public String toString() {
        String result = "";

        for (String planet: this.oreStatisticsPlanets.keySet()) {
            OreStatisticsPlanet p = this.oreStatisticsPlanets.get(planet);
            result += planet + "\r\n";
            for (VoxelMaterial ore: p.getOreList()) {

                OreStatisticsSingle oreStats = p.getOreStatistics(ore);
                String tab = "\t";
                if (ore.getSubtypeId().length() < 8)
                    tab = "\t\t";
                result += "\t" + ore.getMinedOre().getSubtypeId() + tab + "A:" + oreStats.getArea() + "\tV:" + oreStats.getVolume() + "\tY:" + oreStats.getTotalYield() + "\r\n";
            }
        }

        return result;
    }

    public Map < VoxelMaterial, Double > getOverallYield() {
        Map < VoxelMaterial, Double > result = new HashMap();
        this.getPlanetList().forEach(planet -> {
            this.oreStatisticsPlanets.get(planet).getOreList().forEach(ore -> {
                if (result.containsKey(ore))
                    result.put(ore, result.get(ore) + this.oreStatisticsPlanets.get(planet).getOreStatistics(ore).getTotalYield());
                else
                    result.put(ore, this.oreStatisticsPlanets.get(planet).getOreStatistics(ore).getTotalYield());
            });
        });
        return result;
    }

    public Map < VoxelMaterial, Double > getYield(String planet) {
        Map < VoxelMaterial, Double > result = new HashMap();
        this.oreStatisticsPlanets.get(planet).getOreList().forEach(ore -> {
            result.put(ore, this.oreStatisticsPlanets.get(planet).getOreStatistics(ore).getTotalYield());
        });
        return result;
    }

    public Double getTotal() {
        Map < VoxelMaterial, Double > temp = getOverallYield();
        double result = 0d;
        for (double x: temp.values())
            result = result += x;
        return result;
    }

    public Map < VoxelMaterial, Double > getPercent() {
        Map < VoxelMaterial, Double > temp = getOverallYield();
        Map < VoxelMaterial, Double > result = new HashMap();
        double total = getTotal();

        temp.entrySet().forEach(entry -> {
            result.put(entry.getKey(), entry.getValue() / total * 100);
        });

        return result;
    }

    public Map < VoxelMaterial, Double > getIronUnits(VoxelMaterial ironMat) {
        Map < VoxelMaterial, Double > temp = getOverallYield();
        Map < VoxelMaterial, Double > result = new HashMap();
        double total = temp.get(ironMat);

        temp.entrySet().forEach(entry -> {
            try {
                result.put(entry.getKey(), (total / entry.getValue()));
            } catch (ArithmeticException e) {
                result.put(entry.getKey(), 1000000000d);
            }
        });

        return result;
    }
}