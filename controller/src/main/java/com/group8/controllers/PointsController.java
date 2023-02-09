package com.group8.controllers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.group8.api.PointsAPI;
import com.group8.models.DynamoDB;
import com.group8.models.redeemPoints;
import com.group8.utils.PropertiesReader;
import com.group8.utils.SecretManagerUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PointsController implements PointsAPI {

    private static final Logger logger = LoggerFactory.getLogger(PointsController.class);

    private final PropertiesReader propertiesReader;
    private final RestTemplate restTemplate;
    private final SecretsManagerClient secretsManagerClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final WebClient webClient;


    @Autowired
    public PointsController(PropertiesReader propertiesReader, RestTemplate restTemplate, SecretsManagerClient secretsManagerClient, DynamoDBMapper dynamoDBMapper,WebClient webClient) {
        this.propertiesReader = propertiesReader;
        this.restTemplate = restTemplate;
        this.secretsManagerClient = secretsManagerClient;
        this.dynamoDBMapper = dynamoDBMapper;
        this.webClient = webClient;
    }

    @Override
    @PostMapping(API_REDEEM_POINTS)
    public ResponseEntity<?> redeemPoints(@RequestBody redeemPoints redeemPointDetails) throws URISyntaxException {
        redeemPoints redeemPoints = new redeemPoints(redeemPointDetails.getProfileToken(), redeemPointDetails.getAmount(),
                redeemPointDetails.getDescription(), redeemPointDetails.getMemberNo());

        logger.info("{}: POST request received.", API_BASE_PATH);

        //retrieve svctoken from secret manager
        String svcToken = SecretManagerUtils.getSecretValueByKey(secretsManagerClient, "MEMBERSON_CREDENTIALS", "SvcAuth");
        String token = SecretManagerUtils.getSecretValue(secretsManagerClient, "MEMBERSON_TOKEN");

        //get API URL from dynamoDB
        String API_LINK = dynamoDBMapper.load(DynamoDB.class, "memberson_redeem_point").getApi_link();
        logger.debug(API_LINK);

        //set request body variable
        String alphanumericString = RandomStringUtils.randomAlphanumeric(10);
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        String transactionDate = dtf.format(now) + now.getOffset();  //2021/03/22 16:37:15+08:00

        //create request body
        Map<String, Object> bodyValues = new HashMap<>();

        bodyValues.put("MemberNo", redeemPoints.getMemberNo() );
        bodyValues.put("PointType", "C21 Points");
        bodyValues.put("TransactionDate", transactionDate);
        bodyValues.put("Location", "HQ");
        bodyValues.put("Amount", redeemPoints.getAmount());
        bodyValues.put("RedemptionCode", alphanumericString);
        bodyValues.put("Description", redeemPoints.getDescription());
        bodyValues.put("ReceiptNo", alphanumericString);

        Flux<String> stringFlux = WebClient.create().post()
                .uri(new URI(API_LINK))
                .header("SvcAuth", svcToken)
                .header("Token", token)
                .header("ProfileToken", redeemPoints.getProfileToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyValues)
                .retrieve()
                .bodyToFlux(String.class);

        logger.info("{}: POST request started.", API_BASE_PATH);
        stringFlux.subscribe(string -> logger.info(string));

        return ResponseEntity.ok("Still processing");
    }



}
