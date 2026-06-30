package com.sentinelrisk.auth.domain;

import com.sentinelrisk.auth.enums.UserRole;
import com.sentinelrisk.auth.enums.UserStatus;
import com.sentinelrisk.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                )
        }
)
public class User extends BaseEntity {

    @Column(nullable = false,length = 150)
    private String firstName;

    @Column(nullable = false,length = 150)
    private String lastName;


    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private UserRole role  = UserRole.USER;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    @Column(nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    private Instant lastLoginAt;


//    @OneToMany(
//            mappedBy = "user",
//            fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    @Builder.Default
//    private Set<RefreshToken> refreshTokens = new HashSet<>();




}
