/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import java.io.Serializable;

/**
 *
 * @author eiker
 */
public class MappingPixel implements Serializable {
    private static final long serialVersionUID = 47L;
    private final int R;
    private final int G;
    private final int B;
    private final int H;

    public MappingPixel(int R, int G, int B, int H) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.H = H;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public int getH() {
        return H;
    }  
}
