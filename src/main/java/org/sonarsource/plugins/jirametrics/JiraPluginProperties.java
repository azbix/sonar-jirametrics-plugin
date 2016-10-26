package org.sonarsource.plugins.jirametrics;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.PropertyType;
import org.sonar.api.resources.Qualifiers;

public class JiraPluginProperties {

  public static final String JIRA_SERVER_URL = "sonar.jirametrics.jira_server_url";
  public static final String JIRA_SERVER_LOGIN = "sonar.jirametrics.jira_server_login";
  public static final String JIRA_SERVER_PASSWORD = "sonar.jirametrics.jira_server_password";
  public static final String JIRA_FILTER_ID = "sonar.jirametrics.jira_filter_id";

  private JiraPluginProperties() {
    // only statics
  }

  public static List<PropertyDefinition> definitions() {
    int i = 0;
    return asList(
      PropertyDefinition.builder(JIRA_SERVER_URL)
        .name("Jira Server")
        .description("URL of Jira server")
        .onlyOnQualifiers(Qualifiers.PROJECT)
        .index(++i)
        .build(),
      PropertyDefinition.builder(JIRA_SERVER_LOGIN)
        .name("Jira Login")
        .description("User Login")
        .onlyOnQualifiers(Qualifiers.PROJECT)
        .index(++i)
        .build(),
      PropertyDefinition.builder(JIRA_SERVER_PASSWORD)
        .name("Jira Password")
        .description("User Password")
        .type(PropertyType.PASSWORD)
        .onlyOnQualifiers(Qualifiers.PROJECT)
        .index(++i)
        .build(),
      PropertyDefinition.builder(JIRA_FILTER_ID)
        .name("Jira Filter ID")
        .description("Jira Filter ID")
        .type(PropertyType.INTEGER)
        .onlyOnQualifiers(Qualifiers.PROJECT)
        .index(++i)
        .build()
      );
  }
}
