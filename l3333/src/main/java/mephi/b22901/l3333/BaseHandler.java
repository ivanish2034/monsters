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

public abstract class BaseHandler implements Handler {
    public Handler nextHandler;

    @Override
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected boolean passToNext(String fileName, MonsterStorage storage) {
        if (nextHandler != null) {
            return nextHandler.handleRequest(fileName, storage);
        }
        return false;
    }

    protected boolean passExportToNext(String fileName, List<Monster> monsters) {
        if (nextHandler != null) {
            return nextHandler.export(fileName, monsters);
        }
        return false;
    }
}
