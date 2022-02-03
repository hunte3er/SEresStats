/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author eiker
 */
public class PixelCounter {
	/**
	 * returns an array with the number of pixels in the image.
	 *
	 * @return an array with the number of pixels in each colour range that was passed in,
	 * as well as an extra entry at the end with the total number of pixels in the image
	 */
	public int[] count(BufferedImage image, RGBRange... ranges){
		int[] result = new int[ranges.length+1];
		int h = image.getHeight();
		int w = image.getWidth();
		result[ranges.length] = h * w;
		int[] rgbArray = null;
		for (int y = 0; y < h; y++){
			rgbArray = image.getRGB(0, y, w, 1, rgbArray, 0, w);
			for (int r = 0; r<ranges.length; r++){
				result[r] += ranges[r].count(rgbArray);
			}
		}
                image.flush();
		return result;
	}
	
	/**
	 * returns an array with the number of pixels in the image.
	 *
	 * @return an array with the number of pixels in each colour range that was passed in,
	 * as well as an extra entry at the end with the total number of pixels in the image
	 */
	public int[] count(File image, RGBRange... ranges) throws IOException{
		return count(ImageIO.read(image), ranges);
	}
        
        public Map<Integer, Integer> getRComposition(BufferedImage image){
            return getSingleChannelComposition(image, "red");
        }
        
        public Map<Integer, Integer> getGComposition(BufferedImage image){
            return getSingleChannelComposition(image, "green");
        }
        
        public Map<Integer, Integer> getBComposition(BufferedImage image){
            return getSingleChannelComposition(image, "blue");
        }
        
        public Map<Integer, Integer> getSingleChannelComposition(BufferedImage image, String color){
            int shift = 0;
            if("r".equals(color.toLowerCase().substring(0, 0)))
                shift = 16;
            if("g".equals(color.toLowerCase().substring(0, 0)))
                shift = 8;
            if("b".equals(color.toLowerCase().substring(0, 0)))
                shift = 0;
            
            int h = image.getHeight();
            int w = image.getWidth();
            int[] rgbArray = new int[h*w];
            for (int y = 0; y < h; y++){
                for(int x = 0; x < w; x++){
                    rgbArray[y*w+x] = image.getRGB(x, y);
                }
            }
                
            Map<Integer, Integer> pixels = new HashMap();
            for(int k = 0; k < 256; k++)
                pixels.put(k, 0);
            for (int pixel: rgbArray){
                    int px = (pixel >> shift) & 0xFF;
                    if(!pixels.containsKey(px))
                        pixels.put(px, 1);
                    else
                        pixels.put(px, pixels.get(px) + 1);
            }
            return pixels;
        }
        
        public MappingPixel[][] getPixelMapping(BufferedImage mat, BufferedImage height){
            int h = mat.getHeight();
            int w = mat.getWidth();
            MappingPixel[][] rgbhlArray = new MappingPixel[h][w];
            for (int y = 0; y < h; y++){
                for(int x = 0; x < w; x++){                    
                    rgbhlArray[y][x] = new MappingPixel( 
                            getR(mat.getRGB(x, y)),
                            getB(mat.getRGB(x, y)),
                            getG(mat.getRGB(x, y)),
                            (getR(height.getRGB(x, y))+getG(height.getRGB(x, y))+getB(height.getRGB(x, y)))/3
                    );
                }
            }
            System.out.println("Done mapping.");
            return rgbhlArray;
        }     
    
        private int getR(int color){
            return (color >> 16) & 0xFF;
        }
        
        private int getB(int color){
            return (color >> 8) & 0xFF;
        }
        
        private int getG(int color){
            return (color >> 0) & 0xFF;
        }
	
	
	
	/**
	 * Simple wrapper class to hold an RGB colour range
	 */
	
	public class RGBRange{
		private final int minR, minG, minB, maxR, maxG, maxB;
		RGBRange(int minR, int minG, int minB, int maxR, int maxG, int maxB){
			// handle possibly reversed ranges
			this.minR = Math.min(minR, maxR);
			this.minG = Math.min(minG, maxG);
			this.minB = Math.min(minB, maxB);
			this.maxR = Math.max(minR, maxR);
			this.maxG = Math.max(minG, maxG);
			this.maxB = Math.max(minB, maxB);
		}
		/**
		 * count the pixels that fall into this range for the given "rgbArray" ({@link BufferedImage#getRGB(int, int, int, int, int[], int, int)}
		 */
		int count(int[] rgbArray){
			int count = 0;
			for (int pixel: rgbArray){
				// see Color#getRed
				int r = (pixel >> 16) & 0xFF;
				if (r < minR || r > maxR) continue;
				int g = (pixel >> 8) & 0xFF;
				if (g < minG || g > maxG) continue;
				int b = (pixel >> 0) & 0xFF;
				if (b < minB || b > maxB) continue;
				count++;
			}
			return count;
		}
	}
	
	/**
	 * create a range by specifying upper and lower bounds for all three components.All values are inclusive bounds.
         * @return
	 */
	public RGBRange rgb(int minR, int minG, int minB, int maxR, int maxG, int maxB){
		return new RGBRange(minR, minG, minB, maxR, maxG, maxB);
	}

	/**
	 * create a range by specifying a "central point" and a maximum delta 
	 */

	public RGBRange rgb(int r, int g, int b, int maxDelta){
		return new RGBRange(r-maxDelta, g-maxDelta, b-maxDelta,r+maxDelta, g+maxDelta, b+maxDelta);
	}
   }
