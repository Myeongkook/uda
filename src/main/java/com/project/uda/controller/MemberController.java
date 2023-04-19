package com.project.uda.controller;

import com.project.uda.config.JwtTokenProvider;
import com.project.uda.dto.Message;
import com.project.uda.entity.Member;
import com.project.uda.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {


    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    HttpHeaders httpHeaders = new HttpHeaders();

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    }

    @PostMapping("")
    public ResponseEntity signupUser(@RequestBody Member member){
        memberService.saveMember(member);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", true), httpHeaders, HttpStatus.OK);
    }

    /*
    * @Param String info : 계정명
    * 동일한 계정이 있으면 true 아닐 경우 false를 return 한다
    * */
    @GetMapping("/check/{info}")
    public ResponseEntity checkDuplicateInfo(@PathVariable("info")String info){
        memberService.checkUserId(info);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", true), httpHeaders, HttpStatus.OK);
    }

    /*
    * @Param String phone : 가입자 전화번호
    * 중복 가입 제한을 위한 핸드폰 인증 절차로 6개의 숫자를 무작위로 전송, Redis에 저장한다
    * */
    @PostMapping("/send")
    public ResponseEntity sendAuthKey(@RequestParam("phone")String phone){
        memberService.sendAuthKeyBySms(phone);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", true), httpHeaders, HttpStatus.OK);
    }

    /**
     *
     * @param key
     * @return
     * @throws IllegalAccessException
     */
    @GetMapping("/auth")
    public ResponseEntity authenticationKey(@RequestParam("key")String key) throws IllegalAccessException {
        memberService.findByAuthKey(key);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", true), httpHeaders, HttpStatus.OK);
    }

    /**
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity signinUser(@RequestBody Map<String, Object> user){
        String token = null;
        if(memberService.validationByUserInfo(user.get("id").toString(), user.get("password").toString())) {
            token = jwtTokenProvider.createToken(user.get("id").toString());
        }
        return new ResponseEntity(new Message(HttpStatus.OK, "success", "Bearer " + token), httpHeaders, HttpStatus.OK);
    }

}
