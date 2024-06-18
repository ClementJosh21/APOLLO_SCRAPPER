package org.apollo.scrapper.constants;

public class Constants {

  public static final String BANNER =
      "|  |  |  |~~  |     /~~   /~~\\   |\\  /|  |~~    ~~|~~   /~~\\       /\\    |~~\\  |~~\\   /~~\\   |    |     /~~\\     /~~\\   /~~  |~~\\    /\\    |~~\\  |~~  |~~\\\n"
          + "|  |  |  |--  |    |     |    |  | \\/ |  |--      |    |    |     /__\\   |__/  |__/  |    |  |    |    |    |    `--.  |     |__/   /__\\   |__/  |--  |__/\n"
          + " \\/ \\/   |__  |__   \\__   \\__/   |    |  |__      |     \\__/     /    \\  |     |      \\__/   |__  |__   \\__/     \\__/   \\__  |  \\  /    \\  |     |__  |  \\";

  public static final int START_ATTEMPT_COUNT_LOGIN = 0;
  public static final int START_ATTEMPT_COUNT_LIST = 0;
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
  public static final String NAME = "Name";
  public static final String TITLE = "Title";
  public static final String EMAIL = "Email";
  public static final String ORGANIZATION_NAME = "Organization Name";
  public static final String CONTACTS_CSV_HEADER =
      NAME + "," + ORGANIZATION_NAME + "," + TITLE + "," + EMAIL + "\n";
  public static final String QUOTES = "\"";
  public static final String X_API_KEY = "X-Api-Key";
  public static final String API_KEY = "O4s71o31Juq1e4zZBM6oaw";
  public static final String EXCEPTION_OCCURRED = "Exception occurred: {}";

  public static final int CELL_TYPE_NUMERIC = 0;
  public static final int CELL_TYPE_STRING = 1;
  public static final int CELL_TYPE_FORMULA = 2;
  public static final int CELL_TYPE_BLANK = 3;
  public static final int CELL_TYPE_BOOLEAN = 4;
  public static final int CELL_TYPE_ERROR = 5;
  public static final String EXCEL = "Excel";
  public static final String TSV = "TSV";
  public static final String ORIGINAL_SHEET_NAME = "Original";
  public static final String WITHOUT_DUPLICATES_SHEET_NAME = "Without Duplicates";
  public static final int COLUMN_COUNT_EXCEL = 4;
  public static final int NAME_INDEX = 0;
  public static final int ORGANIZATION_NAME_INDEX = 1;
  public static final int TITLE_INDEX = 2;
  public static final int EMAIL_INDEX = 4;
}
