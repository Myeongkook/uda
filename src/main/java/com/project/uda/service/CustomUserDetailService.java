package com.project.uda.service;

import com.project.uda.entity.Member;
import com.project.uda.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUserId = memberRepository.findByUserId(username);
        if(byUserId.isPresent()){
            return byUserId.get();
        }
        throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
    }
}
