package com.example.sms.repository;

import com.example.sms.entity.OemAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemAuditLogRepository extends JpaRepository<OemAuditLog, Long> {
    List<OemAuditLog> findBySheetId(Long sheetId);
    List<OemAuditLog> findByStudentId(Long studentId);
    List<OemAuditLog> findByModifiedById(Long userId);
}
