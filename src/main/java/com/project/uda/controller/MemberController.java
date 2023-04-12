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

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

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

    /*
    * @Param String key : 인증번호
    *  /send 엔드포인트를 통해 수신된 인증번호를 redis에서 꺼내어 유효성 검사를 한다.
    * @TO-DO 한 번 인증된 인증번호는 redis 에서 삭제 되어야 한다.
    * */
    @GetMapping("/auth")
    public ResponseEntity authenticationKey(@RequestParam("key")String key) throws IllegalAccessException {
        memberService.findByAuthKey(key);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", true), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getUserInfo(HttpServletRequest request){
        String token1 = jwtTokenProvider.createToken("0406tester");
        String token = jwtTokenProvider.resolveRequest(request);
        return new ResponseEntity(new Message(HttpStatus.OK, "success", token1), httpHeaders, HttpStatus.OK);
    }

}
