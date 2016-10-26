package org.sonarsource.plugins.jirametrics;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.Description;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;
import org.sonar.api.web.WidgetCategory;
import org.sonar.api.web.WidgetProperties;
import org.sonar.api.web.WidgetProperty;
import org.sonar.api.web.WidgetPropertyType;

@UserRole(UserRole.USER)
@Description("Displays open Jira issues related to the project")
@WidgetCategory("Issues")
@WidgetProperties()
public class JiraWidget extends AbstractRubyTemplate implements RubyRailsWidget {

  @Override
  public String getId() {
    return "jirametrics";
  }

  @Override
  public String getTitle() {
    return "Jira";
  }

  @Override
  protected String getTemplatePath() {
    return "/jira/jira_widget.html.erb";
  }
}
