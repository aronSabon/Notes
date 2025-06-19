package com.apptechnosoft.test;

import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ConnectIPSValidationClient {

    private static final String VALIDATION_URL = "https://uat.connectips.com/connectipswebws/api/creditor/validatetxn";

    private static final String APP_ID = "MER-3154-APP-1";
    private static final String BASIC_AUTH_PASSWORD = "Abcd@123";

    public static void main(String[] args) {
        try {
            String response = sendValidationRequest();
            System.out.println("Validation API Response:\n" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendValidationRequest() {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create Basic Auth header value
        String plainCreds = APP_ID + ":" + BASIC_AUTH_PASSWORD;
        String base64Creds = Base64Utils.encodeToString(plainCreds.getBytes());
        headers.add("Authorization", "Basic " + base64Creds);

        // Prepare form parameters
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("MERCHANTID", "3154");
        formParams.add("APPID", APP_ID);
        formParams.add("REFERENCEID", "txn-123");
        formParams.add("TXNAMT", "500");

        // Create HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

        // Make POST request
        ResponseEntity<String> response = restTemplate.postForEntity(VALIDATION_URL, request, String.class);

        return response.getBody();
    }
}
