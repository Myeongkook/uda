package com.project.uda.service;

import com.project.uda.entity.Member;
import com.project.uda.repository.CoupleRepository;
import com.project.uda.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberServiceImpl memberServiceImpl;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CoupleRepository coupleRepository;

    @Test
    @DisplayName(value = "회원가입이 완료되어야 한다")
    @Transactional
    void member_test01(){
        // 이미 생성되어있는 경우 회원 가입이 완료가 안댈수 있어서 미리 제거
        try{
            memberServiceImpl.checkUserId("tester");
        } catch (DuplicateKeyException e) {
            memberServiceImpl.deleteMember("tester");
        }

        Member member = Member.builder()
                .nickname("tester")
                .password("password")
                .phone("010-2822-9595")
                .userId("tester")
                .build();
        memberServiceImpl.saveMember(member);
        Assertions.assertThat(memberRepository.findByUserId(member.getUserId()).isPresent()).isTrue();
    }

    @Test
    @DisplayName(value = "존재하는 계정명으로는 가입할 수 없다")
    void member_test02(){
        Member sameUser = Member.builder()
                .userId("tester")
                .phone("010-1234-1234")
                .password("password")
                .nickname("tester")
                .build();
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class,
                ()-> memberServiceImpl.saveMember(sameUser));
    }

    @Test
    @DisplayName(value = "사용할 수 없는 계정명은 DuplicateKey Exception이 발생한다")
    void member_test03(){
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class,
                ()-> memberServiceImpl.checkUserId("tester"));
    }

    @Test
    @DisplayName(value = "커플이 생성될 수 있어야 한다.")
    void member_test04(){

    }

}