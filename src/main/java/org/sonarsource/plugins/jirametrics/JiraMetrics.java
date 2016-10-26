package org.sonarsource.plugins.jirametrics;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class JiraMetrics implements Metrics {

  public static final Metric<Integer> JIRA_ISSUES = new Metric.Builder("jira_issues_count", "All Issues", Metric.ValueType.INT)
    .setDescription("Jira issues counter")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> JIRA_ISSUES_RUSH = new Metric.Builder("jira_issues_count_rush", "Rush Issues", Metric.ValueType.INT)
    .setDescription("Jira issues of Rush priority")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> JIRA_ISSUES_HIGH = new Metric.Builder("jira_issues_count_high", "High Issues", Metric.ValueType.INT)
    .setDescription("Jira issues of High priority")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> JIRA_ISSUES_NORMAL = new Metric.Builder("jira_issues_count_normal", "Normal Issues", Metric.ValueType.INT)
    .setDescription("Jira issues of Normal priority")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> JIRA_ISSUES_LOW = new Metric.Builder("jira_issues_count_low", "Low Issues", Metric.ValueType.INT)
    .setDescription("Jira issues of Low priority")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> JIRA_ISSUES_COSMETIC = new Metric.Builder("jira_issues_count_cosmetic", "Cosmetic Issues", Metric.ValueType.INT)
    .setDescription("Jira issues of Cosmetic priority")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  @Override
  public List<Metric> getMetrics() {
    return asList(JIRA_ISSUES, JIRA_ISSUES_RUSH, JIRA_ISSUES_HIGH, JIRA_ISSUES_NORMAL, JIRA_ISSUES_LOW, JIRA_ISSUES_COSMETIC);
  }
}
