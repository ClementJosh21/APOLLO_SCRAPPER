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
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
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

  private void processHeader(XSSFSheet spreadsheet) {
    XSSFRow row = spreadsheet.createRow(0);
    Cell cell = row.createCell(0);
    cell.setCellValue(NAME);
    cell = row.createCell(1);
    cell.setCellValue(ORGANIZATION_NAME);
    cell = row.createCell(2);
    cell.setCellValue(TITLE);
    cell = row.createCell(4);
    cell.setCellValue(EMAIL);
  }

  private void removeDuplicates(XSSFSheet spreadsheet, XSSFSheet noDuplicateSheet) {
    Set<String> uniqueRows = new HashSet<>();
    int newRowNum = 0;

    for (Row row : spreadsheet) {
      StringBuilder rowContent = new StringBuilder();
      for (Cell cell : row) {
        rowContent.append(cell.toString()).append(exporterEnum.getDelimiter());
      }
      String rowString = rowContent.toString();

      if (!uniqueRows.contains(rowString)) {
        uniqueRows.add(rowString);
        XSSFRow newRow = noDuplicateSheet.createRow(newRowNum++);
        int cellNum = 0;
        for (Cell cell : row) {
          XSSFCell newCell = newRow.createCell(cellNum++);
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
  }

  @Override
  public void export(ApolloSavedList apolloSavedList) {
    File file =
        exportHelper.getAndCreateFileWithExtensions(apolloSavedList, exporterEnum.getExtension());
    final int iterationCount = (int) Math.ceil((double) apolloSavedList.getCachedCount() / 100);
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet spreadsheet = getSpreadsheet(workbook, ORIGINAL_SHEET_NAME);
      XSSFSheet noDuplicateSheet = getSpreadsheet(workbook, WITHOUT_DUPLICATES_SHEET_NAME);
      processHeader(spreadsheet);
      for (int i = 1; i <= iterationCount; i++) {
        log.info(
            "Writing {} records into the file {}",
            exportHelper.getProcessedRecordCount(
                i, iterationCount, apolloSavedList.getCachedCount()),
            file.getName());
        processContacts(apolloSavedList, spreadsheet, i);
        log.info(
            "Processed records: {}",
            exportHelper.getProcessedRecordCount(
                i, iterationCount, apolloSavedList.getCachedCount()));
      }
      removeDuplicates(spreadsheet, noDuplicateSheet);

      FileOutputStream fileOutputStream = new FileOutputStream(file);
      workbook.write(fileOutputStream);
      fileOutputStream.close();
    } catch (URISyntaxException | IOException e) {
      log.error("Error occurred while exporting the contacts. Please try again after some time.");
    }
  }

  private XSSFSheet getSpreadsheet(XSSFWorkbook workbook, String sheetName) {
    XSSFSheet spreadsheet = workbook.createSheet(sheetName);
    for (int i = 0; i < COLUMN_COUNT_EXCEL; i++) {
      spreadsheet.autoSizeColumn(i);
    }
    return spreadsheet;
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
    for (int i = 0; i < apolloContactResponse.getContacts().size(); i++) {
      String name = apolloContactResponse.getContacts().get(i).getName();
      String organizationName = apolloContactResponse.getContacts().get(i).getOrganizationName();
      String title = apolloContactResponse.getContacts().get(i).getTitle();
      String email = apolloContactResponse.getContacts().get(i).getEmail();
      if (!Objects.toString(email, "").isEmpty()) {
        XSSFRow row = sheet.createRow(rowNumber++);
        XSSFCell cell = row.createCell(NAME_INDEX);
        cell.setCellValue(name);
        cell = row.createCell(ORGANIZATION_NAME_INDEX);
        cell.setCellValue(organizationName);
        cell = row.createCell(TITLE_INDEX);
        cell.setCellValue(title);
        cell = row.createCell(EMAIL_INDEX);
        cell.setCellValue(email);
      }
    }
  }

  private int getStartingRowNumber(int batchCount) {
    return batchCount > 1 ? (batchCount - 1) * 100 : batchCount;
  }
}
