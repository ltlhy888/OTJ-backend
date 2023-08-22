package com.ltech.bidding.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "password_history")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PasswordHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;
}
