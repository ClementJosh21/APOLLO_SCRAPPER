package org.apollo.scrapper.process;

import static org.apollo.scrapper.constants.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apollo.scrapper.bean.response.contacts.ApolloContactResponse;
import org.apollo.scrapper.bean.response.contacts.ApolloContacts;
import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.bean.response.list.ApolloSavedListResponse;
import org.apollo.scrapper.exporter.ExportHelper;
import org.apollo.scrapper.exporter.Exporter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApolloScrappingProcess {

  public void processContacts(ApolloSavedList apolloSavedList) {

    File file = new File(apolloSavedList.getName() + ".csv");
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
    String json = getResponse(requestBody, CONTACT_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloContactResponse apolloContactResponse =
        mapper.readValue(json, ApolloContactResponse.class);
    for (ApolloContacts contact : apolloContactResponse.getContacts()) {
      outputStream.write(
          (String.join(
                      ",",
                      getQuotedString(contact.getName()),
                      getQuotedString(contact.getOrganizationName()),
                      getQuotedString(contact.getTitle()),
                      getQuotedString(contact.getEmail()))
                  + "\n")
              .getBytes(StandardCharsets.UTF_8));
    }
  }

  private String getResponse(String requestBody, String url) throws URISyntaxException {
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

  private String getQuotedString(String name) {
    return QUOTES + name + QUOTES;
  }

  public ApolloSavedList getListInfo(String listName)
      throws URISyntaxException, JsonProcessingException {
    final String requestBody = String.format(REQUEST_FOR_SAVED_LIST, listName);
    String json = getResponse(requestBody, SAVED_LIST_URL);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ApolloSavedListResponse apolloSavedListResponse =
        mapper.readValue(json, ApolloSavedListResponse.class);
    return apolloSavedListResponse.getLabels().get(0);
  }

  public String getListName() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the list name you want to export: ");
    return scanner.nextLine();
  }

  public void authenticate(int attempts) {
    if (attempts > 3)
      throw new RuntimeException(
          "Max attempts for login exceeded. Please try again after sometime.");
    try {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter your username: ");
      String username = scanner.nextLine();
      System.out.println("Enter your password: ");
      String password = scanner.nextLine();
      final String requestBody = String.format(REQUEST_FOR_LOGIN, username, password);
      getResponse(requestBody, LOGIN_URL);
    } catch (Exception e) {
      log.error("Unable to login. Kindly check the credentials.");
      ++attempts;
      authenticate(attempts);
    }
  }

  public void start() throws URISyntaxException, JsonProcessingException {
    String listName = getListName();
    System.out.println(listName);
    final ApolloSavedList listInfo = getListInfo(listName);
    ExportHelper exportHelper = new ExportHelper();
    Exporter exporter = exportHelper.getExporter(exportHelper, "Excel");
    exporter.export(listInfo);
    printMenu();
  }

  private void printMenu() throws URISyntaxException, JsonProcessingException {
    System.out.println("Export another list? Press 'Y' to proceed and any other key to quit");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) start();
    else System.exit(0);
  }
}
