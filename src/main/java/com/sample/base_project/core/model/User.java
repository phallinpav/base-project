package com.sample.base_project.core.model;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.utils.common.PasswordUtils;
import com.sample.base_project.core.constants.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class User implements BaseEntity {

    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String uuid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_code")
    private String phoneCode;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Transient
    private List<Account> accounts;

    @PrePersist
    public void prePersist() {
        password = PasswordUtils.encode(password);
        status = UserStatus.ACTIVE.getValue();
    }
}
