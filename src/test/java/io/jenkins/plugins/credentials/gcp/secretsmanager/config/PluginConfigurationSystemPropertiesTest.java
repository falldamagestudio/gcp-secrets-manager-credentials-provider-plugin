package io.jenkins.plugins.credentials.gcp.secretsmanager.config;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class PluginConfigurationSystemPropertiesTest {

  @Rule
  public JenkinsRule j = new JenkinsRule();

  private static final String GCP_SECRETS_MANAGER_PROJECT = "jenkins.gcp-secrets-manager.project";
  private static final String GCP_SECRETS_MANAGER_FILTER_LABEL = "jenkins.gcp-secrets-manager.filter.label";
  private static final String GCP_SECRETS_MANAGER_FILTER_VALUE = "jenkins.gcp-secrets-manager.filter.value";
  private static final String GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER = "jenkins.gcp-secrets-manager.server-side-filter";

  private static final String GCP_SECRETS_MANAGER_PROJECT_TESTVALUE = "project-1234"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE = "testlabel"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE = "testvalue"; 
  private static final String GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE = "testfilter"; 

  private static final String GCP_SECRETS_MANAGER_PROJECT_TESTVALUE2 = "project-1234-2"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE2 = "testlabel-2"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE2 = "testvalue-2"; 
  private static final String GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE2 = "testfilter-2"; 

  @Before
  public void before() {
    System.setProperty(GCP_SECRETS_MANAGER_PROJECT, GCP_SECRETS_MANAGER_PROJECT_TESTVALUE);
    System.setProperty(GCP_SECRETS_MANAGER_FILTER_LABEL, GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE);
    System.setProperty(GCP_SECRETS_MANAGER_FILTER_VALUE, GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE);
    System.setProperty(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER, GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE);
  }

  @After
  public void after() {
    System.clearProperty(GCP_SECRETS_MANAGER_PROJECT);
    System.clearProperty(GCP_SECRETS_MANAGER_FILTER_LABEL);
    System.clearProperty(GCP_SECRETS_MANAGER_FILTER_VALUE);
    System.clearProperty(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER);
  }

  @Test
  public void testValuesSet() {
    PluginConfiguration configuration = PluginConfiguration.get();

    assertThat(configuration.getProject()).isEqualTo(GCP_SECRETS_MANAGER_PROJECT_TESTVALUE);
    assertThat(configuration.getFilter().getLabel()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE);
    assertThat(configuration.getFilter().getValue()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE);
    assertThat(configuration.getServerSideFilter().getFilter()).isEqualTo(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE);

    // Test updating value
    System.setProperty(GCP_SECRETS_MANAGER_PROJECT, GCP_SECRETS_MANAGER_PROJECT_TESTVALUE2);
    System.setProperty(GCP_SECRETS_MANAGER_FILTER_LABEL, GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE2);
    System.setProperty(GCP_SECRETS_MANAGER_FILTER_VALUE, GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE2);
    System.setProperty(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER, GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE2);

    configuration = PluginConfiguration.get();

    assertThat(configuration.getProject()).isEqualTo(GCP_SECRETS_MANAGER_PROJECT_TESTVALUE2);
    assertThat(configuration.getFilter().getLabel()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE2);
    assertThat(configuration.getFilter().getValue()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE2);
    assertThat(configuration.getServerSideFilter().getFilter()).isEqualTo(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE2);
  }
}

