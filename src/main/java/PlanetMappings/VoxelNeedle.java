/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlanetMappings;

import VoxelMaterial.VoxelMaterial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author eiker
 */
public class VoxelNeedle {
    private final Map < Float, VoxelLayer > voxelLayers;

    public VoxelNeedle() {
        this.voxelLayers = new TreeMap();
    }

    public void addVoxelLayer(float start2, float depth2, VoxelMaterial voxelMaterial2) {
        VoxelLayer add = null;
        float remove = -1f;
        if (!this.voxelLayers.isEmpty()) {
            for (Map.Entry < Float, VoxelLayer > layer: this.voxelLayers.entrySet()) {
                float start1 = layer.getValue().getStart();
                float end1 = layer.getValue().getEnd();
                float end2 = start2 - depth2;
                VoxelMaterial voxelMaterial1 = layer.getValue().getVoxelMaterial();

                if (start2 < start1 && start2 > end1) {
                    if (end2 > end1) {
                        add = new VoxelLayer(end2, end1, voxelMaterial1);
                    }
                    layer.getValue().setEnd(start2);
                }
                if (start2 >= start1 && end2 >= end1) {
                    if (end2 > end1) {
                        add = new VoxelLayer(end2, end1, voxelMaterial1);
                    }
                    remove = start1;
                }
            }
        }
        if (add != null)
            this.voxelLayers.put(add.getStart(), add);
        if (remove >= 0f)
            this.voxelLayers.remove(remove);
        this.voxelLayers.put(start2, new VoxelLayer(start2, start2 - depth2, voxelMaterial2));
    }

    public void addVoxelLayer(VoxelLayer layer) {
        addVoxelLayer(layer.getStart(), layer.getEnd(), layer.getVoxelMaterial());
    }

    public List < VoxelLayer > getVoxelLayers() {
        return new ArrayList(this.voxelLayers.values());
    }

    @Override
    public String toString() {
        String result = "";
        result = this.voxelLayers.values().stream().map(layer -> layer.toString() + "\r\n").reduce(result, String::concat);
        return result;
    }
}