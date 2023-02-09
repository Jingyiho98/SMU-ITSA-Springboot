package com.group8.api;

import org.springframework.http.ResponseEntity;

public interface CustomerAPI {
    ResponseEntity<?> getCustomerProfile(String customerNo, String profileToken);

    String API_BASE_PATH = "/customer";
    String API_PATH_WITH_ID = API_BASE_PATH + "/{customerNo}";
}