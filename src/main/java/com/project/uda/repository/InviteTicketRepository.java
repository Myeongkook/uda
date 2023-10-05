package com.project.uda.repository;

import com.project.uda.entity.InviteTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteTicketRepository extends JpaRepository<InviteTicket, Long> {

    @Query("select i from InviteTicket i where i.targetPhone =:phone and i.isCancelled = false")
    Optional<InviteTicket> checkForInviteTicket(@Param("phone") String phone);

}
