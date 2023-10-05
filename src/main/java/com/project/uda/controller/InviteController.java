package com.project.uda.controller;

import com.project.uda.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteController {


    private final MemberService memberService;


    @RequestMapping(value = "")
    public ResponseEntity<?> inviteUser(){
        return ResponseEntity
                .ok()
                .body(null);
    }
}
