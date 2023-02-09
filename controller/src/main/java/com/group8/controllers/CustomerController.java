package com.group8.controllers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.group8.api.CustomerAPI;
import com.group8.models.DynamoDB;
import com.group8.utils.PropertiesReader;
import com.group8.utils.SecretManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@RestController
public class CustomerController implements CustomerAPI{

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final PropertiesReader propertiesReader;
    private final RestTemplate restTemplate;
    private final SecretsManagerClient secretsManagerClient;
    private final DynamoDBMapper dynamoDBMapper;


    @Autowired
    public CustomerController(PropertiesReader propertiesReader, RestTemplate restTemplate, SecretsManagerClient secretsManagerClient, DynamoDBMapper dynamoDBMapper) {
        this.propertiesReader = propertiesReader;
        this.restTemplate = restTemplate;
        this.secretsManagerClient = secretsManagerClient;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    @GetMapping(API_PATH_WITH_ID )
    public ResponseEntity<?> getCustomerProfile(@PathVariable String customerNo, String profileToken) {

        logger.info("{}: GET request received.", API_PATH_WITH_ID);

        // Request secrets
        String svcToken = SecretManagerUtils.getSecretValueByKey(secretsManagerClient, "MEMBERSON_CREDENTIALS", "SvcAuth");
        String token = SecretManagerUtils.getSecretValue(secretsManagerClient, "MEMBERSON_TOKEN");

        // Get API URL from DynamoDB: Membership Summary
        String API_LINK = dynamoDBMapper.load(DynamoDB.class, "memberson_get_customer_profile").getApi_link();
        logger.debug(API_LINK);
        String availablePointsURL = API_LINK.replace("{customerNo}", customerNo);

        // Call 2.20 Get membership summary from membersonCRM (need to pass in SVC, Token and Profile Token as headers)
        HttpHeaders headers = new HttpHeaders();
        headers.add("SvcAuth", svcToken);
        headers.add("Token", token);
        headers.add("ProfileToken", profileToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        logger.info("{}: GET request complete.", API_PATH_WITH_ID);
        return restTemplate.exchange(availablePointsURL, HttpMethod.GET, requestEntity, String.class);
    }
}

