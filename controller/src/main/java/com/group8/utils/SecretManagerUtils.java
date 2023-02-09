package com.group8.utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

public class SecretManagerUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecretManagerUtils.class);

    public static String getSecretValue(SecretsManagerClient secretsManagerClient, String secretName) {
        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
            return valueResponse.secretString();

        } catch (SecretsManagerException e) {
            logger.error(e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    public static String getSecretValueByKey(SecretsManagerClient secretsManagerClient, String secretName, String key) {
        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
            JSONObject jsonObject = new JSONObject(valueResponse.secretString());
            return (String) jsonObject.get("SvcAuth");

        } catch (SecretsManagerException e) {
            logger.error(e.awsErrorDetails().errorMessage());
            return null;
        }
    }
}
