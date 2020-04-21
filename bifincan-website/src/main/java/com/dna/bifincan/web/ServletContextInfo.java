/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

/**
 *
 * @author hantsy
 */
public class ServletContextInfo {

    private static String contextPath = "";

    public static String getContextPath() {
        return contextPath;
    }
    
    public static void setContextPath(String _ctxPath){
        contextPath=_ctxPath;
    }
}
