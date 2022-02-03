/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import PlanetMappings.LatitudeMaps;
import PlanetMappings.PointModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author eiker
 */
public final class PlanetMapping {
    private final Map<String, MappingPixel[][]> cubeFaces;
    private final LatitudeMaps latitudeMaps;

    public PlanetMapping(MappingPixel[][] topPlane, MappingPixel[][] leftPlane, MappingPixel[][] frontPlane, MappingPixel[][] rightPlane, MappingPixel[][] backPlane, MappingPixel[][] bottomPlane) {
        this.cubeFaces = new HashMap();
        this.cubeFaces.put("up", topPlane);
        this.cubeFaces.put("front", frontPlane);
        this.cubeFaces.put("right", rightPlane);
        this.cubeFaces.put("back", backPlane);
        this.cubeFaces.put("left", leftPlane);
        this.cubeFaces.put("down", bottomPlane);
        this.latitudeMaps  = new LatitudeMaps();
    }
    
    public PlanetMapping(List<MappingPixel[][]> maps){
        this.cubeFaces = new HashMap();
        this.cubeFaces.put("up", maps.get(5));
        this.cubeFaces.put("front", maps.get(2));
        this.cubeFaces.put("right", maps.get(4));
        this.cubeFaces.put("back", maps.get(0));
        this.cubeFaces.put("left", maps.get(3));
        this.cubeFaces.put("down", maps.get(1));
        this.latitudeMaps  = new LatitudeMaps();
    }
    
    private double calculateGaussianSteepness(int up, int down, int right, int left){
        double dx = (right-left)/2;
        double dy = (down-up)/2;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    private double calculateSobelSteepness(int s, int r, int d){
        double dx = r - s;
        double dy = d - s;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public double getSteepness(String face, int x, int y){
        return switch (face) {
            case "up" -> this.getSteepnessTop(x, y);
            case "front" -> this.getSteepnessFront( x, y);
            case "right" -> this.getSteepnessRight( x, y);
            case "back" -> this.getSteepnessBack( x, y);
            case "left" -> this.getSteepnessLeft( x, y);
            case "down" -> this.getSteepnessBottom( x, y);
            default -> this.getSteepnessBottom( x, y);
        };
    }
    
    public double getSteepnessLeft(int x, int y){
        int max = this.cubeFaces.get("right").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("left")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("front")[y][0].getH();
        if(y < max)
            d = this.cubeFaces.get("left")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("down")[x][max].getH();
        if(x > 0)
            l = this.cubeFaces.get("left")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("back")[y][max].getH();
        if(y > 0)
            u = this.cubeFaces.get("left")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("up")[x][0].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }
        
    public double getSteepnessFront(int x, int y){
        int max = this.cubeFaces.get("right").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("front")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("right")[y][0].getH();
        if(y < max)
            d = this.cubeFaces.get("front")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("down")[max][max-x].getH();
        if(x > 0)
            l = this.cubeFaces.get("front")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("left")[y][max].getH();
        if(y > 0)
            u = this.cubeFaces.get("front")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("up")[max][x].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }
        
    public double getSteepnessRight(int x, int y){
        int max = this.cubeFaces.get("right").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("right")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("back")[y][0].getH();
        if(y < max)
            d = this.cubeFaces.get("right")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("down")[max-x][0].getH();
        if(x > 0)
            l = this.cubeFaces.get("right")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("front")[y][max].getH();
        if(y > 0)
            u = this.cubeFaces.get("right")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("up")[max-x][0].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }
        
    public double getSteepnessBack(int x, int y){
        int max = this.cubeFaces.get("back").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("back")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("left")[y][0].getH();
        if(y < max)
            d = this.cubeFaces.get("back")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("down")[0][x].getH();
        if(x > 0)
            l = this.cubeFaces.get("back")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("right")[y][max].getH();
        if(y > 0)
            u = this.cubeFaces.get("back")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("up")[0][max-x].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }
  
    public double getSteepnessTop(int x, int y){
        int max = this.cubeFaces.get("up").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("up")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("right")[0][max-y].getH();
        if(y < max)
            d = this.cubeFaces.get("up")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("front")[0][x].getH();
        if(x > 0)
            l = this.cubeFaces.get("up")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("left")[0][y].getH();
        if(y > 0)
            u = this.cubeFaces.get("up")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("back")[0][max-x].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }   
        
    public double getSteepnessBottom(int x, int y){
        int max = this.cubeFaces.get("down").length - 1, r = 0, d = 0, l = 0, u = 0;
        if(x < max)
            r = this.cubeFaces.get("down")[y][x+1].getH();
        else if (x == max)
            r = this.cubeFaces.get("left")[y][max].getH();
        if(y < max)
            d = this.cubeFaces.get("down")[y+1][x].getH();
        else if (y == max)
            d = this.cubeFaces.get("front")[max-x][max].getH();
        if(x > 0)
            l = this.cubeFaces.get("down")[y][x-1].getH();
        if(x == 0)
            l = this.cubeFaces.get("right")[max-y][max].getH();
        if(y > 0)
            u = this.cubeFaces.get("down")[y-1][x].getH();
        if(y == 0)
            u = this.cubeFaces.get("back")[max][x].getH();
        return calculateGaussianSteepness(u, d, r, l);
    }
    
    public void saveImage(String path) throws IOException{
        
        int[] bitMasks = new int[]{0xFF0000, 0xFF00, 0xFF, 0xFF000000};
        
        for(Map.Entry<String, MappingPixel[][]> entry : cubeFaces.entrySet()){
            int[] temp = new int[entry.getValue()[0].length * entry.getValue().length];

            for(int i = 0; i < entry.getValue().length; i++) {
                for(int j = 0; j < entry.getValue()[i].length; j++){
                    int rgb = (int)this.latitudeMaps.get(entry.getKey(), entry.getValue().length, i, j);;
                    int result = 255;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    temp[i*entry.getValue()[0].length+j] = result;
                }
            }
            
            SinglePixelPackedSampleModel sm = new SinglePixelPackedSampleModel(
            DataBuffer.TYPE_INT, entry.getValue()[0].length, entry.getValue().length, bitMasks);
            DataBufferInt db = new DataBufferInt(temp, temp.length);
            WritableRaster wr = Raster.createWritableRaster(sm, db, new Point());
            BufferedImage latitudeImg = new BufferedImage(ColorModel.getRGBdefault(), wr, false, null);
            BufferedImage copyLat = new BufferedImage(latitudeImg.getWidth(), latitudeImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = copyLat.createGraphics();
            g2d.setColor(Color.WHITE); // Or what ever fill color you want...
            g2d.fillRect(0, 0, copyLat.getWidth(), copyLat.getHeight());
            g2d.drawImage(latitudeImg, 0, 0, copyLat.getWidth(), copyLat.getHeight(), null);
            g2d.dispose();

            for(int i = 0; i < entry.getValue().length; i++) {
                for(int j = 0; j < entry.getValue()[i].length; j++){
                    int rgb = (int)this.latitudeMaps.get(entry.getKey(), entry.getValue().length, i, j);;
                    int result = 255;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    result = (result << 8) + rgb;
                    temp[i*entry.getValue()[0].length+j] = result;
                }
            }
            
            sm = new SinglePixelPackedSampleModel(
            DataBuffer.TYPE_INT, entry.getValue()[0].length, entry.getValue().length, bitMasks);
            db = new DataBufferInt(temp, temp.length);
            wr = Raster.createWritableRaster(sm, db, new Point());
            BufferedImage slopeImg = new BufferedImage(ColorModel.getRGBdefault(), wr, false, null);
            BufferedImage copySlope = new BufferedImage(slopeImg.getWidth(), slopeImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d2 = copySlope.createGraphics();
            g2d2.setColor(Color.WHITE); // Or what ever fill color you want...
            g2d2.fillRect(0, 0, copySlope.getWidth(), copySlope.getHeight());
            g2d2.drawImage(slopeImg, 0, 0, copySlope.getWidth(), copySlope.getHeight(), null);
            g2d2.dispose();
            
            File slopeFile = new File(path + entry.getKey() + "-slope.png");
            File latFile = new File(path + entry.getKey() + "-lat.png");
            slopeFile.createNewFile();
            latFile.createNewFile();
            ImageIO.write(copySlope, "png", slopeFile);
            ImageIO.write(copyLat, "png", latFile);
        }       
    }

    public Map<String, MappingPixel[][]> getCubeFaces() {
        return cubeFaces;
    }       
        
    public List<PointModel> getPixelRepresentations(double radius, double heightRange, double width){
        List<PointModel> pxL = new ArrayList();
        
        for(String face : this.cubeFaces.keySet()){
            MappingPixel[][] curMap = this.cubeFaces.get(face);
            for(int y = 0; y < curMap.length; y++){
                for(int x = 0; x < curMap[y].length; x++){
                    int red = curMap[y][x].getR();
                    int green = curMap[y][x].getG();
                    int blue = curMap[y][x].getB();
                    double height = (double)curMap[y][x].getH() / (double)255 * heightRange + radius;
//                    System.out.println(curMap[y][x].getH() + "/" + 255 + "*" + heightRange + "+" + radius + "=" + height);
                    double slope = Math.toDegrees(Math.atan(this.getSteepness(face, x, y)/ 255 * heightRange / width));
                    double latitude = this.latitudeMaps.get(face, curMap.length, x, y);
                    pxL.add(new PointModel(red,
                                                    green,
                                                    blue,
                                                    (int)height,
                                                    (int)slope,
                                                    (int)latitude
                    ));
                }
            }
        }
        return pxL;
    }
}
