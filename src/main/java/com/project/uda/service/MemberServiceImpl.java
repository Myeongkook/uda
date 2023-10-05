package com.project.uda.service;

import com.project.uda.dao.RedisDao;
import com.project.uda.entity.Member;
import com.project.uda.repository.MemberMapperRepository;
import com.project.uda.repository.MemberRepository;
import com.project.uda.util.CommonUtil;
import com.project.uda.util.MessageUtils;
import com.project.uda.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MemberMapperRepository memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisDao redisDao;

    @Override
    public List<Member> getUserList (Map<String, Object> paramMap) {
        List<Member> memberList = memberMapper.getUserList(paramMap);
        return memberList;
    }

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
        if(phone.isEmpty()){
            throw new IllegalArgumentException("연락처는 비어있을 수 없습니다.");
        }
        phone = phone.replaceAll("[^0-9]", "");
        if(phone.startsWith("010") && phone.length() == 11) {
            String generateKey = CommonUtil.generateKey(6);
            SmsUtil.sendSMS(phone, MessageUtils.getMessage("phone.send.key", new String[]{generateKey}));
            redisDao.setValues(generateKey, phone, Duration.ofMillis(60000 * 3));
        }else {
            throw new IllegalArgumentException("올바른 연락처가 아닙니다.");
        }
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

    @Override
    public void deleteMember(String userId) {
        Optional<Member> userAccount = memberRepository.findByUserId(userId);
        if(userAccount.isEmpty()){
            throw new IllegalArgumentException("해당 계정이 존재하지 않습니다.");
        }else {
            Member member = userAccount.get();
            memberRepository.delete(member);
        }
    }


}
