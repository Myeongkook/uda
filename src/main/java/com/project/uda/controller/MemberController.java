package com.project.uda.controller;

import com.project.uda.config.JwtTokenProvider;
import com.project.uda.dto.Message;
import com.project.uda.entity.Member;
import com.project.uda.repository.InviteTicketRepository;
import com.project.uda.service.MemberService;
import com.project.uda.util.SessionUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {


    private final MemberService memberService;
    private final InviteTicketRepository repository;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/get")
    public ResponseEntity<?> getUserList(@RequestBody String userId){

        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success",
                memberService.getUserList(new HashMap<>() {
                    {
                        put("userId", userId);
                    }
                })
        ));
    }

    @PostMapping("")
    public ResponseEntity<?> signupUser(@RequestBody Member member){
        memberService.saveMember(member);
        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success", true));
    }

    /*
    * @Param String info : 계정명
    * 동일한 계정이 있으면 true 아닐 경우 false를 return 한다
    * */
    @GetMapping("/check-id/{info}")
    public ResponseEntity<?> checkDuplicateInfo(@PathVariable("info")String info){
        memberService.checkUserId(info);
        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success", true));
    }

    /*
    * @Param String phone : 가입자 전화번호
    * 중복 가입 제한을 위한 핸드폰 인증 절차로 6개의 숫자를 무작위로 전송, Redis에 저장한다
    * */
    @PostMapping("/send-key")
    public ResponseEntity<?> sendAuthKey(@RequestParam("phone")String phone){
        memberService.sendAuthKeyBySms(phone);
        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success", true));
    }

    /**
     *
     * @param key : 휴대폰으로 받은 6자리 인증번호
     * @return 인증번호 유효 여부 true 또는 false
     * @throws IllegalAccessException
     */
    @GetMapping("/check-key")
    public ResponseEntity<?> authenticationKey(@RequestParam("key")String key) throws IllegalAccessException {
        memberService.findByAuthKey(key);
        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success", true));
    }

    /**
     *
     * @param user : 로그인을 시도하는 계정 정보 (id, pw)
     * @return 메인페이지 또는 초대 페이지로 리다이렉트, 인증 실패시 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> signinUser(@RequestBody Map<String, String> user) throws IllegalAccessException {

        String id = user.get("id");
        String password = user.get("password");

        if(memberService.validationByUserInfo(id, password)) {
            String token = jwtTokenProvider.createToken(id);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            String redirectUrl = determineRedirectUrl(SessionUtil.getSessionUser());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .header("Location", "http://localhost:8080/" + redirectUrl)
                    .body(new Message(HttpStatus.OK, "success", "Bearer " + token));
        }else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Message(HttpStatus.UNAUTHORIZED, "error", "Invalid credentials"));
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id")String userId){
        memberService.deleteMember(userId);
        return ResponseEntity
                .ok()
                .body(new Message(HttpStatus.OK, "success", ""));
    }


    /**
     *
     * @param myInfo : 현재 요청을 보낸 유저 세션 정보
     * @return 로그인 후 리다이렉트 할 페이지 URL
     */
    private String determineRedirectUrl(Member myInfo) {
        if (myInfo.getCouple() != null) {
            return "main";
        } else if (repository.checkForInviteTicket(myInfo.getPhone()).isPresent()) {
            return "invite-res";
        }
        return "invite";
    }

}
