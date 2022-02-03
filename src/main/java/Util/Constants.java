/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.HashMap;

/**
 *
 * @author eiker
 */
public final class Constants {
    public static final String[] BIOMEMAP_SIDES = {
        "up_mat.png",
        "left_mat.png",
        "front_mat.png",
        "right_mat.png",
        "back_mat.png",
        "down_mat.png"
    };

    public static final String[] HEIGHTMAP_SIDES = {
        "up.png",
        "left.png",
        "front.png",
        "right.png",
        "back.png",
        "down.png"
    };

    public static final HashMap < String, Integer > PLANET_RADIANS = new HashMap();
    static {
        PLANET_RADIANS.put("EarthLike", 60000);
        PLANET_RADIANS.put("Mars", 60000);
        PLANET_RADIANS.put("Alien", 60000);
        PLANET_RADIANS.put("Triton", 40125);
        PLANET_RADIANS.put("Pertam", 30000);
        PLANET_RADIANS.put("Moon", 9500);
        PLANET_RADIANS.put("Europa", 9500);
        PLANET_RADIANS.put("Titan", 9500);
    }

    public static final String IMAGE_FOLDER = "PlanetDataFiles\\";

    public static final float LITER_PER_KG = 0.37f;
    public static final String ORE_TYPE = "Ore";
    public static final String INGOT_TYPE = "Ingot";

    public static final String[] EXCLUDES = {
        "EarthLikeTutorial",
        "MarsTutorial",
        "MoonTutorial",
        "SystemTestMap",
        "EarthLikeModExample"
    };
    public static final String XPATH_PLANET_GENERATOR_DEF = "//Definitions/Definition | //PlanetGeneratorDefinitions/PlanetGeneratorDefinition";
    public static final String XPATH_SUBTYPE_ID = "./Id/SubtypeId";
    public static final String XPATH_ORE = "./OreMappings/Ore";
    public static final String XPATH_MATERIAL = "./CustomMaterialTable/Material";
    public static final String XPATH_COMPLEX = "./ComplexMaterials/MaterialGroup";
    public static final String PATH_SEPERATOR = "\\";
    
    public static final String[] BLUEPRINT_BACKLIST = {
        "X10",
        "Disassembly",
        "Disassambly",
        "Disassmebly",
        "Disassebly",
        "Adv_",
        "Eli_",
        "Refill",
        "Demolished",
        "Depleted",
        "toGas",
        "Conversion"
    };
}