package com.example.blogdomain.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity(
    @LastModifiedDate
    @Column(nullable = false , name ="SYS_MOD_DTIME")
    var systemModificationDateTime: LocalDateTime = LocalDateTime.now(),

    @CreatedDate
    @Column(nullable = false, name = "SYS_REG_DTIME")
    var systemRegistrationDateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, name = "SYS_REGR_ID")
    var systemRegistrantId: String = "",

    @Column(nullable = false, name = "SYS_MODR_ID")
    var systemModifierId: String = ""

)