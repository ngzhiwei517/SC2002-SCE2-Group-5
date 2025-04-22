package entity;

import java.time.LocalDateTime;

public class AuditLog {
    String username;
    LocalDateTime time;
    String description;

    public AuditLog(String username, String description) {
        this.username = username;
        this.time = LocalDateTime.now();
        this.description = description;
    }

    public String stringifyForExport()
    {
        return username + "," + time + "," + description;
    }
}
