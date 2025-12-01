package com.sample.base_project.core.model;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.core.constants.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "accounts")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class Account implements BaseEntity {

    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String uuid;

    @Column(name = "user_uuid")
    private String userUuid;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "status")
    private String status;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @PrePersist
    public void prePersist() {
        balance = BigDecimal.ZERO;
        status = AccountStatus.ACTIVE.getValue();
    }
}
