package org.sonarsource.plugins.jirametrics;

import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;

import org.sonarsource.plugins.jirametrics.JiraMetrics;
import org.sonarsource.plugins.jirametrics.JiraPluginProperties;
import org.sonarsource.plugins.jirametrics.JiraIssuesSensor;
import org.sonarsource.plugins.jirametrics.JiraWidget;

public class JiraMetricsPlugin implements Plugin {

  @Override
  public void define(Context context) {
    context
      .addExtensions(JiraPluginProperties.definitions())
      .addExtensions(JiraMetrics.class, JiraIssuesSensor.class, JiraWidget.class);
  }
}
