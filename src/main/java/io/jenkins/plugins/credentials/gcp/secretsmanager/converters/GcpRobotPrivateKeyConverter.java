package io.jenkins.plugins.credentials.gcp.secretsmanager.converters;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.SecretBytes;
import com.google.jenkins.plugins.credentials.oauth.GoogleRobotPrivateKeyCredentials;
import com.google.jenkins.plugins.credentials.oauth.JsonServiceAccountConfig;
import com.google.jenkins.plugins.credentials.oauth.ServiceAccountConfig;
import io.jenkins.plugins.credentials.gcp.secretsmanager.CredentialsFactory.SecretSupplier;
import io.jenkins.plugins.credentials.gcp.secretsmanager.GcpCredentialsConverter;
import io.jenkins.plugins.credentials.gcp.secretsmanager.Labels;
import io.jenkins.plugins.credentials.gcp.secretsmanager.SecretGetter;
import io.jenkins.plugins.credentials.gcp.secretsmanager.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.jenkinsci.plugins.variant.OptionalExtension;

@OptionalExtension(requirePlugins = {"google-oauth-plugin"})
public class GcpRobotPrivateKeyConverter extends GcpCredentialsConverter {

  private static final Logger LOGGER = Logger.getLogger(GcpRobotPrivateKeyConverter.class.getName());

  @Override
  public boolean canResolve(String type) {
    return Type.SERVICE_ACCOUNT_PRIVATE_KEY.equals(type);
  }

  private static ServiceAccountConfig createJsonServiceAccountConfig(String name, String secretJsonKeyAsPlainText) {

    final JsonServiceAccountConfig jsonServiceAccountConfig = new JsonServiceAccountConfig();
    // Use secret ID as our filename; there is no value in letting the user supply a filename
    jsonServiceAccountConfig.setFilename(name);

    // Encode secret JSON key in the encoding expected by SecretBytes
    //   (see SecretBytes.toString() for implicit format specification)
    final String secretJsonKeyAsSecretBytesString = "{" + Base64.encodeBase64String(secretJsonKeyAsPlainText.getBytes()) + "}";
    final SecretBytes secretJsonKeyAsSecretBytes = SecretBytes.fromString(secretJsonKeyAsSecretBytesString);
    jsonServiceAccountConfig.setSecretJsonKey(secretJsonKeyAsSecretBytes);

    return jsonServiceAccountConfig;
  }

  @Override
  public StandardCredentials resolve(
      String name, String description, Map<String, String> labels, SecretGetter secretGetter) {
    final String projectId = labels.getOrDefault(Labels.PROJECT_ID, "");

    // TODO: support P12 format keys as well

    final String secretJsonKeyAsPlainText = secretGetter.getSecretString(name);
    final ServiceAccountConfig serviceAccountConfig = createJsonServiceAccountConfig(name, secretJsonKeyAsPlainText);

    try {
      return new GoogleRobotPrivateKeyCredentials(projectId, serviceAccountConfig, null);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to create GoogleRobotPrivateKeyCredentials from provided service-account-config", e);
      return null;
    }
  }
}
