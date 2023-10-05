package com.project.uda.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource source) {
        messageSource = source;
    }

    public static String getMessage(String code, Object[] args){
        try {
            return messageSource.getMessage(code, args, Locale.KOREA);
        }catch (Exception e){
            throw new NoSuchElementException("코드 변환에 실패하였습니다.");
        }
    }
}
