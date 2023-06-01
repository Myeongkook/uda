package com.project.uda.service;

import com.project.uda.entity.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {

    List<Member> getUserList (Map<String, Object> paramMap);
    void saveMember(Member member);
    void checkUserId(String userId);
    void sendAuthKeyBySms(String phone);
    void findByAuthKey(String key) throws IllegalAccessException;
    boolean validationByUserInfo(String userId, String rawPassword);
    void deleteMember(String userId);

}
