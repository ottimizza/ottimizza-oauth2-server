package br.com.ottimizza.application.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Base64;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.RestController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private String OAUTH2_SERVER_URL = "https://development-oauth-server.herokuapp.com";

    private String OAUTH2_CLIENT_ID = "1defe81df9442d2b74c2";

    private String OAUTH2_CLIENT_SECRET = "72e9208c85fed78cb43fec9f953662664ab5f649";

    @PostMapping("/callback")
    public ResponseEntity<String> oauthCallback(@RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri) throws IOException {

        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            String uri = MessageFormat.format("{0}/oauth/token?grant_type={1}&code={2}&redirect_uri={3}",
                    OAUTH2_SERVER_URL, "authorization_code", code, redirectUri);

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();

            return ResponseEntity.ok(EntityUtils.toString(responseEntity, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> oauthRefresh(@RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_id") String clientId) throws IOException {

        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            URIBuilder uriBuilder = new URIBuilder(OAUTH2_SERVER_URL + "/oauth/token");
            uriBuilder.addParameter("refresh_token", refreshToken);
            uriBuilder.addParameter("grant_type", "refresh_token");

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();

            return ResponseEntity.ok(EntityUtils.toString(responseEntity, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }
}
