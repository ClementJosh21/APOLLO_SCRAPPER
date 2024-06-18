package org.apollo.scrapper.exporter;

import static org.apollo.scrapper.constants.Constants.*;
import static org.apollo.scrapper.constants.Constants.QUOTES;
import static org.apollo.scrapper.enums.ExporterEnum.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ExportHelper {

  public String getResponse(String requestBody, String url) throws URISyntaxException {
    log.info("Request made to apollo with url {}", url);
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(X_API_KEY, API_KEY);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> result =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

    return result.getBody();
  }

  public String getQuotedString(String name) {
    return QUOTES + name + QUOTES;
  }

  public Exporter getExporter(ExportHelper exportHelper, String name) {
    return switch (name) {
      case "Excel" -> new ExcelExporter(exportHelper, EXCEL);
      case "TSV" -> new TSVExporter(exportHelper, TSV);
      default -> new CSVExporter(exportHelper, CSV);
    };
  }

  public File getAndCreateFileWithExtensions(ApolloSavedList apolloSavedList, String extension) {
    return new File(apolloSavedList.getName() + extension);
  }
}
