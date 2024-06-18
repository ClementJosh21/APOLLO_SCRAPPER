package org.apollo.scrapper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExporterEnum {
  CSV("Comma Separated value", ",", ".csv"),
  TSV("Tab Separated value", "\t", ".tsv"),
  EXCEL("Excel", "", ".xlsx");

  private final String format;
  private final String delimiter;
  private final String extension;
}
