package dao;

import entity.AuditLog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuditCSVDAO implements AuditDAO {

    List<AuditLog> auditLogs = new ArrayList<AuditLog>();
    private final String path = "AuditLog.csv";

    @Override
    public boolean write() {
        //dump all logs.
        for(AuditLog auditLog : auditLogs) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                writer.write(auditLog.stringifyForExport());
                writer.newLine(); // adds a new line at the end
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        //clear buffer.
        auditLogs.clear();
        return true;
    }

    @Override
    public boolean append(AuditLog auditLog) {
        auditLogs.add(auditLog);
        return true;
    }
}
