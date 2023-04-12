package com.project.uda.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String content;

    private String location;

    @Column(name = "scheduled_ts")
    private LocalDateTime scheduledTime;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "couple_id", referencedColumnName = "id")
    private Couple couple;

    @CreationTimestamp
    @Column(name = "created_ts")
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_ts")
    private LocalDateTime updatedDateTime;
}
