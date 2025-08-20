package com.wfit.springbootbookstores.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "App_User") // Renamed from "User" to avoid SQL reserved keyword conflict
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 10)
    private UserType userType;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "account_balance", precision = 10, scale = 2)
    private BigDecimal accountBalance;

    public enum UserType {
        ADMIN,
        STAFF,
        OWNER
    }
}
