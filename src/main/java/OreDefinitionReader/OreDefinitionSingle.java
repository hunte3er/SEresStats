/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OreDefinitionReader;

/**
 *
 * @author eiker
 */
public class OreDefinitionSingle {
    private final int color;
    private final int start;
    private final int depth;
    private final String subtypeId;

    public OreDefinitionSingle(int color, int start, int depth, String subtypeId) {
        this.color = color;
        this.start = start;
        this.depth = depth;
        this.subtypeId = subtypeId;
    }

    public int getColor() {
        return color;
    }

    public int getStart() {
        return start;
    }

    public int getDepth() {
        return depth;
    }

    public String getSubtypeId() {
        return subtypeId;
    }
}
