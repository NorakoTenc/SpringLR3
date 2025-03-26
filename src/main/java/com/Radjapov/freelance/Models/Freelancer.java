package com.Radjapov.freelance.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "freelancers")
public class Freelancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @PrePersist
    @PreUpdate
    private void setNameFromUser() {
        if (this.user != null && this.user.getUsername() != null) {
            this.name = this.user.getUsername();
        }
    }
}