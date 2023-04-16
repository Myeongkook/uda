package com.project.uda.service;

import com.project.uda.entity.Member;

public interface MemberService {
    void saveMember(Member member);
    void checkUserId(String userId);
    void sendAuthKeyBySms(String phone);
    void findByAuthKey(String key) throws IllegalAccessException;

}
