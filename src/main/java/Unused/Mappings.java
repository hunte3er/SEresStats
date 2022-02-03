/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eiker
 */
public class Mappings {
    private final Map<String, PlanetMapping> PlanetMappings;
    
    public Mappings(){
        this.PlanetMappings = new HashMap();
    }
    
    public void addPlanetMapping(String subtypeId, 
            MappingPixel[][] up, MappingPixel[][] left, MappingPixel[][] front, 
            MappingPixel[][] right, MappingPixel[][] back, MappingPixel[][] down){
        this.PlanetMappings.put(subtypeId, new PlanetMapping(up, left, front, right, back, down));
    }

    public Map<String, PlanetMapping> getPlanetMappings() {
        return PlanetMappings;
    }    
    
    public PlanetMapping getPlanetMapping(String subtypeId){
        return this.PlanetMappings.get(subtypeId);
    }

    public Set<String> getPlanetList() {
        return this.PlanetMappings.keySet();
    } 
    
    public void addPlanet(String subtypeId){
        this.PlanetMappings.put(subtypeId, null);
    }    
}
