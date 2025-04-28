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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//public class JsonHandler extends BaseHandler {
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public boolean handleRequest(String fileName, MonsterStorage storage) {
//        if (fileName.toLowerCase().endsWith(".json")) {
//            try {
//                List<Monster> monsters = Arrays.asList(mapper.readValue(new File(fileName), Monster[].class));
//                storage.setMonsters(monsters);
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return passToNext(fileName, storage);
//    }
//
//    @Override
//    public boolean export(String fileName, List<Monster> monsters) {
//        if (fileName.toLowerCase().endsWith(".json")) {
//            try {
//                mapper.enable(SerializationFeature.INDENT_OUTPUT);
//                mapper.writeValue(new File(fileName), monsters);
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return passExportToNext(fileName, monsters);
//    }
//}
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
            
            // Единообразный подход с XmlHandler
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
            // Аналогично XmlHandler, но без корневого элемента (стандартно для JSON)
            mapper.writeValue(new File(fileName), monsters);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте в JSON: " + fileName);
            e.printStackTrace();
            return false;
        }
    }
}