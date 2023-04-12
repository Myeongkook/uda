package com.project.uda.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilTest {

    @Test
    @DisplayName("임의의 숫자가 생성되어야 한다")
    void util_test01(){
        for(int i=0;i<100;i++) System.out.println(CommonUtil.generateKey(6));
    }

    @Test
    @DisplayName("문자메시지가 발송되어야 한다")
    void util_test02(){
        String content = "회원가입 인증번호는 \n" +
                CommonUtil.generateKey(6) + "입니다.";
        SmsUtil.sendSMS("01084898972", content);
    }


}