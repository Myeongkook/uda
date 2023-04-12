package com.project.uda.service;

import com.project.uda.entity.Member;
import com.project.uda.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import javax.transaction.Transactional;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName(value = "회원가입이 완료되어야 한다")
    @Transactional
    void member_test01(){
        Member member = Member.builder()
                .nickname("mason")
                .password("password")
                .phone("010-8489-8972")
                .userId("myeongkook")
                .build();
        memberService.saveMember(member);
        Assertions.assertThat(memberRepository.findByUserId(member.getUserId()).isPresent()).isTrue();
    }

    @Test
    @DisplayName(value = "존재하는 계정명으로는 가입할 수 없다")
    void member_test02(){
        Member sameUser = Member.builder()
                .userId("myeongkook")
                .phone("010-1234-1234")
                .password("password")
                .nickname("tester")
                .build();
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class,
                ()->memberService.saveMember(sameUser));
    }

    @Test
    @DisplayName(value = "사용할 수 없는 계정명은 DuplicateKey Exception이 발생한다")
    void member_test03(){
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class,
                ()->memberService.checkUserId("myeongkook"));
    }

}