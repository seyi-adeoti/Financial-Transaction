package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.AuditLog;
import com.oluwaseyi.in.Moneymanager.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    @Autowired
    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public AuditLog save(AuditLog log) {
        return repository.save(log);
    }
}
