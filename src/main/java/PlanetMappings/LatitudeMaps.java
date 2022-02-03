/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlanetMappings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author eiker
 */
public class LatitudeMaps {
    private final Map < String, Integer[][] > maps;

    public LatitudeMaps() {
        this.maps = new HashMap();
    }

    private void generate(int res) {
        Integer[][] resultSide = new Integer[res][res];
        Integer[][] resultPole = new Integer[res][res];
        int max = resultSide.length - 1;
        double offset = (max - 1) / 2;
        for (int y = 0; y < resultSide.length; y++) {
            for (int x = 0; x < resultSide[y].length; x++) {
                resultSide[y][x] = (int) Math.rint(calculateLatitude(-(x - offset) / offset, 1, -(y - offset) / offset) / 90 * 255);
                resultPole[y][x] = (int) Math.rint(calculateLatitude((x - offset) / offset, (y - offset) / offset, 1) / 90 * 255);
            }
        }
        this.maps.put("side-" + res, resultSide);
        this.maps.put("pole-" + res, resultPole);
    }

    private double[] calculateSphericCoordinates(double cx, double cy, double cz) {
        double sx = cx * Math.sqrt(1 - ((cy * cy) / 2) - ((cz * cz) / 2) + ((cy * cy * cz * cz) / 3));
        double sy = cy * Math.sqrt(1 - ((cz * cz) / 2) - ((cx * cx) / 2) + ((cx * cx * cz * cz) / 3));
        double sz = cz * Math.sqrt(1 - ((cx * cx) / 2) - ((cy * cy) / 2) + ((cx * cx * cy * cy) / 3));

        double[] vec = {sx, sy, sz};
        return vec;
    }

    private double calculateLatitude(double cx, double cy, double cz) {
        double[] sphericCoords = calculateSphericCoordinates(cx, cy, cz);
        double sx = sphericCoords[0];
        double sy = sphericCoords[1];
        double sz = sphericCoords[2];
        double radiansV = Math.asin(sz / (Math.sqrt(sx * sx + sy * sy + sz * sz)));
        return Math.abs(Math.toDegrees(radiansV));
    }

    public void saveImage(String path) throws IOException {
        int[] bitMasks = new int[] {
            0xFF0000,
            0xFF00,
            0xFF,
            0xFF000000
        };
        for (Map.Entry < String, Integer[][] > map: maps.entrySet()) {
            int[] temp = new int[map.getValue()[0].length * map.getValue().length];

            for (int i = 0; i < map.getValue().length; i++) {
                for (int j = 0; j < map.getValue()[i].length; j++) {
                    int rgb = (int) map.getValue()[i][j];
                    int result = 255;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    temp[i * map.getValue()[0].length + j] = result;
                }
            }
            SinglePixelPackedSampleModel sm = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, map.getValue()[0].length, map.getValue().length, bitMasks);
            DataBufferInt db = new DataBufferInt(temp, temp.length);
            WritableRaster wr = Raster.createWritableRaster(sm, db, new Point());
            BufferedImage slopeImg = new BufferedImage(ColorModel.getRGBdefault(), wr, false, null);
            BufferedImage copySlope = new BufferedImage(slopeImg.getWidth(), slopeImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d2 = copySlope.createGraphics();
            g2d2.setColor(Color.WHITE); 
            g2d2.fillRect(0, 0, copySlope.getWidth(), copySlope.getHeight());
            g2d2.drawImage(slopeImg, 0, 0, copySlope.getWidth(), copySlope.getHeight(), null);
            g2d2.dispose();

            File f = new File(path + map.getKey() + ".png");
            f.createNewFile();
            ImageIO.write(copySlope, "png", f);
        }
    }

    public double get(String face, int res, int x, int y) {
        if (face.equals("up") || face.equals("down")) {
            return this.getPole(res, x, y);
        } else if (face.equals("left") || face.equals("right") || face.equals("front") || face.equals("back")) {
            return this.getSide(res, x, y);
        }
        return 0d;
    }

    private double getSide(int res, int x, int y) {
        if (!maps.containsKey("pole-" + res)) {
            this.generate(res);
        }
        return this.maps.get("side-" + res)[y][x];
    }

    private double getPole(int res, int x, int y) {
        if (!maps.containsKey("pole-" + res)) {
            this.generate(res);
        }
        return this.maps.get("pole-" + res)[y][x];
    }

    public double get(int res, int x) {
        int newCount = reduceCount(res, x);
        if (x < res * res || x > 5 * res * res)
            return this.getPole(res, newCount % res, newCount / res);
        else
            return this.getSide(res, newCount % res, newCount / res);
    }

    private int reduceCount(int res, int x) {
        int up = res * res;
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