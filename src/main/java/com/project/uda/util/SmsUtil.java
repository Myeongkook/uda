package com.project.uda.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

@Component
@Slf4j
public class SmsUtil {

    private static String accessKey;            	// 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키
    private static String secretKey;  // 2차 인증을 위해 서비스마다 할당되는 service secret key
    private static String serviceId;       // 프로젝트에 할당된 SMS 서비스 ID

    public SmsUtil(@Value("${sms.accessKey}") String access, @Value("${sms.secretKey}")String secret, @Value("${sms.serviceId}")String service){
        accessKey = access;
        secretKey = secret;
        serviceId = service;
    }


    private static String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";
        String newLine = "\n";
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;

        signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        return encodeBase64String;
    }

    public static void sendSMS(String toPhoneNumber, String content){
        ObjectMapper objectMapper = new ObjectMapper();
        String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/sms/v2/services/";
        String requestUrlType = "/messages";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;
        HashMap<String, Object> bodyMap = new HashMap<>();
        HashMap<String, String> message = new HashMap<>();
        ArrayList<HashMap<String, String>> toArr = new ArrayList<>();
        message.put("to", toPhoneNumber);
        toArr.add(message);
        bodyMap.put("type","SMS");
        bodyMap.put("content", content);
        bodyMap.put("messages", toArr);
        bodyMap.put("from", "01084898972");
        try {
            String body = objectMapper.writeValueAsString(bodyMap);
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode == 202) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            if(responseCode != 202){
                log.error(response.toString());
                throw new IllegalArgumentException("전송에 실패하였습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

