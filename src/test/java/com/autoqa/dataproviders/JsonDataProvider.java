package com.autoqa.dataproviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads test data from JSON files under src/test/resources/.
 * Used by @DataProvider methods – no inline data in test classes.
 */
public class JsonDataProvider {

    private JsonDataProvider() {}

    /**
     * Reads loginData.json and returns a 2D Object array for TestNG @DataProvider.
     * Each row: { email, password, expectedResult, description }
     */
    public static Object[][] getLoginData() {
        String filePath = System.getProperty("user.dir")
                + "/src/test/resources/loginData.json";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(filePath));
            List<Object[]> rows = new ArrayList<>();
            for (JsonNode node : rootNode) {
                rows.add(new Object[]{
                        node.get("email").asText(),
                        node.get("password").asText(),
                        node.get("expectedResult").asText(),
                        node.get("description").asText()
                });
            }
            return rows.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read loginData.json: " + e.getMessage());
        }
    }
}
