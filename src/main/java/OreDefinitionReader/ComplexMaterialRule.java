/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreDefinitionReader;

import VoxelMaterial.VoxelMaterial;
import java.util.Map;

/**
 *
 * @author eiker
 */
public class ComplexMaterialRule {
    private final Map < VoxelMaterial, Integer > layers;
    private final double heightMin;
    private final double heightMax;
    private final int latMin;
    private final int latMax;
    private final int slopeMin;
    private final int slopeMax;

    public ComplexMaterialRule(
        Map < VoxelMaterial, Integer > layers,
        double heightMin, double heightMax, int latMin, int latMax,
        int slopeMin, int slopeMax) {
        this.layers = layers;
        this.heightMin = heightMin;
        this.heightMax = heightMax;
        this.latMin = latMin;
        this.latMax = latMax;
        this.slopeMin = slopeMin;
        this.slopeMax = slopeMax;
    }

    public Map < VoxelMaterial, Integer > getLayers() {
        return layers;
    }

    public double getHeightMin() {
        return heightMin;
    }

    public double getHeightMax() {
        return heightMax;
    }

    public int getLatMin() {
        return latMin;
    }

    public int getLatMax() {
        return latMax;
    }

    public int getSlopeMin() {
        return slopeMin;
    }

    public int getSlopeMax() {
        return slopeMax;
    }
}