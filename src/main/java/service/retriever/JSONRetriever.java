package service.retriever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import service.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONRetriever {
    public static <T> List<T> retrieveAll(String filePath, Class<T> clazz) {
        String json = FileReader.readFile(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<T> list = objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, clazz));
            return list;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
