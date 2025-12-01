package com.sample.base_project.core.model;

import com.sample.base_project.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class Transaction implements BaseEntity {

    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String uuid;

    @Column(name = "account_uuid")
    private String accountUuid;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reference_uuid")
    private String referenceUuid;

    @Column(name = "referenceType")
    private String referenceType;

    @Column(name = "status")
    private String status;

    @Column(name = "remark")
    private String remark;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;
}
