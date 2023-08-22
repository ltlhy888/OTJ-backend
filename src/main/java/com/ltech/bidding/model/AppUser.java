package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ltech.bidding.model.enumeration.AuthProvider;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonIgnore
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password_reset_token")
    private String pwdResetToken;

    @JsonIgnore
    @Column(name = "password_reset_token_expired_at")
    private Date pwdResetTokenExpiredAt;

    @JsonIgnore
    @Column(name = "activate_token")
    private String activateToken;

    @JsonIgnore
    @Column(name = "activate_token_expired_at")
    private Date activateTokenExpiredAt;

    @ToString.Exclude
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean locked;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private Date lastSignedIn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = false )
    private Set<PasswordHistory> passwordHistories = new HashSet<>();

    @JsonIgnore
    @Column(name="registration_ip")
    private String ip;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @JsonIgnore
    @Column(name="provider_id")
    private String providerId;

    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    @JoinColumn(name = "avatar_id")
    private FileInfo avatar;

}
