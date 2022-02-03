/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import Util.FindUtils;
import VoxelMaterial.VoxelMaterial;
import java.util.Collection;

/**
 *
 * @author eiker
 */
public class VoxelMaterialUtils {
    public static VoxelMaterial findBySubtypeId(Collection<VoxelMaterial> listVM, String subtypeId) {
        return FindUtils.findByProperty(listVM, vM -> subtypeId.equals(vM.getSubtypeId()));
    }

    public static VoxelMaterial findMinedOre(Collection<VoxelMaterial> listVM, String minedOre) {
        return FindUtils.findByProperty(listVM, vM -> minedOre.equals(vM.getMinedOre()));
    }

    public static VoxelMaterial findByMinedOreRatio(Collection<VoxelMaterial> listVM, double minedOreRatio) {
        return FindUtils.findByProperty(listVM, vM -> minedOreRatio == vM.getMinedOreRatio());
    }
}
