/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlanetMappings;

/**
 *
 * @author eiker
 */
public class PointModel {
    private final int red;
    private final int green;
    private final int blue;
    private final int height;
    private final int slope;
    private final int latitude;

    public PointModel(int red, int green, int blue, int height, int slope, int latitude) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.height = height;
        this.slope = slope;
        this.latitude = latitude;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getHeight() {
        return height;
    }

    public int getSlope() {
        return slope;
    }

    public int getLatitude() {
        return latitude;
    }
}