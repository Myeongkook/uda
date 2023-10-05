package com.project.uda.util;

import com.project.uda.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageUtilTest {

    @Autowired
    MemberService service;


    @Test
    void test001() throws Exception {
        service.sendAuthKeyBySms("010-8489-8972");

    }
}
