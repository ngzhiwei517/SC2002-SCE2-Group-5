package controller;

import entity.AuditLog;

import java.util.ArrayList;
import java.util.List;

public class AuditController {
    private List<AuditLog> logs = new ArrayList<AuditLog>();

    public boolean init()
    {
        return true;
    }

    public boolean exit()
    {
        return true;
    }

    private boolean addLog()
    {
        return false;
    }
}
