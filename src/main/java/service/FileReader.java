package service;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
    /**
     * Read the file
     * @param filePath
     * @return String
     */
    public static String readFile(String filePath) {
        try (java.io.FileReader fileReader = new java.io.FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // Append newline character
            }

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
