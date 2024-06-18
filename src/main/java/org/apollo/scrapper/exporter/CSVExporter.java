package org.apollo.scrapper.exporter;

import static org.apollo.scrapper.constants.Constants.*;
import static org.apollo.scrapper.constants.Constants.CONTACT_LIST_URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.response.contacts.ApolloContactResponse;
import org.apollo.scrapper.bean.response.contacts.ApolloContacts;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.enums.ExporterEnum;

@Slf4j
@Data
@AllArgsConstructor
public class CSVExporter implements Exporter {

  private final ExportHelper exportHelper;
  private final ExporterEnum exporterEnum;

  @Override
  public void export(ApolloSavedList apolloSavedList) {

    File file =
        exportHelper.getAndCreateFileWithExtensions(apolloSavedList, exporterEnum.getExtension());
    final int iterationCount = apolloSavedList.getCachedCount() / 100;
    try (OutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(CONTACTS_CSV_HEADER.getBytes(StandardCharsets.UTF_8));
      if (iterationCount == 0 && apolloSavedList.getCachedCount() > 0) {
        processContacts(apolloSavedList, outputStream, 1);
      } else {
        for (int i = 1; i <= iterationCount; i++) {
          processContacts(apolloSavedList, outputStream, i);
        }
      }
    } catch (URISyntaxException | IOException e) {
      log.error(EXCEPTION_OCCURRED, e.getMessage(), e);
    }
  }

  private void processContacts(ApolloSavedList apolloSavedList, OutputStream outputStream, int i2)
      throws URISyntaxException, IOException {
    final String requestBody = String.format(REQUEST_FOR_CONTACT_LIST, apolloSavedList.getId(), i2);
    String json = exportHelper.getResponse(requestBody, CONTACT_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloContactResponse apolloContactResponse =
        mapper.readValue(json, ApolloContactResponse.class);
    for (ApolloContacts contact : apolloContactResponse.getContacts()) {
      outputStream.write(
          (String.join(
                      exporterEnum.getDelimiter(),
                      exportHelper.getQuotedString(contact.getName()),
                      exportHelper.getQuotedString(contact.getOrganizationName()),
                      exportHelper.getQuotedString(contact.getTitle()),
                      exportHelper.getQuotedString(contact.getEmail()))
                  + "\n")
              .getBytes(StandardCharsets.UTF_8));
    }
  }
}
