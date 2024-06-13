package org.apollo.scrapper;

import static org.apollo.scrapper.constants.Constants.BANNER;
import static org.apollo.scrapper.constants.Constants.START_ATTEMPT_COUNT;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apollo.scrapper.process.ApolloScrappingProcess;

public class ApolloScrapperMain {

  public static void main(String[] args) throws IOException, URISyntaxException {
    System.out.println(BANNER);
    ApolloScrappingProcess apolloScrappingProcess = new ApolloScrappingProcess();
    apolloScrappingProcess.authenticate(START_ATTEMPT_COUNT);
    apolloScrappingProcess.start();

  }
}
