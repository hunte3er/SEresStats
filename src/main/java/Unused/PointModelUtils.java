/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import PlanetMappings.PointModel;
import Util.FindUtils;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author eiker
 */
public class PointModelUtils {
    public static List < PointModel > findAllByRed(Collection < PointModel > listBP, int color) {
        return FindUtils.findAllByProperty(listBP, bP -> color == bP.getRed());
    }

    public static List < PointModel > findAllByBlue(Collection < PointModel > listBP, int color) {
        return FindUtils.findAllByProperty(listBP, bP -> color == bP.getBlue());
    }

    public static List < PointModel > findAllByGreen(Collection < PointModel > listBP, int color) {
        return FindUtils.findAllByProperty(listBP, bP -> color == bP.getGreen());
    }
}