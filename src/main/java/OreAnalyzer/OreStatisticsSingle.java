/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreAnalyzer;

/**
 *
 * @author eiker
 */
public class OreStatisticsSingle {
    private double volume = 0d;
    private double area = 0d;
    private double kgPerLiter = 1 / 0.37;
    private double minedOreRatio = 0d;
    private double totalYield = 0d;

    public double getKgPerLiter() {
        return this.kgPerLiter;
    }

    public OreStatisticsSingle(double litersPerKg, double minedOreRatio) {
        this.kgPerLiter = 1 / litersPerKg;
        this.minedOreRatio = minedOreRatio;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getArea() {
        return this.area;
    }

    public void addArea(double area) {
        this.area += area;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return this.volume;
    }

    public void addVolume(double volume) {
        this.volume += volume;
    }

    public double getTotalYield() {
        if (this.totalYield == 0d) {
            if (this.volume == 0d)
                return 0d;
            else
                return this.totalYield = this.volume *
                    this.kgPerLiter *
                    this.minedOreRatio;
        } else
            return this.totalYield;
    }
}