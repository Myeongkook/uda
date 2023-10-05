package com.project.uda.service;

import com.project.uda.entity.InviteTicket;
import com.project.uda.entity.Member;
import com.project.uda.repository.InviteTicketRepository;
import com.project.uda.util.MessageUtils;
import com.project.uda.util.SessionUtil;
import com.project.uda.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService{

    private final InviteTicketRepository inviteRepository;

    @Override
    public void createAndSend(String phone) {
        Member session = SessionUtil.getSessionUser();
        InviteTicket ticket = InviteTicket.builder()
                .publisher(session)
                .targetPhone(phone)
                .build();

        inviteRepository.save(ticket);
        SmsUtil.sendSMS(phone, MessageUtils.getMessage("phone.send.invite", new String[]{session.getNickname()}));
    }
}
