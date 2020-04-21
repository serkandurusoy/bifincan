/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.user;

/**
 *
 * @author hantsy
 */
public enum AdminRoleType {
    ROLE_ADMINISTRATOR("administrator"), 
    ROLE_MANAGER("manager"), 
    ROLE_WAREHOUSE_MANAGER("warehouse manager"),
    ROLE_WAREHOUSE_OPERATOR("warehouse operator");
    
    String label;
    AdminRoleType(String _name){
        this.label=_name;
    }

    public String getLabel() {
        return this.label;
    }
     
}
