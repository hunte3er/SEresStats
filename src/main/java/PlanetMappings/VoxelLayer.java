/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlanetMappings;

import VoxelMaterial.VoxelMaterial;

/**
 *
 * @author eiker
 */
public class VoxelLayer {
    private float start;
    private float end;
    private VoxelMaterial voxelMaterial;

    public VoxelLayer(float start, float end, VoxelMaterial voxelMaterial) {
        this.start = start;
        this.end = end;
        this.voxelMaterial = voxelMaterial;
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public VoxelMaterial getVoxelMaterial() {
        return voxelMaterial;
    }

    public void setVoxelMaterial(VoxelMaterial voxelMaterial) {
        this.voxelMaterial = voxelMaterial;
    }

    @Override
    public String toString() {
        return start + " - " + end + ": " + voxelMaterial.getSubtypeId();
    }
}