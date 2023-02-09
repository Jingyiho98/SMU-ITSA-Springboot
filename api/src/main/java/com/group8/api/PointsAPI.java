package com.group8.api;

import com.group8.models.redeemPoints;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface PointsAPI {

    ResponseEntity<?> redeemPoints(redeemPoints redeemPoints) throws URISyntaxException;

    String API_BASE_PATH = "/points";
    String API_REDEEM_POINTS = API_BASE_PATH + "/redeem";
    String API_PATH_WITH_ID = API_BASE_PATH + "/{customerNo}";
}
