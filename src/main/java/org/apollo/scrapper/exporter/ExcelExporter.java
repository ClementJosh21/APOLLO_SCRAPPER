package org.apollo.scrapper.exporter;

import static org.apollo.scrapper.constants.Constants.*;
import static org.apollo.scrapper.constants.Constants.CONTACT_LIST_URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apollo.scrapper.bean.response.contacts.ApolloContactResponse;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.enums.ExporterEnum;

@Slf4j
@Data
@AllArgsConstructor
public class ExcelExporter implements Exporter {

  private final ExportHelper exportHelper;
  private final ExporterEnum exporterEnum;

  @Override
  public void export(ApolloSavedList apolloSavedList) {

    File file =
        exportHelper.getAndCreateFileWithExtensions(apolloSavedList, exporterEnum.getExtension());
    final int iterationCount = apolloSavedList.getCachedCount() / 100;
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet spreadsheet = workbook.createSheet(apolloSavedList.getName() + " - Original");
      XSSFSheet noDuplicateSheet =
          workbook.createSheet(apolloSavedList.getName() + " - Without Duplicate");
      if (iterationCount == 0 && apolloSavedList.getCachedCount() > 0) {
        processContacts(apolloSavedList, spreadsheet, 1);
      } else {
        for (int i = 1; i <= iterationCount; i++) {
          processContacts(apolloSavedList, spreadsheet, i);
        }
      }
      Set<String> uniqueRows = new HashSet<>();
      int newRowNum = 0;

      for (Row row : spreadsheet) {
        StringBuilder rowContent = new StringBuilder();
        for (Cell cell : row) {
          rowContent.append(cell.toString()).append(",");
        }
        String rowString = rowContent.toString();

        if (!uniqueRows.contains(rowString)) {
          uniqueRows.add(rowString);
          Row newRow = noDuplicateSheet.createRow(newRowNum++);
          int cellNum = 0;
          for (Cell cell : row) {
            Cell newCell = newRow.createCell(cellNum++);
            switch (cell.getCellType()) {
              case CELL_TYPE_STRING:
                newCell.setCellValue(cell.getStringCellValue());
                break;
              case CELL_TYPE_NUMERIC:
                newCell.setCellValue(cell.getNumericCellValue());
                break;
              case CELL_TYPE_BOOLEAN:
                newCell.setCellValue(cell.getBooleanCellValue());
                break;
              case CELL_TYPE_FORMULA:
                newCell.setCellFormula(cell.getCellFormula());
                break;
              default:
                newCell.setCellValue(cell.toString());
                break;
            }
          }
        }
      }

      FileOutputStream fileOutputStream = new FileOutputStream(file);
      workbook.write(fileOutputStream);
      fileOutputStream.close();
    } catch (URISyntaxException | IOException e) {
      log.error(EXCEPTION_OCCURRED, e.getMessage(), e);
    }
  }

  private void processContacts(ApolloSavedList apolloSavedList, XSSFSheet sheet, int batchCount)
      throws URISyntaxException, IOException {
    final String requestBody =
        String.format(REQUEST_FOR_CONTACT_LIST, apolloSavedList.getId(), batchCount);
    String json = exportHelper.getResponse(requestBody, CONTACT_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloContactResponse apolloContactResponse =
        mapper.readValue(json, ApolloContactResponse.class);
    int rowNumber = getStartingRowNumber(batchCount);
    XSSFRow row = sheet.createRow(rowNumber++);
    Cell cell = row.createCell(0);
    cell.setCellValue(NAME);
    cell = row.createCell(1);
    cell.setCellValue(ORGANIZATION_NAME);
    cell = row.createCell(2);
    cell.setCellValue(TITLE);
    cell = row.createCell(4);
    cell.setCellValue(EMAIL);
    for (int i = 0; i < apolloContactResponse.getContacts().size(); i++) {
      row = sheet.createRow(rowNumber++);
      cell = row.createCell(0);
      cell.setCellValue(apolloContactResponse.getContacts().get(i).getName());
      cell = row.createCell(1);
      cell.setCellValue(apolloContactResponse.getContacts().get(i).getOrganizationName());
      cell = row.createCell(2);
      cell.setCellValue(apolloContactResponse.getContacts().get(i).getTitle());
      cell = row.createCell(4);
      cell.setCellValue(apolloContactResponse.getContacts().get(i).getEmail());
    }
  }

  private int getStartingRowNumber(int batchCount) {
    return batchCount > 1 ? (batchCount - 1) * 100 : batchCount;
  }
}
