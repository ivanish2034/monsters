/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

/**
 *
 * @author ivis2
 */
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.util.List;

public class JsonHandler extends BaseHandler {
    private final ObjectMapper mapper;

    public JsonHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean handleRequest(String fileName, MonsterStorage storage) {
        if (!fileName.toLowerCase().endsWith(".json")) {
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
            System.err.println("Ошибка при обработке JSON файла: " + fileName);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean export(String fileName, List<Monster> monsters) {
        if (!fileName.toLowerCase().endsWith(".json")) {
            return passExportToNext(fileName, monsters);
        }

        try {
            mapper.writeValue(new File(fileName), monsters);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте в JSON: " + fileName);
            e.printStackTrace();
            return false;
        }
    }
}