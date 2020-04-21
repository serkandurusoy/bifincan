/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.user;

/**
 *
 * @author hantsy
 */
public class UserLevelValue {

    private String username;
    private int activityLevel = 0;

    public UserLevelValue(String username, int level) {
        this.username = username;
        this.activityLevel = level;
    }

    public UserLevelValue() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void increaseActivityLevel() {
        if (this.activityLevel < 3) {
            this.activityLevel++;
        }
    }

    public void decreaseActivityLevel() {
        if (this.activityLevel > 1) {
            this.activityLevel--;
        }
    }
}
