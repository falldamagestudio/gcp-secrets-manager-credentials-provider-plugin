package io.jenkins.plugins.credentials.gcp.secretsmanager.config;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.jenkins.plugins.casc.misc.EnvVarsRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.jvnet.hudson.test.JenkinsRule;

public class PluginConfigurationEnvVarsTest {
    
  public final JenkinsRule j = new JenkinsRule();

  private static final String GCP_SECRETS_MANAGER_PROJECT_TESTVALUE = "project-1234"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE = "testlabel"; 
  private static final String GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE = "testvalue"; 
  private static final String GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE = "testfilter"; 

  @Rule
  public RuleChain chain = RuleChain
    .outerRule(new EnvVarsRule()
        .set("GCP_SECRETS_MANAGER_PROJECT", GCP_SECRETS_MANAGER_PROJECT_TESTVALUE)
        .set("GCP_SECRETS_MANAGER_FILTER_LABEL", GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE)
        .set("GCP_SECRETS_MANAGER_FILTER_VALUE", GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE)
        .set("GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER", GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE)
    )
    .around(j);

  @Test
  public void testValuesSet() {
    PluginConfiguration configuration = PluginConfiguration.get();

    assertThat(configuration.getProject()).isEqualTo(GCP_SECRETS_MANAGER_PROJECT_TESTVALUE);
    assertThat(configuration.getFilter().getLabel()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_LABEL_TESTVALUE);
    assertThat(configuration.getFilter().getValue()).isEqualTo(GCP_SECRETS_MANAGER_FILTER_VALUE_TESTVALUE);
    assertThat(configuration.getServerSideFilter().getFilter()).isEqualTo(GCP_SECRETS_MANAGER_SERVER_SIDE_FILTER_TESTVALUE);

  }
}

