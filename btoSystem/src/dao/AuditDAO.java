package dao;

import entity.AuditLog;

import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface AuditDAO extends Reader, Writer{
    HashMap<Integer, AuditLog> get();
    boolean read(String path);
    boolean write();
    boolean add(AuditLog log);
    boolean remove(AuditLog log);
}
