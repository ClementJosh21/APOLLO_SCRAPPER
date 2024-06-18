package org.apollo.scrapper;

import static org.apollo.scrapper.constants.Constants.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apollo.scrapper.process.ApolloScrappingProcess;

public class ApolloScrapperMain {

  public static void main(String[] args)
      throws IOException, URISyntaxException, InterruptedException {
    System.out.println(BANNER);
    ApolloScrappingProcess apolloScrappingProcess = new ApolloScrappingProcess();
    //    apolloScrappingProcess.authenticate(START_ATTEMPT_COUNT);
    apolloScrappingProcess.start(START_ATTEMPT_COUNT_LIST);
  }
}
