package com.autoqa.dataproviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataProvider – reads test data from BOTH:
 *   1. src/test/resources/loginData.json
 *   2. src/test/resources/testdata.xlsx  (sheets: LoginData, SearchData)
 *
 * Used by @DataProvider in test classes – zero inline data anywhere.
 */
public class TestDataProvider {

    private static final String RESOURCES_PATH =
            System.getProperty("user.dir") + "/src/test/resources/";

    private TestDataProvider() {}

    // ════════════════════════════════════════════════════════
    //  JSON – Login data
    // ════════════════════════════════════════════════════════

    /**
     * Reads loginData.json
     * Returns Object[][] { email, password, expectedResult, description }
     */
    public static Object[][] getLoginDataFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(RESOURCES_PATH + "loginData.json"));
            List<Object[]> rows = new ArrayList<>();
            for (JsonNode node : root) {
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

    // ════════════════════════════════════════════════════════
    //  EXCEL – Login data  (sheet: LoginData)
    // ════════════════════════════════════════════════════════

    /**
     * Reads LoginData sheet from testdata.xlsx
     * Returns Object[][] { email, password, expectedResult, description }
     * Skips header row (row 0).
     */
    public static Object[][] getLoginDataFromExcel() {
        return readExcelSheet("LoginData", 4);
    }

    // ════════════════════════════════════════════════════════
    //  EXCEL – Search keywords  (sheet: SearchData)
    // ════════════════════════════════════════════════════════

    /**
     * Reads SearchData sheet from testdata.xlsx
     * Returns Object[][] { keyword }
     * Skips header row (row 0).
     */
    public static Object[][] getSearchKeywordsFromExcel() {
        return readExcelSheet("SearchData", 1);
    }

    // ════════════════════════════════════════════════════════
    //  Private helper – generic Excel sheet reader
    // ════════════════════════════════════════════════════════

    private static Object[][] readExcelSheet(String sheetName, int colCount) {
        String filePath = RESOURCES_PATH + "testdata.xlsx";
        List<Object[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName
                        + "' not found in testdata.xlsx");
            }

            // Start from row 1 to skip header
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Object[] data = new Object[colCount];
                for (int c = 0; c < colCount; c++) {
                    Cell cell = row.getCell(c);
                    data[c] = (cell == null) ? "" : getCellValue(cell);
                }
                rows.add(data);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read testdata.xlsx ["
                    + sheetName + "]: " + e.getMessage());
        }

        return rows.toArray(new Object[0][]);
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default:      return "";
        }
    }
}
