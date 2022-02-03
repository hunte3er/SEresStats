/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import static Util.Constants.BLUEPRINT_BACKLIST;

/**
 *
 * @author eiker
 */
public class BlacklistCheck {
    public static boolean checkBlueprint(String subtypeId){
        boolean result = true;
        for(String blackisted : BLUEPRINT_BACKLIST){
            if(subtypeId.contains(blackisted)){
                result = false;
                break;
            }                
        }
        return result;
    }
}
