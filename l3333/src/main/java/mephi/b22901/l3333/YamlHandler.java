/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

/**
 *
 * @author ivis2
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.*;
public class YamlHandler extends BaseHandler {
    private final ObjectMapper mapper;

    public YamlHandler() {
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean handleRequest(String fileName, MonsterStorage storage) {
        if (!isYamlFile(fileName)) {
            return passToNext(fileName, storage);
        }

        try {
            File file = new File(fileName);
            
            Map<String, List<Map<String, Object>>> root = mapper.readValue(
                file,
                new TypeReference<Map<String, List<Map<String, Object>>>>() {}
            );
            
            List<Map<String, Object>> monstersData = root.get("monsters");
            if (monstersData == null) {
                throw new IOException("YAML файл не содержит ключа 'monsters'");
            }

            List<Monster> monsters = mapper.convertValue(
                monstersData,
                new TypeReference<List<Monster>>() {}
            );
            
            storage.setMonsters(monsters);
            return true;
            
        } catch (IOException e) {
            System.err.println("Ошибка при обработке YAML файла: " + fileName);
            return false;
        }
    }

    @Override
    public boolean export(String fileName, List<Monster> monsters) {
        if (!isYamlFile(fileName)) {
            return passExportToNext(fileName, monsters);
        }

        try {
            Map<String, Object> root = Collections.singletonMap("monsters", monsters);
            mapper.writeValue(new File(fileName), root);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте в YAML: " + fileName);
            return false;
        }
    }

    private boolean isYamlFile(String fileName) {
        return fileName != null && 
               (fileName.toLowerCase().endsWith(".yaml") || 
                fileName.toLowerCase().endsWith(".yml"));
    }
}