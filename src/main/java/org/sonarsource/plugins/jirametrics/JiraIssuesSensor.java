package org.sonarsource.plugins.jirametrics;

import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES;
import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES_RUSH;
import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES_HIGH;
import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES_NORMAL;
import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES_LOW;
import static org.sonarsource.plugins.jirametrics.JiraMetrics.JIRA_ISSUES_COSMETIC;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.config.Settings;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

public class JiraIssuesSensor implements Sensor {

  private static Settings settings;
  private static String jql = null;

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Fetch open Jira issues");
  }

  @Override
  public void execute(SensorContext context) { 
    settings = context.settings();

    String[] jqlWithPriorities = {
      this.getJql("priority IN (Rush, High, Normal, Low, Cosmetic)"),
      this.getJql("priority = Rush"),
      this.getJql("priority = High"),
      this.getJql("priority = Normal"),
      this.getJql("priority = Low"),
      this.getJql("priority = Cosmetic")
    };

    Integer[] issuesCount = new Integer[6];
    for(int i =0; i < jqlWithPriorities.length ; i++) {
      issuesCount[i] = this.fetchIssuesCount(jqlWithPriorities[i]);
      Loggers.get(getClass()).info("Jira import " + i + " of 5: " + issuesCount[i] + " issues");
    }

    context.<Integer>newMeasure().forMetric(JIRA_ISSUES).on(context.module()).withValue(issuesCount[0]).save();
    context.<Integer>newMeasure().forMetric(JIRA_ISSUES_RUSH).on(context.module()).withValue(issuesCount[1]).save();
    context.<Integer>newMeasure().forMetric(JIRA_ISSUES_HIGH).on(context.module()).withValue(issuesCount[2]).save();
    context.<Integer>newMeasure().forMetric(JIRA_ISSUES_NORMAL).on(context.module()).withValue(issuesCount[3]).save();
    context.<Integer>newMeasure().forMetric(JIRA_ISSUES_LOW).on(context.module()).withValue(issuesCount[4]).save();
    context.<Integer>newMeasure().forMetric(JIRA_ISSUES_COSMETIC).on(context.module()).withValue(issuesCount[5]).save();
  }

  private String getJql(String extraRestr) {
    String jqlWithExtraRestr = "";

    if (jql == null) {
      jql = this.fetchJql();
    }

    if (jql.length() > 0) {
      jqlWithExtraRestr = "(" + jql +") AND " + extraRestr;
    } else if (extraRestr.length() > 0) {
      jqlWithExtraRestr = extraRestr;
    }
    return jqlWithExtraRestr;
  }

  private String fetchJql() {
    try {
      String jiraServerUrl = settings.getString(JiraPluginProperties.JIRA_SERVER_URL);
      String jiraFilterId = settings.getString(JiraPluginProperties.JIRA_FILTER_ID);
      String requestUrl = jiraServerUrl + "/rest/api/2/filter/" + jiraFilterId;

      String response = this.doGetRequest(requestUrl);
      JSONObject responseJson = new JSONObject(response);

      String jql = responseJson.getString("jql");
      return jql;
    } catch(Exception ex) {
      Loggers.get(getClass()).info("jirametrics failure: " + ex.toString());
      return "";
    }
  }

  private Integer fetchIssuesCount(String jql) {
    try {
      String jiraServerUrl = settings.getString(JiraPluginProperties.JIRA_SERVER_URL);
      String requestUrl = jiraServerUrl + "/rest/api/2/search";

      JSONObject requestJson = new JSONObject();
      requestJson.put("jql", jql);
      requestJson.put("fields", new String[]{"none"});
      requestJson.put("maxResults", new Integer(0));
      String response = this.doPostRequest(requestUrl, requestJson.toString());

      JSONObject responseJson = new JSONObject(response);
      Integer issuesCount = responseJson.getInt("total");
      return issuesCount;
    } catch(Exception ex) {
      Loggers.get(getClass()).info("jirametrics failure: " + ex.toString());
      return -1;
    }
  }

  private String doGetRequest(String url) throws Exception {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("Authorization", this.getBasicAuthenticationEncoded());
      con.setRequestProperty("Content-Type", "application/json");

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      return response.toString();
  }

  private String doPostRequest(String url, String data) throws Exception {
      byte[] postDataBytes = data.getBytes("UTF-8");

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Authorization", this.getBasicAuthenticationEncoded());
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
      con.setDoOutput(true);

      con.getOutputStream().write(postDataBytes);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      return response.toString();
  }

  private String getBasicAuthenticationEncoded() {
    String user = settings.getString(JiraPluginProperties.JIRA_SERVER_LOGIN);
    String password = settings.getString(JiraPluginProperties.JIRA_SERVER_PASSWORD);
    String userPassword = user + ":" + password;
    String authHeader = "Basic ";
    String authEncoded = new String(Base64.encodeBase64(userPassword.getBytes()));
    authHeader += authEncoded;
    return authHeader;
  }
}
