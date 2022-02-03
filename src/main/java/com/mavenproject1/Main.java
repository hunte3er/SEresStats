/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject1;

import Blueprints.Blueprint;
import Blueprints.Item;
import OreAnalyzer.OreAnalyzerAsteroids;
import OreAnalyzer.OreStatistics;
import OreAnalyzer.OreToIngotConvertor;
import OreDefinitionReader.OreDefinition;
import PlanetMappings.PlanetModel;
import SbcReader.SbcReader;
import VoxelMaterial.VoxelMaterial;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author eiker
 */
public class Main {
    public static void main(String[] args) throws IOException, Exception {

        String path1 = "F:\\SteamLibrary\\steamapps\\workshop\\content\\244850\\1759275190\\Data\\";
        String path2 = "F:\\SteamLibrary\\steamapps\\common\\SpaceEngineers\\Content\\Data\\";

        SbcReader sbcr = new SbcReader(path1);

        Map < String, VoxelMaterial > vmm = sbcr.getVoxelMaterials();
        Map < String, PlanetModel > pmm = sbcr.getPlanetModels();
        List < Blueprint > bpl = sbcr.getBlueprints();
        OreDefinition oreDef = sbcr.getOreDefinition();

//        OreAnalyzer oA = new OreAnalyzer(oreDef, vmm, pmm);
        OreAnalyzerAsteroids oA = new OreAnalyzerAsteroids(1279150382225417.567624646425863 ,vmm);
        OreStatistics oreStats = oA.getOreStatistics();
        OreToIngotConvertor otic = new OreToIngotConvertor(oreStats, bpl, vmm);
        Map < Item, Double > ingotCount = otic.getIngotCountTotal();
        
        
        System.out.println(oreStats.toString());
        System.out.println("-------------------------------");
        System.out.println(otic.getIngotTotalYield());

        Map < Item, Double > overall = otic.getIngotCountTotal();
        Map < Item, Double > percent = otic.getIngotCountPercent();
        Map < Item, Double > iu = otic.getIngotCountIronValues();

        System.out.println("-------------------------------");

        for (Map.Entry < Item, Double > entry: overall.entrySet()) {
            System.out.println(entry.getKey().getSubtypeId() + "-" + entry.getKey().getTypeId() + ":\tO:" + entry.getValue() + "\tP:" + percent.get(entry.getKey()) + "\tIU:" + iu.get(entry.getKey()));
        }

        for (Map.Entry < Item, Double > ingot: ingotCount.entrySet()) {
            System.out.println(ingot.getKey().getSubtypeId() + "-" + ingot.getKey().getTypeId() + " x " + ingot.getValue());
        }
//        
//        IngotToComponentConverter itcc = new IngotToComponentConverter(iu, bpl);
//        
//        Map<Item, Double> compCost = itcc.getComponentIronCost();
//        for(Map.Entry<Item, Double> entry: compCost.entrySet())
//            System.out.println(entry.getKey().getSubtypeId() + "\t" + entry.getKey().getTypeId() + "\t" + entry.getValue());
    }

}