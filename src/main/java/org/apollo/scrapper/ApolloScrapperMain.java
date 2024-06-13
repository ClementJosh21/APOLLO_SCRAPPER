package org.apollo.scrapper;

import org.apollo.scrapper.bean.response.list.ApolloSavedList;
import org.apollo.scrapper.process.ApolloScrappingProcess;

import java.io.IOException;
import java.net.URISyntaxException;

public class ApolloScrapperMain {

  public static void main(String[] args) throws IOException, URISyntaxException {

    ApolloScrappingProcess apolloScrappingProcess = new ApolloScrappingProcess();
    apolloScrappingProcess.authenticate();
    String listName = apolloScrappingProcess.getListName();
    final ApolloSavedList listInfo = apolloScrappingProcess.getListInfo(listName);
    apolloScrappingProcess.processContacts(listInfo);
  }
}
