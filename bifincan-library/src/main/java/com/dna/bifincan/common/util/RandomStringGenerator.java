/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.common.util;

import java.util.Random;
import javax.inject.Named;


@Named("randomStringGenerator")
public class RandomStringGenerator {

    private static final String INT_SEEDS = "0123456789";

    //private static final String STRING_SEEDS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String STRING_SEEDS = "0123456789abcdefghijklmnopqrstuvwxyz";
    
    private  Random ran = new Random();

    public String randomInt(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(INT_SEEDS.charAt(ran.nextInt(INT_SEEDS.length()-1)));
        }
        return sb.toString();
    }

    public String randomString(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(STRING_SEEDS.charAt(ran.nextInt(STRING_SEEDS.length()-1)));
        }
        return sb.toString();
    }
}
