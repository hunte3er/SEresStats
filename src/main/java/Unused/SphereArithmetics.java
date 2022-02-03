/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

/**
 *
 * @author eiker
 */
public class SphereArithmetics {      
    public double getSphereConeCutSliceA(double r, double angle, double start, double depth){        
        double V = (2*Math.PI/3)*Math.pow(r-start, 3)*angle-(2*Math.PI/3*Math.pow((r-start)-depth, 3)*angle);       
        return V;
    }
    
    public double getSphereConeCutSliceA(double angle, double r1, double r2){        
        double V = (2*Math.PI/3)*Math.pow(r1, 3)*angle-(2*Math.PI/3*Math.pow(r2, 3)*angle);         
        return V;
    }
    
    public double getAngle(double r, double A){
        double nA = getSphereCapArea(r,A); 
        double h = nA/(2*Math.PI*r);
        double angle = h/r;
        return angle;
    }
    
    public double getArea(double angle, double r){
        double h = angle * r;
        double nA = h * (2*Math.PI * r);
        return nA;
    }
    
    public double getSphereCapArea(double r, double A){
        double As = 4*Math.PI*Math.pow(r,2);
        double Ac = 6*2048*2048;
        double nA = (A/Ac)*As;   
        
        return nA;
    }
//    
//    public Map<Integer, Integer> getPlanetBPxCount(String planet, String[] sides) throws IOException{        
//        PixelCounter pc = new PixelCounter();         
//        
//        Map<Integer,Integer> countOverall = new HashMap();
//        
//        for(String side : sides){
//            Map<Integer,Integer> countSide = pc.getBComposition(ImageIO.read(new File(path + IMAGE_FOLDER + planet + PATH_SEPERATOR + side)));
//            
//            countSide.entrySet().forEach(countEntry -> {
//                if(!countOverall.containsKey(countEntry.getKey()))
//                    countOverall.put(countEntry.getKey(), countEntry.getValue());
//                else
//                    countOverall.put(countEntry.getKey(), countEntry.getValue() + countOverall.get(countEntry.getKey()));
//            });
//        }
//        return new TreeMap(countOverall);
//    }   
//    
//    public Map<Integer, Integer> getPlanetRPxCount(String planet, String[] sides) throws IOException{        
//        PixelCounter pc = new PixelCounter();         
//        
//        Map<Integer,Integer> countOverall = new HashMap();
//        
//        for(String side : sides){
//            Map<Integer,Integer> countSide = pc.getRComposition(ImageIO.read(new File(path + IMAGE_FOLDER + planet + PATH_SEPERATOR + side)));
//            
//            countSide.entrySet().forEach(countEntry -> {
//                if(!countOverall.containsKey(countEntry.getKey()))
//                    countOverall.put(countEntry.getKey(), countEntry.getValue());
//                else
//                    countOverall.put(countEntry.getKey(), countEntry.getValue() + countOverall.get(countEntry.getKey()));
//            });
//        }
//        return new TreeMap(countOverall);
//    }  
//    
//    public double getMeanRadius(String planet, double hillMin, double hillMax) throws IOException{        
//        Map<Integer,Integer> countResult = getPlanetBPxCount(planet, HEIGHTMAP_SIDES);
//        
//        double result;
//        
//        double sum = 0d;
//        double totalPx = 0d;
//        
//        for(Map.Entry<Integer, Integer> px : countResult.entrySet()){
//            sum += px.getKey()*px.getValue();
//            totalPx += px.getValue();
//        }
//        
//        double weightedAv = sum / totalPx / 256;
//        double interval = (hillMax+100)-(hillMin+100);
//        double hillMean = weightedAv * interval + hillMin + 1;
//        result = hillMean * PLANET_RADIANS.get(planet);
//        
//        return result;
//    }  
//
//    public Map<String, VoxelMaterial> getVoxelMaterialL() {
//        return voxelMaterialL;
//    }
//    
//    public double getSphereConeCutSlice(double r, double A, double depth, double start){
//        double nA = getSphereCapArea(r-start,A); 
//        double h = nA/(2*Math.PI*(r-start));
//        double angle = h/(r-start);
//        
//        double V = (2*Math.PI/3)*Math.pow(r-start, 3)*angle-(2*Math.PI/3*Math.pow((r-start)-depth, 3)*angle);   
//        
//        return getSphereConeCutSlice(r-start, r-start-depth, A);
//    }
//    
//    public double getSphereConeCutSlice(double r1, double r2, double A){
//        double nA = getSphereCapArea(r1,A); 
//        double h = nA/(2*Math.PI*r1);
//        double angle = h/r1;
//        
//        double V = (2*Math.PI/3)*Math.pow(r1, 3)*angle-(2*Math.PI/3*Math.pow(r2, 3)*angle);   
//        
//        return V;
//    }
//    
//    public float getSphereConeCutSliceA(float r, float angle, float start, float depth){        
//        double V = (2*Math.PI/3)*Math.pow(r-start, 3)*angle-(2*Math.PI/3*Math.pow((r-start)-depth, 3)*angle);       
//        return (float)V;
//    }
//    
//    
//    public double getAngle(double r, double A){
//        double nA = getSphereCapArea(r,A); 
//        double h = nA/(2*Math.PI*r);
//        double angle = h/r;
//        return angle;
//    }
//    
//    
//    public double getSphereCapArea(double r, double A){
//        double Ar = 4*Math.PI*Math.pow(r,2);
//        double Ad = 6*2048*2048;
//        double nA = (A/Ad)*Ar;   
//        
//        return nA;
//    }
}
