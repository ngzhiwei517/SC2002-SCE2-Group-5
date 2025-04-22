package dao;

import entity.AuditLog;

import java.util.HashMap;

public interface AuditDAO {
    HashMap<Integer, AuditLog> get();
    boolean read(String path);
    boolean write();
    boolean add(AuditLog log);
    boolean remove(AuditLog log);
}
