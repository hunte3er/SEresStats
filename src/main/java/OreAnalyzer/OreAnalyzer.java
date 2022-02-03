/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

import OreDefinitionReader.ComplexMaterialRule;
import OreDefinitionReader.ComplexMaterialGroup;
import VoxelMaterial.VoxelMaterial;
import static Util.Constants.*;
import OreDefinitionReader.OreDefinition;
import Util.ComplexMaterialRuleUtils;
import OreDefinitionReader.OreDefinitionPlanet;
import OreDefinitionReader.OreDefinitionSingle;
import PlanetMappings.PointModel;
import PlanetMappings.PlanetModel;
import PlanetMappings.VoxelLayer;
import PlanetMappings.VoxelNeedle;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eiker
 */
public class OreAnalyzer {
    private final OreDefinition oreDefinition;
    private final Map < String, VoxelMaterial > voxelMaterialsBySubtypeId;
    private final Map < String, PlanetModel > planetModels;

    public OreAnalyzer(OreDefinition oreDefinition, Map < String, VoxelMaterial > voxelMaterialsBySubtypeId, Map < String, PlanetModel > planetModels) {
        this.oreDefinition = oreDefinition;
        this.voxelMaterialsBySubtypeId = voxelMaterialsBySubtypeId;
        this.planetModels = planetModels;
    }

    public OreStatistics getOreStatistics() throws IOException {
        OreStatistics oreStatistics = new OreStatistics();

        this.oreDefinition.getPlanetList().forEach(planetName -> {
            PlanetModel planetModel = planetModels.get(planetName);

            oreStatistics.addOreStatisticsPlanet(planetName);
            OreStatisticsPlanet oreStatisticsPlanet = oreStatistics.getOreStatisticsPlanet(planetName);
            OreDefinitionPlanet oreDefinitionPlanet = this.oreDefinition.getOreDefinitionPlanet(planetName);
            Map < Integer, OreDefinitionSingle > oreDefinitionsByValue = oreDefinitionPlanet.getOreDefinitionsByValue();
            Map < Integer, OreDefinitionSingle > materialDefinitionsByValue = oreDefinitionPlanet.getMaterialDefinitionsByValue();
            Map < Integer, ComplexMaterialGroup > complexMaterialGroupsByValue = oreDefinitionPlanet.getComplexMaterialDefinitions();

            for (int i = 0; i < planetModel.getPointCount(); i++) {
                PointModel pointModel = planetModel.getPointModel(i);
                
                
                float height = (pointModel.getHeight() / 255f) * oreDefinitionPlanet.getHeightRange() + oreDefinitionPlanet.getRadius();
                VoxelNeedle voxelNeedle = new VoxelNeedle();
                voxelNeedle.addVoxelLayer(height, (float)(height - 0.001 * oreDefinitionPlanet.getRadius()), oreDefinitionPlanet.getDefaultSubsurfaceMaterial());
                VoxelLayer dsm = oreDefinitionPlanet.getDefaultSurfaceMaterial();
                voxelNeedle.addVoxelLayer(height, dsm.getEnd(), dsm.getVoxelMaterial());
                ComplexMaterialGroup complexMaterialGroup = complexMaterialGroupsByValue.get(pointModel.getGreen());
                if (complexMaterialGroup != null) {
                    List < ComplexMaterialRule > CompMatRulesHeightSlopeLatMatches = ComplexMaterialRuleUtils.findAllByHSL(complexMaterialGroup.getComplexMaterials(), pointModel.getHeight(), pointModel.getSlope(), pointModel.getLatitude());
                    CompMatRulesHeightSlopeLatMatches.get(0).getLayers().entrySet().forEach(entry -> {
                        voxelNeedle.addVoxelLayer(height, entry.getValue(), entry.getKey());
                    });
                }
                OreDefinitionSingle blue = oreDefinitionsByValue.get(pointModel.getBlue());
                if (blue != null)
                    voxelNeedle.addVoxelLayer(height - blue.getStart(), blue.getDepth(), this.voxelMaterialsBySubtypeId.get(blue.getSubtypeId()));
                OreDefinitionSingle red = materialDefinitionsByValue.get(pointModel.getRed());
                if (red != null)
                    voxelNeedle.addVoxelLayer(height - red.getStart(), red.getDepth(), this.voxelMaterialsBySubtypeId.get(red.getSubtypeId()));
                
                
                voxelNeedle.getVoxelLayers().forEach((VoxelLayer voxelLayer) -> {
                    float radiusOuter = voxelLayer.getStart();
                    float radiusInner = voxelLayer.getEnd();
                    float theta = (float) planetModel.getOneMinusCosTheta(planetModel.getResolution());
                    float area = this.getSphereMantleArea(theta, radiusOuter);
                    float volume = this.getSphereConeCutSlice(theta, radiusOuter, radiusInner) * 1000;
                    VoxelMaterial found = voxelLayer.getVoxelMaterial();
                    if (found != null) {
                        float minedOreRatio = (float) found.getMinedOreRatio();
                        oreStatisticsPlanet.addOre(found, LITER_PER_KG, minedOreRatio);
                        oreStatisticsPlanet.getOreStatistics(found).addArea(area);
                        oreStatisticsPlanet.getOreStatistics(found).addVolume(volume);
                    }
                });
            }
        });
        return oreStatistics;
    }

    public float getSphereConeCutSlice(float angle, float r1, float r2) {
        return (float)((2 * Math.PI / 3) * Math.pow(r1, 3) * angle - (2 * Math.PI / 3 * Math.pow(r2, 3) * angle));
    }

    public float getSphereMantleArea(float angle, float r) {
        return (float)(angle * 2 * Math.PI * r * r);
    }
}