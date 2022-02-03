/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlanetMappings;

import OreDefinitionReader.OreDefinitionPlanet;
import static Util.Constants.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author eiker
 */
public final class PlanetModel {
    private final Map < FACES, BufferedImage > cubeFacesHeight;
    private final Map < FACES, BufferedImage > cubeFacesColor;
    private final LatitudeMaps latitudeMaps;
    private final String path;
    private final OreDefinitionPlanet oreDefinitionPlanet;

    public PlanetModel(String path, OreDefinitionPlanet oreDefinitionPlanet) {
        this.cubeFacesHeight = new HashMap();
        this.cubeFacesColor = new HashMap();
        this.latitudeMaps = new LatitudeMaps();
        this.oreDefinitionPlanet = oreDefinitionPlanet;
        this.path = path;
        this.loadImages();
    }

    public enum FACES {
        UP,
        FRONT,
        RIGHT,
        BACK,
        LEFT,
        DOWN
    }

    private void loadImages() {
        try {
            for (String hFace: HEIGHTMAP_SIDES) {
                this.cubeFacesHeight.put(decideName(hFace), ImageIO.read(new File(path + IMAGE_FOLDER + PATH_SEPERATOR + this.oreDefinitionPlanet.getSubtypeId() + PATH_SEPERATOR + hFace)));
            }
            for (String mFace: BIOMEMAP_SIDES) {
                this.cubeFacesColor.put(decideName(mFace), ImageIO.read(new File(path + IMAGE_FOLDER + PATH_SEPERATOR + this.oreDefinitionPlanet.getSubtypeId() + PATH_SEPERATOR + mFace)));
            }
        } catch (IOException ex) {
            Logger.getLogger(PlanetModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FACES decideName(String name) {
        if (name.toLowerCase().contains("up"))
            return FACES.UP;
        else if (name.toLowerCase().contains("front"))
            return FACES.FRONT;
        else if (name.toLowerCase().contains("right"))
            return FACES.RIGHT;
        else if (name.toLowerCase().contains("back"))
            return FACES.BACK;
        else if (name.toLowerCase().contains("left"))
            return FACES.LEFT;
        else if (name.toLowerCase().contains("down"))
            return FACES.DOWN;
        else
            return null;
    }

    private double calculateGaussianSteepness(int up, int down, int right, int left) {
        double dx = (right - left) / 2;
        double dy = (down - up) / 2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double calculateSobelSteepness(int s, int r, int d) {
        double dx = r - s;
        double dy = d - s;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getSteepness(FACES face, int x, int y) {
        return switch (face) {
        case UP -> this.getSteepnessTop(x, y);
        case FRONT -> this.getSteepnessFront(x, y);
        case RIGHT -> this.getSteepnessRight(x, y);
        case BACK -> this.getSteepnessBack(x, y);
        case LEFT -> this.getSteepnessLeft(x, y);
        case DOWN -> this.getSteepnessBottom(x, y);
        default -> this.getSteepnessFront(x, y);
        };
    }

    public double getSteepness(FACES face, int x, int width, boolean b) {
        return getSteepness(face, x % width, x / width);
    }

    public double getSteepnessLeft(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.RIGHT).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y, 0)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(x, max)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y, max)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(x, 0)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getSteepnessFront(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.RIGHT).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y, 0)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(max, max - x)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y, max)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(max, x)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getSteepnessRight(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.RIGHT).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y, 0)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(max - x, 0)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(y, max)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(max - x, 0)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getSteepnessBack(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.BACK).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y, 0)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(0, x)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(y, max)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(0, max - x)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getSteepnessTop(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.UP).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(0, max - y)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(0, x)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(0, y)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.UP).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(0, max - x)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getSteepnessBottom(int x, int y) {
        int max = this.cubeFacesHeight.get(FACES.DOWN).getHeight() - 1, r = 0, d = 0, l = 0, u = 0;
        if (x < max)
            r = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(y, x + 1)).getRed();
        else if (x == max)
            r = new Color(this.cubeFacesHeight.get(FACES.LEFT).getRGB(y, max)).getRed();
        if (y < max)
            d = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(y + 1, x)).getRed();
        else if (y == max)
            d = new Color(this.cubeFacesHeight.get(FACES.FRONT).getRGB(max - x, max)).getRed();
        if (x > 0)
            l = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(y, x - 1)).getRed();
        if (x == 0)
            l = new Color(this.cubeFacesHeight.get(FACES.RIGHT).getRGB(max - y, max)).getRed();
        if (y > 0)
            u = new Color(this.cubeFacesHeight.get(FACES.DOWN).getRGB(y - 1, x)).getRed();
        if (y == 0)
            u = new Color(this.cubeFacesHeight.get(FACES.BACK).getRGB(max, x)).getRed();
        return calculateGaussianSteepness(u, d, r, l);
    }

    public double getOneMinusCosTheta(int imageWidth) {
        double r = Math.sqrt((6 * imageWidth * imageWidth) / (4 * Math.PI));
        return 1 / (2 * Math.PI * r * r);
    }

    public int getResolution() {
        return this.cubeFacesColor.get(FACES.UP).getHeight();
    }

    public PointModel getPointModel(int count) {
        int newCount = reduceCount(count);
        int iWidth = this.cubeFacesColor.get(decideFace(count)).getWidth();
        Color color = new Color(this.cubeFacesColor.get(decideFace(count)).getRGB(newCount % iWidth, newCount / iWidth));
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int res = this.cubeFacesColor.get(decideFace(count)).getHeight() * this.cubeFacesColor.get(decideFace(count)).getWidth();
        float width = (float) Math.sqrt(oreDefinitionPlanet.getHullArea() / 6 * res);
        int height = new Color(this.cubeFacesHeight.get(decideFace(count)).getRGB(newCount % iWidth, newCount / iWidth)).getRed();
        int slope = (int)(Math.toDegrees(Math.atan(this.getSteepness(decideFace(count), newCount, iWidth, true) / 255d * oreDefinitionPlanet.getHeightRange() / width)));
        int latitude = (int)(this.latitudeMaps.get(iWidth, count) / 255f * 90f);
        return new PointModel(red,
            green,
            blue,
            height,
            slope,
            latitude
        );
    }

    public int getPointCount() {
        return this.cubeFacesColor.get(FACES.BACK).getHeight() * this.cubeFacesColor.get(FACES.BACK).getWidth() * 6;
    }

    private FACES decideFace(int x) {
        int up = this.cubeFacesColor.get(FACES.UP).getHeight() * this.cubeFacesColor.get(FACES.UP).getWidth();
        int front = 2 * up;
        int right = 3 * up;
        int back = 4 * up;
        int left = 5 * up;
        int down = 6 * up;

        if (x < up)
            return FACES.UP;
        else if (x < front)
            return FACES.FRONT;
        else if (x < right)
            return FACES.RIGHT;
        else if (x < back)
            return FACES.BACK;
        else if (x < left)
            return FACES.LEFT;
        else if (x < down)
            return FACES.DOWN;
        else
            return null;
    }

    private int reduceCount(int x) {
        int up = this.cubeFacesColor.get(FACES.UP).getHeight() * this.cubeFacesColor.get(FACES.UP).getWidth();
        int front = 2 * up;
        int right = 3 * up;
        int back = 4 * up;
        int left = 5 * up;

        if (x >= left)
            return x - left;
        else if (x >= back)
            return x - back;
        else if (x >= right)
            return x - right;
        else if (x >= front)
            return x - front;
        else if (x >= up)
            return x - up;
        else
            return x;
    }
}