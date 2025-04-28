/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

/**
 *
 * @author ivis2
 */
import java.util.*;

public class MonsterStorage {
    private List<Monster> monsters;
    private String sourceType;

    public MonsterStorage(String sourceType) {
        this.monsters = new ArrayList<>();
        this.sourceType = sourceType;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public String getSourceType() {
        return sourceType;
    }
    
    public void setMonsters(List<Monster> monsters) {
    this.monsters = monsters;
    }
}
