/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import java.util.Date;
import java.util.List;

/**
 *
 * @author hantsy
 */
public class Feed {
    
    private List<Item> items;
    private Date lastBuildDate;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Date getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(Date lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }
    
    
}
