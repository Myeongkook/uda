package com.project.uda.util;

import com.project.uda.entity.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    public static Member getSessionUser(){
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
