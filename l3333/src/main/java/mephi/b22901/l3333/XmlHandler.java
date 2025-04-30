/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

import java.io.File;
import java.util.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
/**
 *
 * @author ivis2
 */

public class XmlHandler extends BaseHandler {
    private final XmlMapper mapper;

    public XmlHandler() {
        this.mapper = new XmlMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean handleRequest(String fileName, MonsterStorage storage) {
        if (!fileName.toLowerCase().endsWith(".xml")) {
            return passToNext(fileName, storage);
        }

        try {
            File file = new File(fileName);
            
            List<Monster> monsters = mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(List.class, Monster.class)
            );
            
            storage.setMonsters(monsters);
            return true;
            
        } catch (IOException e) {
            System.err.println("Ошибка при обработке XML файла: " + fileName);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean export(String fileName, List<Monster> monsters) {
        if (!fileName.toLowerCase().endsWith(".xml")) {
            return passExportToNext(fileName, monsters);
        }

        try {
            Map<String, Object> root = Map.of("monsters", monsters);
            mapper.writeValue(new File(fileName), root);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте в XML: " + fileName);
            e.printStackTrace();
            return false;
        }
    }
}