package com.oluwaseyi.in.Moneymanager.repository;

import com.oluwaseyi.in.Moneymanager.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
