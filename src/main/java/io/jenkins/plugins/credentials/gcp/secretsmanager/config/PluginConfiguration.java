package io.jenkins.plugins.credentials.gcp.secretsmanager.config;

import hudson.Extension;
import hudson.ExtensionList;
import java.util.Optional;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
@Symbol("gcpCredentialsProvider")
public class PluginConfiguration extends GlobalConfiguration {

  private String project;
  private Filter filter;
  private ServerSideFilter serverSideFilter;

  public PluginConfiguration() {
    load();
  }

  public static PluginConfiguration getInstance() {
    return all().get(PluginConfiguration.class);
  }

  public String getProject() {
    Optional<String> newProject = resolveProjectFromEnvironment();
    if (newProject.isPresent() && !newProject.get().equals(project)) {
      project = newProject.get();
      save();
    }

    return project;
  }

  @DataBoundSetter
  @SuppressWarnings("unused")
  public void setProject(String project) {
    this.project = project;
    save();
  }

  public Filter getFilter() {
    Optional<Filter> newFilter = resolveFilterFromEnvironment();
    if (newFilter.isPresent() && !newFilter.get().equals(filter)) {
      filter = newFilter.get();
      save();
    }

      return filter;
  }

  @DataBoundSetter
  @SuppressWarnings("unused")
  public void setFilter(Filter filter) {
    this.filter = filter;
    save();
  }

  public ServerSideFilter getServerSideFilter() {
    Optional<ServerSideFilter> newServerSideFilter = resolveServerSideFilterFromEnvironment();
    if (newServerSideFilter.isPresent() && !newServerSideFilter.get().equals(serverSideFilter)) {
      serverSideFilter = newServerSideFilter.get();
      save();
    }

      return serverSideFilter;
  }

  @DataBoundSetter
  @SuppressWarnings("unused")
  public void setServerSideFilter(ServerSideFilter serverSideFilter) {
    this.serverSideFilter = serverSideFilter;
    save();
  }

  private Optional<String> resolveProjectFromEnvironment() {
    Optional<String> project = getPropertyByEnvOrSystemProperty("GCP_SECRETS_MANAGER_PROJECT", "jenkins.gcp-secrets-manager.project");
    return project;
  }

  private Optional<Filter> resolveFilterFromEnvironment() {
    Optional<String> filterLabel = getPropertyByEnvOrSystemProperty("GCP_SECRETS_MANAGER_FILTER_LABEL", "jenkins.gcp-secrets-manager.filter.label");
    Optional<String> filterValue = getPropertyByEnvOrSystemProperty("GCP_SECRETS_MANAGER_FILTER_VALUE", "jenkins.gcp-secrets-manager.filter.value");
    boolean validFilter = (filterLabel.isPresent() && filterValue.isPresent());
    Optional<Filter> filter = (validFilter ? Optional.of(new Filter(filterLabel.get(), filterValue.get())) : Optional.empty());
    return filter;
  }

  private Optional<ServerSideFilter> resolveServerSideFilterFromEnvironment() {
    Optional<String> serverSideFilterString = getPropertyByEnvOrSystemProperty("GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER", "jenkins.gcp-secrets-manager.server-side-filter");
    Optional<ServerSideFilter> serverSideFilter = (serverSideFilterString.isPresent() ? Optional.of(new ServerSideFilter(serverSideFilterString.get())) : Optional.empty());
    return serverSideFilter;
  }

  private Optional<String> getPropertyByEnvOrSystemProperty(String envVariable, String systemProperty) {
    String envResult = System.getenv(envVariable);
    if (envResult != null) {
        return Optional.of(envResult);
    }

    String systemResult = System.getProperty(systemProperty);
    if (systemResult != null) {
        return Optional.of(systemResult);
    }

    return Optional.empty();
  }

  @Override
  public synchronized boolean configure(StaplerRequest req, JSONObject json) {
    this.project = null;
    this.filter = null;
    this.serverSideFilter = null;

    req.bindJSON(this, json);
    save();
    return true;
  }

  public static PluginConfiguration get() {
    return ExtensionList.lookupSingleton(PluginConfiguration.class);
  }
}
