package com.project.uda.service;

import com.project.uda.config.JwtTokenProvider;
import com.project.uda.entity.Member;
import com.project.uda.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class InviteServiceImplTest {


    @Autowired
    InviteService service;

    @Autowired
    MemberRepository repository;

    @Autowired
    JwtTokenProvider provider;


    @Test
    @DisplayName("티켓이 생성된 후, 메시지가 발송되어야 한")
    @Rollback(value = false)
    void test001() throws Exception {

        Member member = repository.findByUserId("park").get();
        String token = provider.createToken(member.getUserId());
        Authentication authentication = provider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //given
        service.createAndSend("01084898972");

        //when


        //then

    }
}