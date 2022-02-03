/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoxelMaterial;

import Blueprints.Item;
import java.util.Objects;

/**
 *
 * @author eiker
 */
public class VoxelMaterial implements Comparable<VoxelMaterial>{
    private String subtypeId;
    private Item minedOre;
    private double minedOreRatio;
    private double asteroidProbability;

    public VoxelMaterial(String subtypeId, Item minedOre, double minedOreRatio, double asteroidProbability) {
        this.subtypeId = subtypeId;
        this.minedOre = minedOre;
        this.minedOreRatio = minedOreRatio;
        this.asteroidProbability = asteroidProbability;
    }
    
    public double getAsteroidProbability(){
        return this.asteroidProbability;
    }

    public void setAsteroidProbability(double asteroidProbability) {
        this.asteroidProbability = asteroidProbability;
    }
    
    public String getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(String subtypeId) {
        this.subtypeId = subtypeId;
    }

    public Item getMinedOre() {
        return minedOre;
    }

    public void setMinedOre(Item minedOre) {
        this.minedOre = minedOre;
    }

    public double getMinedOreRatio() {
        return minedOreRatio;
    }

    public void setMinedOreRatio(double minedOreRatio) {
        this.minedOreRatio = minedOreRatio;
    }

    @Override
    public String toString() {
        return this.subtypeId + ": " + this.minedOre + " x " + this.minedOreRatio;
    }    

    @Override
    public int hashCode() {
        return Objects.hash(subtypeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VoxelMaterial other = (VoxelMaterial) obj;
        if (Double.doubleToLongBits(this.minedOreRatio) != Double.doubleToLongBits(other.minedOreRatio)) {
            return false;
        }
        if (Double.doubleToLongBits(this.asteroidProbability) != Double.doubleToLongBits(other.asteroidProbability)) {
            return false;
        }
        if (!Objects.equals(this.subtypeId, other.subtypeId)) {
            return false;
        }
        if (!Objects.equals(this.minedOre, other.minedOre)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(VoxelMaterial o) {
        return this.getSubtypeId().compareTo(o.getSubtypeId());
    }
}