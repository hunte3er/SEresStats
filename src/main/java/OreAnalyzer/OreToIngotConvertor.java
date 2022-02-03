/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import Blueprints.Blueprint;
import Blueprints.Ingredient;
import Blueprints.Item;
import VoxelMaterial.VoxelMaterial;
import Util.BlueprintUtils;
import Util.IngredientUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author eiker
 */
public class OreToIngotConvertor {
    private final OreStatistics oreStatistics;
    private final List < Blueprint > blueprints;
    private Map < Item, Double > IngotCountTotal;
    private final Map < Item, Double > IngotCountPercent, IngotCountIronValues;
    private final Map < String, VoxelMaterial > voxelMaterials;
    private final Map < Item, List < Blueprint >> blueprintstByOre;
    private List < Blueprint > blueprintsSingleIngredient;
    private double IngotTotalYield;

    public OreToIngotConvertor(OreStatistics oreStatistics, List < Blueprint > blueprints, Map < String, VoxelMaterial > voxelMaterials) {
        this.oreStatistics = oreStatistics;
        this.blueprints = blueprints;
        this.IngotCountTotal = new TreeMap();
        this.IngotCountPercent = new TreeMap();
        this.voxelMaterials = voxelMaterials;
        this.blueprintsSingleIngredient = new ArrayList();
        this.blueprintstByOre = new TreeMap();
        this.IngotTotalYield = 0d;
        this.IngotCountIronValues = new TreeMap();
    }

    public Map < Item, Double > getIngotCountTotal() {
        if (this.IngotCountTotal.isEmpty()) {
            this.IngotCountTotal = this.getIngotCount(oreStatistics.getOverallYield());
        }
        return this.IngotCountTotal;
    }

    private Map < Item, Double > getIngotCount(Map < VoxelMaterial, Double > oreStats) {
        if (this.IngotCountTotal.isEmpty()) {
            for(Map.Entry<VoxelMaterial, Double> oStat : oreStats.entrySet()){
                Item ore = oStat.getKey().getMinedOre();

                getSingleIngredientBlueprints(ore);
                for(Blueprint bp: this.blueprintsSingleIngredient){
                    for(Ingredient i : bp.getOutput()){
                        if(!IngredientUtils.findAllByItemTypeId(bp.getOutput(), "Ore").isEmpty() || !IngredientUtils.findAllByItemTypeId(bp.getOutput(), "Ingot").isEmpty()){
                            if (!this.IngotCountTotal.containsKey(i.getItem()))
                                this.IngotCountTotal.put(i.getItem(), oStat.getValue() * (i.getAmount() / bp.getInput().get(0).getAmount()));
                            else
                                this.IngotCountTotal.put(i.getItem(), this.IngotCountTotal.get(i.getItem()) + oStat.getValue() * (i.getAmount() / bp.getInput().get(0).getAmount()));
                        }
                    }
                }
            }
        }
        return this.IngotCountTotal;
    }

    private List < Blueprint > getSingleIngredientBlueprints(Item ore) {
        getBlueprintsSingleIngredient();
        if (!this.blueprintstByOre.containsKey(ore)) {
            List < Blueprint > lIron = new ArrayList();
            this.blueprintsSingleIngredient.stream().filter(bp -> (!bp.getInputsByItem(ore).isEmpty() && !bp.getInputsByType("Ore").isEmpty())).forEachOrdered(bp -> {
                lIron.add(bp);
            });
            this.blueprintstByOre.put(ore, lIron);
        }
        return this.blueprintstByOre.get(ore);
    }

    private List < Blueprint > getBlueprintsSingleIngredient() {
        if (this.blueprintsSingleIngredient.isEmpty()) {
            this.blueprintsSingleIngredient = BlueprintUtils.findAllByInputIngredientCount(this.blueprints, 1);
        }
        return this.blueprintsSingleIngredient;
    }

    public Map < Item, Double > getIngotCountPlanet(String planet) {
        return this.getIngotCount(oreStatistics.getYield(planet));
    }

    public double getIngotTotalYield() {
        if (this.IngotTotalYield == 0d) {
            getIngotCountTotal();
            this.IngotCountTotal.entrySet().forEach(entry -> {
                this.IngotTotalYield += entry.getValue();
            });
        }
        return this.IngotTotalYield;
    }

    public Map < Item, Double > getIngotCountPercent() {
        if (this.IngotCountPercent.isEmpty()) {
            this.getIngotTotalYield();
            this.IngotCountTotal.entrySet().forEach(entry -> {
                this.IngotCountPercent.put(entry.getKey(), entry.getValue() / this.IngotTotalYield * 100);
            });
        }
        return this.IngotCountPercent;
    }

    public Map < Item, Double > getIngotCountIronValues() {
        if (this.IngotCountIronValues.isEmpty()) {
            this.getIngotTotalYield();
            Item ironIng = new Item("Iron", "Ingot");
            this.IngotCountTotal.entrySet().forEach(entry -> {
                this.IngotCountIronValues.put(entry.getKey(), this.IngotCountTotal.get(ironIng) / entry.getValue());
            });
//            for(Map.Entry<Item, Double> entry : this.IngotCountTotal.entrySet()){
//                List<Blueprint> bpl = BlueprintUtils.findAllByOutputItem(this.blueprints, entry.getKey());
//                for(Blueprint bp : bpl){
//                    if(!this.IngotCountIronValues.containsKey(bp.getInput().get(0).getItem())){
//                        this.IngotCountIronValues.put(bp.getInput().get(0).getItem(), this.IngotCountIronValues.get(bp.getOutput().get(0).getItem()) / bp.getOutput().get(0).getAmount() * bp.getInput().get(0).getAmount());
//                    }
//                }
//            }
        }
        return this.IngotCountIronValues;
    }
}