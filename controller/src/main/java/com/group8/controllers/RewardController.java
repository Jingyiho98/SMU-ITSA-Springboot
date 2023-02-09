package com.group8.controllers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.group8.api.RewardAPI;
import com.group8.models.DynamoDB;
import com.group8.utils.PropertiesReader;
import com.group8.utils.SecretManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;


@RestController
public class RewardController implements RewardAPI {

    private static final Logger logger = LoggerFactory.getLogger(RewardController.class);

    private final PropertiesReader propertiesReader;
    private final RestTemplate restTemplate;
    private final SecretsManagerClient secretsManagerClient;
    private final DynamoDBMapper dynamoDBMapper;


    @Autowired
    public RewardController(PropertiesReader propertiesReader, RestTemplate restTemplate, SecretsManagerClient secretsManagerClient, DynamoDBMapper dynamoDBMapper) {
        this.propertiesReader = propertiesReader;
        this.restTemplate = restTemplate;
        this.secretsManagerClient = secretsManagerClient;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    //For get all rewards: use /rewardtype/search without any criterias
    //To get details for a particular reward you can use the /reward/{rewardId} endpoint
    //To redeem a reward, it is /reward/redeem endpoint.

    @Override
    @GetMapping(API_BASE_PATH + "/getAllAvailableItems" )
    public ResponseEntity<?> getAllRedeemable() {

        logger.info("{}: GET request received.", API_BASE_PATH + "/getAllAvailableItems");

        // Request secrets
        String svcToken = SecretManagerUtils.getSecretValueByKey(secretsManagerClient, "MEMBERSON_CREDENTIALS", "SvcAuth");
        String token = SecretManagerUtils.getSecretValue(secretsManagerClient, "MEMBERSON_TOKEN");

        String API_LINK = dynamoDBMapper.load(DynamoDB.class, "memberson_get_all_rewards").getApi_link();
        logger.debug(API_LINK);

        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("SvcAuth", svcToken);
        headers.add("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{}";
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody ,headers);

        ResponseEntity<String> response = restTemplate.exchange(API_LINK, HttpMethod.POST,requestEntity,String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response);
        }
        else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.badRequest().body("Bad credentials");
        } else {
            return ResponseEntity.badRequest().body("Error!");
        }


    }

    @Override
    @GetMapping(API_BASE_PATH + "/getDetailsOfItem" )
    public ResponseEntity<?> getDetailsOfItemByItemNo(@RequestParam String rewardNo, String profileToken) {


        //get Authorization token from Secret Manager (SVC and token)
        String svcToken = SecretManagerUtils.getSecretValueByKey(secretsManagerClient, "MEMBERSON_CREDENTIALS", "SvcAuth");
        String token = SecretManagerUtils.getSecretValue(secretsManagerClient, "MEMBERSON_TOKEN");

        //get API URL from dynamodb rewards api - /reward/{rewardNo}
        String API_LINK = dynamoDBMapper.load(DynamoDB.class, "memberson_get_reward_details").getApi_link();
        logger.debug(API_LINK);
        String getRewardURL = API_LINK.replace("{rewardNo}", rewardNo);

//        String getRewardURL = "https://c21sguat.memgate.com/MockRewardApi/reward/{rewardNo}" ;
        //rewardNo is globalId from /rewardtype/search

        //call API
        HttpHeaders headers = new HttpHeaders();
        headers.add("SvcAuth", svcToken);
        headers.add("Token", token);
        headers.add("ProfileToken", profileToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getRewardURL, HttpMethod.GET,requestEntity,String.class);
        return ResponseEntity.ok(response.getBody());
    }

    //mock
    @Override
    public ResponseEntity<?> redeemItem(@RequestParam String rewardNo, String profileToken) {

        //get Authorization token from Secret Manager (SVC and token)
        String svcToken = SecretManagerUtils.getSecretValueByKey(secretsManagerClient, "MEMBERSON_CREDENTIALS", "SvcAuth");
        String token = SecretManagerUtils.getSecretValue(secretsManagerClient, "MEMBERSON_TOKEN");

        //get API URL from dynamodb rewards api - mock /reward/redeem
//        String API_LINK = dynamoDBMapper.load(DynamoDB.class, "memberson_redeem_reward").getApi_link();
//        logger.debug(API_LINK);

        return ResponseEntity.ok("Reward successfully redeemed");
    }
}
