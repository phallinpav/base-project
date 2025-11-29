package com.sample.base_project.model;

import com.sample.base_project.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class User implements BaseEntity {

    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_code")
    private String phoneCode;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "password")
    private String password;

    @Column(name = "notification")
    private Boolean notification;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

}
