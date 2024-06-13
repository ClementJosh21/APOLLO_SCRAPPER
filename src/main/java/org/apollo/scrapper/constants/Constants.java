package org.apollo.scrapper.constants;

public class Constants {

  public static final String BANNER =
      "|  |  |  |~~  |     /~~   /~~\\   |\\  /|  |~~    ~~|~~   /~~\\       /\\    |~~\\  |~~\\   /~~\\   |    |     /~~\\     /~~\\   /~~  |~~\\    /\\    |~~\\  |~~  |~~\\\n"
          + "|  |  |  |--  |    |     |    |  | \\/ |  |--      |    |    |     /__\\   |__/  |__/  |    |  |    |    |    |    `--.  |     |__/   /__\\   |__/  |--  |__/\n"
          + " \\/ \\/   |__  |__   \\__   \\__/   |    |  |__      |     \\__/     /    \\  |     |      \\__/   |__  |__   \\__/     \\__/   \\__  |  \\  /    \\  |     |__  |  \\";

  public static final int START_ATTEMPT_COUNT = 0;
  public static final String CONTACT_LIST_URL = "https://api.apollo.io/v1/mixed_people/search";
  public static final String SAVED_LIST_URL = "https://app.apollo.io/api/v1/labels/search";
  public static final String LOGIN_URL = "https://app.apollo.io/api/v1/auth/login";

  public static final String REQUEST_FOR_CONTACT_LIST =
      "{\n"
          + "    \"finder_table_layout_id\": \"6667075ffa473b09db16d1fc\",\n"
          + "    \"contact_label_ids\": [\n"
          + "        \"%s\"\n"
          + "    ],\n"
          + "    \"prospected_by_current_team\": [\n"
          + "        \"yes\"\n"
          + "    ],\n"
          + "    \"page\": %s,\n"
          + "    \"display_mode\": \"explorer_mode\",\n"
          + "    \"per_page\": 100,\n"
          + "    \"open_factor_names\": [],\n"
          + "    \"num_fetch_result\": 1,\n"
          + "    \"context\": \"people-index-page\",\n"
          + "    \"show_suggestions\": false,\n"
          + "    \"ui_finder_random_seed\": \"zf114oj3ic\",\n"
          + "    \"cacheKey\": 1718117101909\n"
          + "}";
  public static final String REQUEST_FOR_SAVED_LIST =
      "{\n"
          + "  \"team_lists_only\": [\n"
          + "    \"no\"\n"
          + "  ],\n"
          + "  \"q_name\": \"%s\",\n"
          + "  \"page\": 1,\n"
          + "  \"label_modality\": \"contacts\",\n"
          + "  \"display_mode\": \"explorer_mode\",\n"
          + "  \"per_page\": 50,\n"
          + "  \"open_factor_names\": [\n"
          + "    \n"
          + "  ],\n"
          + "  \"num_fetch_result\": 3,\n"
          + "  \"show_suggestions\": false,\n"
          + "  \"ui_finder_random_seed\": \"osa5xvbu6qb\",\n"
          + "  \"cacheKey\": 1718264896583\n"
          + "}";

  public static final String REQUEST_FOR_LOGIN =
      "{\n"
          + "  \"email\": \"%s\",\n"
          + "  \"password\": \"%s\",\n"
          + "  \"timezone_offset\": -330,\n"
          + "  \"cacheKey\": 1718267668563\n"
          + "}";
  public static final String CONTACTS_CSV_HEADER = "Name,Organization Name,Title,Email\n";
  public static final String QUOTES = "\"";
  public static final String X_API_KEY = "X-Api-Key";
  public static final String API_KEY = "O4s71o31Juq1e4zZBM6oaw";
  public static final String EXCEPTION_OCCURRED = "Exception occurred: {}";
}
