/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin;

import com.dna.bifincan.model.user.AdminRoleType;
import javax.inject.Named;

/**
 *
 * @author hantsy
 */
@Named("referenceData")
public class ReferenceData {
    
    public AdminRoleType[] getAdminRoleTypes(){
        return AdminRoleType.values();
    }
}
