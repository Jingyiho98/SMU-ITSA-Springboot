package com.group8.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface RewardAPI {

    ResponseEntity<?> getAllRedeemable();
    ResponseEntity<?> getDetailsOfItemByItemNo(@RequestParam String rewardNo, String profileToken);
    ResponseEntity<?> redeemItem(@RequestParam String rewardNo, String profileToken);

    String API_BASE_PATH =  "/reward";

    String API_PATH_WITH_ID = API_BASE_PATH + "/{rewardNo}";

}
