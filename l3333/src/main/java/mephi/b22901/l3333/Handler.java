/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

import java.util.List;

/**
 *
 * @author ivis2
 */
public interface Handler {
    boolean handleRequest(String fileName, MonsterStorage storage); 
    void setNextHandler(Handler nextHandler);
    boolean export(String fileName, List<Monster> monsters); 
}
