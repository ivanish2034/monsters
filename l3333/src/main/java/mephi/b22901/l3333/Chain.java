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
public class Chain {
    private Handler chain;

    public Chain() {
        buildChain();
    }

    private void buildChain() {
        Handler jsonHandler = new JsonHandler();
        Handler xmlHandler = new XmlHandler();
        Handler yamlHandler = new YamlHandler();

        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);

        this.chain = jsonHandler;
    }

    public boolean process(String fileName, MonsterStorage storage) {
        return chain.handleRequest(fileName, storage);
    }
    
    public boolean processExport(String fileName, List<Monster> monsters) {
        return chain.export(fileName, monsters);
    }
}
