/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreDefinitionReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eiker
 */
public class OreDefinition {
    private final Map < String, OreDefinitionPlanet > oreDefinitionPlanets;

    public OreDefinition() {
        this.oreDefinitionPlanets = new HashMap();
    }

    public void addOreDefinitionPlanet(String subtypeId) {
        this.oreDefinitionPlanets.put(subtypeId, new OreDefinitionPlanet(subtypeId));
    }

    public Map < String, OreDefinitionPlanet > getOreDefinitionPlanets() {
        return oreDefinitionPlanets;
    }

    public OreDefinitionPlanet getOreDefinitionPlanet(String subtypeId) {
        return this.oreDefinitionPlanets.get(subtypeId);
    }

    public Set < String > getPlanetList() {
        return this.oreDefinitionPlanets.keySet();
    }

    public void addPlanet(String subtypeId) {
        this.oreDefinitionPlanets.put(subtypeId, null);
    }

    @Override
    public String toString() {
        String result = "";

        for (String planet: this.oreDefinitionPlanets.keySet()) {
            OreDefinitionPlanet p = this.oreDefinitionPlanets.get(planet);
            result += p.getSubtypeId() + "\r\n";
            for (String ore: p.getOreList()) {
                result += "\t" + ore + "\r\n";
                OreDefinitionSingle pair = p.getOreDefinition(ore);
                result += "\t\t[" + pair.getColor() + "]" + pair.getStart() + "\r\n";
            }
        }
        return result;
    }

    public void combine(OreDefinition oreDef) {
        oreDef.getPlanetList().stream().filter(planet -> (!this.oreDefinitionPlanets.containsKey(planet))).forEachOrdered(planet -> {
            this.oreDefinitionPlanets.put(planet, oreDef.getOreDefinitionPlanets().get(planet));
        });
    }
}