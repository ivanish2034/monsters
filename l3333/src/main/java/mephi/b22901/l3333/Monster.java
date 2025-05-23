/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

import java.util.*;

/**
 *
 * @author ivis2
 */


public class Monster {
    public String name;
    public String description;
    public int dangerLevel;
    public String habitat;
    public String firstMention;
    public List<String> immunities;
    public String vulnerability;
    public String activityTime;
    public String size;
    public String weight;
    public Map<String, Object> recipe;
    public String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(int dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getFirstMention() {
        return firstMention;
    }

    public void setFirstMention(String firstMention) {
        this.firstMention = firstMention;
    }

    public List<String> getImmunities() {
        return immunities;
    }

    public void setImmunities(List<String> immunities) {
        this.immunities = immunities;
    }

    public String getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(String vulnerability) {
        this.vulnerability = vulnerability;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Map<String, Object> getRecipe() {
        return recipe;
    }

    public void setRecipe(Map<String, Object> recipe) {
        this.recipe = recipe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dangerLevel=" + dangerLevel +
                ", habitat='" + habitat + '\'' +
                ", firstMention='" + firstMention + '\'' +
                ", immunities=" + immunities +
                ", vulnerability='" + vulnerability + '\'' +
                ", activityTime='" + activityTime + '\'' +
                ", size='" + size + '\'' +
                ", weight='" + weight + '\'' +
                ", recipe=" + recipe +
                ", source='" + source + '\'' +
                '}';
    }
}
