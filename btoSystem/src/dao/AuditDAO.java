package dao;

import entity.AuditLog;

import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface AuditDAO extends Writer{
    boolean write();
    boolean append(AuditLog auditLog);
}
