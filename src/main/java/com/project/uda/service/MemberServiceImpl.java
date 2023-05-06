package com.project.uda.service;

import com.project.uda.dao.RedisDao;
import com.project.uda.entity.Member;
import com.project.uda.repository.MemberRepository;
import com.project.uda.util.CommonUtil;
import com.project.uda.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisDao redisDao;

    @Transactional
    public void saveMember(Member member){
        if(memberRepository.findByUserId(member.getUserId()).isEmpty()){
            memberRepository.save(member.objectSetPattern(passwordEncoder.encode(member.getPassword())));
        }
        else throw new DuplicateKeyException("이미 존재하는 계정명입니다.");
    }
    public void checkUserId(String userId){
        if(memberRepository.findByUserId(userId).isPresent()) throw new DuplicateKeyException("사용할 수 없는 계정명입니다.");
    }

    public void sendAuthKeyBySms(String phone){
        String generateKey = CommonUtil.generateKey(6);
        String content = "인증번호는 " + generateKey + "입니다.\n"+
                         "3분간 유효합니다.";
        SmsUtil.sendSMS(phone, content);
        redisDao.setValues(generateKey, phone, Duration.ofMillis(60000 * 3));
    }

    public void findByAuthKey(String key) throws IllegalAccessException {
        if(redisDao.getValues(key) == null)throw new IllegalAccessException("인증에 실패하였습니다.");
    }

    public boolean validationByUserInfo(String userId, String rawPassword){
        Optional<Member> byUserId = memberRepository.findByUserId(userId);
        if(byUserId.isEmpty()){
            throw new IllegalArgumentException("로그인에 실패하였습니다.");
        }else {
            return passwordEncoder.matches(rawPassword, byUserId.get().getPassword());
        }
    }

}
